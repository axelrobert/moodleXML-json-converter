package dcll_project.mdrlv.ihm;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class SelecteurDeFichier extends JFrame{

	private JFileChooser fc;
	
	public SelecteurDeFichier(FileFilter f){
		//Sélecteur de fichiers
		fc = new JFileChooser();
		fc.setFileFilter(f);
		fc.setAcceptAllFileFilterUsed(false); //Bloque le sélecteur d'extensions 
		
		this.add(fc);
		this.setVisible(true);
		
		
	}
	
	public JFileChooser getFileChooser(){
		return fc;
	}
	
	public String getCheminAbsolu(){
		return fc.getSelectedFile().getAbsolutePath();
	}
}
