/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CosinePlusReferencesSettings.java
 *
 * Created on 29/06/2011, 17:24:11
 */
package scienceview.ui.desktop.wizard;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;

import scienceview.datamining.projection.ProjectionData;
import scienceview.datamining.termrepresentation.Reference;
import scienceview.ui.desktop.view.tools.ZipfCurveReferences;

/**
 *
 * @author Aretha
 */
public class CosinePlusReferencesSettings extends javax.swing.JDialog {

    private static CosinePlusReferencesSettings instance;
    private ProjectionData pdata;
    private DefaultTableModel tableModel;

    /** Creates new form CosinePlusReferencesSettings */
    public CosinePlusReferencesSettings(javax.swing.JDialog parent) {
        super(parent);
        initModels();
        initComponents();
        this.setLocationRelativeTo(null);
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

        dataPanel = new javax.swing.JPanel();
        JPanel = new javax.swing.JPanel();
        curvePanel = new ZipfCurveReferences();
        frequencyPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        referencesTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        numberofreferencesTextField = new javax.swing.JTextField();
        cutoffPanel = new javax.swing.JPanel();
        lowercutPanel = new javax.swing.JPanel();
        lowercutLabel = new javax.swing.JLabel();
        lowercutSlider = new javax.swing.JSlider();
        lowercutButtonPanel = new javax.swing.JPanel();
        lowercutPlusButton = new javax.swing.JButton();
        lowercutTextField = new javax.swing.JTextField();
        lowercutMinusButton = new javax.swing.JButton();
        uppercutPanel = new javax.swing.JPanel();
        uppercutLabel = new javax.swing.JLabel();
        uppercutSlider = new javax.swing.JSlider();
        uppercutButtonPanel = new javax.swing.JPanel();
        uppercutPlusButton = new javax.swing.JButton();
        uppercutTextField = new javax.swing.JTextField();
        uppercutMinusButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Upper and Lower Cuts for References");
        setMinimumSize(new java.awt.Dimension(250, 200));
        setModal(true);

        dataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));
        dataPanel.setLayout(new java.awt.BorderLayout());

        JPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Curve"));
        JPanel.setLayout(new java.awt.BorderLayout());

        curvePanel.setPreferredSize(new java.awt.Dimension(300, 100));
        curvePanel.setLayout(new java.awt.BorderLayout());
        JPanel.add(curvePanel, java.awt.BorderLayout.CENTER);

        dataPanel.add(JPanel, java.awt.BorderLayout.CENTER);

        frequencyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("References and Frequency"));
        frequencyPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(246, 351));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(246, 351));

        referencesTable.setModel(this.tableModel);
        jScrollPane1.setViewportView(referencesTable);

        frequencyPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Number of References Selected:");
        jPanel3.add(jLabel1);

        numberofreferencesTextField.setColumns(5);
        numberofreferencesTextField.setEditable(false);
        jPanel3.add(numberofreferencesTextField);

        frequencyPanel.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        dataPanel.add(frequencyPanel, java.awt.BorderLayout.EAST);

        cutoffPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cut-off Configuration"));
        cutoffPanel.setLayout(new java.awt.BorderLayout());

        lowercutPanel.setLayout(new java.awt.BorderLayout());

        lowercutLabel.setText("Lower Cut");
        lowercutPanel.add(lowercutLabel, java.awt.BorderLayout.WEST);

        lowercutSlider.setMajorTickSpacing(1);
        lowercutSlider.setSnapToTicks(true);
        lowercutSlider.setValue(0);
        lowercutSlider.setInverted(true);
        lowercutSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lowercutSliderStateChanged(evt);
            }
        });
        lowercutPanel.add(lowercutSlider, java.awt.BorderLayout.CENTER);

        lowercutButtonPanel.setLayout(new java.awt.GridBagLayout());

        lowercutPlusButton.setText("+");
        lowercutPlusButton.setPreferredSize(new java.awt.Dimension(43, 15));
        lowercutPlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowercutPlusButtonActionPerformed(evt);
            }
        });
        lowercutButtonPanel.add(lowercutPlusButton, new java.awt.GridBagConstraints());

        lowercutTextField.setColumns(5);
        lowercutTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        lowercutButtonPanel.add(lowercutTextField, gridBagConstraints);

        lowercutMinusButton.setText("-");
        lowercutMinusButton.setMaximumSize(new java.awt.Dimension(43, 15));
        lowercutMinusButton.setMinimumSize(new java.awt.Dimension(43, 15));
        lowercutMinusButton.setPreferredSize(new java.awt.Dimension(43, 15));
        lowercutMinusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowercutMinusButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        lowercutButtonPanel.add(lowercutMinusButton, gridBagConstraints);

        lowercutPanel.add(lowercutButtonPanel, java.awt.BorderLayout.EAST);

        cutoffPanel.add(lowercutPanel, java.awt.BorderLayout.NORTH);

        uppercutPanel.setLayout(new java.awt.BorderLayout());

        uppercutLabel.setText("Upper Cut");
        uppercutPanel.add(uppercutLabel, java.awt.BorderLayout.WEST);

        uppercutSlider.setMajorTickSpacing(1);
        uppercutSlider.setSnapToTicks(true);
        uppercutSlider.setValue(0);
        uppercutSlider.setInverted(true);
        uppercutSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                uppercutSliderStateChanged(evt);
            }
        });
        uppercutPanel.add(uppercutSlider, java.awt.BorderLayout.CENTER);

        uppercutButtonPanel.setLayout(new java.awt.GridBagLayout());

        uppercutPlusButton.setText("+");
        uppercutPlusButton.setPreferredSize(new java.awt.Dimension(43, 15));
        uppercutPlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uppercutPlusButtonActionPerformed(evt);
            }
        });
        uppercutButtonPanel.add(uppercutPlusButton, new java.awt.GridBagConstraints());

        uppercutTextField.setColumns(5);
        uppercutTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        uppercutButtonPanel.add(uppercutTextField, gridBagConstraints);

        uppercutMinusButton.setText("-");
        uppercutMinusButton.setMaximumSize(new java.awt.Dimension(43, 15));
        uppercutMinusButton.setMinimumSize(new java.awt.Dimension(43, 15));
        uppercutMinusButton.setPreferredSize(new java.awt.Dimension(43, 15));
        uppercutMinusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uppercutMinusButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        uppercutButtonPanel.add(uppercutMinusButton, gridBagConstraints);

        uppercutPanel.add(uppercutButtonPanel, java.awt.BorderLayout.EAST);

        cutoffPanel.add(uppercutPanel, java.awt.BorderLayout.SOUTH);

        dataPanel.add(cutoffPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(dataPanel, java.awt.BorderLayout.CENTER);

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel2.add(okButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel2.add(closeButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void lowercutPlusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowercutPlusButtonActionPerformed
        this.lowercutSlider.setValue(this.lowercutSlider.getValue() - 1);
    }//GEN-LAST:event_lowercutPlusButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (this.lowercutTextField.getText().trim().length() > 0) {
            this.pdata.setReferencesLowerCut(Integer.parseInt(this.lowercutTextField.getText()));
        }
        if (this.uppercutTextField.getText().trim().length() > 0) {
            this.pdata.setReferencesUpperCut(Integer.parseInt(this.uppercutTextField.getText()));
        }
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void lowercutSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_lowercutSliderStateChanged
        int lowerCut = this.lowercutSlider.getValue();
        int upperCut = this.uppercutSlider.getValue();

        int[] freqs = ((ZipfCurveReferences) this.curvePanel).setCutLines(lowerCut, upperCut);
        this.lowercutTextField.setText(Integer.toString(lowerCut));
        this.uppercutTextField.setText(Integer.toString(upperCut));
        this.referencesTable.setRowSelectionInterval(freqs[0], freqs[1]);
        this.numberofreferencesTextField.setText(Integer.toString(freqs[1] - freqs[0] + 1));
        if (this.lowercutSlider.getValue() > this.uppercutSlider.getValue()) {
            this.uppercutSlider.setValue(this.lowercutSlider.getValue());
        }

    }//GEN-LAST:event_lowercutSliderStateChanged

    private void uppercutSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_uppercutSliderStateChanged
        int upperCut = this.uppercutSlider.getValue();
        int lowerCut = this.lowercutSlider.getValue();

        int[] freqs = ((ZipfCurveReferences) this.curvePanel).setCutLines(lowerCut, upperCut);
        this.lowercutTextField.setText(Integer.toString(lowerCut));
        this.uppercutTextField.setText(Integer.toString(upperCut));
        this.referencesTable.setRowSelectionInterval(freqs[0], freqs[1]);
        this.numberofreferencesTextField.setText(Integer.toString(freqs[1] - freqs[0] + 1));

        if (this.uppercutSlider.getValue() < this.lowercutSlider.getValue()) {
            this.lowercutSlider.setValue(this.uppercutSlider.getValue());
        }
    }//GEN-LAST:event_uppercutSliderStateChanged

    private void lowercutMinusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lowercutMinusButtonActionPerformed
        this.lowercutSlider.setValue(this.lowercutSlider.getValue() + 1);
    }//GEN-LAST:event_lowercutMinusButtonActionPerformed

    private void uppercutPlusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uppercutPlusButtonActionPerformed
        this.uppercutSlider.setValue(this.uppercutSlider.getValue() + 1);
    }//GEN-LAST:event_uppercutPlusButtonActionPerformed

    private void uppercutMinusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uppercutMinusButtonActionPerformed
        this.uppercutSlider.setValue(this.uppercutSlider.getValue() - 1);
    }//GEN-LAST:event_uppercutMinusButtonActionPerformed

    public void display(ProjectionData pdata) {
        this.pdata = pdata;

        String[] label;
        ArrayList<Reference> references = pdata.getDatabaseCorpus().getCorpusReferences(-1, -1);
        Collections.sort(references);
        this.initModels();
        this.referencesTable.setModel(this.tableModel);
        for (Reference ref : references) {
            label = new String[2];
            label[0] = ref.reference;
            label[1] = Integer.toString(ref.frequency);
            this.tableModel.addRow(label);
        }

        ((ZipfCurveReferences) this.curvePanel).setReferences(references);

        int max = references.get(0).frequency, min = references.get(references.size() - 1).frequency;

        this.lowercutSlider.setMaximum(max);
        this.lowercutSlider.setMinimum(min);
        if (pdata.getReferencesLowerCut() != -1) {
            this.lowercutSlider.setValue(pdata.getReferencesLowerCut());
        } else {
            this.lowercutSlider.setValue(min);
        }
        this.lowercutSlider.setEnabled(true);

        this.uppercutSlider.setMaximum(max);
        this.uppercutSlider.setMinimum(min);
        if (pdata.getReferencesUpperCut() != -1) {
            this.uppercutSlider.setValue(pdata.getReferencesUpperCut());
        } else {
            this.uppercutSlider.setValue(max);
        }
        this.uppercutSlider.setEnabled(true);

        this.referencesTable.getColumnModel().getColumn(1).setPreferredWidth(10);

        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }

    public static CosinePlusReferencesSettings getInstance(java.awt.Container parent) {
        if ((instance == null || instance.getParent() != parent) && parent instanceof javax.swing.JDialog) {
            instance = new CosinePlusReferencesSettings((javax.swing.JDialog) parent);
        }
        return instance;
    }

    private void initModels() {
        String[] titulos = new String[]{"Reference", "Frequency"};
        this.tableModel = new DefaultTableModel(null, titulos);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel curvePanel;
    private javax.swing.JPanel cutoffPanel;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JPanel frequencyPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel lowercutButtonPanel;
    private javax.swing.JLabel lowercutLabel;
    private javax.swing.JButton lowercutMinusButton;
    private javax.swing.JPanel lowercutPanel;
    private javax.swing.JButton lowercutPlusButton;
    private javax.swing.JSlider lowercutSlider;
    private javax.swing.JTextField lowercutTextField;
    private javax.swing.JTextField numberofreferencesTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JTable referencesTable;
    private javax.swing.JPanel uppercutButtonPanel;
    private javax.swing.JLabel uppercutLabel;
    private javax.swing.JButton uppercutMinusButton;
    private javax.swing.JPanel uppercutPanel;
    private javax.swing.JButton uppercutPlusButton;
    private javax.swing.JSlider uppercutSlider;
    private javax.swing.JTextField uppercutTextField;
    // End of variables declaration//GEN-END:variables
}