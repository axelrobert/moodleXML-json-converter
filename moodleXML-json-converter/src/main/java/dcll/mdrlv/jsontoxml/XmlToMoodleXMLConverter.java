package dcll.mdrlv.jsontoxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Francois MANCIET
 *
 */
public class XmlToMoodleXMLConverter {

	/**
	 * Valeur pour l'indentation.
	 */
	private int indent;

	/**
	 * buff : buffer dans lequel on ecrit
	 * le contenu du fichier transforme.
	 */
	private BufferedWriter buff;


	/**
	 * Logger pour afficher les erreurs.
	 */
	private Logger lOGGER;

	/**
	 * @return indent
	 */
	public final int getIndent() {
		return indent;
	}

	/**
	 * Constructeur.
	 */
	public XmlToMoodleXMLConverter() {
		lOGGER = Logger.getLogger(XmlToMoodleXMLConverter.class);
		this.indent = 1;
	}

	/**
	 * @param newIndent : nouvelle valeur d'indentation
	 */
	public final void setIndent(final int newIndent) {
		this.indent = newIndent;
	}

	/**
	 * @return buff
	 */
	public final BufferedWriter getBuff() {
		return buff;
	}

	/**
	 * @param newBuff nouvelle valeur pour buff
	 */
	public final void setBuff(final BufferedWriter newBuff) {
		this.buff = newBuff;
	}


	/**
	 * @return logger
	 */
	public final Logger getlOGGER() {
		return lOGGER;
	}

	/**
	 * @param newLOGGER : nouveau logger
	 */
	public final void setlOGGER(final Logger newLOGGER) {
		this.lOGGER = newLOGGER;
	}

	/**
	 * @param line : ligne a afficher
	 */
	public final void afficher(final String line) {
		try {
			buff.write(line);
		} catch (IOException e) {
			lOGGER.warn("Erreur ecriture"
			  + "dans xmlToMoodleXML");
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
	 * @param str : string a tester
	 * @return : renvoie vrai si la chaine contient un
	 * caractere numerique [0-9] ou alphabetique [A-Z a-z]
	 */
	public final boolean contientChiffreLettre(final String str) {
		boolean retour = false;
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				final Character caracter = str.charAt(i);
				if (Character.isDigit(caracter)
					|| Character.isAlphabetic(caracter)) {
					retour = true;
				}
			}
		}
		return retour;
	}

	/**
	 * @param node : noeud en entree
	 */
	public final void printDocument(final Node node) {
		if (!node.getNodeName().contains("#")) {
			afficherNoeud(node);
			afficherValeur(node);
		}
		traiterfils(node);
		if (!node.getNodeName().contains("#")) {
			fermerNoeud(node);
		}
	}


	/**
	 * @param node :
	 */
	public final void fermerNoeud(final Node node) {
		final String name = node.getNodeName();
		if (name.equals("type")
				|| name.equals("format")
				|| name.equals("fraction")) {
			afficher("");
		} else {
			if (name.equals("question")
				|| name.equals("answer")) {
				String parent = node.getParentNode().
						getNodeName();
				if ((parent.equals("question")
						&& name.equals("question"))
			        || (parent.equals("answer")
			        		&& name.equals("answer"))) {

					if (node.getNextSibling().
						getNextSibling() == null) {
						afficher("");
					} else {
						afficher("</"
					+ node.getNodeName() + ">\n");
						final Node suivant =
							node.getNextSibling();
						Node frere = null;
						if (suivant != null) {
							frere =
						    suivant.getNextSibling();
						}
						if (frere != null) {
							indenter();
						} else {
							indent--;
							indenter();
						}
					}
				} else {
					afficher("</"
				+ node.getNodeName() + ">\n");
					final Node suivant = node.getNextSibling();
					Node frere = null;
					if (suivant != null) {
						frere =
						suivant.getNextSibling();
					}
					if (frere != null) {
						indenter();
					} else {
						indent--;
						indenter();
					}
				}
			} else {
				afficher("</"
			+ node.getNodeName() + ">\n");
				final Node suivant = node.getNextSibling();
				Node frere = null;
				if (suivant != null) {
					frere = suivant.getNextSibling();
				}
				if (frere != null) {
					indenter();
				} else {
					indent--;
					indenter();
				}
			}
		}
	}

	/**
	 * @param node noeud a traiter
	 */
	private void traiterfils(final Node node) {
		final NodeList child = node.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			printDocument(child.item(i));
		}
	}

	/**
	 * @param node noeud a afficher
	 */
	private void afficherNoeud(final Node node) {
		final String name = node.getNodeName();

		//Le noeud est egal a question
		if (name.equals("question")
				|| name.equals("answer")
				|| name.equals("questiontext")) {
			final Node suivant = node.getFirstChild();
			Node fils = null;
			if (suivant != null) {
				fils = suivant.getNextSibling();
			}
			//Son fils est egalement question ou answer
			if (fils.getNodeName().
					equals("question")
			   || fils.getNodeName().equals("answer")) {
				//On affiche pas ce noeud
				afficher("");
			} else {
				//Autrement on affiche le d�but du noeud
				afficher("<" + name);
			}
		} else {
			if (name.equals("type")
				|| name.equals("format")
				|| name.equals("fraction")) {
				afficher(name + "=\"");
			} else {
				afficher("<" + name + ">");
			}
		}
	}

	/**
	 * @param node noeud a traiter
	 */
	public final void afficherValeur(final Node node) {
		final String name = node.getNodeName();
		if (node.hasChildNodes()) {
			final Node fils = node.getFirstChild();
			if (fils.getNodeType() == Node.TEXT_NODE) {
				if (contientChiffreLettre(
						fils.getNodeValue())) {
					if (name.equals("type")
						|| name.equals("format")
						|| name.equals("fraction")) {
						afficher(fils.getNodeValue()
								+ "\">\n");
						indent++;
						indenter();
					} else {
						afficher(fils.getNodeValue());
					}
				} else {
					if (name.equals("question")
						|| name.equals("answer")
						|| name.equals(
							"questiontext")) {
							afficher(" ");
					} else {
						afficher("\n");
						indent++;
						indenter();
					}
				}
			}
		}
	}

	/**
	 * @param input : chemin du fichier a traiter
	 * @param output : chemin du fichier de sortie
	 */
	public final void convertXMLtoMoodleXML(final String input,
			final String output) {
			try {
				final File file = new File(input);
				final DocumentBuilderFactory dbf =
					    DocumentBuilderFactory.newInstance();
				DocumentBuilder docbuilder;
				docbuilder = dbf.newDocumentBuilder();
				Document doc;
				try {
					doc = docbuilder.parse(file);
				    try {
						this.buff = new BufferedWriter(
								new FileWriter(
										new File(output)));
					    afficher(
					    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
						printDocument(doc);
						this.buff.flush();
						this.buff.close();
						this.indent = 0;
					} catch (IOException e) {
						lOGGER.warn("Erreur fichier entree"
						+ " XML-MoodleXML");
					}
				} catch (SAXException e) {
					lOGGER.warn("Erreur parsage"
					+ "XML-MoodleXML");
				} catch (IOException e) {
					lOGGER.warn("Erreur lecture fichier");
				}
			} catch (ParserConfigurationException e) {
				lOGGER.warn("Erreur parser XML-MoodleXML");
			}
	}
}
