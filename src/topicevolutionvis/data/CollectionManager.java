package topicevolutionvis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class CollectionManager {

	public int getNextCollectionId() {
		ConnectionManager connManager = ConnectionManager.getInstance();
		int aux = -1;
		try (
			Connection conn = connManager.getConnection();
			PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "SELECT.MAX.IDCOLLECTION");
			ResultSet rs = stmt.executeQuery();
		) {
			rs.next();
			aux = rs.getInt(1) + 1;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
		return aux;
	}

	public int getCollectionId(String name) {
		ConnectionManager connManager = ConnectionManager.getInstance();
        int aux = -1;
        try (
			Connection conn = connManager.getConnection();
    		PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "SELECT.COLLECTION.BY.NAME")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    aux = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Could not find a collection named " + name, ex);
        }
            
        return aux;
    }

	
    public boolean isUnique(String collection)
    {
    	ConnectionManager connManager = ConnectionManager.getInstance();
        try (
        	Connection conn = connManager.getConnection();
            PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "SELECT.COLLECTION.BY.NAME");
        ) {
            stmt.setString(1, collection);
            try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return false;
	            } else {
	                return true;
	            }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database", e);
        }
    }
	
    public ArrayList<String> getCollections()
    {
    	ArrayList<String> collections = new ArrayList<>();
        ConnectionManager connManager = ConnectionManager.getInstance();
        try (
        	Connection conn = connManager.getConnection();
            PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "SELECT.COLLECTIONS");
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                String name = rs.getString("name");
                collections.add(name);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading collection", e);
        }
        
        return collections;
    }

    public boolean removeCollection(String name) {
        ConnectionManager connManager = ConnectionManager.getInstance();
        int rows = 0;

        try (
        	Connection conn = connManager.getConnection();
        	PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "REMOVE.COLLECTION_BY_NAME");
        ) {
            stmt.setString(1, name);
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
        	throw new RuntimeException("Error removing collection", e);
        }

        return (rows > 0);
    }
    
    public boolean removeCollection(int id) {
        ConnectionManager connManager = ConnectionManager.getInstance();
        int rows = 0;

        try (
        	Connection conn = connManager.getConnection();
        	PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "REMOVE.COLLECTION_BY_ID");
        ) {
            stmt.setInt(1, id);
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
        	throw new RuntimeException("Error removing collection", e);
        }
        
        return (rows > 0);
    }
}
