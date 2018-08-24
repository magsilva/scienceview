package topicevolutionvis.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import topicevolutionvis.data.csv.CSVDatabaseImporter;

public class CSVSourceCodeImporterTest {

	@Before
	public void setUp() throws Exception {
		// TODO: create database for testing
		
	}

	@Test
	public void test() {
		String file = "/home/dsambugaro/UTFPR/IC/teste/dados.csv";
		String collectionName = "teste_csv" + System.currentTimeMillis();;
		String path = "/home/dsambugaro/UTFPR/IC/";
		/*
		String file = "/home/magsilva/t/scienceview/test-resources/topicevolutionvis/data/dados.csv";
		String collectionName = "teste_csv" + System.currentTimeMillis();
		String path = "/home/magsilva/t/scienceview/test-resources/topicevolutionvis/data/";
		*/
		DatabaseImporter importer = new CSVDatabaseImporter(file, collectionName, path, "C", null);
		assertNotNull(importer);
		importer.importData();
		
		Corpus databaseTeste = new DatabaseCorpus(collectionName);
		assertEquals(4, databaseTeste.getNumberOfDocuments());
		assertEquals(2017, databaseTeste.getYear(1));
		assertEquals("recursividade.c", databaseTeste.getTitle(3));
	}
}