package dcll.mdrlv.xmltojson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXExcept<dependency>
<groupId>log4j</groupId>
<artifactId>log4j</artifactId>
<version>1.2.15</version>
</dependency>
        ion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.tools.Tools;

public class XmlToJsonConverter extends WebStandardConverter {

	private final String xsltStylesheet;


	public XmlToJsonConverter(final String chaine) {
		super();
		xsltStylesheet = "ressources/" + chaine;
		}

	@Override
	public final boolean accordanceWithMoodleStandard(final File file) {
		//A définir
		final File xsd = new File("ressources/moodleXMLSchema.xsd");
		try {
			accordanceWithMoodleXML(file, xsd);
		} catch (SAXException e) {
			LOGGER.error("Erreur lors du parsing"
					+
							" du document");
		} catch (IOException e) {
			LOGGER.error("Erreur d'entrée-sortie");
		} catch (ParserConfigurationException e) {
			LOGGER.error("Erreur de configuration"
					+ " du parseur DOM");
		}
		return true;
	}

	private boolean accordanceWithMoodleXML(
			final File file, final File xsd)
			throws SAXException, IOException,
			ParserConfigurationException {
	    final SchemaFactory factory =
	    		SchemaFactory.newInstance(
	    				XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = factory.newSchema(xsd);
        final Validator validator = schema.newValidator();
		final Document doc = extractDocumentFromFile(file);
	    try {
			validator.validate(new DOMSource(doc));
			return true;
		} catch (SAXException e) {
			LOGGER.error("Erreur lors du parsing"
					+
							" du document");
		} catch (IOException e) {
			LOGGER.error("Erreur d'entrée-sortie");
		}
	    return false;
	}

	public final Document extractDocumentFromFile(final File file) {
		Document outputDoc = null;
		final DocumentBuilderFactory dbfact =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dbfact.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error("Erreur de configuration"
					+ " du parseur DOM");
		}
		try {
			outputDoc = docBuilder.parse(file);
		} catch (SAXException e) {
			LOGGER.error("Erreur lors du parsing"
					+
							" du document");
		} catch (IOException e) {
			LOGGER.error("Erreur d'entrée-sortie");
		}
		return outputDoc;
	}


	@Override
	public final boolean accordanceWithStandard(final File file) {
			return (extractDocumentFromFile(file) != null);
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
		//Creating the JSON object, and getting as String:
		//Trying to pretify JSON String:
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = new JsonParser().parse(text);
		String json = gson.toJson(jsonElement);
		Tools.writeStringIntoFile(json, outFileUri);
		return 0;
	}

	private void transformXmlToJsonViaXSLT(final StreamSource inputFile,
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


	public static void main(final String[] arg)
			throws IOException, URISyntaxException,
			TransformerException {
		XmlToJsonConverter convert = new XmlToJsonConverter(
				"xmltojsontransformer.xslt");
		convert.accordanceWithStandard(new File("ressources/quiz-moodle-exemple.xml"));
		/*test.convert("quiz-moodle-exemple.xml",
				"quiz-moodle-exemple.json");*/
	}




}
