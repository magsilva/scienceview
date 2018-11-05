package topicevolutionvis.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import scienceview.datamining.corpus.Corpus;
import scienceview.datamining.corpus.DatabaseCorpus;
import scienceview.datarepresentation.Ngram;

public class DatabaseCorpusTest {

	@Test
	public void testGetNgrams() throws IOException {
		Corpus corpus = new DatabaseCorpus("teste");
		
		ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
		ngrams = corpus.getNgrams(56);
		assertNull(ngrams);
	}

}
