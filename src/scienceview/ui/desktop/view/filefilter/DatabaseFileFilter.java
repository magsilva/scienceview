/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view.filefilter;

/**
 *
 * @author Aretha
 */
public class DatabaseFileFilter extends MyFileFilter {


    @Override
    public String getDescription() {
        return "Database File (*.db)";
    }

    @Override
    public String getFileExtension() {
        return "db";
    }

    @Override
    public String getProperty() {
        return "DB.DIR";
    }
}
