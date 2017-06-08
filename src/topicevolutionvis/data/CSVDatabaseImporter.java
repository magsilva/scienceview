package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import topicevolutionvis.database.ConnectionManager;
import topicevolutionvis.database.SqlManager;
import topicevolutionvis.database.SqlUtil;
import topicevolutionvis.matrix.SparseMatrix;
import topicevolutionvis.preprocessing.Ngram;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.wizard.DataSourceChoiceWizard;

/**
 *
 * @author Aretha
 */
public class CSVDatabaseImporter extends DatabaseImporter {

	public CSVDatabaseImporter(String filename, String collection, String path, DataSourceChoiceWizard view) {
		super(filename, collection, path, 1, view, false);
	}

	@Override
	protected Void doInBackground() {
		ConnectionManager connManager = ConnectionManager.getInstance();
		this.setLoadingDatabase(true);
		
		try (Connection conn = connManager.getConnection()) {
			createCollection(conn);
			readCSVFile(conn);
		} catch (Exception e) {
			System.out.println("Error loading CSV file");
			throw new RuntimeException("Error loading CSV file", e);
		}
		return null;
	}

	private void createCollection(Connection conn) {
		try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "INSERT.COLLECTION")) {
			stmt.setString(1, collection);
			stmt.setString(2, filename);
			stmt.setInt(3, nrGrams);
			stmt.setString(4, "csv");
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				id_collection = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not create and initialize collection into database", e);
		}
	}
	
	private void readCSVFile(Connection conn) throws IOException, SQLException {
		CSVFormat format = CSVFormat.newFormat(':');
		SparseMatrix sm = new SparseMatrix();
		Map<String, Double> corpusNgrams = new HashMap<String, Double>();
		List<String> header = new ArrayList<String>();
		Pattern yearPattern = Pattern.compile(".*(\\d+).*");
		
		try (
				Reader in = new FileReader(filename);
				CSVParser parser = format.parse(in);
		) {
			Iterable<CSVRecord> records = parser.getRecords();
			int linha = 0;
			
			for (CSVRecord record : records) {		
				if (linha == 0) {
					Iterator<String> it = record.iterator();
					while (it.hasNext()) {
						header.add(it.next());
					}
					sm.setDimensions(header.size() - 1);
				} else {
					String filename = null;
					String rawContent = null;
					String description = null;
					double[] featuresVector;
					int ano = -1;
					List<Ngram> ngrams;


					// Aux variables
					String pathExercise = null;
					String pathDescriptionExercise = null;
					String separator = System.getProperty("file.separator");
					String[] chunks;
					
					// Read values, skipping columns with text (for now, just the first column)
					featuresVector = new double[header.size() - 1];
					for (int i = 1, position = 0; position < featuresVector.length; i++, position++) {
						featuresVector[position] = Double.parseDouble(record.get(i));
					}
					sm.addRow(featuresVector, linha);

					// Get file's name
					filename = path.toString() + separator + record.get(0);

					// Get file's description
					chunks = record.get(0).split(separator);
					pathDescriptionExercise = new String(chunks[1].trim()); 
					pathExercise = path.toString() + separator + chunks[0].trim() + separator + chunks[1].trim();
					description = readDescriptionExerciseFile(pathExercise, separator);

					// Find "year"
					Matcher yearMatcher = yearPattern.matcher(pathDescriptionExercise);
					if (yearMatcher.matches()) {
						ano = Integer.parseInt(yearMatcher.group(1));
					} else {
						System.out.println("Could not find a year for: " + record.toString());
					}
					
					// Read file's content
					try (BufferedReader bf = new BufferedReader(new FileReader(filename))) {
						String line = null;
						rawContent = new String();
						while ((line = bf.readLine()) != null) {
							rawContent = rawContent + line;
							rawContent = rawContent + "\n";
						}
					}
					
					saveToDataBase(conn, linha, 0, record.get(0), null, null, rawContent, description, null, null, ano, 0, null, null, null, "", null, null, null, 0);
					
					ngrams = getNgramsFromCSVRecord(record, header);
					for (Ngram n : ngrams) {
	                    if (corpusNgrams.containsKey(n.ngram)) {
	                        corpusNgrams.put(n.ngram, corpusNgrams.get(n.ngram) + n.frequency);
	                    } else {
	                        corpusNgrams.put(n.ngram, n.frequency);
	                    }
	                }
					

					// Insert ngrams on document's table
					try (
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(baos);
							PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.DOCUMENT");
					) {
						oos.writeObject(ngrams);
						oos.flush();
						stmt.setBytes(1, baos.toByteArray());
						stmt.setInt(2, linha);
						stmt.setInt(3, id_collection);
						stmt.executeUpdate();
					}
					
					// TODO: it should be a SparseVector. However, the SELECT.SPARSEMATRIX.DOCUMENT is never required (we just need the sparsematrix of the collection)
					// Insert SparseMatrix on the document's table
					try (
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(baos);
							PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.DOCUMENT");
					) {
						oos.writeObject(sm);
						oos.flush();
						stmt.setBytes(1, baos.toByteArray());
						stmt.setInt(2, linha);
						stmt.setInt(3, id_collection);
						stmt.executeUpdate();
					}
				}
	
				linha++;
			}
		}
		
		// Save collection's ngrams
		List<Ngram> collectionNgrams = new ArrayList<Ngram>(corpusNgrams.size());
        for (Entry<String, Double> e : corpusNgrams.entrySet()) {
        	collectionNgrams.add(new Ngram(e.getKey(), e.getValue()));
        }
		Collections.sort(collectionNgrams);
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.COLLECTION");
		) {
			oos.writeObject(collectionNgrams);
			oos.flush();
			stmt.setBytes(1, baos.toByteArray());
			stmt.setInt(2, id_collection);
			stmt.executeUpdate();
		}

		// Save collection's sparce matrix
		ProjectionData pData = this.view.getPdata();
		pData.setMatrix(sm);
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.COLLECTION");
		) {
			oos.writeObject(sm);
			oos.flush();
			stmt.setBytes(1, baos.toByteArray());
			stmt.setInt(2, id_collection);
			stmt.executeUpdate();
		}
		this.setLoadingDatabase(false);
	}
	
	private String getCodePath(String file){
		String path_separator = System.getProperty("path.separator");
		if (path_separator.compareTo("\\")==0){
			file = file.replace("/", "\\");
		}
		File teste = new File(System.getProperty("user.dir")+File.separator+file);
		return teste.getAbsolutePath();
	}
	
	private String readDescriptionExerciseFile(String path, String separator) throws IOException {
		File file = new File(path);
		String folderName = file.getName();
		file = new File(file.getAbsolutePath() + separator + folderName + ".txt");
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			return new String(data, "UTF-8");
		}
		
	}

	public List<Ngram> getNgramsFromCSVRecord(CSVRecord record, List<String> header) {
		List<Ngram> ngrams = new ArrayList<Ngram>();
		for (int i=1;i<header.size();i++){
			ngrams.add(new Ngram(header.get(i), Double.valueOf(record.get(i))));
		}
		return ngrams;
	}

}
