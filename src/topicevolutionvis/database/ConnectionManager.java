package topicevolutionvis.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public abstract class ConnectionManager
{
	private static ConnectionManager instance;
	
	private int connectionsRequested;
	
	private int connectionsFailed;

    /**
     * File that hosts the configuration required to connect to the database.
     */
    static final String DEFAULT_DATABASE_CONFIG = "/scienceview/database.properties";
	
    /**
     * Create the ConnectionManager. The constructor must be private due to the Singleton
     * pattern.
     */
    protected final void preparePool(Properties props)
    {
        try {
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            
            boolean poolOk = createPool(url + ";AUTO_SERVER=TRUE", username, password); 
            if (! poolOk) {
            	throw new IllegalArgumentException("Cannot create pool of database connections");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot load configuration for database connection", e);
        } finally {
        }
    }


	/**
	 * Get JDBC connection from database connection pool.
	 * 
	 * @return Database connection
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			while (conn == null) {
				connectionsRequested++;
				try {
	    			conn = createConnection();
	    			if (conn == null) {
	    	        	connectionsFailed++;
	    				Thread.sleep(1000);
	    			}
				} catch (Exception e) {
					connectionsFailed++;
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			connectionsFailed++;
			Thread.currentThread().interrupt();
		}
		return conn;
	}

	public synchronized void close() {
		if (instance != null) {
			instance = null;
		}
	}
		

	
   	/**
	 * Get the instance of ConnectionManager (Singleton pattern).
	 * 
	 * @return ConnectionManager
	 * 
	 * @throws IllegalArgumentException if configuration data could not be loaded or the
	 * data was incorrect (and no connection to the database was possible).
	 */
	public static synchronized ConnectionManager getInstance() {
		try (InputStream in = ConnectionManager.class.getResourceAsStream(DEFAULT_DATABASE_CONFIG)) {
			Properties props  = new Properties();
			props.load(in);
			return getInstance(props);
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid properties resource", e);
		}
	}
    
	/**
	 * Get the instance of ConnectionManager (Singleton pattern).
	 * 
	 * @return ConnectionManager
	 * 
	 * @throws IllegalArgumentException if configuration data could not be loaded or the
	 * data was incorrect (and no connection to the database was possible).
	 */
	public static synchronized ConnectionManager getInstance(Properties props) {
	    if (instance == null) {
//    		instance = new H2ConnectionManager();
	   		instance = new BoneCPConnectionManager();
	   		instance.preparePool(props);
	    }
	
	    return instance;
	}
		
	protected abstract boolean createPool(String url, String username, String password);

	protected abstract Connection createConnection() throws SQLException; 
}
