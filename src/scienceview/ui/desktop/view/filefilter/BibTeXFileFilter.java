/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view.filefilter;

/**
 *
 * @author Aretha
 */
public class BibTeXFileFilter extends MyFileFilter {

    @Override
    public String getDescription() {
        return "BibTeX File (*.bib)";
    }

    @Override
    public String getProperty() {
        return "BIB.DIR";
    }

    @Override
    public String getFileExtension() {
        return "bib";
    }
}
