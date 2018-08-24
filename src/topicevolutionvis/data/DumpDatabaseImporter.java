package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import topicevolutionvis.wizard.DataImportWizard;

/**
 *
 * @author Aretha
 */
public class DumpDatabaseImporter extends AbstractDatabaseImporter {

    public DumpDatabaseImporter(String filename, DataImportWizard view, boolean removeStopwordsByTagging) {
        super(filename, null, -1, view, removeStopwordsByTagging);
    }

	@Override
	public boolean isDataValid() {
		return true;
	}

	@Override
	public int getNumberOfDocuments() {
		return 0;
	}

	@Override
	protected String getDataType() {
		return "dump";
	}

	@Override
	protected void readData() {

    	
        String line;
        StringBuilder command = new StringBuilder();
        String collectionName = "";
        
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
        	boolean search_collection_name = false;
	        int begin, end;
        
	        while (((line = in.readLine().trim()) != null)) {
	            if (search_collection_name) {
	                begin = line.indexOf("'");
	                end = line.indexOf("'", begin + 1);
	                collectionName = line.substring(begin + 1, end);
	            }
	            if (! search_collection_name && line.contains("INSERT INTO PUBLIC.COLLECTIONS(ID_COLLECTION, NAME, NRGRAMS, FORMAT, GRAMS)")) {
	                search_collection_name = true;
	            }
	            command.append(line);
                if (line.length() > 1 && line.endsWith(";")) {
	                try (PreparedStatement stmt = SqlManager.getInstance().createSqlStatement(connection, command.toString())) {
	                	stmt.execute();
	                } catch (SQLException e) {
	                }
                }
	            command.setLength(0);
	        }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
	}

	@Override
	protected Corpus createCollection() {
		return null;
	}
}
