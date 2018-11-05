/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view.tools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import scienceview.datamining.dimensionreduction.LDAOutput;
import scienceview.datamining.preprocessing.steemer.StemmerType;
import scienceview.datamining.projection.ProjectionData;
import scienceview.datamining.termweight.MatrixTransformationType;
import scienceview.datamining.topicmodeling.TopicData;
import scienceview.datarepresentation.LDARepresentation;
import scienceview.datarepresentation.RepresentationFactory;
import scienceview.datarepresentation.RepresentationType;
import scienceview.projection.temporal.TemporalProjection;
import scienceview.ui.desktop.view.ToolOptions;
import scienceview.utils.matrix.SparseMatrix;

/**
 *
 * @author USER
 */
public class LDAProgressDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;

    private int num_topics, num_iterations;
    private double alpha, beta;
    private TemporalProjection projection;
    private Thread thread;

    /**
     * Creates new form LDAProgressDialog
     */
    public void start() {
        thread = new Thread() {
            @Override
            public void run() {
                setProgressBar("This process may take a few minutes...", true);
                try {
                    LDARepresentation representation = (LDARepresentation) RepresentationFactory.getInstance(RepresentationType.LDA, projection.getDatabaseCorpus());
                    ProjectionData pdata = (ProjectionData) projection.getProjectionData().clone();
                    pdata.setNumberOfTopics(num_topics);
                    pdata.setNumberOfLDAIterations(num_iterations);
                    pdata.setAlpha(alpha);
                    pdata.setBeta(beta);
                    pdata.setStemmer(StemmerType.Type.NONE);
                    pdata.setMatrixTransformationType(MatrixTransformationType.NONE);
                    SparseMatrix matrix = representation.getMatrix(pdata);
                    projection.getProjectionData().setLDAMatrices(new LDAOutput(representation.getAlphabet(), matrix, representation.getSortedWords()));
                } catch (IOException | CloneNotSupportedException ex) {
                    Logger.getLogger(LDAProgressDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                setProgressBar("Finished", false);
                ToolOptions.getInstance().ldaSettingsButton.setEnabled(true);
                projection.getTopicData().setTopicType(TopicData.TopicType.LDA);
                dispose();
            }
        };
        thread.start();
    }

    public void stop() {
        try {
            setProgressBar("Cancelled", false);
            this.cancelButton.setEnabled(false);
            projection.getProjectionData().setLDAMatrices(null);
            Thread.sleep(1000);
            this.thread.stop();
            dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(LDAProgressDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LDAProgressDialog(javax.swing.JDialog parent, TemporalProjection projection, int num_topics, int num_iterations, double alpha, double beta) {
        super(parent);
        initComponents();
        this.num_topics = num_topics;
        this.num_iterations = num_iterations;
        this.alpha = alpha;
        this.beta = beta;
        this.projection = projection;
        this.setLocationRelativeTo(null);
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
        progressLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LDA Model");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        progressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Progress"));
        progressPanel.setLayout(new java.awt.GridBagLayout());

        progressLabel.setMaximumSize(new java.awt.Dimension(750, 14));
        progressLabel.setMinimumSize(new java.awt.Dimension(250, 14));
        progressLabel.setPreferredSize(new java.awt.Dimension(250, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        progressPanel.add(progressLabel, gridBagConstraints);

        progressBar.setMinimumSize(new java.awt.Dimension(300, 14));
        progressBar.setPreferredSize(new java.awt.Dimension(300, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        progressPanel.add(progressBar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(progressPanel, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(9, 3, 3, 3);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.stop();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void setProgressBar(String progressText, boolean value) {
        this.progressLabel.setText(progressText);
        this.progressBar.setIndeterminate(value);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JPanel progressPanel;
    // End of variables declaration//GEN-END:variables
}
