/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis;

import topicevolutionvis.database.CreateDataBase;
import topicevolutionvis.view.ScienceViewMainFrame;

public class Main {

    protected static ScienceViewMainFrame mainFrame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	if (args.length > 0 && "createdb".equals(args[0])) {
    		CreateDataBase cd = new CreateDataBase();
    		cd.create();
    	}
        mainFrame = ScienceViewMainFrame.getInstance();
        mainFrame.display();
    }
}
