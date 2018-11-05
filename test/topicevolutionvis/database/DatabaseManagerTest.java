package topicevolutionvis.database;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scienceview.database.DatabaseManager;

public class DatabaseManagerTest
{
	private DatabaseManager db;

	@Before
	public void setUp() throws Exception {
		db = new DatabaseManager();
	}

	@After
	public void tearDown() throws Exception {
		db.close();
	}

	@Test
	public void testOpenDatabaseDoesNotExist() {
		db.eraseDatabase();
		assertFalse(db.isDatabaseReady());
	}

	@Test
	public void testOpenDatabaseOk() {
		db.createDatabase();
		assertTrue(db.isDatabaseReady());
	}
}
