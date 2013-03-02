package dcll_project.mdrlv.moodleXML_json_converter.xmltojson;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import dcll_project.mdrlv.moodleXML_json_converter.WebStandardConverter;

public class XmlToJsonConverter extends WebStandardConverter {

	@Override
	public boolean accordanceWithMoodleStandard(File f) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Document extractDocumentFromFile(File f) throws ParserConfigurationException, SAXException, IOException{
		Document outputDoc=null;
		DocumentBuilderFactory dbfact=DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder=dbfact.newDocumentBuilder();
		outputDoc=docBuilder.parse(f);
		return outputDoc;
	}

	@Override
	public boolean accordanceWithStandard(File f) {
		// TODO Auto-generated method stub
			try {
				extractDocumentFromFile(f);
				return true;
			} catch (ParserConfigurationException e) {
				System.out.println("Erreur de configuration du parseur DOM");
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur lors du parsing du document");
			} catch (IOException e) {
				System.out.println("Erreur d'entrée-sortie");
			}
		return false;
	}

	@Override
	public int convert(String inputFileUri, String outputFileUri)
			throws IOException, URISyntaxException, TransformerException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
