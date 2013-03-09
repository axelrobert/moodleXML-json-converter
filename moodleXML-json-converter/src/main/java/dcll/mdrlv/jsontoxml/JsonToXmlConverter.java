package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.tools.FileConformity;
import dcll.mdrlv.tools.Tools;

public class JsonToXmlConverter extends WebStandardConverter {

	@Override
	public final boolean accordanceWithMoodleStandard(final File file) {
		return false;
	}

	@Override
	public final boolean accordanceWithStandard(final File file) {
		return false;
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri) throws IOException, URISyntaxException,
			TransformerException {
		return 0;
	}

	public static String convertToXml(final String json) throws Exception {
		return convertToXml(json, "");
	}

	public static String convertToXml(final String json, final String namespace)
			throws Exception {
		return convertToXml(json, namespace, false);
	}

	public static String convertToXml(final String json,
			final String namespace, final boolean addTypeAttributes)
			throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		InputSource source = new InputSource(new StringReader(json));
		Result result = new StreamResult(out);
		transformer.transform(new SAXSource(new JSONXmlReader(
				namespace, addTypeAttributes), source), result);
		return new String(out.toByteArray());
	}

	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(new File("ressources/json-example.json"));
		String text = scan.useDelimiter("\\A").next();
		scan.close();
		System.out.println(text);
		String output = JsonToXmlConverter.convertToXml(text, "<document xmlns=\"http://javacrumbs.net/test\">", false);
		// SAXBuilder sb=new SAXBuilder();
		// Document doc=sb.build("results/output.xml");
		// String xmlIndent = new
		// XMLOutputter(Format.getPrettyFormat()).outputString(doc);
		System.out.println(output);
		Tools.writeStringIntoFile(output, "results/output.xml");
	}

	@Override
	public FileConformity validationXML(File xmlFile) throws FileNotFoundException,
			SAXException {
		// TODO Auto-generated method stub
		return FileConformity.OK;
	}

}
