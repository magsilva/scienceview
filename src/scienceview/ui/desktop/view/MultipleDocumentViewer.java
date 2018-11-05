/*
 * MultipleDocumentViewer.java
 *
 * Created on 04/05/2009, 15:09:44
 */
package scienceview.ui.desktop.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import scienceview.datamining.corpus.Corpus;
import scienceview.datarepresentation.Ngram;
import scienceview.utils.matrix.SparseMatrix;

/**
 * Viewer of multiple documents. It uses a tab to organized multiple documents.
 * 
 * @author Aretha
 * @author Marco Aurélio
 */
public class MultipleDocumentViewer extends JDialog
{
	private static final long serialVersionUID = 1L;

    private JTabbedPane fileviewTabbedPane;
    
    private JTextField highlightTextField;
    private JButton highlightButton;
    private JLabel highlightLabel;
    private JPanel highlightPanel;


    /**
     * Documents to be shown.
     */
    private int[] documents;
    
    /**
     * Document currently active (shown in the panel).
     */
    private DocumentViewerPanel showedDocument;
    
    /**
     * Database corpus.
     */
    private Corpus corpus;
    
    /**
     * Maximum size for document title when shown at the tab title. If the title is larger than
     * this, it will be trimmed.
     */
    private static final int MAX_TITLE_SIZE = 35;

    /** Creates new form MultipleDocumentViewer */
    public MultipleDocumentViewer(int[] documents, Corpus corpus) {
        super(ScienceViewMainFrame.getInstance());
        initComponents();
        this.documents = documents;
        this.corpus = corpus;
        this.setLocationRelativeTo(ScienceViewMainFrame.getInstance());

    }

    private List<String> getHeader(List<Ngram> ngrams) {
    	List<String> errors = new ArrayList<String>();
    	for (int i = 0; i < ngrams.size(); i++) {
    		errors.add(ngrams.get(i).toString());
    	}
    	return errors;
    }

    /** 
     * Initialize visual components
     */
    private void initComponents()
    {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Documents Viewer");
        setResizable(true);
        setLayout(new BorderLayout());

        initHighlightPanel();
        initDocumentsPanel();
        
        getContentPane().add(highlightPanel, BorderLayout.PAGE_START);
        getContentPane().add(fileviewTabbedPane, BorderLayout.CENTER);
        pack();
    }

    private void initDocumentsPanel()
    {
    	fileviewTabbedPane = new JTabbedPane();
        fileviewTabbedPane.setMinimumSize(new Dimension(600, 400));
        fileviewTabbedPane.setPreferredSize(new Dimension(800, 500));
        fileviewTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
                fileviewTabbedPaneStateChanged(evt);
            }
        });
    }
    
    private void initHighlightPanel()
    {
        highlightPanel = new JPanel();
        highlightPanel.setLayout(new FlowLayout());

        highlightLabel = new JLabel();
        highlightLabel.setText("Highlight:");
        highlightPanel.add(highlightLabel);

        highlightTextField = new JTextField();
        highlightTextField.setColumns(20);
        highlightTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                highlightTextFieldKeyPressed(evt);
            }
        });
        highlightPanel.add(highlightTextField);

        highlightButton = new JButton();
        highlightButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Bookmarks16.gif")));
        highlightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
                higthlightButtonActionPerformed(evt);
            }
        });
        highlightPanel.add(highlightButton);    	
    }
    
    public void display() {
        SparseMatrix sm = corpus.getCorpusSparseMatrix();
        SparseMatrix normalizedSm = corpus.getNormalizedSm();
        
        // Create one tab for each document
        for (int i = 0; i < documents.length; i++) {
        	int id = documents[i];
            List<Ngram> ngrams = corpus.getNgrams(id);
       		List<String> errorTypes = getHeader(ngrams);
            String title = corpus.getTitle(id);
            DocumentViewerPanel documentViewerPanel = null;

            List<Double> errorValues = new ArrayList<Double>();
   			List<Double> normalizedValues = new ArrayList<Double>();
   			for (int j = 0, index = sm.getIndexWithId(id); j < ngrams.size(); j++) {
   				errorValues.add(sm.getValueWithId(index, j));
   				normalizedValues.add(normalizedSm.getValueWithId(index, j));
   			}
            
            String[] columnsName = new String[] {"Error", "Quantity", "Normalized"};
            DefaultTableModel model = new DefaultTableModel(0, columnsName.length) {
				private static final long serialVersionUID = 1L;

				public Class<?> getColumnClass(int column) {
    				if (column == 0) {
    					return String.class;
    				} else {
    					return Double.class;
    				}
    			}
    		};
   			model.setColumnIdentifiers(columnsName);

   			
   			documentViewerPanel = new DocumentViewerPanel(title, corpus.getAbstract(id), corpus.getYear(id), corpus.getDOI(id), corpus.getKeywords(id), model);
       		documentViewerPanel.insertTable(errorTypes, errorValues, normalizedValues);
            documentViewerPanel.sortTable();
            if (title.length() > MAX_TITLE_SIZE) {
                title = title.substring(0, MAX_TITLE_SIZE) + "...";
            }
            fileviewTabbedPane.add(title, documentViewerPanel);
            fileviewTabbedPane.setTabComponentAt(i, new ButtonTabComponent(this, fileviewTabbedPane));
        }
        
        setVisible(true);
    }

    private void highlightText(String text) {
    	   if (showedDocument != null && text != null && text.length() > 0) {
               showedDocument.highlight(text);
           }
    }

    private void higthlightButtonActionPerformed(ActionEvent evt) {
    	highlightText(highlightTextField.getText());
    }

    private void fileviewTabbedPaneStateChanged(ChangeEvent evt) {
        showedDocument = (DocumentViewerPanel) fileviewTabbedPane.getSelectedComponent();
        highlightText(highlightTextField.getText());
    }

    private void highlightTextFieldKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        	highlightText(highlightTextField.getText());
        }
    }
}
