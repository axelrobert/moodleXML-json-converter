package dcll.mdrlv.ihm;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Emilien DUBOIS, Romain LECHIEN, Francois MANCIET, Axel ROBERT, David
 *         VILLARD.
 *
 *         Permet de selectionner un fichier.
 */
public class SelecteurDeFichier extends JFileChooser {

	/**
	 * chosenFile : le fichier choisi par l'utilisateur.
	 */
	private String chosenFile;
	/**
	 * parent : la fenetre parent au selecteur de fichier.
	 */
	private Gui parent;

	/**
	 * Constructeur de la classe SelecteurDeFichier.
	 * @param pFile
	 * Filtre permettant la restriction des fichiers choisis sur le
	 * selecteur de fichiers.
	 * @param pParent
	 * La fenetre à adapter en fonction du choix de dossier.
	 */
	public SelecteurDeFichier(final FileFilter pFile, final Gui pParent) {
		//Sélecteur de fichiers
		super();
		parent = pParent;
		this.setFileFilter(pFile);
		//Bloque le sélecteur d'extensions
		this.setAcceptAllFileFilterUsed(false);

		this.setVisible(true);

		this.handlerSelecteurDeFichiers();
	}

	/**
	 * Méthode permettant la gestion du choix de l'utilisateur dans le
	 * selecteur.
	 */
	private void handlerSelecteurDeFichiers() {
		//Gestion du choix.
		final int result = this.showOpenDialog(null);
	       switch(result) {
	        	case JFileChooser.APPROVE_OPTION:
		       	 chosenFile = this.getCheminAbsolu();
		       	 parent.setPathIN(this.chosenFile);
		       	 this.setVisible(false);
		        parent.getPathIN().setEditable(false);

	        	case JFileChooser.CANCEL_OPTION:
	        	chosenFile = "";
		        parent.getPathIN().setEditable(true);
		      	this.setVisible(false);

		      	default: break;
	        }
	}

	/**
	 * @return la fenetre en elle meme.
	 */
	public final JFileChooser getFileChooser() {
		return this;
	}

	/**
	 * @return le chemin du fichier choisi.
	 */
	public final String getCheminAbsolu() {
		return this.getSelectedFile().getAbsolutePath();
	}
}
