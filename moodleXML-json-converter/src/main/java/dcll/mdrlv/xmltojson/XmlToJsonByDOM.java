package dcll.mdrlv.xmltojson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Francois Manciet
 *
 */
public class XmlToJsonByDOM {

    /**
     * indent : Valeur pour l'indentation.
     */
    private int indent;

    /**
	* out : sortie pour l'ecrture du fichier.
	*/
	private BufferedWriter out;

	/**
	 * lOGGER : logger de la classe.
	 */
	private static Logger lOGGER;

	/**
	 * @param output : chemin du fichier Json a creer
	 * @param doc : document en entree, utilise par le parser
	 * Constructeur
	 */
	public XmlToJsonByDOM(final String output, final Document doc) {
		try {
			this.out = new BufferedWriter(new FileWriter(
					new File(output)));
			this.indent = 1;
			lOGGER = Logger.getLogger(XmlToJsonByDOM.class);
		} catch (IOException e) {
			lOGGER.warn("Fichier non trouve!!");
		}
	}

	/**
	 * @param log :
	 */
	public final void setlOGGER(final Logger log) {
		lOGGER = log;
	}

	/**
	 * @return :
	 */
	public final Logger getlOGGER() {
		return lOGGER;
	}

	/**
	 * @return out
	 */
    public final  BufferedWriter getOut() {
		return out;
	}

	/**
	 * @param buff : nouveau BufferWriter
	 */
	public final void setOut(final BufferedWriter buff) {
		this.out = buff;
	}

	/**
	 * @return indent
	 */
	public final int getIndent() {
		return indent;
	}

	/**
	 * @param newIndent : nouvelle valeur pour l'indentation
	 */
	public final void setIndent(final int newIndent) {
		indent = newIndent;
	}

	/**
	 * @param line : ligne a afficher
	 */
	public final void afficher(final String line) {
		try {
			out.write(line);
		} catch (IOException e) {
			System.out.print(
			  "Erreur affichage : fichier non trouv�.");
		}
	}

	/**
	 * Ajoute une ligne d'indentation
	 * selon le parametre indent.
	 */
	public final void indenter() {
		for (int i = 0; i < indent; i++) {
			afficher("   ");
		}
	}

