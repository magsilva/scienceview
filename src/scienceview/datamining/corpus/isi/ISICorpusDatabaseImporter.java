package scienceview.datamining.corpus.isi;

import java.io.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import scienceview.PExConstants;
import scienceview.database.AbstractDatabaseImporter;
import scienceview.database.SqlManager;
import scienceview.datarepresentation.Ngram;
import scienceview.ui.desktop.wizard.DataImportWizard;

/**
 * Import data from ISI format
 *
 * @author Aretha
 */
public class ISICorpusDatabaseImporter extends AbstractDatabaseImporter
{
	private static final Pattern isiPattern = Pattern.compile("FN\\s.*|VR\\s.*\\s*|PT\\s.*\\s*|AU\\s.*\\s*|AF\\s.*\\s*|ED\\s.*\\s*|C1\\s.*\\s*|TI\\s.*\\s*|RID\\s.*\\s*|SO\\s.*\\s*|LA\\s.*\\s*|DI\\s.*\\s*|DT\\s.*\\s*|NR\\s.*\\s*|SN\\s.*\\s*|PU\\s.*\\s*|C1\\s.*\\s*|DE\\s.*\\s*|ID\\s.*\\s*|AB\\s.*\\s*|CR\\s.*\\s*|TC\\s.*\\s*|BP\\s.*\\s*|EP\\s.*\\s*|PG\\s.*\\s*|JI\\s.*\\s*|SE\\s.*\\s*|BS\\s.*\\s*|PY\\s.*\\s*|CY\\s.*\\s*|PD\\s.*\\s*|VL\\s.*\\s*|IS\\s.*\\s*|PN\\s.*\\s*|SU\\s.*\\s*|SI\\s.*\\s*|GA\\s.*\\s*|PI\\s.*\\s*|WP\\s.*\\s*|RP\\s.*\\s*|CP\\s.*\\s*|J9\\s.*\\s*|PA\\s.*\\s*|UT\\s.*\\s*|MH\\s.*\\s*|SS\\s.*\\s*|JC\\s.*\\s*|PS\\s.*\\s?\\s*|RC\\s.*\\s?\\s*|SC\\s.*\\s?\\s*|PE\\s.*\\s?\\s*|ER\\s?\\s*|EF\\s?");

	private static final Pattern referencePattern = Pattern.compile("\\*?([\\w\\s-\\.'()]{2,50})(,\\s(\\d{4}))?,(\\s(UNPUB|INPRESS))?\\s([\\w\\d\\s-\\.\\+\\&():\\d]+){1}(,\\s?[Vv]([\\w\\d-]+))?(,\\sCH([\\d\\w]+))?(,\\s(\\w([\\w\\d]+)))?(,\\sUNSP\\s[\\d\\w\\-/\\.()]+|,\\sARTN\\s([\\d\\w\\.]+))?(,\\sDOI\\s(.{5,100}))?");
	
    private static final Pattern authorP = Pattern.compile("[a-zA-Z\\-\\s]{2,}, [A-Z][a-z]+");
    
    private static final Pattern authorPattern2 = Pattern.compile("[a-zA-Z\\-\\s]{2,}, [A-Z]+");
    
    private static final Pattern authorPattern3 = Pattern.compile("[a-zA-Z\\-\\s]{2,}, [[A-Z\\.][\\s]?]+");

	// TODO: Move this to JabRef
	public static final String FILE_EXTENSION = ".isi";

    public ISICorpusDatabaseImporter(String filename, String collection, int nrGrams, DataImportWizard view, boolean removeStopwordsByTagging) {
        super(filename, collection, nrGrams, view, removeStopwordsByTagging);
    }
    
	@Override
	public boolean isDataValid() {
		return true;
	}

	@Override
	public int getNumberOfDocuments() {
		return 0;
	}

	@Override
	protected String getDataType() {
		return "ISI";
	}
	
