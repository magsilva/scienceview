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
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = SqlManager.getInstance().getSqlStatement(conn, "INSERT.COLLECTION");
			stmt.setString(1, collection);
			stmt.setString(2, filename);
			stmt.setInt(3, nrGrams);
			stmt.setString(4, "csv");
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			id_collection = rs.getInt(1);
		} catch (SQLException e) {
			throw new RuntimeException("Could not create and initialize collection into database", e);
		} finally {
			SqlUtil.close(rs);
			SqlUtil.close(stmt);
		}
	}
	
	private void readCSVFile(Connection conn) throws IOException, SQLException {
		CSVFormat format = CSVFormat.newFormat(':');
		SparseMatrix sm = new SparseMatrix();
		ArrayList<Ngram> fngrams;
		HashMap<String, Double> corpusNgrams = new HashMap<String, Double>();
		ArrayList<Ngram> csv_ngrams = new ArrayList<Ngram>();
		ArrayList<String> header = new ArrayList<String>();
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
					String file = null;
					String codigo = null;
					String pathExercise = null;
					String descriptionExercise = null;
					String pathDescriptionExercise = null;
					String separator = System.getProperty("file.separator");
					String[] chunks;
					double[] vetor;
					int ano = -1;
					
					// Read values, skipping columns with text (for now, just the first column)
					vetor = new double[header.size() - 1];
					for (int i = 1, position = 0; position < vetor.length; i++, position++) {
						vetor[position] = Double.parseDouble(record.get(i));
					}
					sm.addRow(vetor, linha);
					
					
					file = path.toString() + separator + record.get(0);
					chunks = record.get(0).split(separator);
					pathDescriptionExercise = new String(chunks[1].trim()); 
					pathExercise = path.toString() + separator + chunks[0].trim() + separator + chunks[1].trim();
					descriptionExercise = readDescriptionExerciseFile(pathExercise, separator);
					Matcher yearMatcher = yearPattern.matcher(pathDescriptionExercise);
					if (yearMatcher.matches()) {
						ano = Integer.parseInt(yearMatcher.group(1));
					} else {
						System.out.println("Could not find a year for: " + record.toString());
					}
					
					BufferedReader bf = new BufferedReader(new FileReader(file));
					String line = null;
					codigo = new String();
					while ((line = bf.readLine()) != null) {
						codigo = codigo + line;
						codigo = codigo + "\n";
					}
					bf.close();
					saveToDataBase(conn, linha, 0, record.get(0), null, null, codigo, descriptionExercise, null, null, ano, 0, null, null,
							null, "", null, null, null, 0);
					fngrams = getNgramsFromCSVRecord(record, header);
					csv_ngrams.addAll(fngrams);
					
					for (Ngram n : fngrams) {
	                    if (corpusNgrams.containsKey(n.ngram)) {
	                        corpusNgrams.put(n.ngram, corpusNgrams.get(n.ngram) + n.frequency);
	                    } else {
	                        corpusNgrams.put(n.ngram, n.frequency);
	                    }
	                }
					

					// inserting the ngrams on document's table
					try (
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(baos);
							PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.DOCUMENT");
					) {
						oos.writeObject(fngrams);
						oos.flush();
						stmt.setBytes(1, baos.toByteArray());
						stmt.setInt(2, linha);
						stmt.setInt(3, id_collection);
						stmt.executeUpdate();
					}
					
					// insert the sparsematrix on the document's table
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
		
        for (Entry<String, Double> e : corpusNgrams.entrySet()) {
        	csv_ngrams.add(new Ngram(e.getKey(), e.getValue()));
        }
		Collections.sort(csv_ngrams);

		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.COLLECTION");
		) {
			oos.writeObject(csv_ngrams);
			oos.flush();
			stmt.setBytes(1, baos.toByteArray());
			stmt.setInt(2, id_collection);
			stmt.executeUpdate();
		}

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
		
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		
		return new String(data, "UTF-8");
	}

	public ArrayList<Ngram> getNgramsFromCSVRecord(CSVRecord record, ArrayList<String> header) {
		ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
		for (int i=1;i<header.size();i++){
			ngrams.add(new Ngram(header.get(i), Double.valueOf(record.get(i))));
		}
		return ngrams;
	}

}
