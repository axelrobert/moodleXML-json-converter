package dcll.mdrlv.jsontoxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

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
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import dcll.mdrlv.WebStandardConverter;
import dcll.mdrlv.jsontoxml.JSONSaxAdapter.ParserException;
import dcll.mdrlv.tools.Tools;

/**
 * @author : Robert Axel, Villard David
 *
 */
public class JsonToXmlConverter extends WebStandardConverter {
	/**.
	 * String error.
	 */
	private final String error;
	/**
	 * String tempPath.
	 */
	private final String tempPath;
	/**
	 * Logger lOGGER.
	 */
	private  Logger lOGGER;
	/**
	 * @Constructeur.
	 */

	public JsonToXmlConverter() {
		super();
		error = "error";
		tempPath = "ressources/temp.xml";
		lOGGER = super.getlOGGER();
	}

	/**
	 * @return : chaine erreur
	 */
	public final String getError() {
		return error;
	}
	/**.
	 * @return : adresse du fichier temporaire
	 * utilisée pour les transformations
	 */
	public final String getTempPath() {
		return tempPath;
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
		boolean successExecution = true;
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
				successExecution = false;
				lOGGER.error("TransformerException");
			}
		} catch (ParserException e) {
			successExecution = false;
			lOGGER.error("ParserException");
		}
		if (successExecution) {
			output = new String(out.toByteArray());
		} else {
			output = error;
		}
		return output;
	}


	@Override
	public final boolean accordanceWithMoodleStandard(final
			File file) {
		boolean successExecution = true;
		final String compactedXML =
				convertJsonStringToCompactedXmlString(
						Tools.readStringFromFile(file));
		Tools.writeStringIntoFile(compactedXML, tempPath);
		final File temp = new File(tempPath);
		//On crée une instance de SAXBuilder
	      final SAXBuilder sxb = new SAXBuilder();
	      Document document = null;
	      try {
	         //On crée un nouveau document JDOM
	    	  //avec en argument le fichier XML
	         //Le parsing est terminé ;)
	    	 document = sxb.build(temp);
	      } catch (Exception e) {
	    	  lOGGER.error("Erreur lors de la creation du JDOM");
	    	  successExecution = false;
	      }
	      if (successExecution) {
		      //On initialise un nouvel élément racine
		      //avec l'élément racine du document.
		      final Element racine = document.getRootElement();
		      //On crée une List contenant tous les noeuds "question"
		      //de l'Element racine
		      final List<Element> list = racine.getChildren("question");
		      //Si la racine n'est pas de type quiz ou
		      // si il n'y a aucune balise de type question
		      // Alors le fichier en entrée ne peut etre du MOODLE XML.
		      // Inutile de continuer le test
		     if (!racine.getName().contentEquals("quiz")
		    		  || list.isEmpty()) {
		    	successExecution = false;
		      }
		  }
	    temp.delete();
		return successExecution;
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
		boolean successExecution = false;
		final String text =
				Tools.readStringFromFile(
						new File(inputFileUri));
		final String output =
				convertJsonStringToCompactedXmlString(text);
		Tools.writeStringIntoFile(output, tempPath);

		final SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		final File temp = new File(tempPath);
		try {
			document = saxBuilder.build(temp);
			successExecution = true;
		} catch (JDOMException e) {
			lOGGER.error("JDOM Exception");
		} catch (IOException e) {
			lOGGER.error("IO Exception");
		}
		temp.delete();
		if (successExecution) {
			final XMLOutputter xmlOutputter = new XMLOutputter(
					Format.getPrettyFormat());
			final String xmlIndent = xmlOutputter.
					outputString(document);
			Tools.writeStringIntoFile(xmlIndent, tempPath);
			new XmlToMoodleXMLConverter().convertXMLtoMoodleXML(
					tempPath, outputFileUri);
		}
		return 0;
	}

}
