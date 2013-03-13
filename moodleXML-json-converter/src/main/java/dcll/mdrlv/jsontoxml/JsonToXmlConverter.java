package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.jsontoxml.JSONSaxAdapter.ParserException;
import dcll.mdrlv.tools.Tools;

/**
 * @author :
 *
 */
public class JsonToXmlConverter extends WebStandardConverter {

	/**
	 * String error
	 */
	private final String error;

	/**
	 * Constructeur
	 */
	public JsonToXmlConverter() {
		super();
		error = "error";
	}

	/**
	 * @param json : un string contenant du JSON
	 *
	 * @return : XML String, null sinon
	 */
	public final String convertJsonStringToCompactedXmlString(
			final String json) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		Transformer transformer = null;
		boolean ok = true;
		Logger lOGGER = super.getlOGGER();
		try {
			transformer =
					TransformerFactory.newInstance().
					newTransformer();
		} catch (TransformerConfigurationException e1) {
			lOGGER.error("Instanciation de la transformation");
		} catch (TransformerFactoryConfigurationError e1) {
			lOGGER.error("TransformerFactoryConfigurations");
		}
		final InputSource source =
				new InputSource(new StringReader(json));
		final Result result = new StreamResult(out);
		final JSONXmlReader reader = new JSONXmlReader("", false);
		final SAXSource sax = new SAXSource(reader, source);
		String output = null;
		try {
			try {
				transformer.transform(sax, result);
			} catch (TransformerException e) {
				ok = false;
				lOGGER.error("TransformerException");
			}
		} catch (ParserException e) {
			ok = false;
			lOGGER.error("ParserException");
		}
		if (ok) {
			output = new String(out.toByteArray());
		} else {
			output = error;
		}
		return output;
	}


	@Override
	public final boolean accordanceWithMoodleStandard(final File file) {
		// On pensait, pour tester si le JSON du fichier était conforme
		// à la norme MOODLE, le traduire en XML puis appeler la méthode
		// de conformormité du MOODLE XML.
		// Or force est de constater que le passage
		// de JSON vers XML supprime
		// tous les attributs pour les représenter sous forme de balises
		return true;

	}

	@Override
	public final boolean accordanceWithStandard(final File file) {
		final String json = Tools.readStringFromFile(file);
		return (!convertJsonStringToCompactedXmlString(json).
				contentEquals(error));
	}

	@Override
	public final int convert(final String inputFileUri,
			final String outputFileUri) {
		final String text =
				Tools.readStringFromFile(
						new File(inputFileUri));
		final String output =
				convertJsonStringToCompactedXmlString(text);
		Tools.writeStringIntoFile(output, outputFileUri);
		Logger lOGGER = super.getlOGGER();

		final SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = null;

		try {
			doc = saxBuilder.build(outputFileUri);
		} catch (JDOMException e) {
			lOGGER.error("JDOM Exception");
			return -1;
		} catch (IOException e) {
			lOGGER.error("IO Exception");
			return -1;
		}
		final XMLOutputter xmlOutputter = new XMLOutputter(
				Format.getPrettyFormat());
		final String xmlIndent = xmlOutputter.outputString(doc);
		Tools.writeStringIntoFile(xmlIndent, outputFileUri);

		return 0;
	}

	/**
	 * @param args
	 *            :
	 */
	public static void main(final String[] args) {
		JsonToXmlConverter converter = new JsonToXmlConverter();
		String testPath = "ressources/exemple.json";
		File testFile = new File(testPath);
		Logger lOGGER = converter.getlOGGER();

		boolean validate = converter.accordanceWithStandard(testFile);
		lOGGER.info(testPath + " is a " + validate + " json file.");
		validate = converter.accordanceWithMoodleStandard(testFile);
		lOGGER.info(testPath + " is a " + validate + " moodle file.");
		if (validate) {
			converter.convert(testPath, "results/output.xml");
		}
	}
}
