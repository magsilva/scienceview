package topicevolutionvis.wizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.opendevl.JFlat;
import com.mashape.unirest.http.Unirest;

import cc.mallet.pipe.Filename2CharSequence;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import topicevolutionvis.data.*;
import topicevolutionvis.database.CollectionManager;
import topicevolutionvis.projection.ProjectionData;

/**
 * Wizard to create and select code collections.
 * @author Danilo Sambugaro
 */

public class SourceCodeWizard extends DataImportWizard {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel newCorpusInputPathLabel;
	private JTextField newCorpusInputPathTextField;
	private JButton newCorpusInputPathSearchButton;
	private JComboBox<String> newCorpusLanguageComboBox;
	private JLabel newCorpusLanguageLabel;

	private JLabel corpusLanguageLabel;
	private JTextField corpusLanguageTextField;
	
	
	private JPanel corpusServicesPanel;
	private JCheckBox[] corpusServices;
	private JButton corpusServiceRefresh;
	private static String LABEL_SERVICES[] = {"CCCC","Halstead"};
	private static String URL_SERVICES[] = {"http://localhost:5000/cccc/run/default/","http://localhost:5000/halstead/run/"};
	private static String languagesSupported[] = {"C", "C++"};
	

	/**
	 * Creates new form DataSourceChoice
	 */
	public SourceCodeWizard(ProjectionData pdata) {
		this.pdata = pdata;
		collectionManager = new CollectionManager();
		initComponents();
		updateCollections("");
	}

	protected void initComponents() {
		super.initComponents();
		
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.HORIZONTAL;

		initCorpusServicesPanel();
		bc.gridy = 2;
		bc.gridx = 0;
		bc.gridwidth = 2;
		add(corpusServicesPanel, bc);
	}

	private void initCorpusServicesPanel() {
		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);

		corpusServicesPanel = new JPanel();
		corpusServicesPanel.setBorder(BorderFactory.createTitledBorder("Services"));
		corpusServicesPanel.setLayout(new GridBagLayout());
		
		int y = 0;
		int x = 0;
		
		corpusServices = new JCheckBox[LABEL_SERVICES.length];
		
		for (int i = 0; i < LABEL_SERVICES.length; i++) {
			corpusServices[i] = new JCheckBox(LABEL_SERVICES[i]);
			bc.anchor = GridBagConstraints.LINE_START;
			bc.fill = GridBagConstraints.NONE;
			bc.gridy = y;
			bc.gridx = x++;
			bc.gridwidth = 1;
			corpusServicesPanel.add(corpusServices[i], bc);
			corpusServices[i].setEnabled(checkServiceState(URL_SERVICES[i]));
			if (x == 5) {
				y++;
				x = 0;
			}
		}
		
