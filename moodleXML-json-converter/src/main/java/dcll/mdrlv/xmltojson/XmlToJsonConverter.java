package dcll.mdrlv.xmltojson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.tools.FileConformity;
import dcll.mdrlv.tools.Tools;

/**
 * @author :
 *
 */
public class XmlToJsonConverter extends WebStandardConverter {

	/**
	 * Feuille de style XSLT
	 */
	private final String xsltStylesheet;

	/**
	 * Document
	 */
	private Document document;

    /**
     * Racine JDOM Tree
     */
    private Element racine;

    /**
     * Fichier XML temporaire
     */
    private File xmlFile;

	/**
	 * @param chaine : le nom du fichier XSLT
	 */
	public XmlToJsonConverter(final String chaine) {
		super();
		xsltStylesheet = "ressources/" + chaine;
		}

	@Override
	public final boolean accordanceWithMoodleStandard(final File file) {
			//Appel à la méthode accordanceWithMoodleXML pour
			//vérifier que le fichier est bien au format MoodleXML
			return (accordanceWithMoodleXML(file));
	}

	/**
	 * @param anXmlFile : le fichier XML à valider
	 * @param xsdFile : le XML Schéma
	 * @return : true si le XML est conforme au XSD, false sinon
	 */
	public final boolean isValidateXSD(final File anXmlFile,
			final File xsdFile) {
		//Création d'un schéma XML (XSD) factory
		XMLReaderJDOMFactory schemafac = null;
		Logger lOGGER = super.getlOGGER();
		try {
			schemafac = new XMLReaderXSDFactory(xsdFile);
		} catch (JDOMException e) {
			lOGGER.error("Erreur lors de l'instanciation"
					+ " du XMLReaderXSDFactory");
		}
		//Création d'un builder SAX à partir du schéma factory
		SAXBuilder builder = new SAXBuilder(schemafac);
		try {
			//On parse le fichier XML selon le XSD fournit plus haut
			builder.build(anXmlFile);
		} catch (JDOMException e) {
			//Si erreur lors du build
			//alors le XML n'est pas conforme au XSD
			lOGGER.error(e.getMessage());
			return false;
		} catch (IOException e) {
			lOGGER.error("Erreur d'entrée-sortie");
			return false;
		}
		return true;
	}

	/**
	 * @param elt : element courant du JDOM Tree
	 * @return : boolean (voir la valeur de retour de
	 * {@link #isValidateXSD(File, File) isValidateXSD})
	 */
	public final boolean validateAccordingToType(final Element elt) {
		//On récupère le type de la question
		String type = elt.getAttributeValue("type");
		//Selon le type on fait appel au XSD adéquat et
		//on fait appel à la méthode isValidateXSD pour
		//vérifier si la question est valide ou pas
		String uriFile = "ressources/" + type + ".xsd";
		final  File xsdFile = new File(uriFile);
		return (isValidateXSD(xmlFile, xsdFile));
	}

