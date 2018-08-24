package topicevolutionvis.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import topicevolutionvis.data.Corpus;
import topicevolutionvis.data.DatabaseCorpus;
import topicevolutionvis.preprocessing.Ngram;

public class DatabaseCorpusTest {

	@Test
	public void testGetNgrams() throws IOException {
		Corpus corpus = new DatabaseCorpus("teste");
		
		ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
		ngrams = corpus.getNgrams(56);
		assertNull(ngrams);
	}

}
