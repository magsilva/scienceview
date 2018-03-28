package topicevolutionvis.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import topicevolutionvis.database.Corpus;
import topicevolutionvis.database.DatabaseCorpus;

public class CSVSourceCodeImporterTest {

	@Before
	public void setUp() throws Exception {
		// TODO: create database for testing
		
	}

	@Test
	public void test() {
		/*
		String file = "/home/dsambugaro/UTFPR/IC/dados.csv";
		String collectionName = "teste_csv";
		String path = "/home/dsambugaro/UTFPR/IC/";
		*/
		String file = "/home/magsilva/t/scienceview/test-resources/topicevolutionvis/data/dados.csv";
		String collectionName = "teste_csv" + System.currentTimeMillis();
		String path = "/home/magsilva/t/scienceview/test-resources/topicevolutionvis/data/";
		
		CSVDatabaseImporter importer = new CSVDatabaseImporter(file, collectionName, path, "C", null);
		assertNotNull(importer);
		importer.importData();
		
		Corpus databaseTeste = new DatabaseCorpus(collectionName); 	// A execução não está chegando até "saveToDataBase"
		assertEquals(4, databaseTeste.getNumberOfDocuments());				// no método readCSVFile em CSVSourceCodeImporter
	}
}