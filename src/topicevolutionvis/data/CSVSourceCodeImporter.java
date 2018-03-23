package topicevolutionvis.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.ironiacorp.computer.ComputerSystem;
import com.ironiacorp.computer.Filesystem;
import com.ironiacorp.computer.OperationalSystem;
import com.ironiacorp.computer.OperationalSystemDetector;
import com.ironiacorp.computer.OperationalSystemType;

import topicevolutionvis.database.ConnectionManager;
import topicevolutionvis.database.SqlManager;
import topicevolutionvis.matrix.SparseMatrix;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.wizard.SourceCodeWizard;

/**
 * Importer of CSV data.
 * 
 * CSV should consist only of numbers (integer or real number), which are internally stored as double.
 * If any text field is found, it will be ignored when creating the bag of words (but it can be stored
 * within the document class).
 */
public class CSVSourceCodeImporter extends SourceCodeImporter {

	public CSVSourceCodeImporter(String filename, String collection, String path, String language, SourceCodeWizard sourceCodeWizard) {
		super(filename, collection, path, language,sourceCodeWizard, false);
	}

	@Override
	protected Void doInBackground() {
		ConnectionManager connManager = ConnectionManager.getInstance();
		setLoadingDatabase(true);
		
		try (Connection conn = connManager.getConnection()) {
			createCollection(conn);
			readCSVFile(conn);
		} catch (Exception e) {
			throw new RuntimeException("Error loading CSV file", e);
		} finally {
			setLoadingDatabase(false);
		}
		return null;
	}

	private void createCollection(Connection conn) {
		try (PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "INSERT.COLLECTION")) {
			stmt.setString(1, collection);
			stmt.setString(2, filename);
			stmt.setInt(3, 1);
			stmt.setString(4, "csv");
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				id_collection = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Could not create and initialize collection into database", e);
		}
	}
	
	private void readCSVFile(Connection conn) throws IOException, SQLException {
		CSVFormat format = CSVFormat.newFormat(':');
		SparseMatrix sm = new SparseMatrix();
		List<String> header = new ArrayList<String>();
		List<Integer> numericFields = new ArrayList<Integer>();
		List<Integer> othersFields = new ArrayList<Integer>();
		Pattern yearPattern = Pattern.compile(".*(\\d+).*");
		String separator = System.getProperty("file.separator");

		
		try (
				Reader in = new FileReader(filename);
				CSVParser parser = format.parse(in);
		) {
			Iterable<CSVRecord> records = parser.getRecords();
			for (CSVRecord record : records) {
				int currentLine = (int) record.getRecordNumber();
				int recordId = currentLine - 2;  // We must subtract two because (1) the first line contains the header, not data; and (2) the count starts at 1 :-)
				if (currentLine == 1) {
					Iterator<String> it = record.iterator();
					while (it.hasNext()) {
						String label = it.next();
						header.add(label);
					}
				} else {
					String filename = null;
					String rawContent = null;
					String description = null;
					double[] featuresVector;
					int year = -1;

					// Discover quantity of fields that are numeric or that are string
					if (currentLine == 2) {
						for (int i = 0; i < record.size(); i++) {
							try {
								String data = record.get(i);
								Double.parseDouble(data);
								numericFields.add(i);
							} catch (Exception e) {
								// Not a numeric header.
								othersFields.add(i);
							}
						}
						sm.setDimensions(numericFields.size());
					}

					// Aux variables
//					String pathExercise = null;
//					String pathDescriptionExercise = null;
//					String[] chunks;
					
					// TODO: check if csv is valid (fields at header equals to fields at rows)
					// Read values, skipping columns with text (for now, just the first column)
					featuresVector = new double[numericFields.size()];
					Iterator<Integer> numericFieldIterator = numericFields.iterator();
					int currentFeaturesVectorIndex = 0;
					while (numericFieldIterator.hasNext()) {
						int numericFieldIndex = numericFieldIterator.next();
						String data = record.get(numericFieldIndex);
						double value = Double.parseDouble(data);
						featuresVector[currentFeaturesVectorIndex] = value;
						currentFeaturesVectorIndex++;
					}
					sm.addRow(featuresVector, recordId);

					Iterator<Integer> otherFieldIterator = othersFields.iterator();
					while (otherFieldIterator.hasNext()) {
						int otherFieldIndex = otherFieldIterator.next();
						String data = getCodePath(record.get(otherFieldIndex));

						// Get file's name
						filename = path.toString() + data;
						

						// Get file's description
//						chunks = data.split(Pattern.quote(separator));
//						pathDescriptionExercise = new String(chunks[1].trim());
//						pathExercise = path.toString() + separator + chunks[0].trim() + separator + chunks[1].trim();

						// Guess "year"
//						Matcher yearMatcher = yearPattern.matcher(pathDescriptionExercise);
//						if (yearMatcher.matches()) {
//							year = Integer.parseInt(yearMatcher.group(1));
//						} else {
//							System.out.println("Could not find a year for: " + record.toString());
//						}
						
						// Read file's content
						try (BufferedReader bf = new BufferedReader(new FileReader(filename))) {
							String line = null;
							rawContent = new String();
							while ((line = bf.readLine()) != null) {
								rawContent = rawContent + line;
								rawContent = rawContent + "\n";
							}
							System.out.println(rawContent);
						}
						

					}
					
//					rawContent = "teste";
					description = "teste";
					year = 99;
					saveToDataBase(conn, recordId, 0, record.get(0), null, null, rawContent, description, null, null, year, 0, null, null, null, "", null, null, null, 0);
					
					// TODO: it should be a SparseVector. However, the SELECT.SPARSEMATRIX.DOCUMENT is never required (we just need the sparsematrix of the collection)
					// Insert SparseMatrix on the document's table
					try (
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(baos);
							PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.DOCUMENT");
					) {
						oos.writeObject(sm);
						oos.flush();
						stmt.setBytes(1, baos.toByteArray());
						stmt.setInt(2, recordId);
						stmt.setInt(3, id_collection);
						stmt.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
			

		// Save collection's sparse matrix
		// TODO: Remove view dependence
//		ProjectionData pData = this.view.getPdata();
//		pData.setMatrix(sm);

		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			PreparedStatement stmt = SqlManager.getInstance().getSqlStatement(conn, "UPDATE.SPARSEMATRIX.COLLECTION");
		) {
			oos.writeObject(sm);
			oos.flush();
			stmt.setBytes(1, baos.toByteArray());
			stmt.setInt(2, id_collection);
			stmt.executeUpdate();
		}
	}
	
	private String getCodePath(String file){
		OperationalSystem currentOS = ComputerSystem.getCurrentOperationalSystem();
		Filesystem fs = currentOS.getFilesystem();
		OperationalSystemDetector osd = new OperationalSystemDetector();
		OperationalSystemType fileOSType = osd.detectOSfromFilename(file);
		OperationalSystem fileOS;
		
		if (fileOSType == null || fileOSType.os == null) {
			throw new RuntimeException("Cannot detect filesystem type and properly convert the filename");
		}
		fileOS = fileOSType.os;
		return fs.convertFilename(file, fileOS);
	}
	
//	private String readDescriptionExerciseFile(String path, String separator) throws IOException {
//		File file = new File(path);
//		String folderName = file.getName();
//		file = new File(file.getAbsolutePath() + separator + folderName + ".txt");
//		try (FileInputStream fis = new FileInputStream(file)) {
//			byte[] data = new byte[(int) file.length()];
//			fis.read(data);
//			return new String(data, "UTF-8");
//		}
//		
//	}

}
