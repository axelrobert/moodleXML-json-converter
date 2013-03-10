package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class JsonToXmlConverter extends WebStandardConverter {

	public JsonToXmlConverter() {
		super();
	}

	public final String convertJsonStringToCompactedXmlString(
			final String json) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.
					newInstance().newTransformer();
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
	public final boolean accordanceWithMoodleStandard(final File file)
			throws SAXException {
		// TODO Auto-generated method stub
		String json = Tools.readStringFromFile(file);
		String xml = convertJsonStringToCompactedXmlString(json);
		String tempPathFile = "ressources/temp.xml";
		File temp = new File(tempPathFile);
		Tools.writeStringIntoFile(xml, tempPathFile);
		org.w3c.dom.Document res =
				XmlToJsonConverter.accordanceWithXML(temp);
		temp.delete();
		return (res != null);

	}

	@Override
	public final boolean accordanceWithStandard(final File file) {
		// TODO Auto-generated method stub
		String json = Tools.readStringFromFile(file);
		return ((!convertJsonStringToCompactedXmlString(json)
				.contentEquals("error")));
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri) throws IOException,
			URISyntaxException, TransformerException {
		// TODO Auto-generated method stub
		String text = Tools.readStringFromFile(new File(inputFileUri));
		String output = convertJsonStringToCompactedXmlString(text);
		Tools.writeStringIntoFile(output, outputFileUri);
		SAXBuilder sb = new SAXBuilder();
		Document doc;
		try {
			doc = sb.build(outputFileUri);
			XMLOutputter xmlOutputter = new XMLOutputter(
					Format.getPrettyFormat());
			String xmlIndent = xmlOutputter.outputString(doc);
			Tools.writeStringIntoFile(xmlIndent, outputFileUri);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static void main(final String[] args) 
			throws SAXException, IOException,
			URISyntaxException, TransformerException {
		JsonToXmlConverter converter = new JsonToXmlConverter();
		String testPath = "ressources/exemple.json";
		File testFile = new File(testPath);
		boolean validate = converter.accordanceWithStandard(testFile);
		System.out.println(testPath + " is a "
				+ validate + " json file.");
		validate = converter.accordanceWithMoodleStandard(testFile);
		System.out.println(testPath + " is a "
				+ validate + " moodle file.");
		if (validate) {
			converter.convert(testPath, "results/output.xml");
		}
	}
}
