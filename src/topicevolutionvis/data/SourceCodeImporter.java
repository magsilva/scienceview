package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import topicevolutionvis.database.CollectionManager;
import topicevolutionvis.database.ConnectionManager;
import topicevolutionvis.database.SqlManager;
import topicevolutionvis.database.SqlUtil;
import topicevolutionvis.preprocessing.Ngram;
import topicevolutionvis.wizard.SourceCodeWizard;

/**
 *
 * @authorReference Danilo Sambugaro
 */
public abstract class SourceCodeImporter extends SwingWorker<Void, Void> {

    protected String collection, filename, language, path, msg = "";
    
    protected int id_collection;
    
    protected boolean removeStopwordsByTagging;
    
    protected boolean loadingDatabase = false;
    
	protected SourceCodeWizard view = null;
    
    protected CollectionManager collectionManager;
    
    Pattern referencePattern = Pattern.compile("\\*?([\\w\\s-\\.'()]{2,50})(,\\s(\\d{4}))?,(\\s(UNPUB|INPRESS))?\\s([\\w\\d\\s-\\.\\+\\&():\\d]+){1}(,\\s?[Vv]([\\w\\d-]+))?(,\\sCH([\\d\\w]+))?(,\\s(\\w([\\w\\d]+)))?(,\\sUNSP\\s[\\d\\w\\-/\\.()]+|,\\sARTN\\s([\\d\\w\\.]+))?(,\\sDOI\\s(.{5,100}))?");
    
    Pattern isiPattern = Pattern.compile("FN\\s.*|VR\\s.*\\s*|PT\\s.*\\s*|AU\\s.*\\s*|AF\\s.*\\s*|ED\\s.*\\s*|C1\\s.*\\s*|TI\\s.*\\s*|RID\\s.*\\s*|SO\\s.*\\s*|LA\\s.*\\s*|DI\\s.*\\s*|DT\\s.*\\s*|NR\\s.*\\s*|SN\\s.*\\s*|PU\\s.*\\s*|C1\\s.*\\s*|DE\\s.*\\s*|ID\\s.*\\s*|AB\\s.*\\s*|CR\\s.*\\s*|TC\\s.*\\s*|BP\\s.*\\s*|EP\\s.*\\s*|PG\\s.*\\s*|JI\\s.*\\s*|SE\\s.*\\s*|BS\\s.*\\s*|PY\\s.*\\s*|CY\\s.*\\s*|PD\\s.*\\s*|VL\\s.*\\s*|IS\\s.*\\s*|PN\\s.*\\s*|SU\\s.*\\s*|SI\\s.*\\s*|GA\\s.*\\s*|PI\\s.*\\s*|WP\\s.*\\s*|RP\\s.*\\s*|CP\\s.*\\s*|J9\\s.*\\s*|PA\\s.*\\s*|UT\\s.*\\s*|MH\\s.*\\s*|SS\\s.*\\s*|JC\\s.*\\s*|PS\\s.*\\s?\\s*|RC\\s.*\\s?\\s*|SC\\s.*\\s?\\s*|PE\\s.*\\s?\\s*|ER\\s?\\s*|EF\\s?");

    private ConnectionManager connManager;
    
    private SqlManager sqlManager;
    
    public SourceCodeImporter(String filename, String collection, String path, String language, SourceCodeWizard view, boolean removeStopwordsByTagging) {
        this.filename = filename;
        this.collection = collection;
        this.path = path;
        this.language = language;
        this.removeStopwordsByTagging = removeStopwordsByTagging;
        this.view = view;
        connManager = ConnectionManager.getInstance();
        sqlManager = SqlManager.getInstance();
        collectionManager = new CollectionManager();
    }

    @Override
    public void done() {
    	try {
    		if (! isCancelled()) {
        		get();
            }
    	} catch (ExecutionException e) {
    		Throwable realException = e.getCause();
    		throw new RuntimeException(realException);
    	} catch (InterruptedException e) {
    		throw new RuntimeException(e);
		} finally {
			if (view != null) {
				view.setStatus("Finished", false);
	            view.finishedLoadingCollection(collection, isCancelled());
			}
		}
       
    }

    public boolean isLoadingDatabase() {
		return loadingDatabase;
	}

	public void setLoadingDatabase(boolean loadingDatabase) {
		this.loadingDatabase = loadingDatabase;
	}
    
