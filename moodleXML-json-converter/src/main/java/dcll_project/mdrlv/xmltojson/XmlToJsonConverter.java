package dcll_project.mdrlv.xmltojson;

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
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dcll_project.mdrlv.WebStandardConverter;
import dcll_project.mdrlv.tools.Tools;

public class XmlToJsonConverter extends WebStandardConverter {

	private String transformationStylesheet;


	public XmlToJsonConverter(final String ch) {
		super();
		transformationStylesheet = "ressources/"+ch;
		}

	@Override
	public final boolean accordanceWithMoodleStandard(final File f) {
		// TODO Auto-generated method stub
		File xsd = new File("ressources/moodleXMLSchema.xsd"); //A définir
		try {
			accordanceWithMoodleXML(f, xsd);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			/*Impossible d'arriver ici.
			 * accordanceWithStandard filtre deja cette exception
			 */
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			//Impossible d'arriver ici
			e.printStackTrace();
		}
		return true;
	}

	private boolean accordanceWithMoodleXML(
			final File f, final File xsd)
			throws SAXException, IOException,
			ParserConfigurationException {
		// TODO Auto-generated method stub
	    SchemaFactory factory =
	    		SchemaFactory.newInstance(
	    				XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsd);
        Validator validator = schema.newValidator();
		Document doc = extractDocumentFromFile(f);
	    try {
			validator.validate(new DOMSource(doc));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
	}

	public final Document extractDocumentFromFile(final File f)
			throws ParserConfigurationException,
			SAXException, IOException {
		Document outputDoc = null;
		DocumentBuilderFactory dbfact =
				DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfact.newDocumentBuilder();
		outputDoc = docBuilder.parse(f);
		return outputDoc;
	}
	


	@Override
	public final boolean accordanceWithStandard(final File f) {
		// TODO Auto-generated method stub
		try {
			extractDocumentFromFile(f);
			return true;
		} catch (ParserConfigurationException e) {
			System.out.println("Erreur de configuration"
		+
					" du parseur DOM");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Erreur lors du parsing"
			+
					" du document");
		} catch (IOException e) {
			System.out.println("Erreur d'entrée-sortie");
		}
		return false;
	}

	@Override
	public final int convert(String inputFileUri,
			String outputFileUri)
			throws IOException, URISyntaxException,
			TransformerException {
		inputFileUri = "ressources/" + inputFileUri;
		outputFileUri = "results/" + outputFileUri;
		// TODO Auto-generated method stub
		StreamSource inputFile = new StreamSource(
				new File(inputFileUri));
		StreamSource xslTransformer = new StreamSource(
				new File(transformationStylesheet));
		FileWriter outputFile = new FileWriter(
				new File(outputFileUri));
		StreamResult out = new StreamResult(outputFile);
		transformXmlToJsonViaXSLT(inputFile, xslTransformer, out);
		String text = new Scanner(new File(
				outputFileUri)).useDelimiter("\\A").next();
		System.out.println(text);
	
		//Creating the JSON object, and getting as String:
		//Trying to pretify JSON String:
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = new JsonParser().parse(text);
		String json = gson.toJson(jsonElement);
		Tools.writeStringIntoFile(json, outputFileUri);
		return 0;
	}
	
	

	private void transformXmlToJsonViaXSLT(final StreamSource inputFile,
			final StreamSource transformationStylesheet,
			final StreamResult out)
			throws TransformerConfigurationException {
		// TODO Auto-generated method stub
		TransformerFactory transfact =
				TransformerFactory.newInstance();
		Transformer transformer =
				transfact.newTransformer(
						transformationStylesheet);
		
		try {
			transformer.transform(inputFile, out);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public static void main(final String[] arg)
			throws IOException, URISyntaxException,
			TransformerException {
		XmlToJsonConverter test = new XmlToJsonConverter(
				"xmltojsontransformer.xslt");
		test.convert("quiz-moodle-exemple.xml",
				"quiz-moodle-exemple.json");
	}




}
