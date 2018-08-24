/*
***** BEGIN LICENSE BLOCK *****
This is free software: you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free 
Software Foundation, either version 3 of the License, or (at your option) 
any later version.

It is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
for more details.

You should have received a copy of the GNU General Public License along 
with this software. If not, see <http://www.gnu.org/licenses/>.
***** END LICENSE BLOCK *****
*/

package topicevolutionvis.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import scienceview.ui.desktop.wizard.DataImportWizard;
import topicevolutionvis.preprocessing.Ngram;

/**
 * DatabaseImporter that stores data into a database.
 * 
 * @author Aretha Barbosa Alencar
 * @author Marco Aurélio Graciotto Silva
 */
public abstract class AbstractDatabaseImporter implements DatabaseImporter
{
    protected String collection;
    
    protected String filename;
    
    protected String msg = "";
    
    protected int nrGrams;
    
    protected int id_collection;
    
    protected boolean removeStopwordsByTagging;
    
    protected boolean loadingDatabase = false;
    
	protected DataImportWizard view = null;
    
    protected CollectionManager collectionManager;
    
    private static Pattern referencePattern = Pattern.compile("\\*?([\\w\\s-\\.'()]{2,50})(,\\s(\\d{4}))?,(\\s(UNPUB|INPRESS))?\\s([\\w\\d\\s-\\.\\+\\&():\\d]+){1}(,\\s?[Vv]([\\w\\d-]+))?(,\\sCH([\\d\\w]+))?(,\\s(\\w([\\w\\d]+)))?(,\\sUNSP\\s[\\d\\w\\-/\\.()]+|,\\sARTN\\s([\\d\\w\\.]+))?(,\\sDOI\\s(.{5,100}))?");
    
    private ConnectionManager connManager;
    
    protected Connection connection;
    
    private SqlManager sqlManager;
    
    public AbstractDatabaseImporter(String filename, String collection, int nrGrams, DataImportWizard view, boolean removeStopwordsByTagging) {
        this.filename = filename;
        this.collection = collection;
        this.nrGrams = nrGrams;
        this.removeStopwordsByTagging = removeStopwordsByTagging;
        this.view = view;
        connManager = ConnectionManager.getInstance();
        sqlManager = SqlManager.getInstance();
        collectionManager = new CollectionManager();
    }

    @Override
    public void done() {
    	if (view != null) {
			view.setStatus("Finished", false);
			view.finishedLoadingCollection(collection, true);
		}      
    }

    public synchronized boolean isLoadingDatabase() {
		return loadingDatabase;
	}

	private synchronized void setLoadingDatabase(boolean loadingDatabase) {
		this.loadingDatabase = loadingDatabase;
	}
    
    protected void matchReferencesToPapers() {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(connection, "MATCH.CORE.REFERENCES")) {
            stmt.setInt(1, id_collection);
            stmt.setInt(2, id_collection);
            stmt.setInt(3, id_collection);
            stmt.setInt(4, id_collection);
            try (ResultSet result = stmt.executeQuery()) {
	            while (result.next()) {
	                int id_doc = result.getInt(1);
	                int id_ref = result.getInt(2);
	                try (PreparedStatement stmt2 = sqlManager.getSqlStatement(connection, "UPDATE.REFERENCE")) {
		                stmt2.setInt(1, id_doc);
		                stmt2.setInt(2, id_ref);
		                stmt2.setInt(3, id_collection);
		                stmt2.executeUpdate();
	                }
	            }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database", e);
        }
    }

    public int getNumberOfReferences() {
        try (
        		Connection conn = connManager.getConnection();
        		PreparedStatement stmt = sqlManager.getSqlStatement(conn, "SELECT.NUMBER.OF.REFERENCES");
        ) { 
            stmt.setInt(1, id_collection);
            try (ResultSet result = stmt.executeQuery()) {
            	result.next();
            	int number = result.getInt(1);
                return number;
            }
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        }
    }


