package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.util.Scanner;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import dcll.mdrlv.tools.Tools;



public class JsonToXmlConverter {

	public static String convertToXml(final String json,
			final String namespace, final boolean addTypeAttributes)
			throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		InputSource source = new InputSource(new StringReader(json));
		Result result = new StreamResult(out);
		JSONXmlReader reader = new JSONXmlReader(namespace, addTypeAttributes);
		SAXSource sax = new SAXSource(reader, source);
		transformer.transform(sax, result);
		return new String(out.toByteArray());
	}

	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(new File("ressources/exemple.json"));
		String text = scan.useDelimiter("\\A").next();
		scan.close();
		String output = JsonToXmlConverter.convertToXml(text, "", false);
		SAXBuilder sb=new SAXBuilder();
		Tools.writeStringIntoFile(output, "results/output.xml");
		Document doc=sb.build("results/output.xml");
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		String xmlIndent = xmlOutputter.outputString(doc);
		Tools.writeStringIntoFile(xmlIndent, "results/output.xml");
	}

}
