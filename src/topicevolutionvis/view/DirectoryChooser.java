package topicevolutionvis.view;

import javax.swing.JFileChooser;

public class DirectoryChooser {
	
	public DirectoryChooser() {
		init();
	}
	
	private void init() {
		fc = new JFileChooser();
		
		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("Select he implementations directory");
		fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);

        int result = fc.showOpenDialog(null); 
        if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("SAIR");
        }
	}
	
	public JFileChooser getFc() {
		return fc;
	}

	public void setFc(JFileChooser fc) {
		this.fc = fc;
	}

	private JFileChooser fc;
}
