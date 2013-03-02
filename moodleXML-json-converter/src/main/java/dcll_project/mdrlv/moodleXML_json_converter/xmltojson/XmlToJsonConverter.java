package dcll_project.mdrlv.moodleXML_json_converter.xmltojson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import dcll_project.mdrlv.moodleXML_json_converter.WebStandardConverter;

public class XmlToJsonConverter extends WebStandardConverter {
	private String transformationStylesheet;
	
	public XmlToJsonConverter(String ch){
		super();
		transformationStylesheet=ch;
	}
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
		StreamSource inputFile=new StreamSource(new File(inputFileUri));
		StreamSource xslTransformer=new StreamSource(new File(transformationStylesheet));
		FileWriter outputFile=new FileWriter(new File(outputFileUri));
		StreamResult out=new StreamResult(outputFile);
		transformXmlToJsonViaXSLT(inputFile, xslTransformer, out);
		return 0;
	}
	private void transformXmlToJsonViaXSLT(StreamSource inputFile,
			StreamSource transformationStylesheet, StreamResult out) throws TransformerConfigurationException {
		// TODO Auto-generated method stub
		TransformerFactory transfact=TransformerFactory.newInstance();
		Transformer transformer=transfact.newTransformer(transformationStylesheet);
		try {
			transformer.transform(inputFile, out);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