		corpusServiceRefresh = new JButton("Refresh");
		corpusServiceRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				refreshServicesState(evt);
			}
		});
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = ++y;
		bc.gridx = 0;
		bc.gridwidth = 1;
		corpusServicesPanel.add(corpusServiceRefresh, bc);
		
		corpusServiceRefresh = new JButton("Run");
		corpusServiceRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
							runServices(evt);
							// TODO: use CSVDatabaseImporter
			}
		});
		bc.anchor = GridBagConstraints.LINE_END;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = y;
		bc.gridx = 1;
		bc.gridwidth = 1;
		corpusServicesPanel.add(corpusServiceRefresh, bc);
		
		
	}

	/**
	 * Generate CSV file from JSON data received from service.
	 * 
	 * @param evt
	 */
	
	protected void runServices(ActionEvent evt) {
		String fileName = null;
		String fileContent = null;
		String URL = null;
		String fileYear = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String path = newCorpusInputPathTextField.getText();
//		for (int i = 0; i < LABEL_SERVICES.length; i++) {
//			if (corpusServices[i].isSelected()) {
//	            File folder = new File(path);
//	            File[] listOfFiles = folder.listFiles();
//	            JSONArray results = new JSONArray();
//	            JFlat parse;
//	            for (int j=0; j < listOfFiles.length; j++ ) {
//	            	String extension = listOfFiles[j].getName().substring(listOfFiles[j].getName().lastIndexOf(".")+1);
//	                if ((listOfFiles[j].isFile()) && (checkFileExtension(extension, newCorpusLanguageComboBox.getSelectedItem().toString()))) {
//	                    try {
//	                    	fileName = listOfFiles[j].getName();
//	                    	fileContent = FileUtils.readFileToString(listOfFiles[j]);
//	                    	fileYear = sdf.format(listOfFiles[j].lastModified()).concat("/");
//	                    	URL = URL_SERVICES[i].concat(fileYear).concat(fileName);
//	                    	results.put(Unirest.post(URL).body(fileContent).asJson().getBody().getObject());
//	            		} catch (Exception e) {
//	            			String error = "Falha com o arquivo " + listOfFiles[j].getName()+"\nErro: " + e;
//	            			JOptionPane.showMessageDialog(corpusServicesPanel, error, "error", JOptionPane.ERROR_MESSAGE);
//	            			System.out.println("Falha com o arquivo " + listOfFiles[j].getName()+"\nErro: " + e);
//	            		}
//	                }
//	            }
//	            System.out.println(results.toString());
//	            parse = new JFlat(results.toString());
//	            try {
//					parse.json2Sheet().headerSeparator("_").write2csv("/home/dsambugaro/UTFPR/IC/teste/dados.csv",':');
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		JFlat parse;
		File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        JSONArray results = new JSONArray();
		for (int j = 0; j < listOfFiles.length; j++) {
			String extension = listOfFiles[j].getName().substring(listOfFiles[j].getName().lastIndexOf(".")+1);
			if ((listOfFiles[j].isFile()) && (checkFileExtension(extension, newCorpusLanguageComboBox.getSelectedItem().toString()))) {
				ArrayList<JSONObject> aux_results = new ArrayList<JSONObject>();
				for (int i=0; i < LABEL_SERVICES.length; i++ ) {
					if (corpusServices[i].isSelected()) {
						try {
							fileName = listOfFiles[j].getName();
							fileContent = FileUtils.readFileToString(listOfFiles[j]);
							fileYear = sdf.format(listOfFiles[j].lastModified()).concat("/");
							URL = URL_SERVICES[i].concat(fileYear).concat(fileName);
							aux_results.add(Unirest.post(URL).body(fileContent).asJson().getBody().getObject());
						} catch (Exception e) {
							String error = "Falha com o arquivo " + listOfFiles[j].getName()+"\nErro: " + e;
							JOptionPane.showMessageDialog(corpusServicesPanel, error, "error", JOptionPane.ERROR_MESSAGE);
							System.out.println("Falha com o arquivo " + listOfFiles[j].getName()+"\nErro: " + e);
						}
					}
				}
				JSONObject merged_results = new JSONObject();
				JSONObject[] file_results = new JSONObject[aux_results.size()];
				for (int k = 0; k < aux_results.size(); k++) {
					file_results[k] = aux_results.get(k);
				}
				for (JSONObject result : file_results) {
				    Iterator it = result.keys();
				    while (it.hasNext()) {
				        String key = it.next().toString();
				        merged_results.put(key, result.get(key));
				    }
				}
				results.put(merged_results);
			}
		}
        System.out.println(results.toString());
        parse = new JFlat(results.toString());
        try {
			parse.json2Sheet().headerSeparator("_").write2csv("/home/dsambugaro/UTFPR/IC/teste/dados.csv",':');
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private boolean checkFileExtension(String extension, String language) {
		if((language.intern() == "C") && (extension.intern() == "c")) {
			return true;
		}
		if ((language.intern() == "C++") && ((extension.intern() == "cc") || (extension.intern() == "cpp"))) {
			return true;
		}
		return false;
	}

	private boolean checkServiceState(String url) {
		try {
			int status = Unirest.get(url).asJson().getStatus();
			if (status == 200) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;

		}
	}

	protected void refreshServicesState(ActionEvent evt) {
		for (int i = 0; i < LABEL_SERVICES.length; i++) {
			corpusServices[i].setEnabled(checkServiceState(URL_SERVICES[i]));
		}
	}

	protected void initCorpusInformationPanel() {
		super.initCorpusInformationPanel();

		GridBagConstraints bc = new GridBagConstraints();
		bc.insets = new Insets(1, 1, 1, 1);

		corpusLanguageLabel = new JLabel();
		corpusLanguageLabel .setText("Language");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 1;
		bc.gridx = 0;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusLanguageLabel , bc);
		
		corpusLanguageTextField = new JTextField();
		corpusLanguageTextField.setEditable(false);
		corpusLanguageTextField.setText("");
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 1;
		bc.gridx = 1;
		bc.gridwidth = 1;
		corpusInformationPanel.add(corpusLanguageTextField, bc);
	}

	protected void initNewCorpusPanel() {
		super.initNewCorpusPanel();

		GridBagConstraints bc = new GridBagConstraints();
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.insets = new Insets(1, 1, 1, 1);



		// Input path
		newCorpusInputPathLabel = new JLabel();
		newCorpusInputPathLabel.setText("Input path:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 4;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusInputPathLabel, bc);

		newCorpusInputPathTextField = new JTextField();
		newCorpusInputPathTextField.setColumns(30);
		newCorpusInputPathTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				corpusPathActionPerformed();
			}
		});
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 4;
		bc.gridx = 1;
		bc.gridwidth = 2;
		newCorpusPanel.add(newCorpusInputPathTextField, bc);

		newCorpusInputPathSearchButton = new JButton();
		newCorpusInputPathSearchButton.setText("Select path");
		newCorpusInputPathSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchPathButtonActionPerformed();
			}
		});
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 4;
		bc.gridx = 3;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusInputPathSearchButton, bc);
		
		// Language selection
		newCorpusLanguageLabel = new JLabel();
		newCorpusLanguageLabel.setText("Language:");
		bc.anchor = GridBagConstraints.LINE_START;
		bc.fill = GridBagConstraints.NONE;
		bc.gridy = 6;
		bc.gridx = 0;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusLanguageLabel, bc);

		newCorpusLanguageComboBox = new JComboBox<String>(languagesSupported);
		newCorpusLanguageComboBox.setEditable(false);
		
		bc.fill = GridBagConstraints.HORIZONTAL;
		bc.anchor = GridBagConstraints.LINE_START;
		bc.gridy = 6;
		bc.gridx = 1;
		bc.gridwidth = 1;
		newCorpusPanel.add(newCorpusLanguageComboBox, bc);
	}
	
	protected void setEnableNewCorpus(boolean Enable) {
		super.setEnableNewCorpus(Enable);
		newCorpusInputPathLabel.setEnabled(Enable);
		newCorpusInputPathSearchButton.setEnabled(Enable);
		newCorpusInputPathTextField.setEnabled(Enable);
		newCorpusLanguageComboBox.setEnabled(Enable);
		newCorpusLanguageLabel.setEnabled(Enable);
	}

	protected void corpusPathActionPerformed() {
		File file = new File(newCorpusInputPathTextField.getText().trim());
		checkNewCorpusInputFile(file);
	}

	private void searchPathButtonActionPerformed() {
		JFileChooser fc = new JFileChooser();
		fc = new JFileChooser();

		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("Select the implementations directory");
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);

		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			newCorpusInputPathTextField.setText(fc.getSelectedFile().toString());
		}
	}

	@Override
	protected void loadCancelButtonActionPerformed(ActionEvent evt) {
		if ("Load".equalsIgnoreCase(newCorpusFilenameLoadCancelButton.getText())) {
			String collectionName = newCorpusNameTextField.getText().trim();
			String filename = newCorpusFilenameTextField.getText().trim();
			String path = newCorpusInputPathTextField.getText().trim();
			String language =newCorpusLanguageComboBox.getSelectedItem().toString().trim();
			File inputFile = new File(filename);
			if (! filename.isEmpty() && inputFile.isFile() && inputFile.exists() && inputFile.canRead() && ! collectionName.isEmpty()) {
				pdata.setSourceFile(filename);
				String corpusType = filename.substring(filename.lastIndexOf(".") + 1);
				if (corpusType.equalsIgnoreCase("csv")) {
					importer = new CSVDatabaseImporter(filename, collectionName, path, language, this);
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
}
