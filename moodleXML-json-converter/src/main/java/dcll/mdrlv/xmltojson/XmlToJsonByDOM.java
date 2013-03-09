package dcll.mdrlv.xmltojson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	 * @param output
	 * @param doc
	 * Constructeur
	 */
	public XmlToJsonByDOM(final String output, final Document doc) {
		try {
			out = new BufferedWriter(new FileWriter(
					new File(output)));
		} catch (IOException e) {
			System.out.println("Fichier non trouv�!!");
		}
	}

	/**
	 * @return out
	 */
    public BufferedWriter getOut() {
		return out;
	}

	/**
	 * @param out
	 */
	public void setOut(final BufferedWriter out) {
		this.out = out;
	}

	/**
	 * @return indent
	 */
	public final int getIndent() {
		return indent;
	}

	/**
	 * @param newIndent
	 */
	public final void setIndent(final int newIndent) {
		indent = newIndent;
	}

	/**
	 * @param line : ligne � afficher
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
	 * d�part en fonction de l'�l�ment node en entr�e
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
				//  mais a une valeur
				NamedNodeMap map = node.getAttributes();
				if (map.getLength() == 0) {
					afficher("\n");
				} else {
					//Affichage classique : {
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
		//Le noeud a un frere suivant identique
		//ou un frere precedent identique
		if ((suivant != null && frereSuivant != null
			&& frereSuivant.getNodeName().equals(nodeName))
			||
			(precedent != null && frerePrecedent != null
			&& frerePrecedent.getNodeName().equals(nodeName))) {
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node item = map.item(i);
				afficher("\"-" + item.getNodeName() + "\": \""
				+ item.getNodeValue() + "\"");
				afficher(",\n");
				if (i < map.getLength() - 1) {
					indenter();
				}
			}
		} else {
			//Le noeud n'a pas de freres identiques
			NamedNodeMap map = node.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node item = map.item(i);
				afficher("\"-" + item.getNodeName()
				+ "\": \"" + item.getNodeValue() + "\"");
				afficher(",\n");
				indenter();
			}
		}
	}

	/**
	 * @param node
	 * Affiche la valeur du node
	 */
	public final void afficherValeur(final Node node) {
		NamedNodeMap map = node.getAttributes();
		if (node.getTextContent() != null && map.getLength() != 0) {
			afficher("\"" + node.getTextContent()
			+ "\" ");
		} else {
			if (node.getTextContent() != null) {
				afficher("\"#text\": \""
			    + node.getTextContent() + "\"");
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
				if (suivant != null && frereSuivant != null) {
					//on affiche rien
					afficher("");
				} else {
					indent--;
					indenter();
					afficher("}\n");
				}
			}
		}
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
