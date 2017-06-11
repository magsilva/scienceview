/*
 * DocumentViewerPanel.java
 *
 * Created on 04/05/2009, 15:18:36
 */
package topicevolutionvis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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
public class DocumentViewerPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private JSplitPane rootPanel;
    private JPanel documentMetadataPanel;
    private JTextPane contentTextPane;
    private JSplitPane dataPanel;
    private JPanel datePanel;
    private JTextField dateTextField;
    private JButton doiButton;
    private JPanel documentContentPanel;
    private JPanel mandatoryInformationPanel;
    private JScrollPane scrollableContentTextPane;
    private JPanel doiPannel;
    private JPanel titlePanel;
    private JTextField titleTextField;
    private JTable featuresTable;
    private JPanel featuresTablePanel;
    private JScrollPane scrollableFeaturesPanel;
    private JPanel optionalInformationPanel;
    private JScrollPane scrollableDescriptionTextArea;
    private JTextArea descriptionTextArea;
	

    private DefaultTableModel model;
    
    private Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.YELLOW);
    
    private String title;
    
    private String content;
    
    private int date;
    
    private String doi;
    
    private String exercise;
    
    private static final String DOI_URL_PREFIX = "http://dx.doi.org/";

    /** Creates new form DocumentViewerPanel */
    public DocumentViewerPanel(String title, String content, int date, String doi, String exercise, DefaultTableModel model) {
    	this.title = title;
    	this.content = content;
    	this.date = date;
    	this.doi = doi;
    	this.exercise = exercise;
    	this.model = model;
        initComponents();
        display();
    }

    /**
	 * Create visual components.
	 */
    private void initComponents()
    {
        setLayout(new BorderLayout());

        initMetadataPanel();
        initDataPanel();
        rootPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, documentMetadataPanel, dataPanel);
        rootPanel.setResizeWeight(0.2);
        add(rootPanel, BorderLayout.CENTER);
        
    }

	private void initMetadataPanel() {
        initDOI();
    	initTitle();
    	initDate();
    	initDescription();
    	
    	documentMetadataPanel = new JPanel();
    	documentMetadataPanel.setLayout(new BorderLayout());
    	documentMetadataPanel.setPreferredSize(new Dimension(1000, 150));
    	
    	mandatoryInformationPanel = new JPanel();
        mandatoryInformationPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        mandatoryInformationPanel.add(titlePanel);
        mandatoryInformationPanel.add(datePanel);
        mandatoryInformationPanel.add(doiPannel);
        documentMetadataPanel.add(mandatoryInformationPanel, BorderLayout.NORTH);
         
        optionalInformationPanel = new JPanel();
        optionalInformationPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        optionalInformationPanel.setLayout(new BorderLayout());
        optionalInformationPanel.add(scrollableDescriptionTextArea, BorderLayout.CENTER);
        documentMetadataPanel.add(optionalInformationPanel, BorderLayout.CENTER);
	}

    
    private void initDataPanel()
    {
    	initFeaturesTable();
    	initContent();
    	
    	dataPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, documentContentPanel, featuresTablePanel);
    	dataPanel.setResizeWeight(0.8);

	}

	
	private void initContent()
    {
    	contentTextPane = new JTextPane();
    	contentTextPane.setEditable(false);
    	contentTextPane.setPreferredSize(new Dimension(800, 600));
    	contentTextPane.setMinimumSize(new Dimension(700, 550));
        scrollableContentTextPane = new JScrollPane(contentTextPane);
        scrollableContentTextPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableContentTextPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        documentContentPanel = new JPanel();
        documentContentPanel.setBorder(BorderFactory.createTitledBorder("Document content"));
        documentContentPanel.setLayout(new BorderLayout());
        documentContentPanel.add(scrollableContentTextPane, BorderLayout.CENTER);
	}

	private void initFeaturesTable()
    {
    	featuresTable = new JTable(model);
    	featuresTable.setPreferredSize(new Dimension(150, 600));
    	featuresTable.setMinimumSize(new Dimension(120, 300));
        featuresTable.setPreferredScrollableViewportSize(new Dimension(150, 600));
        featuresTable.setFillsViewportHeight(true);
        featuresTable.setEnabled(false);
        featuresTable.setAutoCreateRowSorter(true);
        TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) featuresTable.getRowSorter();
        List<SortKey> list = new ArrayList<SortKey>();
        list.add( new RowSorter.SortKey(2, SortOrder.DESCENDING));
        sorter.setSortKeys(list);
        sorter.sort();

        scrollableFeaturesPanel = new JScrollPane(featuresTable);
        scrollableFeaturesPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        featuresTablePanel = new JPanel();
        featuresTablePanel.setBorder(BorderFactory.createTitledBorder("Features"));
        featuresTablePanel.setLayout(new BorderLayout());
        featuresTablePanel.add(scrollableFeaturesPanel, BorderLayout.CENTER);
        
	}

	
	private void initDescription()
    {
    	 descriptionTextArea = new JTextArea();
    	 descriptionTextArea.setEditable(false);
         descriptionTextArea.setBorder(null);
         descriptionTextArea.setLineWrap(true);
         descriptionTextArea.setOpaque(false);
         descriptionTextArea.setPreferredSize(new Dimension(600, 80));
         descriptionTextArea.setMinimumSize(new Dimension(600, 80));
    	 scrollableDescriptionTextArea = new JScrollPane(descriptionTextArea);
         scrollableDescriptionTextArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

	private void initDate()
    {
        datePanel = new JPanel();
        dateTextField = new JTextField();
    	datePanel.setBorder(BorderFactory.createTitledBorder("Time reference"));
        datePanel.setPreferredSize(new Dimension(100, 50));
        datePanel.setLayout(new BorderLayout());
        dateTextField.setEditable(false);
        dateTextField.setHorizontalAlignment(JTextField.CENTER);
        dateTextField.setBorder(null);
        datePanel.add(dateTextField, BorderLayout.CENTER);
    }

	private void initTitle()
    {
        titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder("Document Title"));
        titlePanel.setPreferredSize(new Dimension(450, 50));
        titlePanel.setLayout(new BorderLayout());

        titleTextField = new JTextField();
        titleTextField.setColumns(80);
        titleTextField.setEditable(false);
        titleTextField.setBorder(null);
        titlePanel.add(titleTextField, BorderLayout.CENTER);
	}


	private void initDOI()
    {
        doiButton = new JButton();
    	doiButton.setIcon(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Search24.gif")));
        doiButton.setToolTipText("Open DOI Citation in the Internet");
        doiButton.setBorder(null);
        doiButton.setBorderPainted(false);
    	if (doi != null) {
    		doiButton.setEnabled(true);
            doiButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    doiButtonActionPerformed(evt);
                }
            });
        } else {
            doiButton.setEnabled(false);
        }

        doiPannel = new JPanel();
        doiPannel.setBorder(BorderFactory.createTitledBorder("DOI Citation"));
        doiPannel.setPreferredSize(new Dimension(115, 50));
        doiPannel.setLayout(new BorderLayout());
        doiPannel.add(doiButton);
    }
    
    public final void display() {
        if (title != null) {
            titleTextField.setText(title);
            titleTextField.setCaretPosition(0);
            dateTextField.setText(String.valueOf(date));
            dateTextField.setCaretPosition(0);
            contentTextPane.setText(content);
            contentTextPane.setCaretPosition(0);
            if (exercise != null) {
	            descriptionTextArea.setText(exercise);
	            descriptionTextArea.setCaretPosition(0);
            } else {
            	optionalInformationPanel.setVisible(false);
            }
        }
    }


    private void doiButtonActionPerformed(ActionEvent evt) {
    	String url;
   		if (doi.startsWith(DOI_URL_PREFIX)) {
   			url = doi;
   		} else {
   			url = DOI_URL_PREFIX + doi;
   		}
        URLLauncher.openURL(url);
    }

    public void sortTable() {
    	TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) featuresTable.getRowSorter();
    	sorter.sort();
    }
    
    public void insertTable(List<String> errors, List<Double> errorValues, List<Double> normalizedValues) {
   		for (int i = 0; i < errors.size(); i++) {
   			model.addRow(new Object[] {errors.get(i), errorValues.get(i), normalizedValues.get(i)});
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

}
