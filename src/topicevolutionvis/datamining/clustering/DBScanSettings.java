/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DBScanSettings.java
 *
 * Created on 02/08/2011, 17:13:46
 */
package topicevolutionvis.datamining.clustering;

import java.util.logging.Level;
import java.util.logging.Logger;

import scienceview.ui.desktop.view.ScienceViewMainFrame;
import scienceview.ui.desktop.view.TemporalProjectionViewer;

/**
 *
 * @author Aretha
 */
public class DBScanSettings extends javax.swing.JDialog {

    private TemporalProjectionViewer viewer;

    /** Creates new form DBScanSettings
     * @param viewer */
    public DBScanSettings(TemporalProjectionViewer viewer) {
        super(ScienceViewMainFrame.getInstance());
        initComponents();
        this.setLocationRelativeTo(ScienceViewMainFrame.getInstance());
        this.viewer = viewer;
    }

    public void setStatus(String status, boolean running) {
        this.statusLabel.setText(status);
        this.statusProgressBar.setIndeterminate(running);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        generateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        epsSpinner = new javax.swing.JSpinner();
        minPtsSpinner = new javax.swing.JSpinner();
        progressPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        statusProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DBScan Settings");
        setMinimumSize(new java.awt.Dimension(400, 400));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        generateButton.setText("Run");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(generateButton, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(3, 23, 3, 3);
        jPanel2.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Epsilon:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("minPoints:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel2, gridBagConstraints);

        epsSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.0d, 1.0d, 0.1d));
        epsSpinner.setToolTipText("radius of the epsilon-range-queries");
        epsSpinner.setMinimumSize(new java.awt.Dimension(60, 28));
        epsSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(epsSpinner, gridBagConstraints);

        minPtsSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(6), Integer.valueOf(1), null, Integer.valueOf(1)));
        minPtsSpinner.setToolTipText(" minimun number of instances required in an epsilon-range-query");
        minPtsSpinner.setMinimumSize(new java.awt.Dimension(60, 280));
        minPtsSpinner.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(minPtsSpinner, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        progressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Progress"));
        progressPanel.setLayout(new java.awt.GridBagLayout());

        statusLabel.setMinimumSize(new java.awt.Dimension(100, 17));
        statusLabel.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        progressPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        progressPanel.add(statusProgressBar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(progressPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        try {
            DBScanCluster clusterer = new DBScanCluster(viewer, this, (Double) this.epsSpinner.getValue(), (Integer) this.minPtsSpinner.getValue());
            clusterer.execute();
        } catch (Exception ex) {
            Logger.getLogger(DBScanSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generateButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JSpinner epsSpinner;
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner minPtsSpinner;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JProgressBar statusProgressBar;
    // End of variables declaration//GEN-END:variables
}
