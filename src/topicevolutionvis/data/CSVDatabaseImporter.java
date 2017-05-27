package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
	protected Void doInBackground() throws Exception {
		ConnectionManager connManager = ConnectionManager.getInstance();
		Connection conn = null;
		try {
			this.setLoadingDatabase(true);
			conn = connManager.getConnection();
			createCollection(conn);
			readCSVFile(conn);
		} catch (Exception e) {
			throw new RuntimeException("Could not save collection to database", e);
		} finally {
			SqlUtil.close(conn);
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
		SparseMatrix sm = new SparseMatrix();
		Reader in = new FileReader(filename);
		CSVFormat format = CSVFormat.newFormat(':');
		CSVParser parser = format.parse(in);
		Iterable<CSVRecord> records = parser.getRecords();
		int linha = 0;
		int ano = 2016;
		double[] vetor;
		String file, codigo;
		PreparedStatement stmt = null;

		ArrayList<Ngram> fngrams;
		HashMap<String, Double> corpusNgrams = new HashMap<String, Double>();
		ArrayList<Ngram> csv_ngrams = new ArrayList<Ngram>();
		ArrayList<String> header = new ArrayList<String>();

		for (CSVRecord record : records) {
			if (linha == 0) {
				Iterator<String> it = record.iterator();
				while (it.hasNext()) {
					header.add(it.next());
				}
				sm.setDimensions(header.size());
			} else {
			    vetor = new double[header.size() - 1];
				for (int i = 1, position = 0; position < vetor.length; i++, position++) {
					vetor[position] = Double.parseDouble(record.get(i));
				}
				sm.addRow(vetor, linha);
				
				file = path.toString() + System.getProperty("file.separator") + record.get(0);
				BufferedReader bf = new BufferedReader(new FileReader(file));
				String line = null;
				codigo = new String();
				while ((line = bf.readLine()) != null) {
					codigo = codigo + line;
					codigo = codigo + "\n";
				}
				bf.close();
				saveToDataBase(conn, linha, 0, record.get(0), null, null, codigo, null, null, null, ano, 0, null, null,
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
				
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(fngrams);
				oos.flush();
				// inserting the ngrams on database
				stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.DOCUMENT");
				stmt.setBytes(1, baos.toByteArray());
				stmt.setInt(2, linha);
				stmt.setInt(3, id_collection);
				stmt.executeUpdate();
				stmt.close();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(sm);
			oos.flush();
			// inserting the sparsematrix on the document
			stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.DOCUMENT");
			stmt.setBytes(1, baos.toByteArray());
			stmt.setInt(2, linha);
			stmt.setInt(3, id_collection);
			stmt.executeUpdate();
			stmt.close();

			linha++;
			if (linha % 40 == 0)
				ano--;
		}
		in.close();
		
        for (Entry<String, Double> e : corpusNgrams.entrySet()) {
        	csv_ngrams.add(new Ngram(e.getKey(), e.getValue()));
        }
		Collections.sort(csv_ngrams);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(csv_ngrams);
		oos.flush();
		stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.NGRAMS.COLLECTION");
		stmt.setBytes(1, baos.toByteArray());
		stmt.setInt(2, id_collection);
		stmt.executeUpdate();
		stmt.close();

		ProjectionData pData = this.view.getPdata();
		pData.setMatrix(sm);

		baos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(sm);
		oos.flush();

		stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.COLLECTION");
		stmt.setBytes(1, baos.toByteArray());
		stmt.setInt(2, id_collection);
		stmt.executeUpdate();
		stmt.close();
		
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

	public ArrayList<Ngram> getNgramsFromCSVRecord(CSVRecord record, ArrayList<String> header) {
		ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
		for (int i=1;i<header.size();i++){
			ngrams.add(new Ngram(header.get(i), Double.valueOf(record.get(i))));
		}
		return ngrams;
	}

}
