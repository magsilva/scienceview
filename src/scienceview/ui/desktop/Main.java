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

package scienceview.ui.desktop;

import scienceview.database.DatabaseManager;
import scienceview.ui.desktop.view.ScienceViewMainFrame;

/**
 * Main class for desktop version of ScienceView.
 */
public class Main {

    protected static ScienceViewMainFrame mainFrame;

    /**
     * Run a new desktop instance of ScienceView.
     * 
     * @param args the command line arguments. Currently, we only support the "createdb"
     * argument (at command line), which will reset/reconfigure the database. If no
     * argument is configured and the database has not been properly initialized, we will
     * automatically create a database.
     */
    public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
    	if (args.length > 0 && "createdb".equals(args[0])) {
    		dbm.createDatabase();
    	}
    	
    	if (! dbm.isDatabaseReady()) {
    		dbm.createDatabase();
    	};
    	
        mainFrame = ScienceViewMainFrame.getInstance();
        mainFrame.display();
    }
}
