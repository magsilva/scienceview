/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SaveProjectionDialog.java
 *
 * Created on Sep 28, 2011, 3:49:02 PM
 */
package scienceview.ui.desktop.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import scienceview.SystemPropertiesManager;
import scienceview.projection.temporal.SaveTemporalProjection;
import scienceview.projection.temporal.TemporalProjection;
import scienceview.ui.desktop.view.filefilter.ZipFileFilter;

/**
 *
 * @author aretha
 */
public class SaveProjectionDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;

    private TemporalProjection tproj;

    /**
     * Creates new form SaveProjectionDialog
     */
    public SaveProjectionDialog(TemporalProjection tproj) {
        super(ScienceViewMainFrame.getInstance());
        initComponents();
        this.setLocationRelativeTo(null);
        this.tproj = tproj;
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

        buttonsPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        progressPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        filesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filenameTextField = new javax.swing.JTextField();
        searchDatabaseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Save Temporal Projection");
        setMinimumSize(new java.awt.Dimension(520, 250));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 15);
        buttonsPanel.add(saveButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 3, 3);
        buttonsPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(buttonsPanel, gridBagConstraints);

        progressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Progress"));

        jProgressBar1.setMinimumSize(new java.awt.Dimension(300, 14));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(300, 14));
        progressPanel.add(jProgressBar1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(progressPanel, gridBagConstraints);

        filesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("File"));
        filesPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Filename:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        filesPanel.add(jLabel1, gridBagConstraints);

        filenameTextField.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        filesPanel.add(filenameTextField, gridBagConstraints);

        searchDatabaseButton.setText("Search...");
        searchDatabaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchDatabaseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        filesPanel.add(searchDatabaseButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 29;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(filesPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    public void setStatus(boolean value) {
        this.jProgressBar1.setIndeterminate(value);
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        String filename = this.filenameTextField.getText().trim();
        if (filename.compareTo("") != 0) {
            (new SaveTemporalProjection(filename, this, tproj)).execute();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void searchDatabaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchDatabaseButtonActionPerformed
        final JFileChooser fc = new JFileChooser() {

            @Override
            public void approveSelection() {
                File file = getSelectedFile();
                if (file != null && file.exists()) {
                    String message = "The file \"" + file.getName() + "\" already exists. \n"
                            + "Do you want to replace the existing file?";
                    int answer = JOptionPane.showOptionDialog(this, message, "Save Warning",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    if (answer == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                super.approveSelection();
            }
        };
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        SystemPropertiesManager m = SystemPropertiesManager.getInstance();
        String directory = m.getProperty("SAVE.DIR");
        if (directory != null) {
            fc.setCurrentDirectory(new File(directory));
        } else {
            fc.setCurrentDirectory(new File("."));
        }
        fc.addChoosableFileFilter(new ZipFileFilter());
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getAbsolutePath();
            m.setProperty("SAVE.DIR", fc.getSelectedFile().getParent());

            //checking if the name finishes with the correct extension
            if (!filename.toLowerCase().endsWith(".zip")) {
                filename = filename.concat(".zip");
            }
            filenameTextField.setText(filename);
            filenameTextField.setCaretPosition(0);
        }
    }//GEN-LAST:event_searchDatabaseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField filenameTextField;
    private javax.swing.JPanel filesPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchDatabaseButton;
    // End of variables declaration//GEN-END:variables
}
