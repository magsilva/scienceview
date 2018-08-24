/*
***** BEGIN LICENSE BLOCK *****
Copyright (c) 2018 Marco Aurélio Graciotto Silva, UTFPR, Campo Mourão/PR, Brazil

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


package topicevolutionvis.data;

/**
 * Create collection (DatabaseCorpus) from a set of data.
 */
public interface DatabaseImporter
{
    /**
     * Verify data to be import (validity check);
     * 
     * @return True if data to be imported is valid.
     */
    boolean isDataValid();
	
    /**
     * Get collection number of references of the collection (this takes place
     * before importing the data). It only works if the data is valid!
     * 
     * @return Number of references.
     */
    int getNumberOfReferences();
    
    /**
     * Get collection number of documents of the collection (this takes place
     * before importing the data). It only works if the data is valid!
     * 
     * @return Number of references.
     */
    int getNumberOfDocuments();

    /**
     * Prepare to save data. It is not required to call this method before importing!
     * However, it is useful to check any precondition to save the data, such as:
     * - check data validity
     * - check if importer is properly configured (for instance, database is ready)
     * 
     * @return True if it is ready to import data. 
     */
    boolean canImportData();
    
    /**
     * Start the importing process.
     * @return
     */
    boolean importData();
    
    /**
     * Check if the importer is still working (it often runs several threads
     * asynchronously).
     * 
     * @return True if it still importing data.
     */
    boolean isImportingData();
    
    /**
     * Cancel data import.
     * 
     * @return True if the import process has finished completelyy, False if any data
     * was not still not imported.
     */
    boolean cancel();
        
    /**
     * When the process of importing data has ended, this method should be
     * called to assure that any persistence operation has been completed.
     */
    void done();

    /**
     * After finishing the importation process, a Collection should have been create.
     * This method returns it.
     * 
     * @return Collection created using the imported data.
     */
    Corpus getCollection();
}