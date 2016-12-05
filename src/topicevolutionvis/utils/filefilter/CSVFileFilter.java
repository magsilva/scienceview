package topicevolutionvis.utils.filefilter;

/**
 *
 * @author Aretha
 */
public class CSVFileFilter extends MyFileFilter {

    @Override
    public String getDescription() {
        return "CSV File (*.csv)";
    }

    @Override
    public String getProperty() {
        return "CSV.DIR";
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}