	/**
	 * @param str : string � tester
	 * @return : renvoie vrai si la chaine contient un
	 * caractere num�rique [0-9] ou alphab�tique [A-Z a-z]
	 */
	public final boolean contientChiffreLettre(final String str) {
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				Character c = str.charAt(i);
				if (Character.isDigit(c)
					|| Character.isAlphabetic(c)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param node : contient le noeud a afficher
	 */
	public final void afficherNoeud(final Node node) {
		/* si le noeud a un frere identique apr�s mais pas avant*/
		Node frerePrecedent = null;
		Node frereSuivant = null;
		final Node precedent = node.getPreviousSibling();
		if (precedent != null) {
			frerePrecedent = precedent.getPreviousSibling();
		}
		final Node suivant = node.getNextSibling();
		if (suivant != null) {
			frereSuivant = suivant.getNextSibling();
		}
		final String nodeName = node.getNodeName();
		//Si le noeud a un frere identique apr�s mais pas avant
		if (((precedent != null && frerePrecedent != null
             && !frerePrecedent.getNodeName().equals(nodeName))
			 || (precedent == null))
			 &&
			 (suivant != null && frereSuivant != null
			  && frereSuivant.getNodeName().equals(nodeName))
		   ) {
			afficher("\"" + nodeName + "\": ");

		} else {
			//Le noeud a un frere identique avant
			if (precedent != null && frerePrecedent != null
			&& frerePrecedent.getNodeName().equals(nodeName)) {
				//On affiche rien
				afficher("");
			} else {
				afficher("\"" + nodeName + "\": ");
			}
		}
	}

	/**
	 * @param node : determination du symbole de
	 * depart en fonction de l'element node en entree
	 */
	public final void choixSymboleDebut(final Node node) {
		final Node precedent = node.getPreviousSibling();
		final Node suivant = node.getNextSibling();
		Node frereSuivant = null;
		Node frerePrecedent = null;
		String nodeName = node.getNodeName();
		if (precedent != null) {
			frerePrecedent = precedent.getPreviousSibling();
		}
		if (suivant != null) {
			frereSuivant = suivant.getNextSibling();
		}

		//Le noeud a un frere precedent identique : {
		if (precedent != null && frerePrecedent != null
		&& frerePrecedent.getNodeName().equals(nodeName)) {
			afficher("{ \n");
			indent++;
			indenter();
		} else {
			//le noeud a un frere suivant identique : [
			//                                            {
			if (suivant != null && frereSuivant != null
			&& frereSuivant.getNodeName().equals(nodeName)) {
				afficher("[\n");
				indent++;
				indenter();
				afficher("{\n");
				indent++;
				indenter();
			} else {
				// le noeud n'a pas d'attributs
				NamedNodeMap map = node.getAttributes();
				int childNumber =
						node.getChildNodes().
						getLength();
				if (map.getLength() == 0) {
					if (node.hasChildNodes()
					&& childNumber >= 2) {

						afficher("{\n");
						indent++;
						indenter();
					} else {
						afficher("");
					}
				} else {
					afficher("{\n");
					indent++;
					indenter();
				}
			}
		}
	}

	/**
	 * @param node
	 * Affiche les attributs contenus dans node, si il y en a
	 */
	public final void afficherAttributs(final Node node) {
		NamedNodeMap map = node.getAttributes();
		for (int i = map.getLength() - 1; i >= 0; i--) {
			Node item = map.item(i);
			afficher("\"-" + item.getNodeName() + "\": \""
			+ item.getNodeValue() + "\"");

			// L'attribut n'est pas le dernier
			if (i > 0) {
				afficher(",\n");
				indenter();
			//L'attribut est le dernier
			} else {
				//Si le noeud a un contenu textuel
				if (node.hasChildNodes()) {
					afficher(",\n");
					indenter();
				//Le noeud n'a pas de fils
				//(=> pas de valeur non plus)
				} else {
					afficher("\n");
				}
			}
		}
	}

	/**
	 * @param node
	 * Affiche la valeur du node
	 */
	public final void afficherValeur(final Node node) {
		if (node.hasChildNodes()) {
			Node fils = node.getFirstChild();
			if (fils.getNodeType() == Node.TEXT_NODE) {
				if (contientChiffreLettre(
						fils.getNodeValue())) {
					NamedNodeMap map = node.getAttributes();
					if (map.getLength() != 0) {
						afficher("\"#text\": "
						+ "\"" + fils.getNodeValue()
						+ "\"\n");
					} else {
						afficher("\""
					    + fils.getNodeValue() + "\"");
					}
				}
			}
		}
	}

	/**
	 * @param node
	 * Traite tous les fils de node
	 */
	public final void traiterFils(final Node node) {
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			printDocument(nodes.item(i));
		}
	}

	/**
	 * @param node
	 * Determine le symbole de fin de phrase
	 * en fonction du node en entr�e
	 */
	public final void choixSymboleFin(final Node node) {
		Node precedent = node.getPreviousSibling();
		Node suivant = node.getNextSibling();
		Node frereSuivant = null;
		Node frerePrecedent = null;
		String nodeName = node.getNodeName();
		int childNumber = node.getChildNodes().
				getLength();
		if (precedent != null) {
			frerePrecedent = precedent.getPreviousSibling();
		}
		if (suivant != null) {
			frereSuivant = suivant.getNextSibling();
		}
		//Si le noeud a un fr�re identique apr�s
		if (suivant != null && frereSuivant != null
		&& frereSuivant.getNodeName().equals(nodeName)) {
			indent--;
			indenter();
			afficher("},\n");
			indenter();
		} else {
			//si le noeud a un fr�re identique avant mais pas apr�s
			if (precedent != null && frerePrecedent != null
			&& frerePrecedent.getNodeName().equals(nodeName)) {
				indent--;
				indenter();
				afficher("}\n");
				//si le noeud a un frere apres
				if (suivant != null && frereSuivant != null) {
					indent--;
					indenter();
					afficher("],\n");
					indenter();
				} else {
					indent--;
					indenter();
					afficher("]\n");
				}

			} else {
				//Le noeud n'a pas de frere identique avant
				//il a un frere apres
				if ((suivant != null && frereSuivant != null)) {
					//Le noeud a un fils
					if (node.hasChildNodes()
						&& childNumber > 1) {
						afficher("\n");
						indent--;
						indenter();
						afficher("},\n");
						indenter();
						//Le noeud n'a pas de fils
					} else {
						afficher(",\n");
						indenter();
					}
					//Le noeud n'a pas de fr�re,
					//il est le dernier
				} else {
					//Si le noeud est le dernier
					//et n'a pas de fils
					if (childNumber <= 1) {
						afficher("");
					} else {
						afficher("\n");
						indent--;
						indenter();
						afficher("}\n");
					}
				}
			}
		}
	}

	/**
	 * @param node
	 * Fonction d'appel pour la conversion XML Json
	 * Pr� indentation et post indentation avant l'appel
	 * de printDocument
	 */
	public final void convertXMLtoJSON(final Node node) {
		afficher("{\n");
		indenter();
		printDocument(node);
		afficher("}");
	}

	/**
	 * @param node
	 * Ecrit dans un fichier la conversion
	 * d'un fichier XML en JSON
	 */
	public final void printDocument(final Node node) {
		if (!node.getNodeName().contains("#")) {
			afficherNoeud(node);
			choixSymboleDebut(node);
			afficherAttributs(node);
		}
		traiterFils(node);
		if (!node.getNodeName().contains("#")) {
			choixSymboleFin(node);
		}
	  }
}