	/**
	 * @param file : fichier XML à vérifier
	 * @return : true si valide au format moodleXML, false sinon
	 */
	public final boolean accordanceWithMoodleXML(final File file) {
		 //On crée une instance de SAXBuilder
	      SAXBuilder sxb = new SAXBuilder();
	      Logger lOGGER = super.getlOGGER();

	      try {
	         //On crée un nouveau document JDOM
	    	  //avec en argument le fichier XML
	         //Le parsing est terminé ;)
	    	 document = sxb.build(file);
	      } catch (Exception e) {
	    	  lOGGER.error("Erreur lors de la creation du JDOM");
	    	  return false;
	      }

	      //On initialise un nouvel élément racine
	      //avec l'élément racine du document.
	      racine = document.getRootElement();
	      //On crée une List contenant tous les noeuds "question"
	      //de l'Element racine
	      List<Element> list = racine.getChildren("question");
	      //Si la racine n'est pas de type quiz ou
	      // si il n'y a aucune balise de type question
	      // Alors le fichier en entrée ne peut etre du MOODLE XML.
	      // Inutile de continuer le test
	     if (!racine.getName().contentEquals("quiz")
	    		  || list.size() ==  0) {
	    	  return false;
	      }

	      //On crée un Iterator sur notre liste
	      Iterator<Element> i = list.iterator();
	      while (i.hasNext()) {
	         //On recrée l'Element courant à chaque tour de boucle
	         Element courant = i.next();

	         //Test pour savoir si l'élément shuffleanswers existe 2 fois
	         XPathExpression<Object> xpath =
	        		    XPathFactory.instance().compile(
	        		    		"count(//shuffleanswers)");
	        		Double val = (Double) xpath.evaluateFirst(
	        				document);
	        		if (val > 1.0) {
	        			lOGGER.error(
	        					"La balise "
	        					+
	        					"<shuffleanswers>"
	        					+
	        					" "
	        					+
	        					"ne doit apparaître"
	        					+
	        					" qu'une fois "
	        					+
	        					"par question.");

	        			//Afin d'éviter une java null exception
	        			xmlFile = new File("question.xml");

	        			return false;
	        		}

	         /* Ici, pour chaque question on crée un
	          * nouveau fichier XML contenant
	          * l'aborescence de la question
	          * entouré de la balise <quiz> afin de pouvoir
	          * tester chaque type de question avec son XSD adéquat.
	          */

	         //On crée donc un nouvel Element racine quiz
	         Element root = new Element("quiz");
	         //On crée un nouveau Document à partir de la nouvelle racine
	         Document xmlDoc = new Document(root);
	         //On crée un nouvel Element question et on l'ajoute
	         //en tant qu'Element de racine
	         Element question = new Element(courant.getName());
	         root.addContent(question);
	         //On crée un nouvel Attribut type et on l'ajoute à question
	         //grâce à la méthode setAttribute
	         Attribute typeAtt = new Attribute("type",
	        		 courant.getAttributeValue("type"));
	         question.setAttribute(typeAtt);
	         //On clone le contenu du noeud courant
	         //pour récupérer l'arborescence
	         //de l'Element question et on l'ajoute en tant qu'Element
	         //du nouveau noeud question
	         question.addContent(courant.cloneContent());
	         //On utilise ici un affichage classique avec getPrettyFormat()
	         XMLOutputter sortie = new XMLOutputter(
	        		 Format.getPrettyFormat());
	         try {
	        	//Création du fichier XML qui contiendra
	        	//l'arborescence de la question courante
	        	xmlFile = new File("question.xml");
	        	//On écrit le contenu du Document dans le fichier XML
				sortie.output(xmlDoc,
						new FileOutputStream(xmlFile));
			 } catch (IOException e) {
				 lOGGER.error("Erreur d'affichage du document");
			 }
	         //On fait appel à la méthode validateAccordingToType
	         //pour vérifier que le XML contenant l'arborescence
	         //de la question courante est conforme à son XSD
	         //selon le type de la question
	         if (!validateAccordingToType(courant)) {
	        	 return false;
	         }

	      }
	      return true;
	}

