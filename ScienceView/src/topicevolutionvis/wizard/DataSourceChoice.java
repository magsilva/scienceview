/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DataSourceChoice.java
 *
 * Created on 14/05/2009, 15:02:37
 */
package topicevolutionvis.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import topicevolutionvis.data.*;
import topicevolutionvis.database.CollectionsManager;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.util.SystemPropertiesManager;
import topicevolutionvis.utils.filefilter.*;

/**
 *
 * @author Aretha
 */
public class DataSourceChoice extends WizardPanel implements ActionListener {

    private ProjectionData pdata;
    private DatabaseImporter importer = null;

    /**
     * Creates new form DataSourceChoice
     */
    public DataSourceChoice(ProjectionData pdata) {
        this.pdata = pdata;
        initComponents();
        this.updateCollections("");
    }

    private void updateCollections(String collection) {
        String index = null;
        this.corpusComboBox.removeAllItems();
        ArrayList<String> collections = CollectionsManager.getCollections();
        this.corpusComboBox.addItem("Select...");
        for (String col : collections) {
            this.corpusComboBox.addItem(col);
            if (collection.equalsIgnoreCase(col)) {
                index = col;
            }
        }
        if (collection.equalsIgnoreCase("")) {
            corpusComboBox.setSelectedIndex(0);
        }
        if (index != null) {
            corpusComboBox.setSelectedItem(index);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        filenameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        collectionnameTextField = new javax.swing.JTextField();
        nrgramsLabel = new javax.swing.JLabel();
        nrgramsComboBox = new javax.swing.JComboBox();
        removeStopwordsByTaggingCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        statusProgressBar = new javax.swing.JProgressBar();
        statusLabel = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        corpusComboBox = new javax.swing.JComboBox();
        removeButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Choose the Data Source"));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Document Collection"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        filenameTextField.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(filenameTextField, gridBagConstraints);

        jLabel1.setText("File:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Collection name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(jLabel2, gridBagConstraints);

        collectionnameTextField.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(collectionnameTextField, gridBagConstraints);

        nrgramsLabel.setText("Number of Grams:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel3.add(nrgramsLabel, gridBagConstraints);

        nrgramsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel3.add(nrgramsComboBox, gridBagConstraints);

        removeStopwordsByTaggingCheckBox.setText("Remove terms tagged as verbs, conjunctions ...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel3.add(removeStopwordsByTaggingCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        statusProgressBar.setPreferredSize(new java.awt.Dimension(400, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(statusProgressBar, gridBagConstraints);

        statusLabel.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(statusLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jPanel4, gridBagConstraints);

        searchButton.setText("Search ...");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(searchButton, gridBagConstraints);

        loadButton.setText("Load");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        jPanel5.add(loadButton);

        cancelButton.setText("Cancel");
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel5.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Collection already loaded into the database"));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(corpusComboBox, java.awt.BorderLayout.CENTER);

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jPanel2.add(removeButton, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        SystemPropertiesManager m = SystemPropertiesManager.getInstance();
        String directory = m.getProperty("COLLECTIONS.DIR");
        if (directory != null) {
            fc.setCurrentDirectory(new File(directory));
        } else {
            fc.setCurrentDirectory(new File("."));
        }
        SupportedFormatsCollections generalFilter = new SupportedFormatsCollections();
        fc.addChoosableFileFilter(generalFilter);
        fc.addChoosableFileFilter(new BibTeXFileFilter());
        fc.addChoosableFileFilter(new ISIFileFilter());
        fc.addChoosableFileFilter(new EndnoteExportFileFilter());
        fc.addChoosableFileFilter(new DatabaseFileFilter());
        fc.setFileFilter(generalFilter);
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            filenameTextField.setText(file.getAbsolutePath());
            filenameTextField.setCaretPosition(0);
            this.collectionnameTextField.setText(file.getName().substring(0, file.getName().indexOf(".")));
            m.setProperty("COLLECTIONS.DIR", fc.getSelectedFile().getParent());
        }
}//GEN-LAST:event_searchButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (this.corpusComboBox.getSelectedIndex() > 0) {
            try {
                String collection = (String) this.corpusComboBox.getSelectedItem();
                CollectionsManager.removeCollection(CollectionsManager.getCollectionId(collection));
                this.corpusComboBox.removeItem(collection);
            } catch (IOException ex) {
                Logger.getLogger(DataSourceChoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "A collection must be selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    public void setStatus(String status, boolean running) {
        this.statusLabel.setText(status);
        this.statusProgressBar.setIndeterminate(running);
    }

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        String collectionName = this.collectionnameTextField.getText().trim();
        String filename = this.filenameTextField.getText().trim();
        if (filename.compareTo("") != 0 && collectionName.compareTo("") != 0) {
            this.pdata.setSourceFile(filename);
            String corpusType = filename.substring(filename.lastIndexOf(".") + 1);
            int nrGrams = Integer.valueOf((String) this.nrgramsComboBox.getSelectedItem()).intValue();
            if (corpusType.compareTo("bib") == 0) {
                importer = new BibtexDatabaseImporter(filename, collectionName, nrGrams, this, removeStopwordsByTaggingCheckBox.isSelected());
            } else if (corpusType.compareTo("isi") == 0) {
                importer = new ISICorpusDatabaseImporter(filename, collectionName, nrGrams, this, removeStopwordsByTaggingCheckBox.isSelected());
            } else if (corpusType.compareTo("enw") == 0) {
                importer = new EndnoteDatabaseImporter(filename, collectionName, nrGrams, this, removeStopwordsByTaggingCheckBox.isSelected());
            } else if (corpusType.compareTo("db") == 0) {
                importer = new DumpDatabaseImporter(filename, this, removeStopwordsByTaggingCheckBox.isSelected());
            }

            if (importer != null) {
                this.cancelButton.setEnabled(true);
                this.setStatus("Loading collection " + collectionName + "...", true);
                this.loadingCollection();
                importer.execute();
            }

        } else {
            JOptionPane.showMessageDialog(this, "All parameters must be filled to load a new collection", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.importer.cancel(true);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void loadingCollection() {
        this.loadButton.setEnabled(false);
        this.filenameTextField.setEnabled(false);
        this.collectionnameTextField.setEnabled(false);
        this.searchButton.setEnabled(false);
        this.nrgramsComboBox.setEnabled(false);
        this.removeButton.setEnabled(false);
    }

    public void finishedLoadingCollection(String collection, boolean canceled) {
        if (!canceled) {
            this.updateCollections(collection);
            this.filenameTextField.setText(null);
            this.collectionnameTextField.setText(null);
            this.cancelButton.setEnabled(false);
        }
        this.filenameTextField.setEnabled(true);
        this.collectionnameTextField.setEnabled(true);
        this.searchButton.setEnabled(true);
        this.nrgramsComboBox.setEnabled(true);
        this.loadButton.setEnabled(true);
        this.removeButton.setEnabled(true);
    }

    public DataSourceChoice reset() {
        return this;
    }

    @Override
    public void refreshData() {
        String collectionName = (String) this.corpusComboBox.getSelectedItem();
        if (collectionName.compareToIgnoreCase("Select...") != 0) {
            this.pdata.setCollectionName(collectionName);

        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton cancelButton;
    private javax.swing.JTextField collectionnameTextField;
    private javax.swing.JComboBox corpusComboBox;
    private javax.swing.JTextField filenameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton loadButton;
    private javax.swing.JComboBox nrgramsComboBox;
    private javax.swing.JLabel nrgramsLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JCheckBox removeStopwordsByTaggingCheckBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JProgressBar statusProgressBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
