/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis;

import topicevolutionvis.database.DatabaseManager;
import topicevolutionvis.view.ScienceViewMainFrame;

public class Main {

    protected static ScienceViewMainFrame mainFrame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
    	if (args.length > 0 && "createdb".equals(args[0])) {
    		dbm.createDatabase();
    	}
    	
    	if (dbm.checkDatabase() == false) {
    		dbm.createDatabase();
    	};
    	
        mainFrame = ScienceViewMainFrame.getInstance();
        mainFrame.display();
    }
}