	/**
	 * @param file : fichier XML à vérifier
	 * @return : org.w3c.dom.Document (fichier XML parsé)
	 * / null si non valide au format XML
	 */
	public final org.w3c.dom.Document accordanceWithXML(
			final File file) {
		org.w3c.dom.Document outputDoc = null;
		Logger lOGGER = super.getlOGGER();
		//On crée une nouvelle instance d'un Document factory
		final DocumentBuilderFactory dbfact =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			//On crée un nouveau Document builder
			//à partir de la factory
			docBuilder = dbfact.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			lOGGER.error("Erreur de configuration"
					+ " du parseur DOM");
		}
		try {
			//On parse le fichier XML
			outputDoc = docBuilder.parse(file);
		} catch (SAXException e) {
			//Si exception alors le fichier
			//n'est pas conforme au format XML
			lOGGER.error("Erreur lors du parsing"
					+
							" du document");
			lOGGER.error(e.getMessage());
		} catch (IOException e) {
			lOGGER.error("Erreur d'entrée-sortie");
		}
		return outputDoc;
	}


	@Override
	public final boolean accordanceWithStandard(final File file) {
			//Appel à la méthode accordanceWithXML pour
			//vérifier que le fichier est bien au format XML
			return (accordanceWithXML(file) != null);
	}

	/**
	 * @param inputFile : StreamSource du fichier XML
	 * @param anXsltStylesheet : StreamSource du XSLT
	 * @param out : StreamResult du fichier résultat JSON
	 * @return 0 si OK, -1 si erreur de configuration du transformer,
	 * 1 si erreur lors de la transformation
	 */
	public final int transformXmlToJsonViaXSLT(
			final StreamSource inputFile,
			final StreamSource anXsltStylesheet,
			final StreamResult out) {
		int retour = 0;
		//On crée une nouvelle instance de Transformer factory
		TransformerFactory transfact =
				TransformerFactory.newInstance();
		Logger lOGGER = super.getlOGGER();

		//A partir de cette factory on créer une nouvelle instance
		//de la classe Transformer avec en paramètre le XSLT
		Transformer transformer = null;
		try {
			transformer = transfact.newTransformer(
					anXsltStylesheet);
		} catch (TransformerConfigurationException e1) {
			lOGGER.error("Erreur de "
					+ "configuration du Transformer");
			retour = -1;
		}
		try {
			//On applique la méthode transform
			//qui va modifier le XML
			//en fonction du XSLT
			transformer.transform(inputFile, out);
		} catch (TransformerException e) {
			lOGGER.error("Erreur durant la "
				+ "transformation du fichier");
			retour = 1;
		}
		return retour;
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri) {

		//On créé un flux du XML
		final StreamSource inputFile = new StreamSource(
				new File(inputFileUri));
		//On crée un flux du XSLT
		StreamSource xslTransformer = new StreamSource(
				new File(xsltStylesheet));
		//On créer un FileWriter du fichier résultat
		//pour permettre l'écriture de flux de caractères
		FileWriter outputFile = null;
		Logger lOGGER = super.getlOGGER();
		try {
			outputFile = new FileWriter(
					new File(outputFileUri));
		} catch (IOException e) {
			lOGGER.error("Erreur d'entrée-sortie");
			return -1;
		}
		//On crée un StreamResult à partir du FileWriter
		//pour pouvoir écrire dedans le résultat
		//de la transformation du XML par le XSLT
		StreamResult out = new StreamResult(outputFile);
		//On fait appel à la méthode de transformation
		transformXmlToJsonViaXSLT(inputFile, xslTransformer, out);
		//On scanne le fichier résultat pour pouvoir
		//le récupérer sous forme de string
		String text = Tools.readStringFromFile(new File(outputFileUri));
		//System.out.println(text);
		//On crée un Gson builder avec propriété d'indentation
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//On parse le string récupérer précédemment
		//On obtient un Json Element
		JsonElement jsonElement = new JsonParser().parse(text);
		//On transforme le Json Element en string
		String json = gson.toJson(jsonElement);
		//On fait appel à la méthode d'écriture
		//d'un string dans un fichier
		Tools.writeStringIntoFile(json, outputFileUri);

		return 0;
	}


	/**
	 * @return :
	 */
	public final File getXMLFile() {
		return this.xmlFile;
	}


	/**
	 * @param arg :
	 * @throws IOException :
	 * @throws URISyntaxException :
	 * @throws TransformerException :
	 * @throws SAXException :
	 */
	public static void main(final String[] arg)
			throws IOException, URISyntaxException,
			TransformerException, SAXException {
		XmlToJsonConverter converter =
				new XmlToJsonConverter("xmltojsonml.xslt");
		Logger lOGGER = converter.getlOGGER();
		if (converter.fileValidation(new File(
				"ressources/exemple-moodle.xml"))
				== FileConformity.OK) {
			converter.convert(
					"ressources/exemple-moodle.xml",
					"exemple-moodle.json");
			lOGGER.info(
					"Fin de la conversion : "
					+ "le ficher a bien été converti.");
		} else {
			lOGGER.warn("Le ficher n'est pas valide, "
					+ "conversion annulée.");
		}
		converter.getXMLFile().delete();
	}
}
