package topicevolutionvis.wizard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import topicevolutionvis.data.CSVDatabaseImporter;
import topicevolutionvis.data.DatabaseImporter;
import topicevolutionvis.database.CollectionManager;
import topicevolutionvis.database.Corpus;
import topicevolutionvis.database.DatabaseCorpus;
import topicevolutionvis.projection.ProjectionData;
import topicevolutionvis.util.SystemPropertiesManager;
import topicevolutionvis.utils.filefilter.BibTeXFileFilter;
import topicevolutionvis.utils.filefilter.CSVFileFilter;
import topicevolutionvis.utils.filefilter.DatabaseFileFilter;
import topicevolutionvis.utils.filefilter.EndnoteExportFileFilter;
import topicevolutionvis.utils.filefilter.ISIFileFilter;

public abstract class DataImportWizard extends WizardPanel implements ActionListener {

	protected DatabaseImporter importer;
	
	protected ProjectionData pdata;
	protected CollectionManager collectionManager;
	private JPanel selectCorpusPanel;
	private JLabel selectCorpusNameLabel;
	private JComboBox<String> selectCorpusNameComboBox;
	private JButton selectCorpusRemoveButton;
	private JLabel selectCorpusDescriptionLabel;
	private JTextField selectCorpusDescriptionTextField;
	protected JPanel newCorpusPanel;
	private JLabel newCorpusNameLabel;
	protected JTextField newCorpusNameTextField;
	private JLabel newCorpusDescriptionLabel;
	private JTextField newCorpusDescriptionTextField;
	private JLabel newCorpusInputFilenameLabel;
	protected JTextField newCorpusFilenameTextField;
	private JButton newCorpusFilenameSearchButton;
	protected JButton newCorpusFilenameLoadCancelButton;
	private JProgressBar newCorpusProgressBar;
	protected JPanel corpusInformationPanel;
	private JLabel corpusNumberDocumentsLabel;
	private JTextField corpusNumberDocumentsTextField;
	private static final String DEFAULT_CORPUS_NAME = "Create...";

	public DataImportWizard() {
		super();
	}

	protected void updateCollections(String collection) {
		String index = null;
		ArrayList<String> collections = collectionManager.getCollections();
	
		selectCorpusNameComboBox.removeAllItems();
		selectCorpusNameComboBox.addItem(DEFAULT_CORPUS_NAME);
		for (String col : collections) {
			selectCorpusNameComboBox.addItem(col);
			if (col.equals(collection)) {
				index = col;
			}
		}
	
		if (collection.isEmpty()) {
			selectCorpusNameComboBox.setSelectedIndex(0);
		}
		if (index != null) {
			selectCorpusNameComboBox.setSelectedItem(index);
		}
	}

	protected void initComponents() {
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.HORIZONTAL;
	
		initSelectCorpusPanel();
		initCorpusInformationPanel();
		initNewCorpusPanel();
	
		setLayout(new GridBagLayout());
	
		bc.gridy = 0;
		bc.gridx = 0;
		bc.gridwidth = 1;
		add(selectCorpusPanel, bc);
	
		bc.gridy = 0;
		bc.gridx = 1;
		bc.gridwidth = 1;
		add(corpusInformationPanel, bc);
	
		bc.gridy = 1;
		bc.gridx = 0;
		bc.gridwidth = 2;
		add(newCorpusPanel, bc);
	}

	private void initSelectCorpusPanel() {
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);
	
		selectCorpusPanel = new JPanel();
		selectCorpusPanel.setBorder(BorderFactory.createTitledBorder("Selected collection"));
		selectCorpusPanel.setLayout(new GridBagLayout());
	