    protected void createIndexForBibliographicCoupling() {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(connection, "CREATE.INDEX.BC")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating index for bibliographic coupling", e);
        }
    }

    protected void dropIndexForBibliographicCoupling() {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(connection, "DROP.INDEX.BC")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error reseting index for bibliographic coupling", e);
        }
    }

    // TODO: rewrite this using code from SysRevPEx
    protected ArrayList<Ngram> getNgramsFromFileRemovingStopwordsByTagging(String content) {
    	throw new UnsupportedOperationException();

    	/*
    	if (content == null) {
        	throw new IllegalArgumentException(new NullPointerException());
        }
    
       	Map<String, Integer> ngramsTable = new HashMap<>();
        try (
        	InputStream rules_POS = new FileInputStream("resources/en-pos-maxent.bin");
        	InputStream modelIn = new FileInputStream("resources/en-token.bin")
        ) {
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            String paras[] = tokenizer.tokenize(content);
            POSModel modelPOS = new POSModel(rules_POS);
            POSTaggerME tagger = new POSTaggerME(modelPOS);
            String tags[] = tagger.tag(paras);
            String stopTags[] = { "CC", "CD", "DT", "EX", "IN", "JJR", "JJS", "LS", "MD",
            		"PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", 
            		"UH", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$",
            		"WRB"};
            ArrayList<String> words = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
            	boolean shouldIncludeWord = true;
            	for (String stopTag : stopTags) {
           			shouldIncludeWord &= (tags[i].compareTo(stopTag) == 0);
            	}
            	if (shouldIncludeWord) {
            		String cleantWord = paras[i].trim().toLowerCase();
            		if (cleantWord.length() > 0) {
            			words.add(cleantWord);
            		}
                }
            }

            // TODO: Rewrite the n-gram generation code
            // For each word, create 1-gram
            String[] ngram = new String[nrGrams];
            for
            int count = 0;
            while (count < nrGrams && i < words.size()) {
                // TODO: the old code was:
            	// if (words.get(i).length() > 0 && ! words.get(i).matches("[\\p{Punct}\\p{Digit}]+|'s")) {
            	// It was ignoring some words. Why? Shouldn't this be handled latter on?
                ngram[count] = words.get(i);
                count++;
                i++;
            }

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < ngram.length - 1; j++) {
                sb.append(ngram[j]).append("<>");
            }
            sb.append(ngram[ngram.length - 1]);

            //adding to the frequencies table
            ngramsTable.put(sb.toString(), 1);

            // For each word, create 1-gram
            //creating the remaining ngrams
            for (String word : words) {
                String ng = addNextWord(ngram, word);
                //verify if the ngram already exist on the document
                if (ngramsTable.containsKey(ng)) {
                    ngramsTable.put(ng, ngramsTable.get(ng) + 1);
                } else {
                    ngramsTable.put(ng, 1);
                }
            }
        } catch (IOException e) {
           	throw new RuntimeException(e);
        }
    
        ArrayList<Ngram> ngrams = new ArrayList<>();
        for (Entry<String, Integer> entry : ngramsTable.entrySet()) {
            ngrams.add(new Ngram(entry.getKey(), entry.getValue()));
        }
        Collections.sort(ngrams);
        return ngrams;
        */
    }

    
    protected void saveToDataBase(Connection conn, int id, int type, String title, String research_address, String authors, String abs, String keywords, String authors_keywords, String references, int year, int times_cited, String doi, String begin_page, String end_page, String pdf_file, String journal, String journal_abbrev, String volume, int classId) {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "INSERT.CONTENT")) {
            stmt.setInt(1, id);
            stmt.setInt(2, id_collection);
            stmt.setInt(3, type);
            stmt.setString(4, title.substring(0, 1) + title.substring(1).toLowerCase(Locale.ENGLISH));
            stmt.setString(5, research_address);
            stmt.setString(6, abs);
            stmt.setString(7, keywords);
            stmt.setString(8, authors_keywords);
            stmt.setInt(9, year);
            stmt.setInt(10, times_cited);
            stmt.setString(11, doi);
            stmt.setString(12, begin_page);
            stmt.setString(13, end_page);
            stmt.setString(14, pdf_file);
            stmt.setString(15, journal);
            stmt.setString(16, journal_abbrev);
            stmt.setString(17, volume);
            stmt.setInt(18, classId);
            stmt.executeUpdate();

            if (authors != null) {
                saveAuthors(conn, id, authors);
            }
            if (references != null){
                saveReferences(conn, id, references);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving data to database", e);
        }
    }

    private void saveAuthors(Connection conn, int id, String authors) {
        StringTokenizer authorsTokenizer = new StringTokenizer(authors, "|");
        String author;
        int author_order = 0;
        int index_author;

        while (authorsTokenizer.hasMoreTokens()) {
            author = authorsTokenizer.nextToken().trim().toUpperCase();
            try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "SELECT.SAME.AUTHOR")) {
                stmt.setString(1, author);
                try (ResultSet result = stmt.executeQuery()) {
	                author_order++;
	                if (result.next()) {
	                    index_author = result.getInt(1);
	                    try (PreparedStatement stmt2 = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.AUTHOR")) {
		                    stmt2.setInt(1, id);
		                    stmt2.setInt(2, id_collection);
		                    stmt2.setInt(3, index_author);
		                    stmt2.setInt(4, author_order);
		                    stmt2.executeUpdate();
	                    }
	                } else {
	                	try (PreparedStatement stmt2 = sqlManager.getSqlStatement(conn, "INSERT.AUTHOR")) {
		                    stmt2.setString(1, author);
		                    stmt2.setInt(2, id_collection);
		                    stmt2.executeUpdate();
		                    try (ResultSet result2 = stmt2.getGeneratedKeys()) {
		                    	result2.next();
		                    	index_author = result2.getInt(1);
		                    }
	
		                	try (PreparedStatement stmt3 = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.AUTHOR")) {
				                stmt3.setInt(1, id);
				                stmt3.setInt(2, id_collection);
				                stmt3.setInt(3, index_author);
				                stmt3.setInt(4, author_order);
				                stmt3.executeUpdate();
			                }
		                }
		            }
	            }
            } catch (SQLException e) {
            	throw new RuntimeException("Error loading data from database", e);
            }
       }
    }

    private void saveReferences(Connection conn, int id, String references) {
        int id_author = 0;
        int id_ref = 0;
        String reference = null, aux;
        Matcher referenceMatcher;
        try {
            if ((references != null) && (references.contains("|"))) {
                StringTokenizer referencesTokenizer = new StringTokenizer(references, "|");
                String authorReference, doiReference, journalReference, volumeReference, pagesReference, typeReference, chapterReference, artnReference;

                int yearReference;
                while (referencesTokenizer.hasMoreTokens()) {
                    reference = referencesTokenizer.nextToken().trim();
                    referenceMatcher = referencePattern.matcher(reference);
                    if (referenceMatcher.matches()) {
                        id_ref = -1;
                        yearReference = -1;
                        journalReference = volumeReference = doiReference = pagesReference = chapterReference = artnReference = null;
                        typeReference = "CONF";

                        //splitting the reference
                        aux = referenceMatcher.group(1);
                        if (aux != null && !aux.isEmpty()) {
                            authorReference = aux.trim().replace(". ", "").replace(".", "").toUpperCase();
                            //trying to find out if the author of this reference is already on the authors table
                            try ( PreparedStatement stmtSameAuthor = sqlManager.getSqlStatement(conn, "SELECT.SAME.AUTHOR")) {
                            	stmtSameAuthor.setString(1, authorReference);
                                try (ResultSet resultSameAuthor = stmtSameAuthor.executeQuery()) {
                                	if (resultSameAuthor.next()) {
                                        id_author = resultSameAuthor.getInt(1);
                                    } else {
                                    	try (PreparedStatement stmtInsertAuthor = sqlManager.getSqlStatement(conn, "INSERT.AUTHOR")) {
                                    		stmtInsertAuthor.setString(1, authorReference);
                                    		stmtInsertAuthor.setInt(2, id_collection);
                                    		stmtInsertAuthor.executeUpdate();
                                            try (ResultSet resultAuthorId = stmtInsertAuthor.getGeneratedKeys()) {
                                            	resultAuthorId.next();
                                                id_author = resultAuthorId.getInt(1);                                            	
                                            }
                                    	}
                                    }    	
                                }
                            }
                        } else {
                            id_author = -1;
                        }

                        aux = referenceMatcher.group(10);
                        if (aux != null && !aux.isEmpty()) {
                            chapterReference = aux;
                        }

                        aux = referenceMatcher.group(6).trim();
                        if (aux != null && !aux.isEmpty()) {
                            journalReference = aux;
                        }

                        aux = referenceMatcher.group(3);
                        if (aux != null && !aux.isEmpty()) {
                            yearReference = Integer.valueOf(aux);
                        }

                        aux = referenceMatcher.group(8);
                        if (aux != null && !aux.isEmpty()) {
                            volumeReference = aux;
                        }

                        aux = referenceMatcher.group(12);
                        if (aux != null && !aux.isEmpty()) {
                            if (aux.startsWith("p") || aux.startsWith("P")) {
                                pagesReference = aux.substring(1);
                            } else {
                                pagesReference = aux;
                            }
                        }

                        aux = referenceMatcher.group(17);
                        if (aux != null && !aux.isEmpty()) {
                            doiReference = aux;
                        }

                        aux = referenceMatcher.group(15);
                        if (aux != null && !aux.isEmpty()) {
                            artnReference = aux;
                        }

                        //descobrindo se esta referencia já apareceu antes na colecao
                        if (doiReference != null) {
                            try (PreparedStatement stmtCitationWithDOI = sqlManager.getSqlStatement(conn, "SELECT.CITATION.WITH.DOI")) {
                            	stmtCitationWithDOI.setString(1, doiReference);
                            	stmtCitationWithDOI.setInt(2, id_collection);
                            	try (ResultSet resultCitationWithDOI = stmtCitationWithDOI.executeQuery()) {
                            		if (resultCitationWithDOI.next()) {
                            			id_ref = resultCitationWithDOI.getInt(1);
                            		}
                            	}
                            }
                        }

                        if (id_ref == -1) {
                            StringBuilder sqlStatement = new StringBuilder("SELECT id_citation FROM Citations Where");
                            sqlStatement.append(" type LIKE '").append(typeReference).append("'");
                            sqlStatement.append(" and id_author=").append(id_author);
                            sqlStatement.append(" and year=").append(yearReference);
                            if (volumeReference == null) {
                                sqlStatement.append(" and volume is null");
                            } else {
                                sqlStatement.append(" and volume='").append(volumeReference).append("'");
                            }
                            if (pagesReference == null) {
                                sqlStatement.append(" and pages is null");
                            } else {
                                sqlStatement.append(" and pages='").append(pagesReference).append("'");
                            }
                            if (journalReference == null) {
                                sqlStatement.append(" and journal is null");
                            } else {
                                sqlStatement.append(" and journal='").append(journalReference).append("'");
                            }

                            if (chapterReference == null) {
                                sqlStatement.append(" and chapter is null");
                            } else {
                                sqlStatement.append(" and chapter='").append(chapterReference).append("'");
                            }

                            if (artnReference == null) {
                                sqlStatement.append(" and artn is null");
                            } else {
                                sqlStatement.append(" and artn='").append(artnReference).append("'");
                            }

                            sqlStatement.append(" and id_collection=").append(this.id_collection);

                            try (
                            		PreparedStatement stmtGetCitations = sqlManager.createSqlStatement(conn, sqlStatement.toString());
                            		ResultSet resultCitations = stmtGetCitations.executeQuery()
                            ) {
                            	if (resultCitations.next()) {
                            		id_ref = resultCitations.getInt(1);
                            		try (PreparedStatement stmtInsertDocument2Reference = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.REFERENCE")) {
                            			stmtInsertDocument2Reference.setInt(1, id);
                            			stmtInsertDocument2Reference.setInt(2, id_collection);
                            			stmtInsertDocument2Reference.setInt(3, id_ref);
                            			stmtInsertDocument2Reference.executeUpdate();
                            		}
                            	} else {
                            		// inserindo referencia na tabela de citacoes
                            		try (PreparedStatement stmtInsertReference = sqlManager.getSqlStatement(conn, "INSERT.REFERENCE")) {
                            			stmtInsertReference.setInt(1, id_collection);
                            			stmtInsertReference.setInt(2, id_author);
                            			stmtInsertReference.setString(3, typeReference);
                            			stmtInsertReference.setInt(4, yearReference);
                            			stmtInsertReference.setString(5, journalReference);
                            			stmtInsertReference.setString(6, volumeReference);
                            			stmtInsertReference.setString(7, chapterReference);
                            			stmtInsertReference.setString(8, doiReference);
                            			stmtInsertReference.setString(9, pagesReference);
                            			stmtInsertReference.setString(10, artnReference);
                            			stmtInsertReference.setString(11, reference);
                            			stmtInsertReference.setInt(12, -1);
                            			stmtInsertReference.executeUpdate();
                            			try (ResultSet resultReferenceInsertion = stmtInsertReference.getGeneratedKeys()) {
                            				resultReferenceInsertion.next();
                            				id_ref = resultReferenceInsertion.getInt(1);
                            			}

                            			try (PreparedStatement stmtInsertDocument2Reference = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.REFERENCE")) {
                                			stmtInsertDocument2Reference.setInt(1, id);
                                			stmtInsertDocument2Reference.setInt(2, id_collection);
                                			stmtInsertDocument2Reference.setInt(3, id_ref);
                                			stmtInsertDocument2Reference.executeUpdate();
                                			stmtInsertDocument2Reference.close();
                            			}
                            		}
                            	}
                            }
                        }
                    }
                }
            }

        } catch (SQLException | NumberFormatException ex) {
            System.out.println("Documento: " + id);
            System.out.println("Referencia: " + id_ref);
            System.out.println("[REF DUPLICADA] " + reference);
//            throw new RuntimeException("Error loading data from database", e);
        }
    }

    protected String addNextWord(String[] ngram, String word) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ngram.length - 1; i++) {
            ngram[i] = ngram[i + 1];
            sb.append(ngram[i]).append("<>");
        }

        ngram[ngram.length - 1] = word;
        sb.append(word);

        return sb.toString();
    }
    
    private synchronized void setConnection(Connection conn) {
    	if (this.connection != null) {
    		throw new UnsupportedOperationException("Trying to set connection when on is already defined");
    	}
    	this.connection = conn;
    }

	protected Corpus createCollection() {
        // Check if the collection name already exist
        if (! collectionManager.isUnique(collection)) {
            throw new UnsupportedOperationException("A collection intitled \"" + collection + "\" already exists. Please choose another name.");
        }
        /*
        if (! collectionManager.isUnique(collection)) {
            int answer = JOptionPane.showOptionDialog(view, "A collection intitled \"" + collectionName + "\" already exists. Do you wish to replace this collection?", "Save Warning",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (answer == JOptionPane.YES_OPTION) {
                CollectionManager collectionManager = new CollectionManager();
                collectionManager.removeCollection(collection);
            }
        }
        */

		
		try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(connection, "INSERT.COLLECTION")) {
			stmt.setString(1, collection);
			stmt.setString(2, filename);
			stmt.setInt(3, nrGrams);
			stmt.setString(4, getDataType());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				id_collection = rs.getInt(1);
			}
			return null; // TODO: return Corpus 
		} catch (SQLException e) {
			throw new RuntimeException("Could not create and initialize collection into database", e);
		}
	}
	
    /**
     * Get short name to identify the type of data source (CSV, Json, BibTeX, ISI, etc).
     * 
     * @return String with name of the type of data imported.
     */
    protected abstract String getDataType();
    
    /**
     * Read data from source.
     */
    protected abstract void readData();

	@Override
	public Corpus getCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public boolean canImportData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean importData() {
		ConnectionManager connManager = ConnectionManager.getInstance();
		setLoadingDatabase(true);
				
		try (Connection conn = connManager.getConnection()) {
			setConnection(conn);
            dropIndexForBibliographicCoupling();
			createCollection();
			readData();
            matchReferencesToPapers();
            createIndexForBibliographicCoupling();
		} catch (Exception e) {
			throw new RuntimeException("Error importing data", e);
		} finally {
			setLoadingDatabase(false);
		}
		return true;
	}

	@Override
	public boolean isImportingData() {
		return isLoadingDatabase();
	}
	
	@Override
	public boolean cancel() {
		return true;
	}
}