	@Override
	protected void readData() {
        String line = "";
        try {
            HashMap<String, Double> corpusNgrams = new HashMap<String, Double>();
            Integer index = -1, type = -1;
            String tag, title, authors, abs, author_keywords, keywords, references, doi, aux, journal, journal_abbrev, volume, begin_page, end_page, research_address;
            title = authors = research_address = doi = abs = keywords = author_keywords = references = journal = journal_abbrev = volume = begin_page = end_page = null;
            Integer year = 0, times_cited = 0;
            StringBuilder content = new StringBuilder();
            ArrayList<Ngram> fngrams;
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
            title = authors = research_address = doi = abs = author_keywords = keywords = references = journal = journal_abbrev = volume = begin_page = end_page = null;
            year = 0;
            times_cited = 0;
            index++;
            
            while ((line = in.readLine()) != null) {
          
            	try {
            		tag = line.substring(0, 2);
            	} catch (IndexOutOfBoundsException e) {
            		continue;
            	}
            	
                if (tag.compareTo("FN") == 0) {
                	continue;
                } else if (tag.compareTo("VR") == 0) {
                	continue;
                } else if (tag.compareTo("PT") == 0) {
                	type = -1;
                	aux = line.substring(3).trim();
                	if (aux.compareToIgnoreCase("J") == 0 || aux.compareToIgnoreCase("JOUR") == 0) {
                		type = PExConstants.JOURNAL_ARTICLE;
                	} else if (aux.compareToIgnoreCase("B") == 0) {
                        type = PExConstants.BOOK;
                    } else if (aux.compareToIgnoreCase("BOOK CHAPTER") == 0) {
                        type = PExConstants.BOOK_CHAPTER;
                    } else if (aux.compareToIgnoreCase("C") == 0 || aux.compareToIgnoreCase("CPAPER") == 0) {
                        type = PExConstants.CONFERENCE_PAPER;
                    } else if (aux.compareToIgnoreCase("GOVERNMENT REPORT") == 0) {
                        type = PExConstants.REPORT;
                    } else if (aux.compareToIgnoreCase("ENCYCLOPEDIA ENTRY") == 0) {
                    	type = PExConstants.ENCYCLOPEDIA_ENTRY;
                    } else if (aux.compareToIgnoreCase("NEWSLETTER") == 0) {
                        type = PExConstants.NEWSLETTER;
                    } else if (aux.compareToIgnoreCase("DICTIONARY ENTRY") == 0) {
                        type = PExConstants.DICTIONARY_ENTRY;
                    } else if (aux.compareToIgnoreCase("LECTURE") == 0) {
                        type = PExConstants.LECTURE;
                    }
                } else if (tag.compareTo("TI") == 0) {
                    title = multipleLines(in, line);
                    content = new StringBuilder(title);
                } else if (tag.compareTo("AU") == 0) {
                    authors = processAuthors(in, line);
                } else if (tag.compareTo("AB") == 0 | tag.compareTo("ABS") == 0) {
                    abs = multipleLines(in, line);
                    content = content.append(" ").append(abs);
                } else if (tag.compareTo("PY") == 0) {
                    year = Integer.valueOf(line.substring(3).trim());
                } else if (tag.compareTo("CR") == 0) {
                    references = processReferences(in, line);
                } else if (tag.compareTo("ID") == 0 || tag.compareTo("KW") == 0) {
                    keywords = multipleLines(in, line);
                    content = content.append(" ").append(keywords);
                } else if (tag.compareTo("TC") == 0) {
                    times_cited = Integer.valueOf(line.substring(3));
                } else if (tag.compareTo("VL") == 0) {
                    volume = line.substring(3);
                } else if (tag.compareTo("DI") == 0) {
                    doi = line.substring(3).trim();
                } else if (tag.compareTo("C1") == 0) {
                    research_address = this.multipleLinesWithDelimiter(in, line);
                } else if (tag.compareTo("SO") == 0 || tag.compareTo("JF") == 0) {
                    journal = line.substring(3).trim();
                } else if (tag.compareTo("J9") == 0) {
                    journal_abbrev = line.substring(3).trim();
                } else if (tag.compareTo("DE") == 0) {
                    author_keywords = multipleLines(in, line);
                    content = content.append(" ").append(author_keywords);
                } else if (tag.compareTo("BP") == 0) {
                    begin_page = line.substring(3);
                } else if (tag.compareTo("EP") == 0) {
                    end_page = line.substring(3);
                } else if (tag.compareTo("ER") == 0) {
                    saveToDataBase(connection, index, type, title, research_address, authors, abs, keywords, author_keywords, references, year, times_cited, doi, begin_page, end_page, "", journal, journal_abbrev, volume, 0);
                    fngrams = getNgramsFromFile(content.toString());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(fngrams);
                    oos.flush();

                    //inserting the ngrams
                    try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(connection, "UPDATE.NGRAMS.DOCUMENT")) {
                    	stmt.setBytes(1, baos.toByteArray());
                    	stmt.setInt(2, index);
                    	stmt.setInt(3, id_collection);
                    	stmt.executeUpdate();
                    }
                    
                    for (Ngram n : fngrams) {
                        if (corpusNgrams.containsKey(n.ngram)) {
                            corpusNgrams.put(n.ngram, corpusNgrams.get(n.ngram) + n.frequency);
                        } else {
                            corpusNgrams.put(n.ngram, n.frequency);
                        }
                    }
                    title = authors = research_address = doi = abs = author_keywords = keywords = references = journal = journal_abbrev = volume = begin_page = end_page = null;
                    year = 0;
                    times_cited = 0;
                    index++;
                }
            }

            //add the ngrams to the collection
            ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
            for (Entry<String, Double> e : corpusNgrams.entrySet()) {
            	ngrams.add(new Ngram(e.getKey(), e.getValue()));
            }
            Collections.sort(ngrams);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ngrams);
            oos.flush();

