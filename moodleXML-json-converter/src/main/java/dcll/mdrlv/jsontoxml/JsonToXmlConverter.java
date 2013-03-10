package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.jsontoxml.JSONSaxAdapter.ParserException;
import dcll.mdrlv.tools.Tools;
import dcll.mdrlv.xmltojson.XmlToJsonConverter;



public class JsonToXmlConverter extends WebStandardConverter{

	public JsonToXmlConverter(){
		super();
	}
	
	public String convertJsonStringToCompactedXmlString(String json) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance()
					.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputSource source = new InputSource(new StringReader(json));
		Result result = new StreamResult(out);
		JSONXmlReader reader = new JSONXmlReader("", false);
		SAXSource sax = new SAXSource(reader, source);
		try {
			try {
				transformer.transform(sax, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			return "error";
		}
		return new String(out.toByteArray());
	}

	
	@Override
	public boolean accordanceWithMoodleStandard(File file)
			throws FileNotFoundException, SAXException {
		// TODO Auto-generated method stub
		return (XmlToJsonConverter.accordanceWithXML(file) !=null);
	}

	@Override
	public boolean accordanceWithStandard(File file){
		// TODO Auto-generated method stub
		String json = Tools.readStringFromFile(file);
		return ((!convertJsonStringToCompactedXmlString(json).contentEquals("error")));
	}

	@Override
	public int convert(String inputFileUri, String outputFileUri)
			throws IOException, URISyntaxException, TransformerException {
		// TODO Auto-generated method stub
		String text = Tools.readStringFromFile(new File(inputFileUri));
		String output = convertJsonStringToCompactedXmlString(text);
		Tools.writeStringIntoFile(output, "results/output.xml");
		SAXBuilder sb = new SAXBuilder();
		Document doc;
		try {
			doc = sb.build("results/output.xml");
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			String xmlIndent = xmlOutputter.outputString(doc);
			Tools.writeStringIntoFile(xmlIndent, "results/output.xml");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public static void main(String args[]) throws SAXException, IOException, URISyntaxException, TransformerException{
		JsonToXmlConverter converter = new JsonToXmlConverter();
		String testPath = "ressources/exemple.json";
		File testFile = new File(testPath);
		boolean validate = converter.accordanceWithStandard(testFile);
		System.out.println(testPath + " is a " + validate + " json file." );
		converter.convert(testPath, "results/output.xml");	
	}
	

}
