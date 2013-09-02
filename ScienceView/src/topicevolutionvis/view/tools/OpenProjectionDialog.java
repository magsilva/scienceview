/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OpenProjectionDialog.java
 *
 * Created on Sep 29, 2011, 4:42:37 PM
 */
package topicevolutionvis.view.tools;

import javax.swing.JFileChooser;
import topicevolutionvis.projection.temporal.OpenTemporalProjection;
import topicevolutionvis.util.SystemPropertiesManager;
import topicevolutionvis.utils.filefilter.ZipFileFilter;
import topicevolutionvis.view.ScienceViewMainFrame;

/**
 *
 * @author aretha
 */
public class OpenProjectionDialog extends javax.swing.JDialog {

    /**
     * Creates new form OpenProjectionDialog
     */
    public OpenProjectionDialog() {
        super(ScienceViewMainFrame.getInstance());
        initComponents();
        this.setLocationRelativeTo(ScienceViewMainFrame.getInstance());
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

        progressPanel = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        buttonPanel = new javax.swing.JPanel();
        openButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        filenameTextField = new javax.swing.JTextField();
        filenameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open Temporal Projection");
        setMinimumSize(new java.awt.Dimension(386, 137));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        progressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Progress"));

        jProgressBar1.setMinimumSize(new java.awt.Dimension(300, 14));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(300, 14));
        progressPanel.add(jProgressBar1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 58;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(progressPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        openButton.setText("Open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 15);
        buttonPanel.add(openButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 3, 3);
        buttonPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(buttonPanel, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("File"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        searchButton.setText("Search...");
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

        filenameTextField.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(filenameTextField, gridBagConstraints);

        filenameLabel.setText("Filename:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(filenameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setStatus(boolean value) {
        this.jProgressBar1.setIndeterminate(value);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        SystemPropertiesManager m = SystemPropertiesManager.getInstance();
        String directory = m.getProperty("SAVE.DIR");
        final JFileChooser fc = new JFileChooser(directory);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.addChoosableFileFilter(new ZipFileFilter());

        int result = fc.showOpenDialog(this);
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
    }//GEN-LAST:event_searchButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        String filename = this.filenameTextField.getText().trim();
        if (filename.compareTo("") != 0) {
            (new OpenTemporalProjection(filename, this)).execute();
        }
    }//GEN-LAST:event_openButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel filenameLabel;
    private javax.swing.JTextField filenameTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JButton openButton;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables
}
