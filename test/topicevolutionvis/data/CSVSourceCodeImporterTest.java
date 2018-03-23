package topicevolutionvis.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import topicevolutionvis.database.CollectionManager;
import topicevolutionvis.database.DatabaseCorpus;

public class CSVSourceCodeImporterTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		String file = "/home/dsambugaro/UTFPR/IC/dados.csv";
		String collectionName = "teste_csv";
		String path = "/home/dsambugaro/UTFPR/IC/";
		
		CSVSourceCodeImporter importer = new CSVSourceCodeImporter(file, collectionName, path, "C",null);
		assertNotNull(importer);
		importer.execute();
		
		DatabaseCorpus databaseTeste = new DatabaseCorpus(collectionName); 	// A execução não está chegando até "saveToDataBase"
		assertEquals(4,databaseTeste.getNumberOfDocuments());				// no método readCSVFile em CSVSourceCodeImporter
		
		
		

	}

}
