package dcll.mdrlv.ihm;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * @author Emilien DUBOIS, Romain LECHIEN, Francois MANCIET, Axel ROBERT, David
 *         VILLARD.
 *
 *         Permet de selectionner un dossier.
 */
public class SelecteurDeDossier extends JFileChooser {

	/**
	 * chosenFolder : le chemin du dossier choisi.
	 */
	private String chosenFolder;
	/**
	 * parent : la fenetre parent à recuperer dans le constructeur.
	 */
	private Gui parent;

	/**
	 * Constructeur de la classe SelecteurDeDossier.
	 *
	 * @param p : une fenetre Gui parent
	 * @param url : le chemin d'un dossier
	 * La fenetre à adapter en fonction du choix de dossier.
	 */
	public SelecteurDeDossier(final Gui p, final String url) {
		// Sélecteur de fichiers
		super();
		parent = p;
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// Bloque le sélecteur d'extensions
		this.setAcceptAllFileFilterUsed(false);
		this.setVisible(true);
		this.setCurrentDirectory(new File(url));
		this.handlerSelecteurDeFichiers();
	}

	/**
	 * Méthode permettant la gestion du choix de l'utilisateur dans le
	 * selecteur.
	 */
	private void handlerSelecteurDeFichiers() {
		// Gestion du choix.
		int result = this.showOpenDialog(null);
		switch (result) {
		case JFileChooser.APPROVE_OPTION:

			chosenFolder =
			this.getSelectedFile().getAbsolutePath();

			parent.setPathOut(this.chosenFolder);
			this.setVisible(false);

		case JFileChooser.CANCEL_OPTION:
			chosenFolder = "";
			this.setVisible(false);

		default:
			break;
		}
	}

	/**
	 * @return la classe elle meme.
	 */
	public final JFileChooser getFileChooser() {
		return this;
	}

	/**
	 * @return le chemin absolu du dossier choisi
	 */
	public final String getCheminAbsolu() {
		return this.getSelectedFile().getAbsolutePath();
	}
}
