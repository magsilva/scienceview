package topicevolutionvis.database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionManagerTest
{
	private ConnectionManager connManager;
	
	@Before
	public void setUp() {
		connManager = ConnectionManager.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		connManager.close();
	}

	@Test
	public void test() throws SQLException {
		Connection conn = connManager.getConnection();
		assertNotNull(conn);
	}
	

	@Test
	public void testManyConnections() throws SQLException {
		for (int i = 0; i < 200; i++) {
			Connection conn = connManager.getConnection();
			assertNotNull(conn);
			conn.close();
		}
	}

}
