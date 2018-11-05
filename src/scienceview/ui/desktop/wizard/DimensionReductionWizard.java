/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DimensionReduction.java
 *
 * Created on Nov 3, 2010, 9:58:59 AM
 */
package scienceview.ui.desktop.wizard;

import javax.swing.JFileChooser;

import scienceview.datamining.dimensionreduction.DimensionalityReductionType;
import scienceview.datamining.projection.ProjectionData;
import scienceview.ui.desktop.view.SaveDialog;
import scienceview.ui.desktop.view.filefilter.DATAFilter;
import scienceview.utils.matrix.normalization.NormalizationType;

/**
 *
 * @author barbosaa
 */
public class DimensionReductionWizard extends WizardPanel {

    private ProjectionData pdata;

    /** Creates new form DimensionReduction */
    public DimensionReductionWizard(ProjectionData projectionData) {
        this.pdata = projectionData;
        initComponents();

        for (DimensionalityReductionType drt : DimensionalityReductionType.getTypes()) {
            this.dimensionalityReductionComboBox.addItem(drt);
        }

        for (NormalizationType.Type nt : NormalizationType.getTypes()) {
            this.normalizationComboBox.addItem(nt);
        }
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

        normalPanel = new javax.swing.JPanel();
        normalizationComboBox = new javax.swing.JComboBox();
        saveDocumetsTermsMatrixCheckBox = new javax.swing.JCheckBox();
        parametersPanel = new javax.swing.JPanel();
        dimensionsTextField = new javax.swing.JTextField();
        dimensionsLabel = new javax.swing.JLabel();
        dimensionalityReductionComboBox = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Dimensionality Reduction"));
        setLayout(new java.awt.GridBagLayout());

        normalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Normalization"));
        normalPanel.add(normalizationComboBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 122;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(normalPanel, gridBagConstraints);

        saveDocumetsTermsMatrixCheckBox.setText("Save documents x terms matrix");
        saveDocumetsTermsMatrixCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saveDocumetsTermsMatrixCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        saveDocumetsTermsMatrixCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDocumetsTermsMatrixCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(saveDocumetsTermsMatrixCheckBox, gridBagConstraints);

        parametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dimensionality reduction technique"));
        parametersPanel.setLayout(new java.awt.GridBagLayout());

        dimensionsTextField.setColumns(5);
        dimensionsTextField.setText("15");
        dimensionsTextField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionsTextField, gridBagConstraints);

        dimensionsLabel.setText("dimensions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionsLabel, gridBagConstraints);

        dimensionalityReductionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dimensionalityReductionComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        parametersPanel.add(dimensionalityReductionComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(parametersPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void saveDocumetsTermsMatrixCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDocumetsTermsMatrixCheckBoxActionPerformed
        if (this.saveDocumetsTermsMatrixCheckBox.isSelected()) {
            String filename = this.pdata.getCollectionName().concat(".txt");
            int result = SaveDialog.showSaveDialog(new DATAFilter(), this, filename);

            if (result == JFileChooser.APPROVE_OPTION) {
                filename = SaveDialog.getFilename();
                this.pdata.setDocsTermsFilename(filename);
            } else {
                this.saveDocumetsTermsMatrixCheckBox.setSelected(false);
            }
        } else {
            this.pdata.setDocsTermsFilename("");
        }
}//GEN-LAST:event_saveDocumetsTermsMatrixCheckBoxActionPerformed

    public DimensionReductionWizard reset() {
        return this;
    }

    @Override
    public void refreshData() {
        DimensionalityReductionType drt = (DimensionalityReductionType) this.dimensionalityReductionComboBox.getSelectedItem();
        this.pdata.setDimensionReductionType(drt);

        if (drt != DimensionalityReductionType.NONE) {
            this.pdata.setTargetDimension(Integer.parseInt(this.dimensionsTextField.getText()));
        } else {
            this.pdata.setTargetDimension(0);
        }

        this.pdata.setNormalization((NormalizationType.Type) this.normalizationComboBox.getSelectedItem());
    }

    private void dimensionalityReductionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dimensionalityReductionComboBoxActionPerformed
        DimensionalityReductionType drt = (DimensionalityReductionType) this.dimensionalityReductionComboBox.getSelectedItem();

        if (drt != DimensionalityReductionType.NONE) {
            this.dimensionsTextField.setEnabled(true);
        } else {
            this.dimensionsTextField.setEnabled(false);
        }
}//GEN-LAST:event_dimensionalityReductionComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dimensionalityReductionComboBox;
    private javax.swing.JLabel dimensionsLabel;
    private javax.swing.JTextField dimensionsTextField;
    private javax.swing.JPanel normalPanel;
    private javax.swing.JComboBox normalizationComboBox;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JCheckBox saveDocumetsTermsMatrixCheckBox;
    // End of variables declaration//GEN-END:variables

	@Override
	public boolean isNextStepTerminal() {
		return false;
	}

	@Override
	public boolean canGoToNextStep() {
		return true;
	}

	@Override
	public boolean canGoToPreviousStep() {
		return true;
	}

	@Override
	public boolean canCancel() {
		return true;
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean hasPreviousStep() {
		return true;
	}

	@Override
	public boolean canResetConfiguration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetConfiguration() {
		// TODO Auto-generated method stub
		
	}
}
