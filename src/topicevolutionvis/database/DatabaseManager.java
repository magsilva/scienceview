package topicevolutionvis.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Aretha
 */
public class DatabaseManager {

    private ConnectionManager connManager;

    private SqlManager sqlManager;
    
    public DatabaseManager()
    {
    	connManager = ConnectionManager.getInstance();
    	sqlManager = SqlManager.getInstance();
    }
    
    public synchronized boolean checkDatabase()
    {
          try (Connection conn = connManager.getConnection()) {
        	  DatabaseMetaData dbm = conn.getMetaData();
        	  try (ResultSet rs = dbm.getTables(null, null, "COLLECTIONS", null)) {
        		  if (! rs.next()) {
       				  return false;
        		  }
        	  }
        	  return true;
          } catch (Exception e) {
				  return false;
          }
    }
    
    public synchronized void eraseDatabase()
    {
    	 try {
         	removeTables();
         } catch (SQLException e) {
         	throw new RuntimeException("Error erasing database", e);
         }	
    }

    public synchronized void createDatabase()
    {
        try {
        	createTables();
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
        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.COLLECTIONS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.DOCUMENTS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.REFERENCES")) {
        		stmt.executeUpdate();
        	}
        	
        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.DOCUMENTS.TO.REFERENCES")) {
        		stmt.executeUpdate();
        	}
        	
        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.AUTHORS")) {
        		stmt.executeUpdate();
        	}

        	try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.TABLE.DOCUMENTS.TO.AUTHORS")) {
        		stmt.executeUpdate();
        	}
        }
    }

    public synchronized void close()
    {
    	sqlManager.close();
    	connManager.close();
    }
}
