package dcll.mdrlv.ihm;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class SelecteurDeDossier extends JFileChooser{

	private String chosenFolder;
	private Gui parent;
	
	public SelecteurDeDossier(Gui p){
		//Sélecteur de fichiers
		super();
		parent = p;
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.setAcceptAllFileFilterUsed(false); //Bloque le sélecteur d'extensions 
		
		this.setVisible(true);
		
		this.handlerSelecteurDeFichiers();
	}
	
	private void handlerSelecteurDeFichiers() {
		//Gestion du choix.
		int result = this.showOpenDialog(null);
	       switch(result){
	        	case JFileChooser.APPROVE_OPTION: 
	        		
		       	 chosenFolder= this.getSelectedFile().getAbsolutePath();
		       	 parent.setPathOut(this.chosenFolder);
		       	 this.setVisible(false);
		        	
	        	case JFileChooser.CANCEL_OPTION:
	        	chosenFolder = "";
		      	this.setVisible(false);
	        }
	}

	public JFileChooser getFileChooser(){
		return this;
	}
	
	public String getCheminAbsolu(){
		return this.getSelectedFile().getAbsolutePath();
	}
}
