/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aretha
 */
public class CreateDataBase {

    ConnectionManager connManager;

    private SqlManager sqlManager;

    public void create()  {
        connManager = ConnectionManager.getInstance();
        sqlManager = SqlManager.getInstance();
        try {
        	removeTables();
        	createTables();
        	sqlManager.close();
        	connManager.close();
        } catch (SQLException e) {
        	throw new RuntimeException("Error creating database", e);
        }
        
    }

    private void createTables() throws SQLException {
    	try (Connection conn = connManager.getConnection()) {
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.COLLECTIONS")) {
    			stmt.executeUpdate();
    		}

    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.AUTHORS")) {
    			stmt.executeUpdate();
    		}
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.CONTENT")) {
    			stmt.executeUpdate();
    		}
    		
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.REFERENCES")) {
    			stmt.executeUpdate();
    		}
            
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.DOCUMENTS.TO.REFERENCES")) {
    			stmt.executeUpdate();
    		}
            
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.TABLE.DOCUMENTS.TO.AUTHORS")) {
    			stmt.executeUpdate();
    		}
    		
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.INDEX.REFERENCES")) {
    			stmt.executeUpdate();
    		}
    		
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.INDEX.AUTHORS")) {
    			stmt.executeUpdate();
    		}
            
    		try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.INDEX.MATCH")) {
    			stmt.executeUpdate();
    		}
        }
    }

    /**
     * Remove the tables of this data base.
     */
    private void removeTables() throws SQLException {
    	try (Connection conn = connManager.getConnection()) {
        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.COLLECTIONS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.DOCUMENTS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.REFERENCES")) {
        		stmt.executeUpdate();
        	}
        	
        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.DOCUMENTS.TO.REFERENCES")) {
        		stmt.executeUpdate();
        	}
        	
        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.AUTHORS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "DROP.TABLE.DOCUMENTS.TO.AUTHORS")) {
        		stmt.executeUpdate();
        	}
        }
    }

    public static void main(String[] args) throws Exception {
        CreateDataBase cdb = new CreateDataBase();
        cdb.create();
    }
}