    protected void matchReferencesToPapers(Connection conn) {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "MATCH.CORE.REFERENCES")) {
            stmt.setInt(1, id_collection);
            stmt.setInt(2, id_collection);
            stmt.setInt(3, id_collection);
            stmt.setInt(4, id_collection);
            try (ResultSet result = stmt.executeQuery()) {
	            while (result.next()) {
	                int id_doc, id_ref;
	                id_doc = result.getInt(1);
	                id_ref = result.getInt(2);
	                try (PreparedStatement stmt2 = sqlManager.getSqlStatement(conn, "UPDATE.REFERENCE")) {
		                stmt2.setInt(1, id_doc);
		                stmt2.setInt(2, id_ref);
		                stmt2.setInt(3, id_collection);
		                stmt2.executeUpdate();
		                stmt2.close();
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


    protected void createIndexForBibliographicCoupling(Connection conn) {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "CREATE.INDEX.BC")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating index for bibliographic coupling", e);
        }
    }

    protected void dropIndexForBibliographicCoupling(Connection conn) {
        try (PreparedStatement stmt = sqlManager.getSqlStatement(conn, "DROP.INDEX.BC")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error reseting index for bibliographic coupling", e);
        }
    }

    public String multipleLines(BufferedReader in, String line) {
        StringBuilder content = new StringBuilder(line.substring(3).trim());
        try {
            in.mark(10000);
            while ((line = in.readLine()) != null) {
                if (! line.startsWith("   ")) {
                    in.reset();
                    break;
                } else {
                    content = content.append(" ").append(line.trim());
                    in.mark(10000);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading data from text", e);
        }

        return content.toString();
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
        int author_order = 0, index_author;

        try {
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
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading data from database", e);
        }
    }

    private void saveReferences(Connection conn, int id, String references) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        int id_author, id_ref = 0;
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
                            stmt = sqlManager.getSqlStatement(conn, "SELECT.SAME.AUTHOR");
                            stmt.setString(1, authorReference);
                            result = stmt.executeQuery();
                            if (result.next()) {
                                id_author = result.getInt(1);
                                result.close();
                                stmt.close();
                            } else {
                            	result.close();
                            	stmt.close();
                                stmt = sqlManager.getSqlStatement(conn, "INSERT.AUTHOR");
                                stmt.setString(1, authorReference);
                                stmt.setInt(2, id_collection);
                                stmt.executeUpdate();
                                result = stmt.getGeneratedKeys();
                                result.next();
                                id_author = result.getInt(1);
                                result.close();
                                stmt.close();
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
                            stmt = sqlManager.getSqlStatement(conn, "SELECT.CITATION.WITH.DOI");
                            stmt.setString(1, doiReference);
                            stmt.setInt(2, id_collection);
                            result = stmt.executeQuery();
                            if (result.next()) {
                                id_ref = result.getInt(1);
                            }
                            result.close();
                            stmt.close();
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

                            stmt = sqlManager.createSqlStatement(conn, sqlStatement.toString());
                            result = stmt.executeQuery();

                            if (result.next()) {
                                id_ref = result.getInt(1);
                                result.close();
                                stmt.close();
                                stmt = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.REFERENCE");
                                stmt.setInt(1, id);
                                stmt.setInt(2, id_collection);
                                stmt.setInt(3, id_ref);
                                stmt.executeUpdate();
                                result.close();
                                stmt.close();
                            } else {
                                //inserindo referencia na tabela de citacoes
                                result.close();
                                stmt.close();
                                stmt = sqlManager.getSqlStatement(conn, "INSERT.REFERENCE");
                                stmt.setInt(1, id_collection);
                                stmt.setInt(2, id_author);
                                stmt.setString(3, typeReference);
                                stmt.setInt(4, yearReference);
                                stmt.setString(5, journalReference);
                                stmt.setString(6, volumeReference);
                                stmt.setString(7, chapterReference);
                                stmt.setString(8, doiReference);
                                stmt.setString(9, pagesReference);
                                stmt.setString(10, artnReference);
                                stmt.setString(11, reference);
                                stmt.setInt(12, -1);
                                stmt.executeUpdate();

                                result = stmt.getGeneratedKeys();
                                result.next();
                                id_ref = result.getInt(1);
                                result.close();
                                stmt.close();

                                stmt = sqlManager.getSqlStatement(conn, "INSERT.DOCUMENT.TO.REFERENCE");
                                stmt.setInt(1, id);
                                stmt.setInt(2, id_collection);
                                stmt.setInt(3, id_ref);
                                stmt.executeUpdate();
                                stmt.close();
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
        } finally {
            SqlUtil.close(result);
            SqlUtil.close(stmt);
        }
    }
}
