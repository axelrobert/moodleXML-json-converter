package dcll.mdrlv.xmltojson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

public class XmlToJsonConverter extends WebStandardConverter {

	private final String xsltStylesheet;
	private org.w3c.dom.Document outputDoc;
	private Document document;
    private Element racine;
    private File xmlFile;

	public XmlToJsonConverter(final String chaine) {
		super();
		xsltStylesheet = "ressources/" + chaine;
		}

	@Override
	public final boolean accordanceWithMoodleStandard(final File file) {
			//Appel à la méthode accordanceWithMoodleXML pour
			//vérifier que le fichier est bien au format MoodleXML
			return(accordanceWithMoodleXML(file));
	}

	@Override
	public FileConformity validationXML(final File xmlFile) {
		//On teste si le XML est bien valide
		if(!accordanceWithStandard(xmlFile)) {
			LOGGER.warn("Le fichier n'est pas conforme au format XML.");
			return FileConformity.WRONG_STANDARD;
		} else {
			//On teste si le XML est bien conforme à un MoodleXML format
			if(!accordanceWithMoodleStandard(xmlFile)) {
				LOGGER.warn("Le fichier n'est pas conforme au format MoodleXML.");
				return FileConformity.WRONG_MOODLE;
			}
		}
		return FileConformity.OK;
	}

	public boolean isValidateXSD(File xmlFile, File xsdFile) {
		//Création d'un schéma XML (XSD) factory
		XMLReaderJDOMFactory schemafac = null;
		try {
			schemafac = new XMLReaderXSDFactory(xsdFile);
		} catch (JDOMException e) {
			LOGGER.error("Erreur lors de l'instanciation"
					+
							" du XMLReaderXSDFactory");
		}
		//Création d'un builder SAX à partir du schéma factory
		SAXBuilder builder = new SAXBuilder(schemafac);
		try {
			//On parse le fichier XML (selon le XSD fournit plus haut)
			builder.build(xmlFile);
		} catch (JDOMException e) {
			//Si erreur lors du build alors le XML n'est pas conforme au XSD
			LOGGER.error(e.getMessage());
			return false;
		} catch (IOException e) {
			LOGGER.error("Erreur d'entrée-sortie");
			return false;
		}
		return true;
	}

