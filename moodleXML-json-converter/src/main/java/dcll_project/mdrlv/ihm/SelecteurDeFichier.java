package dcll_project.mdrlv.ihm;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class SelecteurDeFichier extends JFileChooser{

	private String chosenFile;
	
	public SelecteurDeFichier(FileFilter f){
		//Sélecteur de fichiers
		super();
		this.setFileFilter(f);
		this.setAcceptAllFileFilterUsed(false); //Bloque le sélecteur d'extensions 
		
		this.setVisible(true);
		
		this.handlerSelecteurDeFichiers();
	}
	
	private void handlerSelecteurDeFichiers() {
		//Gestion du choix.
		int result = this.showOpenDialog(null);
	       switch(result){
	        	case JFileChooser.APPROVE_OPTION: 
		       	 chosenFile= this.getCheminAbsolu();
		       	 this.setVisible(false);
		        	
	        	case JFileChooser.CANCEL_OPTION:
	        	chosenFile = "";
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
