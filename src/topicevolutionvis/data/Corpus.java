package topicevolutionvis.data;

import java.io.IOException;
import java.util.ArrayList;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import topicevolutionvis.matrix.SparseMatrix;
import topicevolutionvis.preprocessing.Ngram;
import topicevolutionvis.preprocessing.Reference;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.util.Pair;

public interface Corpus {

	String getCollectionName();

	float[] getClassData();

	int[] getDocumentsIds();

	int[] getDocumentsIds(boolean include_only_documents_with_abstract, boolean include_only_documents_with_keywords,
			boolean include_only_documents_with_citations);

	void updateClasses(ArrayList<Integer> ids_docs, int class_docs);

	TIntObjectHashMap<ArrayList<Pair>> getCoreReferences();

	int getLocalCitationCount(int id_doc);

	int getNumberOfCitationsAtYear(int id_doc, int year);

	int getCollectionId();

	String getCollectionFilename();

	int getNumberOfDocuments();

	String getFullContent(int id);

	int getNumberOfUniqueReferences();

	int getMinIdCitation();

	double getBibliographicCoupling_Log(int id1, int id2);

	String getViewContent(int id);

	void saveToPExFormat(String filename, boolean individual, int yearStep, ProjectionData pdata) throws Exception;

	ArrayList<Ngram> getNgrams(int id);

	boolean doesThisDocumentCitesThisReference(int id_doc, int index_citation);

	ArrayList<Reference> getCorpusReferences(int lower, int upper);

	ArrayList<Ngram> getCorpusNgrams();

	int getNumberGrams();

	int[] getAscendingDates();

	SparseMatrix getCorpusSparseMatrix();

	int[] getDocumentsWithLCC(int value, String comparison);

	int[] getDocumentsWithGCC(int value, String comparison);

	TIntArrayList getDocumentsIdsFromYearToYear(int begin_year, int end_year);

	int[] getDocumentsIdsSortedByTitle(int year);

	int[] getDocumentsFromAuthor(String author_name, int year);

	Object[][] getMainAuthors(TIntArrayList docs_ids);

	TIntIntHashMap searchTerm(String term);

	int[] getDocumentsIds(int year);

	String getAbstract(int id);

	String getTitle(int id);

	int getYear(int id);

	String getPDFFile(int id);

	String getKeywords(int id);

	ArrayList<String> getReferences(int id);

	TIntObjectHashMap<ArrayList<Pair>> getCoAuthorship();

	boolean uniqueName(String collection) throws IOException;

	TIntObjectHashMap<TIntIntHashMap> getBibliographicCoupling();

	int getGlobalCitationCount(int id);

	String getAuthors(int id);

	String getDOI(int id);

	int getClass(int id);

	SparseMatrix getNormalizedSm();

	void setNormalizedSm(SparseMatrix normalizedSm);

}