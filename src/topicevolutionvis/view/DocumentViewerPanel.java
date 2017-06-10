/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DocumentViewerPanel.java
 *
 * Created on 04/05/2009, 15:18:36
 */
package topicevolutionvis.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import topicevolutionvis.util.URLLauncher;

/**
 *
 * @author Aretha
 */
public class DocumentViewerPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;

    private String url;
    
    private DefaultTableModel model;
    
    private Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(java.awt.Color.YELLOW);

    /** Creates new form DocumentViewerPanel */
    public DocumentViewerPanel(String title, String content, int date, String doi, String exercise, DefaultTableModel model) {
    	this.model = model;
        initComponents();
        if (doi != null) {
            url = "http://dx.doi.org/".concat(doi);
            this.doiButton.setEnabled(true);
        } else {
            url = null;
            this.doiButton.setEnabled(false);
        }
        display(title, content, date, exercise);
    }

    public final void display(String title, String content, int date, String exercise) {
        if (title != null) {
            titleTextField.setText(title);
            titleTextField.setCaretPosition(0);
            dateTextField.setText(String.valueOf(date));
            dateTextField.setCaretPosition(0);
            contentTextPane.setText(content);
            contentTextPane.setEditable(false);
            contentTextPane.setCaretPosition(0);
            if(exercise != null) {
	            exerciseTextArea.setText(exercise);
	            exerciseTextArea.setCaretPosition(0);
            } else
            	exercisePanel.setVisible(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel4 = new javax.swing.JPanel();
        pdfPanel = new javax.swing.JPanel();
        doiButton = new javax.swing.JButton();
        titlePanel = new javax.swing.JPanel();
        titleTextField = new javax.swing.JTextField();
        datePanel = new javax.swing.JPanel();
        dateTextField = new javax.swing.JTextField();
        exercisePanel = new javax.swing.JPanel();
        scrollExerciseTextArea = new javax.swing.JScrollPane();
        exerciseTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contentTextPane = new javax.swing.JTextPane();
        setLayout(new java.awt.BorderLayout());
        
    	errorsTable = new javax.swing.JTable(model);
        errorsTable.setPreferredScrollableViewportSize(new Dimension(180, 350));
        errorsTable.setFillsViewportHeight(true);
        errorsTable.setEnabled(false);

        
        errorsTable.setAutoCreateRowSorter(true);
        TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) errorsTable.getRowSorter();
        List<SortKey> list = new ArrayList<SortKey>();
        list.add( new RowSorter.SortKey(2, SortOrder.DESCENDING));
        sorter.setSortKeys(list);
        sorter.sort();
        scrollPaneErrorsTable = new javax.swing.JScrollPane(errorsTable);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        pdfPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("DOI Citation"));
        pdfPanel.setMinimumSize(new java.awt.Dimension(115, 50));
        pdfPanel.setPreferredSize(new java.awt.Dimension(115, 50));
        pdfPanel.setLayout(new java.awt.GridBagLayout());

        doiButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Search24.gif"))); // NOI18N
        doiButton.setToolTipText("Open DOI Citation in the Internet");
        doiButton.setBorder(null);
        doiButton.setBorderPainted(false);
        doiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doiButtonActionPerformed(evt);
            }
        });
        pdfPanel.add(doiButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel4.add(pdfPanel, gridBagConstraints);

        titlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Document Title"));
        titlePanel.setPreferredSize(new java.awt.Dimension(450, 50));
        titlePanel.setLayout(new java.awt.BorderLayout());

        titleTextField.setColumns(50);
        titleTextField.setEditable(false);
        titleTextField.setBorder(null);
        titlePanel.add(titleTextField, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(titlePanel, gridBagConstraints);

        datePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Published Date"));
        datePanel.setPreferredSize(new java.awt.Dimension(100, 50));
        datePanel.setLayout(new java.awt.BorderLayout());

        dateTextField.setColumns(5);
        dateTextField.setEditable(false);
        dateTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dateTextField.setBorder(null);
        dateTextField.setPreferredSize(new java.awt.Dimension(30, 14));
        datePanel.add(dateTextField, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel4.add(datePanel, gridBagConstraints);
        
        exercisePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Exercise"));
        exercisePanel.setPreferredSize(new java.awt.Dimension(645, 70));
        exercisePanel.setLayout(new java.awt.BorderLayout());
        
        exerciseTextArea.setEditable(false);
        exerciseTextArea.setBorder(null);
        exerciseTextArea.setLineWrap(true);
        exerciseTextArea.setOpaque(false);
        scrollExerciseTextArea.setViewportView(exerciseTextArea);
        scrollExerciseTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        exercisePanel.add(scrollExerciseTextArea, java.awt.BorderLayout.CENTER);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(exercisePanel, gridBagConstraints);

        add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Document Content"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(contentTextPane);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(scrollPaneErrorsTable, java.awt.BorderLayout.EAST);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void doiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doiButtonActionPerformed
        URLLauncher.openURL(url);
    }//GEN-LAST:event_doiButtonActionPerformed

    public void sortTable() {
    	TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) errorsTable.getRowSorter();
    	sorter.sort();
    }
    
    public void insertTable(List<String> errors, double[] errorValue, double[] normalizedValue) {
   		for (int i = 0; i < errors.size(); i++) {
   			model.addRow(new Object[] {errors.get(i), errorValue[i], normalizedValue[i]});
   		}
    }
    
    // Creates highlights around all occurrences of pattern in textComp
    public void highlight(String pattern) {
        // First remove all old highlights
        removeHighlights();
        try {
            Highlighter hilite = contentTextPane.getHighlighter();
            Document doc = contentTextPane.getDocument();
            String text = doc.getText(0, doc.getLength()).toLowerCase();
            int pos = 0;

            if ((pos = text.indexOf(pattern.toLowerCase(), pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                contentTextPane.setCaretPosition(pos);
                pos += pattern.length();
            }

            // find for pattern
            while ((pos = text.indexOf(pattern.toLowerCase(), pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(MultipleDocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Removes only our private highlights
    public void removeHighlights() {
        Highlighter hilite = contentTextPane.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

        public MyHighlightPainter(java.awt.Color color) {
            super(color);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane contentTextPane;
    private javax.swing.JPanel datePanel;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JButton doiButton;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pdfPanel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JTable errorsTable;
    private javax.swing.JScrollPane scrollPaneErrorsTable;
    private javax.swing.JPanel exercisePanel;
    private javax.swing.JScrollPane scrollExerciseTextArea;
    private javax.swing.JTextArea exerciseTextArea;
    // End of variables declaration//GEN-END:variables
}
