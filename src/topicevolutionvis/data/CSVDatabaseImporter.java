package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import topicevolutionvis.database.ConnectionManager;
import topicevolutionvis.database.SqlManager;
import topicevolutionvis.database.SqlUtil;
import topicevolutionvis.matrix.SparseMatrix;
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
    
    private void createCollection(Connection conn){
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
        Iterable<CSVRecord> records = CSVFormat.newFormat(':').parse(in);
		int posicao, linha = 0;
		int ano = 2016;
		double[] vetor;
		String file, codigo;
		PreparedStatement stmt = null;
		
		for (CSVRecord record : records) {
			if(linha != 0) {
				vetor = new double[64];
				posicao = 0;
				for (int i = 1; i < 65; i++) {
					vetor[posicao] = Double.parseDouble(record.get(i));
					posicao++;
				}
				sm.addRow(vetor, linha);
				
				file = new String();
				file = path.toString() + '/' + record.get(0);
				System.out.println(file);
				BufferedReader bf = new BufferedReader(new FileReader(file));
				String line = null;
				codigo = new String();
				while((line = bf.readLine()) != null) {
					codigo = codigo + line;
					codigo = codigo + "\n";
				}
				bf.close();
			 	saveToDataBase(conn, linha, 0, record.get(0), null, null, codigo, null, null, null, ano, 0, null, null, null, "", null, null, null, 0);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sm);
            oos.flush();
			//inserting the ngrams
            stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.DOCUMENT");
            stmt.setBytes(1, baos.toByteArray());
            stmt.setInt(2, linha);
            stmt.setInt(3, id_collection);
            stmt.executeUpdate();
            stmt.close();
			
			linha++;
			if(linha % 40 == 0)
				ano--;
		}
		in.close();
		
		ProjectionData pData = this.view.getPdata();
		pData.setMatrix(sm);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(sm);
        oos.flush();

        stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.COLLECTION");
        stmt.setBytes(1, baos.toByteArray());
        stmt.setInt(2, id_collection);
        stmt.executeUpdate();
        stmt.close();
		
    }

}
