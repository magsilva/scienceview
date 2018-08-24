package topicevolutionvis.wizard;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import topicevolutionvis.data.CollectionManager;
import topicevolutionvis.data.Corpus;
import topicevolutionvis.data.DatabaseCorpus;
import topicevolutionvis.data.DumpDatabaseImporter;
import topicevolutionvis.data.bibtex.BibTeX2RIS;
import topicevolutionvis.data.csv.CSVDatabaseImporter;
import topicevolutionvis.data.endnote.EndnoteDatabaseImporter;
import topicevolutionvis.data.isi.ISICorpusDatabaseImporter;
import topicevolutionvis.projection.ProjectionData;

/**
 * Wizard to create and select collections.
 */
public class DataSourceChoiceWizard extends DataImportWizard  {
	
	private JLabel corpusNgramsLabel;
	private JTextField corpusNgramsTextField;
	private JLabel corpusNumberReferencesLabel;
	private JTextField corpusNumberReferencesTextField;

	private JLabel newCorpusNgramLabel;
	private JComboBox<Integer> newCorpusNgramDropbox;

	
	/**
	 * Creates new form DataSourceChoice
	 */
	public DataSourceChoiceWizard(ProjectionData pdata) {
		this.pdata = pdata;
		collectionManager = new CollectionManager();
		initComponents();
		updateCollections("");
	}
	
	protected void initCorpusInformationPanel() {
		super.initCorpusInformationPanel();
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);
	
		corpusNumberReferencesLabel = new JLabel();
		corpusNumberReferencesLabel.setText("References");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 1;
		bc.gridx = 0;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNumberReferencesLabel, bc);
	
		corpusNumberReferencesTextField = new JTextField();
		corpusNumberReferencesTextField.setEditable(false);
		corpusNumberReferencesTextField.setText("");
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 1;
		bc.gridx = 1;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNumberReferencesTextField, bc);
	
		corpusNgramsLabel = new JLabel();
		corpusNgramsLabel.setText("NGrams");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 2;
		bc.gridx = 0;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNgramsLabel, bc);
	
		corpusNgramsTextField = new JTextField();
		corpusNgramsTextField.setEditable(false);
		corpusNgramsTextField.setText("");
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 2;
		bc.gridx = 1;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusNgramsTextField, bc);
	}

	protected void initNewCorpusPanel() {
		super.initNewCorpusPanel();

		GridBagConstraints bc = new GridBagConstraints();
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.insets = new Insets(1, 1, 1, 1);

		// N-grams
		newCorpusNgramLabel = new JLabel();
		newCorpusNgramLabel.setText("Number of grams:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 2;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusNgramLabel, bc);
	
		newCorpusNgramDropbox = new JComboBox<Integer>();
		newCorpusNgramDropbox.setEditable(false);
		newCorpusNgramDropbox.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5 }));
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 2;
		bc.gridx = 1;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusNgramDropbox, bc);

	}
	
	
	protected void getInformations(String collectionName) {
		super.getInformations(collectionName);
		if (! collectionManager.isUnique(collectionName)) {
	        Corpus corpus = new DatabaseCorpus(collectionName);
	        Integer ngrams = corpus.getNumberGrams();
	        Integer numberRef = corpus.getNumberOfUniqueReferences();
	        corpusNgramsTextField.setText(ngrams.toString());
	        corpusNumberReferencesTextField.setText(numberRef.toString());
	     }
	}

	@Override
	protected void loadCancelButtonActionPerformed(ActionEvent evt) {
		if ("Load".equalsIgnoreCase(newCorpusFilenameLoadCancelButton.getText())) {
			String collectionName = newCorpusNameTextField.getText().trim();
			String filename = newCorpusFilenameTextField.getText().trim();
			File inputFile = new File(filename);
			if (!filename.isEmpty() && inputFile.isFile() && inputFile.exists() && inputFile.canRead()
					&& !collectionName.isEmpty()) {
				pdata.setSourceFile(filename);
				String corpusType = filename.substring(filename.lastIndexOf(".") + 1);
				int nrGrams = newCorpusNgramDropbox.getItemAt(newCorpusNgramDropbox.getSelectedIndex());
				if (corpusType.equalsIgnoreCase("bib")) {
					BibTeX2RIS bib = null;
					bib = new BibTeX2RIS();
					bib.setInputFile(new File(filename));
					bib.readData();
					bib.convert();
					importer = new ISICorpusDatabaseImporter(bib.getOutputFile().getAbsolutePath(), collectionName, nrGrams, this, false);
				} else if (corpusType.equalsIgnoreCase("isi")) {
					importer = new ISICorpusDatabaseImporter(filename, collectionName, nrGrams, this, false);
				} else if (corpusType.equalsIgnoreCase("enw")) {
					importer = new EndnoteDatabaseImporter(filename, collectionName, nrGrams, this, false);
				} else if (corpusType.equalsIgnoreCase("db")) {
					importer = new DumpDatabaseImporter(filename, this, false);
				} else if (corpusType.equalsIgnoreCase("csv")) {
					importer = new CSVDatabaseImporter(filename, collectionName, "", "", this);
				}
				if (importer != null) {
					newCorpusFilenameLoadCancelButton.setText("Cancel");
					setStatus("Loading collection " + collectionName + "...", true);
					loadingCollection();
					importer.importData();
				}
	
			} else {
				JOptionPane.showMessageDialog(this, "All parameters must be filled to load a new collection", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			importer.cancel();
			setStatus("", true);
		}
	}
	
	protected void loadingCollection() {
		super.loadingCollection();
	    newCorpusNgramDropbox.setEnabled(false);        
    }
	
	public void finishedLoadingCollection(String collection, boolean canceled) {
		super.finishedLoadingCollection(collection, canceled);
	    newCorpusNgramDropbox.setEnabled(true);
	}
	
}
