/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MemoryCheck.java
 *
 * Created on Jul 26, 2010, 12:10:54 PM
 */
package topicevolutionvis.view.tools;

import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import topicevolutionvis.view.ScienceViewMainFrame;

/**
 *
 * @author Aretha
 */
public class MemoryCheck extends javax.swing.JDialog {

    private static Checker checker = new Checker();
    private static MemoryCheck instance;
    private static boolean autoCall = false;

    /** Creates new form MemoryCheck */
    private MemoryCheck() {
        super(ScienceViewMainFrame.getInstance());
        initComponents();

        MemoryCheck.checker.start();
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

        memoryPanel = new javax.swing.JPanel();
        currentLabel = new javax.swing.JLabel();
        currentTextField = new javax.swing.JTextField();
        maximumLabel = new javax.swing.JLabel();
        maximumTextField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        gcButton = new javax.swing.JButton();
        autoCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Memory Check");
        setAlwaysOnTop(true);
        setResizable(false);

        memoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Memory Used"));
        memoryPanel.setLayout(new java.awt.GridBagLayout());

        currentLabel.setText("Current");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        memoryPanel.add(currentLabel, gridBagConstraints);

        currentTextField.setColumns(10);
        currentTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        memoryPanel.add(currentTextField, gridBagConstraints);

        maximumLabel.setText("Maximum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        memoryPanel.add(maximumLabel, gridBagConstraints);

        maximumTextField.setColumns(10);
        maximumTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        memoryPanel.add(maximumTextField, gridBagConstraints);

        getContentPane().add(memoryPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        gcButton.setText("Call Garbage Collector");
        gcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gcButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        buttonPanel.add(gcButton, gridBagConstraints);

        autoCheckBox.setText("Automatically");
        autoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        autoCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        autoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        buttonPanel.add(autoCheckBox, gridBagConstraints);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void gcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gcButtonActionPerformed
        Runtime.getRuntime().gc();
        MemoryCheck.checker.check();
}//GEN-LAST:event_gcButtonActionPerformed

    private void autoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoCheckBoxActionPerformed
        MemoryCheck.autoCall = this.autoCheckBox.isSelected();
}//GEN-LAST:event_autoCheckBoxActionPerformed

    public static void showMemoryCheck() {
        if (instance == null) {
            instance = new MemoryCheck();
        }
        instance.setLocation(150, 30);
        instance.setVisible(true);
    }

    public static class Checker extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    Checker.this.check();
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void check() {
            Runtime r = Runtime.getRuntime();
            if (autoCall) {
                r.gc();
            }

            double usedMemory = (r.totalMemory() / 1024.0 / 1024.0) - (r.freeMemory() / 1024.0 / 1024.0);
            if (this.maxMemoryUsed < usedMemory) {
                this.maxMemoryUsed = usedMemory;
            }

            NumberFormat form = NumberFormat.getInstance();
            form.setMaximumFractionDigits(2);
            form.setMinimumFractionDigits(2);

            currentTextField.setText(form.format(usedMemory) + " MB");
            maximumTextField.setText(form.format(this.maxMemoryUsed) + " MB");
        }
        private double maxMemoryUsed = Double.MIN_VALUE;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoCheckBox;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel currentLabel;
    private static javax.swing.JTextField currentTextField;
    private javax.swing.JButton gcButton;
    private javax.swing.JLabel maximumLabel;
    private static javax.swing.JTextField maximumTextField;
    private javax.swing.JPanel memoryPanel;
    // End of variables declaration//GEN-END:variables
}