	public boolean validateAccordingToType(Element elt) {
		//On récupère le type de la question
		String type = elt.getAttributeValue("type");
		//Selon le type on fait appel au XSD adéquat et
		//on fait appel à la méthode isValidateXSD pour
		//vérifier si la question est valide ou pas
        if(type.toLowerCase().equals("multichoice")) {
			final File xsdFile = new File("ressources/multichoice.xsd");
			boolean validate = isValidateXSD(xmlFile, xsdFile);
			if(!validate) {
				return false;
			}
        } else if(type.toLowerCase().equals("truefalse")) {
	       		final File xsdFile = new File("ressources/truefalse.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
	    } else if(type.toLowerCase().equals("category")) {
	       		final File xsdFile = new File("ressources/category.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
	    } else if(type.toLowerCase().equals("calculated")) {
	       		final File xsdFile = new File("ressources/calculated.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	} else if(type.toLowerCase().equals("description")) {
	       		final File xsdFile = new File("ressources/description.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	} else if(type.toLowerCase().equals("essay")) {
	       		final File xsdFile = new File("ressources/essay.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	} else if(type.toLowerCase().equals("matching")) {
	       		final File xsdFile = new File("ressources/matching.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
		} else if(type.toLowerCase().equals("cloze")) {
	       		final File xsdFile = new File("ressources/clozed.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	} else if(type.toLowerCase().equals("numerical")) {
	       		final File xsdFile = new File("ressources/numerical.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	} else if(type.toLowerCase().equals("shortanswer")) {
	       		final File xsdFile = new File("ressources/shortanswer.xsd");
					boolean validate = isValidateXSD(xmlFile, xsdFile);
					if(!validate) {
						return false;
					}
       	}

        return true;
	}

	public boolean accordanceWithMoodleXML(File file) {
		 //On crée une instance de SAXBuilder
	      SAXBuilder sxb = new SAXBuilder();

	      try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	    	 document = sxb.build(file);
	      }
	      catch(Exception e){
	    	  LOGGER.error("Erreur lors de la creation du JDOM");
	    	  return false;
	      }

	      //On initialise un nouvel élément racine avec l'élément racine du document.
	      racine = document.getRootElement();
	      //On crée une List contenant tous les noeuds "question" de l'Element racine
	      List<Element> list = racine.getChildren("question");

	      //On crée un Iterator sur notre liste
	      Iterator<Element> i = list.iterator();
	      while(i.hasNext())
	      {
	         //On recrée l'Element courant à chaque tour de boucle
	         Element courant = i.next();

	         //Test pour savoir si l'élément shuffleanswers existe 2 fois
	         XPathExpression<Object> xpath =
	        		    XPathFactory.instance().compile("count(//shuffleanswers)");
	        		Double val = (Double)xpath.evaluateFirst(document);
	        		if(val > 1.0){
	        			LOGGER.error("La balise <shuffleanswers> " +
	        					"ne doit apparaître qu'une fois par question.");

	        			//Afin d'éviter une java null exception
	        			xmlFile = new File("question.xml");

	        			return false;
	        		}

	         /* Ici, pour chaque question on crée un nouveau fichier XML contenant
	          * l'aborescence de la question entouré de la balise <quiz> afin de pouvoir
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
	         Attribute typeAtt = new Attribute("type",courant.getAttributeValue("type"));
	         question.setAttribute(typeAtt);
	         //On clone le contenu du noeud courant pour récupérer l'arborescence
	         //de l'Element question et on l'ajoute en tant qu'Element
	         //du nouveau noeud question
	         question.addContent(courant.cloneContent());
	         //On utilise ici un affichage classique avec getPrettyFormat()
	         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	         try {
	        	//Création du fichier XML qui contiendra
	        	//l'arborescence de la question courante
	        	xmlFile = new File("question.xml");
	        	//On écrit le contenu du Document dans le fichier XML
				sortie.output(xmlDoc, new FileOutputStream(xmlFile));
			 } catch (IOException e) {
				 LOGGER.error("Erreur d'affichage du document");
			 }
	         //On fait appel à la méthode validateAccordingToType
	         //pour vérifier que le XML contenant l'arborescence
	         //de la question courante est conforme à son XSD
	         //selon le type de la question
	         if(!validateAccordingToType(courant)) return false;

	      }
	      return true;
	}

	public final org.w3c.dom.Document accordanceWithXML(final File file) {
		outputDoc = null;
		//On crée une nouvelle instance d'un Document factory
		final DocumentBuilderFactory dbfact =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			//On crée un nouveau Document builder
			//à partir de la factory
			docBuilder = dbfact.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error("Erreur de configuration"
					+ " du parseur DOM");
		}
		try {
			//On parse le fichier XML
			outputDoc = docBuilder.parse(file);
		} catch (SAXException e) {
			//Si exception alors le fichier
			//n'est pas conforme au format XML
			LOGGER.error("Erreur lors du parsing"
					+
							" du document");
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Erreur d'entrée-sortie");
		}
		return outputDoc;
	}


	@Override
	public final boolean accordanceWithStandard(final File file) {
			//Appel à la méthode accordanceWithXML pour
			//vérifier que le fichier est bien au format XML
			return (accordanceWithXML(file) != null);
	}

	public void transformXmlToJsonViaXSLT(final StreamSource inputFile,
			final StreamSource xsltStylesheet, final StreamResult out)
			throws TransformerConfigurationException {
		TransformerFactory transfact =
				TransformerFactory.newInstance();
		Transformer transformer =
				transfact.newTransformer(
						xsltStylesheet);
		try {
			transformer.transform(inputFile, out);
		} catch (TransformerException e) {
			LOGGER.error("Erreur durant la transformation du fichier");
		}
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri)
			throws IOException, URISyntaxException,
			TransformerException {
		final String inFileUri = "ressources/" + inputFileUri;
		final String outFileUri = "results/" + outputFileUri;

		final StreamSource inputFile = new StreamSource(
				new File(inFileUri));
		StreamSource xslTransformer = new StreamSource(
				new File(xsltStylesheet));
		FileWriter outputFile = new FileWriter(
				new File(outFileUri));
		StreamResult out = new StreamResult(outputFile);
		transformXmlToJsonViaXSLT(inputFile, xslTransformer, out);
		Scanner scan = new Scanner(new File(outFileUri));
		String text = scan.useDelimiter("\\A").next();
		scan.close();
		//System.out.println(text);
		//Creating the JSON object, and getting as String:
		//Trying to pretify JSON String:
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = new JsonParser().parse(text);
		String json = gson.toJson(jsonElement);
		Tools.writeStringIntoFile(json, outFileUri);
		return 0;
	}


	public File getXMLFile() {
		return this.xmlFile;
	}


	public static void main(final String[] arg)
			throws IOException, URISyntaxException,
			TransformerException {
		XmlToJsonConverter converter =
				new XmlToJsonConverter("xmltojsonml.xslt");
		if(converter.validationXML(new File("ressources/exemple-moodle.xml")) == FileConformity.OK) {
			converter.convert("exemple-moodle.xml","exemple-moodle.json");
			LOGGER.info("Fin de la conversion : le ficher a bien été converti.");
		} else {
			LOGGER.warn("Le ficher n'est pas valide, conversion annulée.");
		}
		converter.getXMLFile().delete();

	      /*SAXBuilder sxb = new SAXBuilder();

	      try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	    	 Document doc = sxb.build(new File("ressources/exemple-moodle.xml"));
	    	 XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	    	 String s = sortie.outputString(doc);
	    	 JSONObject js = XML.toJSONObject(s);
	    	 String json = js.toString(4);
	    	 System.out.println(json);


	    	 //Tools.writeStringIntoFile(json, "results/exemple-moodle-result2.json");
	      }
	      catch(Exception e){
	    	  LOGGER.error("Erreur lors de la creation du JDOM");
	      }*/

	}
}