            try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(connection, "UPDATE.NGRAMS.COLLECTION")) {
            	stmt.setBytes(1, baos.toByteArray());
            	stmt.setInt(2, id_collection);
            	stmt.executeUpdate();
            }
        } catch (Exception e) {
        	throw new RuntimeException("Could not save collection to database", e);
        }
    }

    private String processReferences(BufferedReader in, String line) {
        try {
            StringBuilder value;
            String aux;
            ArrayList<String> lines = new ArrayList<>();
            Matcher referenceMatcher;

            aux = line.substring(2).trim();
            if (aux.compareTo("") != 0) {
                lines.add(aux);
            }

            in.mark(300);
            String previous;
            while ((line = in.readLine().trim()) != null) {
                if (isiPattern.matcher(line).matches()) {
                    in.reset();
                    break;
                } else {
                    if (line.startsWith("10.")) {
                        previous = lines.get(lines.size() - 1);
                        if (previous.endsWith("DOI")) {
                            lines.set(lines.size() - 1, previous.concat(" " + line.trim()));
                        }
                        in.mark(300);
                    } else {
                        referenceMatcher = referencePattern.matcher(line);
                        if (referenceMatcher.find(0)) {
                            lines.add(line);
                        }
                        in.mark(300);
                    }
                }
            }
            value = new StringBuilder();
            for (int i = 0; i < lines.size() - 1; i++) {
                value.append(lines.get(i)).append("|");
            }
            if (!lines.isEmpty()) {
                value.append(lines.get(lines.size() - 1));
            }

            return value.toString();
        } catch (IOException e) {
        	throw new RuntimeException("Could not save references n to database", e);
        }
    }

    private String processAuthors(BufferedReader in, String line) {
        int index;
        line = line.substring(3).trim().replace(",", "");
        StringBuilder value = new StringBuilder(line);
        String author;
    	try {
            in.mark(1000);
            while (((line = in.readLine()) != null)) {
                if (! line.startsWith("   ")) {
                    in.reset();
                    break;
                } else {
                    author = line.trim();
                    if (authorP.matcher(author).matches()) {
                        index = author.indexOf(" ");
                        author = author.substring(0, index + 2).replace(",", "");
                    }
                    if (authorPattern2.matcher(author).matches()) {
                        author = author.replace(",", "");
                    }
                    if (authorPattern3.matcher(author).matches()) {
                        author = author.replace(". ", "").replace(".", "").replace(",", "");
                    }
                    value.append(" | ").append(author);
                    in.mark(1000);
                }
            }
        } catch (IOException e) {
        	throw new RuntimeException("Could not save split authors", e);
        }
        return value.toString();
    }

    private String multipleLines(BufferedReader in, String line) {
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

    
    private String multipleLinesWithDelimiter(BufferedReader in, String line) {
        try {
            if (line.contains("C1")) {
                line = line.substring(3).trim();
            }
            StringBuilder value = new StringBuilder(line);
            in.mark(1000);
            while (((line = in.readLine()) != null)) {
                if (isiPattern.matcher(line).matches()) {
                    in.reset();
                    break;
                } else {
                    value.append("|").append(line.trim());
                    in.mark(1000);
                }
            }
            return value.toString();
        } catch (IOException e) {
        	throw new RuntimeException("Could not split lines", e);
        }
    }
    
    // TODO: rewrite this to let stopwords removal, either by tagging or by natural language analysis, to another class at pre-processing
    private ArrayList<Ngram> getNgramsFromFile(String content) {
        if (this.removeStopwordsByTagging) {
            return this.getNgramsFromFileRemovingStopwordsByTagging(content);
        } else {
            return this.getNgramsFromFileWithStopwordsListOnly(content);
        }
    }
    
    // TODO: rewrite this to let stopwords removal, either by tagging or by natural language analysis, to another class at pre-processing
    public ArrayList<Ngram> getNgramsFromFileWithStopwordsListOnly(String content) {
        HashMap<String, Integer> ngramsTable = new HashMap<>();
        InputStream modelIn = null; //rules_POS = null;
        if (content != null) {
            try {

                modelIn = new FileInputStream("resources/en-token.bin");
                TokenizerModel model = new TokenizerModel(modelIn);
                Tokenizer tokenizer = new TokenizerME(model);
                String paras[] = tokenizer.tokenize(content);

                //create the first ngram
                String[] ngram = new String[nrGrams];
                int i = 0, count = 0;
                while (count < nrGrams && i < paras.length) {
                    if (paras[i].trim().length() > 0 && !paras[i].matches("[\\p{Punct}\\p{Digit}]+|'s")) {
                        String word = paras[i].toLowerCase();
                        if (word.trim().length() > 0) {
                            ngram[count] = word;
                            count++;
                        }
                    }
                    i++;
                }

                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < ngram.length - 1; j++) {
                    sb.append(ngram[j]).append("<>");
                }
                sb.append(ngram[ngram.length - 1]);

                //adding to the frequencies table
                ngramsTable.put(sb.toString(), 1);

                //creating the remaining ngrams
                String word;
                while (i < paras.length) {
                    if (paras[i].trim().length() > 0 && !paras[i].matches("[\\p{Punct}\\p{Digit}']+|'s")) {
                        word = paras[i].toLowerCase();

                        if (word.trim().length() > 0) {
                            String ng = addNextWord(ngram, word);

                            //verify if the ngram already exist on the document
                            if (ngramsTable.containsKey(ng)) {
                                ngramsTable.put(ng, ngramsTable.get(ng) + 1);
                            } else {
                                ngramsTable.put(ng, 1);
                            }
                        }
                    }
                    i++;
                }
            } catch (IOException e) {
                Logger.getLogger(AbstractDatabaseImporter.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                if (modelIn != null) {
                    try {
                        modelIn.close();
                    } catch (IOException e) {
                    }
                }
            }
        }




        ArrayList<Ngram> ngrams = new ArrayList<>();
        for (Entry<String, Integer> entry : ngramsTable.entrySet()) {
            ngrams.add(new Ngram(entry.getKey(), entry.getValue()));
        }
        Collections.sort(ngrams);
        return ngrams;
    }    
}
