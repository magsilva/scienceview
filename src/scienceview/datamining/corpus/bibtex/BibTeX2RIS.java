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

package scienceview.datamining.corpus.bibtex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Collection;

import com.ironiacorp.computer.ComputerSystem;
import com.ironiacorp.computer.Filesystem;
import com.ironiacorp.computer.OperationalSystem;
import com.ironiacorp.string.StringUtil;

import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;
import org.jbibtex.ParseException;

import scienceview.datamining.corpus.isi.ISICorpusDatabaseImporter;

public class BibTeX2RIS
{
	// TODO: move this to JabRef
	private static final String FILENAME_EXTENSION = ".bib";
	
	private boolean referencesEnabled = true;
	
	private BibTeXDatabase database;
	
	private Reader reader;

	public File outputFile;
	
	public PrintWriter outputWriter;
	
	public void setInputFilename(String filename) {
		File file = new File(filename);
		setInputFile(file);
	}
	
	public void setInputFile(File file) {
		String extension;
	
		if (! file.exists() || ! file.isFile()) {
			throw new IllegalArgumentException("Invalid BibTeX file");
		}
		
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found: " + file, e);
		}

		OperationalSystem os = ComputerSystem.getCurrentOperationalSystem();
		Filesystem fs = os.getFilesystem();
		extension = fs.getExtension(file);
		if (StringUtil.isEmpty(extension)) {
			setOutputFile(file + ISICorpusDatabaseImporter.FILE_EXTENSION);	
		} else {
			setOutputFile(file.getAbsolutePath().replaceAll("\\" + BibTeX2RIS.FILENAME_EXTENSION + "$", ISICorpusDatabaseImporter.FILE_EXTENSION));
		}
	}

	public void setInputStream(InputStream is) {
		setInputStream(is, Charset.defaultCharset());
	}

	public void setInputStream(InputStream is, Charset encoding) {
		reader = new InputStreamReader(is, encoding);
	}
	
	public File getOutputFile() {
		return outputFile;
	}
	
    public void setOutputFile(String filename) {
    	File file = new File(filename);
    	setOutputFile(file);
	}

    public void setOutputFile(File file) {
    	outputFile = file;
    }
    
    
    public void readData() {
	try {
		BibTeXParser bibtexParser = new org.jbibtex.BibTeXParser();
		database = bibtexParser.parse(reader);
	} catch (ParseException e) {
		throw new RuntimeException("Error parsing BibTeX file", e);
	}
    }
    
    /**
     * Convert the BibTeX file into ISI data format. The result is saved
     * to a file (which is defined when instantiating the class or with the
     * method setOutputFile()).
     * 
     * @throws IOException
     */
	public void convert() 
	{
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile);
		} catch (IOException e) {
			throw new RuntimeException("Cannot write data to output file: " + outputFile);
		}
		/**
		 * especificando o segundo parametro como true, os dados serão enviados para o arquivo a toda chamada do método println(), 
		 * caso contrário, os dados só são enviados quando enviar uma quebra de linha.
		 */
		outputWriter = new PrintWriter(writer, true);

		printBeginOfRecord();
		
		Map<Key, BibTeXEntry> entryMap = database.getEntries();
		Collection<BibTeXEntry> entries = entryMap.values();	
		for (BibTeXEntry entry : entries) {
/*
			crossrefKey = entry.getField("crossref");
			crossref = null;
			if (crossrefKey != null && crossrefKey.isEmpty() == false) {
				crossref = database.getEntryByKey(crossrefKey);
				if (crossref == null) {
					throw new RuntimeException("Missing cross-reference: " + crossrefKey);
				}
			} else {
				// We do not have crossref, so try to download data from current bibtex entry
				crossref = entry;
			}
*/
			Key entryType = entry.getType();
			if (entryType == BibTeXEntry.TYPE_INPROCEEDINGS || entryType == BibTeXEntry.TYPE_CONFERENCE) {
				printField("PT", "CPAPER");
				printEntry(entry);
/*
				printField("PY", crossref.getField("year").toUserString());
				printField("JF", crossref.getField("booktitle").toUserString());
				
				if (crossref.getField("location") != null) {
					printField("CY", crossref.getField("address").toUserString());
				} else {
					if (crossref.getField("address") != null) {
						printField("CY", crossref.getField("address"));
					}
				}
*/
			} else if (entry.getType() == BibTeXEntry.TYPE_ARTICLE) {
				printField("PT", "JOUR");
				printEntry(entry);
/*
				printField("JF", crossref.getField("journal").toUserString());
				printField("VL", entry.getField("volume").toUserString());
				printField("PY", entry.getField("year").toUserString());
				printField("CY", crossref.getField("address").toUserString());
*/
			}
			printEndOfEntry();
		}
		
		printEndOfRecord();
	}

	private String convertAuthor(String author, String separator) {
		StringBuilder sb = new StringBuilder();
		String[] words;
		
		words = author.split(",");
		if (words.length > 1) {
			author = words[1] + " " + words[0];
		}
		
		words = author.trim().replace("  ", " ").split("\\s");
		sb.append(words[words.length - 1].trim());
		sb.append(separator);
		sb.append(words[0].trim().charAt(0));
		return sb.toString();
	}
	
	private String replaceChars(String data) {
		 if (data == null) {
			 throw new IllegalArgumentException(new NullPointerException());
		 }

		 // "Expand" macros
		 if(data.contains("#")){
			 data = data.replace("#", "");
		 }
			 
		 // Remove "&"
		 if(data.contains("\\&")){
			 data = data.replace("\\&", "");
		 }

		 // Remove \{ and \}
		 if(data.contains("\\{")){
			 data = data.replace("{", "");
		 }
		 if(data.contains("\\}")){
			 data = data.replace("}", "");
		 }

		 // Remove { and }
		 if(data.contains("{")){
			 data = data.replace("{", "");
		 }
		 if(data.contains("}")){
			 data = data.replace("}", "");
		 }
	 			 
		 return data;
	}
	
	private void printEndOfEntry() {
		outputWriter.println("ER\n");
	}

	private void printBeginOfRecord() {
		outputWriter.println("FN ScienceView");
		outputWriter.println("VR 1.0\n");
	}
	
	private void printEndOfRecord() {
		outputWriter.println("EF");
	}

	
	private void printField(String name, String... values) {
		if (name == null || values == null || values.length == 0 || values[0] == null) {
			return;
		}
		
		String risFieldName = replaceChars(name);
		String risFieldValue = replaceChars(values[0]);
		outputWriter.printf(risFieldName);
		outputWriter.printf(" ");
		outputWriter.println(risFieldValue);
		for (int i = 1; i < values.length; i++) {
			risFieldValue = replaceChars(values[i]);
			outputWriter.printf("   ");
			outputWriter.println(risFieldValue);
		}
	}



	private String[] getReferences(String referencesField) {
		StringBuilder sb = new StringBuilder();
		String[] entryKeys = referencesField.split(",");
		String[] references = new String[entryKeys.length];
		
		for (int i = 0; i < entryKeys.length; i++) {
			String entryKey = entryKeys[i];
			BibTeXEntry entry;
			String[] authors;
			String year = null;
			String doi;
			int pages = 0;

			entry = database.resolveEntry(new Key(entryKey.trim()));
			if (entry == null) {
				System.out.println("Could not find the following reference: " + entryKey);
				continue;
			}

			authors = entry.getField(new Key("author")).toUserString().split(" and ");
			year =  entry.getField(new Key("year")).toUserString();

			if (entry.getType() == BibTeXEntry.TYPE_BOOK) {
				pages = Integer.parseInt(entry.getField(new Key("pages")).toUserString());
			} else {
				try {
					int[] pageRange;
					pageRange = pages(entry.getField(new Key("pages")).toUserString());
					pages = pageRange[1] - pageRange[0];
				} catch (Exception e) {
					if (entry.getType() == BibTeXEntry.TYPE_MISC) {
						pages = 0;
					}
				}
			}
				
			doi = entry.getField(new Key("doi")).toUserString();
				
			sb.setLength(0);
			sb.append(convertAuthor(authors[0], " "));
			sb.append(", ");
			sb.append(year);
			if (entry.getType() == BibTeXEntry.TYPE_ARTICLE) {
				sb.append(", ");
				sb.append(entry.getField(new Key("journal")).toUserString());
				sb.append(", ");
				sb.append("V");
				sb.append(entry.getField(new Key("volume")).toUserString());
			}
			if (entry.getType() == BibTeXEntry.TYPE_INPROCEEDINGS) {
				sb.append(", ");
				sb.append(entry.getField(new Key("booktitle")).toUserString());
			}
			if (entry.getType() == BibTeXEntry.TYPE_BOOK) {
				sb.append(", ");
				sb.append(entry.getField(new Key("booktitle")).toUserString());
			}
			if (entry.getType() == BibTeXEntry.TYPE_MISC) {
				sb.append(", ");
				sb.append(entry.getField(new Key("title")).toUserString());
			}
			
			sb.append(", ");
			sb.append("P");
			sb.append(pages);
			if (doi != null) {
				sb.append(", ");
				sb.append("DOI ");
				sb.append(doi);
			}
			references[i] = sb.toString();
		}
		return references;
	}

	private int[] pages(String field) {
		String[] subfields;
		int[] pages = new int[2];
		
		try {
			field = field.replace("--", "-");
			subfields = field.split("-");
			if (subfields[0].contains(":")) {
				pages[0] = Integer.valueOf(subfields[0].split(":")[1]);
			} else {
				pages[0] = Integer.valueOf(subfields[0]);
			}
			if (subfields[1].contains(":")) {
				pages[1] = Integer.valueOf(subfields[1].split(":")[1]);
			} else {
				pages[1] = Integer.valueOf(subfields[1]);
			}
		} catch (NullPointerException e) {
			pages[0] = 0;
			pages[1] = 0;
		} catch (Exception e) {
			System.out.println("Bad pages format: " + field);
		}

		return pages;
	
	 }

	private void printEntry(BibTeXEntry entry) {
		// autor
		/**
		 * tem que dar split em espaço+and+espaço, porque no meio do nome pode formar
		 * a string and. Com espaço garanto que não vai quebrar no meio de nenhum nome.
		 */
		String[] authors = entry.getField(new Key("author")).toUserString().split(" and "); 
		for (int i = 0; i < authors.length; i++) {
			authors[i] = convertAuthor(authors[i], ", ");
		}
		printField("AU", authors);

		String lang = entry.getField(new Key("lang")).toUserString();
		String title;
		String abs;
		String keywords;
		if (entry.getField(new Key("lang")) != null) {
			Value titleValue = entry.getField(new Key("title-" + lang));
			Value absValue = entry.getField(new Key("abstract-" + lang));
			Value keywordsValue = entry.getField(new Key("keywords-" + lang));
			if (titleValue == null || absValue == null || keywordsValue == null) {
				title = entry.getField(new Key("title")).toUserString();
				abs = entry.getField(new Key("abstract")).toUserString();
				keywords = entry.getField(new Key("keywords")).toUserString();
			} else {
				title = titleValue.toUserString();
				abs = absValue.toUserString();
				keywords = keywordsValue.toUserString();
			}
		} else {
			title = entry.getField(new Key("title")).toUserString();
			abs = entry.getField(new Key("abstract")).toUserString();
			keywords = entry.getField(new Key("keywords")).toUserString();
		}
		printField("TI", title);
		printField("AB", abs);
		printField("KW", keywords);
				
		int[] pages = pages(entry.getField(new Key("pages")).toUserString());
		printField("BP", Integer.toString(pages[0]));
		printField("EP", Integer.toString(pages[1]));
	
		printField("DI", entry.getField(new Key("doi")).toUserString());	

		if (referencesEnabled) {
		  	String references = entry.getField(new Key("references")).toUserString();
		   	if (references != null) {
		   		printField("CR", getReferences(references));
		   	}
		}
	}
}

