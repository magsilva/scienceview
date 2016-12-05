/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicevolutionvis.view.tools;

import topicevolutionvis.topic.TopicData;
import topicevolutionvis.view.ToolOptions;

/**
 *
 * @author USER
 */
public class LDADialog extends javax.swing.JDialog {

    private static LDADialog instance;
    private TopicData tdata = null;
    private static final long serialVersionUID = 1L;

    public static LDADialog getInstance(TopicData tdata) {
        if (instance == null) {
            instance = new LDADialog(tdata);
        }
        return instance;
    }

    /**
     * Creates new form LDADialog
     */
    public LDADialog(TopicData tdata) {
        super(ToolOptions.getInstance());
        this.tdata = tdata;
        initComponents();
        this.setLocationRelativeTo(this.getParent());
        this.pack();
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        numberOfTopicsTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        iterationsTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        alphaTextField = new javax.swing.JTextField();
        betaTextField = new javax.swing.JTextField();
        refreshAlphaButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LDA Topic");
        setMinimumSize(new java.awt.Dimension(450, 200));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Help24.gif"))); // NOI18N
        jLabel1.setText("<html>To extract topics using LDA, an LDA model has to be <br>generated first.\n Please enter the following parameters:</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 15, 3);
        getContentPane().add(jLabel1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Number of topics:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel2, gridBagConstraints);

        numberOfTopicsTextField.setColumns(5);
        numberOfTopicsTextField.setText("100");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(numberOfTopicsTextField, gridBagConstraints);

        jLabel3.setText("Number of iterations:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel3, gridBagConstraints);

        iterationsTextField.setColumns(5);
        iterationsTextField.setText("1000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(iterationsTextField, gridBagConstraints);

        jLabel4.setText("Alpha:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 3, 3);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Beta:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 15, 3, 3);
        jPanel1.add(jLabel5, gridBagConstraints);

        alphaTextField.setColumns(5);
        alphaTextField.setText("0.1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(alphaTextField, gridBagConstraints);

        betaTextField.setColumns(5);
        betaTextField.setText("0.01");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(betaTextField, gridBagConstraints);

        refreshAlphaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Refresh16.gif"))); // NOI18N
        refreshAlphaButton.setToolTipText("Calculate alpha as a function of the number of topics (50/T)");
        refreshAlphaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAlphaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(refreshAlphaButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 30);
        jPanel2.add(okButton, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshAlphaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAlphaButtonActionPerformed
        this.alphaTextField.setText(String.valueOf((float) 50 / (float) Integer.parseInt(this.numberOfTopicsTextField.getText())));
        this.alphaTextField.setCaretPosition(0);
    }//GEN-LAST:event_refreshAlphaButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.tdata.setLdaNumberOfTopics(Integer.valueOf(this.numberOfTopicsTextField.getText()));
        this.tdata.setLdaNumberOfIterations(Integer.valueOf(this.iterationsTextField.getText()));
        this.tdata.setLdaAlpha(Double.valueOf(this.alphaTextField.getText()));
        this.tdata.setLdaBeta(Double.valueOf(this.betaTextField.getText()));
        LDAProgressDialog progressDialog = new LDAProgressDialog(ToolOptions.getInstance(), ToolOptions.getInstance().getTemporalProjectionViewer().getTemporalProjection(),
                tdata.getLdaNumberOfTopics(), tdata.getLdaNumberOfIterations(), tdata.getLdaAlpha(), tdata.getLdaBeta());
        this.dispose();
        progressDialog.setVisible(true);
        progressDialog.start();
    }//GEN-LAST:event_okButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alphaTextField;
    private javax.swing.JTextField betaTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField iterationsTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField numberOfTopicsTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JButton refreshAlphaButton;
    // End of variables declaration//GEN-END:variables
}