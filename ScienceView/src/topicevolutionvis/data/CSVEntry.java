package topicevolutionvis.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.ironiacorp.computer.ComputerSystem;
import com.ironiacorp.computer.Filesystem;
import com.ironiacorp.computer.OperationalSystem;
import com.ironiacorp.string.StringUtil;

public class CSVEntry {

	private static final String FILENAME_EXTENSION = ".csv";
	private Reader reader;
	public File outputFile;
	
	public void setInputFilename(String filename) {
		File file = new File(filename);
		setInputFile(file);
	}
	
	public void setInputFile(File file) {
		String extension;
	
		if (! file.exists() || ! file.isFile()) {
			throw new IllegalArgumentException("Invalid CSV file");
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
//			setOutputFile(file + ISICorpusDatabaseImporter.FILE_EXTENSION);	
		} else {
//			setOutputFile(file.getAbsolutePath().replaceAll("\\" + CSVEntry.FILENAME_EXTENSION + "$", ISICorpusDatabaseImporter.FILE_EXTENSION));
		}
	}
}