		selectCorpusNameLabel = new JLabel();
		selectCorpusNameLabel.setText("Name:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 0;
		bc.gridx = 0;
		bc.gridwidth = 1;
		selectCorpusPanel.add(selectCorpusNameLabel, bc);
	
		selectCorpusNameComboBox = new JComboBox<String>();
		selectCorpusNameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				corpusComboBoxActionPerformed(evt);
			}
		});
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.gridy = 0;
		bc.gridx = 1;
		bc.gridwidth = 1;
		selectCorpusPanel.add(selectCorpusNameComboBox, bc);
	
		selectCorpusRemoveButton = new JButton();
		selectCorpusRemoveButton.setText("Remove");
		selectCorpusRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeButtonActionPerformed(evt);
			}
		});
		bc.fill = GridBagConstraints.NONE;
		bc.anchor = GridBagConstraints.LINE_END;
		bc.gridy = 0;
		bc.gridx = 2;
		bc.gridwidth = 1;
		selectCorpusPanel.add(selectCorpusRemoveButton, bc);
	
		selectCorpusDescriptionLabel = new JLabel();
		selectCorpusDescriptionLabel.setText("Description:");
		bc.fill = GridBagConstraints.NONE;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 1;
		bc.gridx = 0;
		bc.gridwidth = 1;
		selectCorpusPanel.add(selectCorpusDescriptionLabel, bc);
	
		selectCorpusDescriptionTextField = new JTextField();
		selectCorpusDescriptionTextField.setColumns(30);
		selectCorpusDescriptionTextField.setText("");
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 1;
		bc.gridx = 1;
		bc.gridwidth = 2;
		selectCorpusPanel.add(selectCorpusDescriptionTextField, bc);
	}

	protected void initCorpusInformationPanel() {
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);
	
		corpusInformationPanel = new JPanel();
		corpusInformationPanel.setBorder(BorderFactory.createTitledBorder("Collection information"));
		corpusInformationPanel.setLayout(new GridBagLayout());
	
		corpusNumberDocumentsLabel = new JLabel();
		corpusNumberDocumentsLabel.setText("Documents");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 0;
		bc.gridx = 0;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNumberDocumentsLabel, bc);
	
		corpusNumberDocumentsTextField = new JTextField();
		corpusNumberDocumentsTextField.setEditable(false);
		corpusNumberDocumentsTextField.setText("");
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 0;
		bc.gridx = 1;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNumberDocumentsTextField, bc);
	}

	protected void initNewCorpusPanel() {
		GridBagConstraints bc = new GridBagConstraints();
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.insets = new Insets(1, 1, 1, 1);
	
		newCorpusPanel = new JPanel();
		newCorpusPanel.setBorder(BorderFactory.createTitledBorder("Create new collection"));
		newCorpusPanel.setLayout(new GridBagLayout());
	
		// Name
		newCorpusNameLabel = new JLabel();
		newCorpusNameLabel.setText("New name:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 0;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusNameLabel, bc);
	
		newCorpusNameTextField = new JTextField();
		newCorpusNameTextField.setColumns(30);
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 0;
		bc.gridx = 1;
		bc.gridwidth = 3;
		newCorpusPanel.add(newCorpusNameTextField, bc);
	
		// Description
		newCorpusDescriptionLabel = new JLabel();
		newCorpusDescriptionLabel.setText("Description:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 1;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusDescriptionLabel, bc);
	
		newCorpusDescriptionTextField = new JTextField();
		newCorpusDescriptionTextField.setColumns(30);
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 1;
		bc.gridx = 1;
		bc.gridwidth = 3;
		newCorpusPanel.add(newCorpusDescriptionTextField, bc);
	
	
		// Input file
		newCorpusInputFilenameLabel = new JLabel();
		newCorpusInputFilenameLabel.setText("Input file:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 3;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusInputFilenameLabel, bc);
	
		newCorpusFilenameTextField = new JTextField();
		newCorpusFilenameTextField.setColumns(30);
		newCorpusFilenameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				corpusFilenameActionPerformed();
			}
		});
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 3;
		bc.gridx = 1;
		bc.gridwidth = 2;
		newCorpusPanel.add(newCorpusFilenameTextField, bc);
	
		newCorpusFilenameSearchButton = new JButton();
		newCorpusFilenameSearchButton.setText("Select file");
		newCorpusFilenameSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchButtonActionPerformed();
			}
		});
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 3;
		bc.gridx = 3;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusFilenameSearchButton, bc);
	
		// Load, cancel, progress bar
		newCorpusFilenameLoadCancelButton = new JButton();
		newCorpusFilenameLoadCancelButton.setText("Load");
		newCorpusFilenameLoadCancelButton.setEnabled(false);
		newCorpusFilenameLoadCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				loadCancelButtonActionPerformed(evt);
			}
		});
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 5;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusFilenameLoadCancelButton, bc);
	
		newCorpusProgressBar = new JProgressBar();
		newCorpusProgressBar.setPreferredSize(new Dimension(300, 14));
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 5;
		bc.gridx = 1;
		bc.gridwidth = 3;
		newCorpusPanel.add(newCorpusProgressBar, bc);
	}

	protected void corpusFilenameActionPerformed() {
		File file = new File(newCorpusFilenameTextField.getText().trim());
		checkNewCorpusInputFile(file);
	}

	private void searchButtonActionPerformed() {
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
	
		SystemPropertiesManager m = SystemPropertiesManager.getInstance();
		String directory = m.getProperty("COLLECTIONS.DIR");
		if (directory != null) {
			fc.setCurrentDirectory(new File(directory));
		} else {
			fc.setCurrentDirectory(new File("."));
		}
	
		fc.addChoosableFileFilter(new BibTeXFileFilter());
		fc.addChoosableFileFilter(new ISIFileFilter());
		fc.addChoosableFileFilter(new EndnoteExportFileFilter());
		fc.addChoosableFileFilter(new DatabaseFileFilter());
		fc.addChoosableFileFilter(new CSVFileFilter());
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			newCorpusFilenameTextField.setText(file.getAbsolutePath());
			newCorpusFilenameTextField.setCaretPosition(0);
			if (newCorpusNameTextField.getText().isEmpty()) {
				newCorpusNameTextField.setText(file.getName().substring(0, file.getName().indexOf(".")));
			}
			checkNewCorpusInputFile(file);
		}
	}

	protected void checkNewCorpusInputFile(File file) {
		SystemPropertiesManager m = SystemPropertiesManager.getInstance();
		if (file.isFile() && file.exists() && file.canRead()) {
			newCorpusFilenameLoadCancelButton.setEnabled(true);
			m.setProperty("COLLECTIONS.DIR", file.getParent());
		}
	}

	protected void removeButtonActionPerformed(ActionEvent evt) {
		if (selectCorpusNameComboBox.getSelectedIndex() > 0) {
			String collection = selectCorpusNameComboBox.getItemAt(selectCorpusNameComboBox.getSelectedIndex());
			int result = JOptionPane.showConfirmDialog(this, "Collection removal",
					"Confirm removal of collection '" + collection + "'?", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				collectionManager.removeCollection(collection);
				updateCollections("");
			}
		}
	}

	public void setStatus(String status, boolean running) {
	    newCorpusProgressBar.setIndeterminate(running);
	}

	protected abstract void loadCancelButtonActionPerformed(ActionEvent evt);
	
	protected void corpusComboBoxActionPerformed(ActionEvent evt) {
    	if (selectCorpusNameComboBox.getSelectedIndex() > 0) {
    		String collectionName = selectCorpusNameComboBox.getItemAt(selectCorpusNameComboBox.getSelectedIndex());
			getInformations(collectionName);
			// setEnableNewCorpus(false);
        } else {
        	setEnableNewCorpus(true);
        }
    		
    }

	protected void loadingCollection() {
	    newCorpusFilenameLoadCancelButton.setEnabled(false);
	    newCorpusFilenameTextField.setEnabled(false);
	    newCorpusNameTextField.setEnabled(false);
	    newCorpusFilenameSearchButton.setEnabled(false);
	    selectCorpusRemoveButton.setEnabled(false);
	}

	public void finishedLoadingCollection(String collection, boolean canceled) {
	    if (! canceled) {
	        updateCollections(collection);
	        newCorpusFilenameTextField.setText(null);
	        newCorpusNameTextField.setText(null);
	        newCorpusFilenameLoadCancelButton.setText("Load");
	    }
	    newCorpusFilenameTextField.setEnabled(true);
	    newCorpusNameTextField.setEnabled(true);
	    newCorpusFilenameSearchButton.setEnabled(true);
	    newCorpusFilenameLoadCancelButton.setEnabled(true);
	    selectCorpusRemoveButton.setEnabled(true);
	}

	public DataImportWizard reset() {
	    return this;
	}

	@Override
	public void refreshData() {
		if (selectCorpusNameComboBox.getSelectedIndex() > 0) {
			String collectionName = selectCorpusNameComboBox.getItemAt(selectCorpusNameComboBox.getSelectedIndex());
			pdata.setCollectionName(collectionName);
			pdata.setDatabaseCorpus(new DatabaseCorpus(collectionName));
	    }
	}

	public void actionPerformed(ActionEvent e) {
	}

	protected void getInformations(String collectionName) {
		if (! collectionManager.isUnique(collectionName)) {
	        Corpus corpus = new DatabaseCorpus(collectionName);
	        Integer numberDocs = corpus.getNumberOfDocuments();
	        corpusNumberDocumentsTextField.setText(numberDocs.toString());
			pdata.setCollectionName(collectionName);
			pdata.setDatabaseCorpus(corpus);
	     }
	}
	
	protected void setEnableNewCorpus(boolean Enable) {
		newCorpusDescriptionLabel.setEnabled(Enable);
		newCorpusDescriptionTextField.setEnabled(Enable);
		newCorpusFilenameLoadCancelButton.setEnabled(Enable);
		newCorpusFilenameSearchButton.setEnabled(Enable);
		newCorpusFilenameTextField.setEnabled(Enable);
		newCorpusInputFilenameLabel.setEnabled(Enable);
		newCorpusNameLabel.setEnabled(Enable);
		newCorpusNameTextField.setEnabled(Enable);
		newCorpusPanel.setEnabled(Enable);
	}

	@Override
	public boolean isNextStepTerminal() {
		return false;
	}

	@Override
	public boolean canGoToNextStep() {
		if (selectCorpusNameComboBox.getSelectedIndex() > 0) {
			String collectionName = selectCorpusNameComboBox.getItemAt(selectCorpusNameComboBox.getSelectedIndex());
			if (!collectionManager.isUnique(collectionName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canGoToPreviousStep() {
		return false;
	}

	@Override
	public boolean hasPreviousStep() {
		return false;
	}

	@Override
	public boolean canCancel() {
		return true;
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean canResetConfiguration() {
		return false;
	}

	@Override
	public void resetConfiguration() {
	}

	public ProjectionData getPdata() {
		return this.pdata;
	}

}