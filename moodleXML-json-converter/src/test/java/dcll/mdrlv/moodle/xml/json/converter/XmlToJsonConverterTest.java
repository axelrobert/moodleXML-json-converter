package dcll.mdrlv.moodle.xml.json.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dcll.mdrlv.tools.FileConformity;
import dcll.mdrlv.xmltojson.XmlToJsonConverter;

/**
 * @author Axel Robert
 *
 */
public class XmlToJsonConverterTest {

	/**
	 * file.
	 */
	private static File file;

	/**
	 * result.
	 */
	private static File result;

	/**
	 * stylesheet.
	 */
	private static File stylesheet;

	/**
	 * xtcj.
	 */
	private static XmlToJsonConverter xtjc;

	/**
	 * inputFile.
	 */
	private static StreamSource inputFile;

	/**
	 * xslt.
	 */
	private static StreamSource xslt;

	/**
	 * outputFile.
	 */
	private static StreamResult outputFile;

	/**
	 * writer.
	 */
	private static FileWriter writer;

	/**
	 * initialisation.
	 */
	@BeforeClass
    public static void initialisation() {
		 file = new File("ressources/examples/rightmoodlexml.xml");
		 result = new File("exemple-moodle-test-result.json");
		 stylesheet = new File("ressources/xslTransformers/xmltojsonml.xslt");
		 xtjc = new XmlToJsonConverter("xmltojsonml.xslt");
		 inputFile = new StreamSource(file);
		 xslt = new StreamSource(stylesheet);
		 try {
			writer = new FileWriter(result);
		} catch (IOException e) {
			e.getMessage();
		}
		 outputFile = new StreamResult(writer);
    }

	/**
	 * apresTests.
	 */
	@AfterClass
    public static void apresTests() {
		xtjc.getXMLFile().delete();
		file = null;
        xtjc = null;
    }

	/**
	 * Test de la conformite avec le standard.
	 */
	@Test
	public final void testAccordanceWithStandard() {
		boolean isOk = xtjc.accordanceWithStandard(file);
        assertTrue(isOk);
    }

	/**
	 * Test de la conformite avec le standard moodle.
	 */
	@Test
	public final void testAccordanceWithMoodleStandard() {
		boolean isOk = xtjc.accordanceWithMoodleStandard(file);
		assertTrue(isOk);
	}

	/**
	 * Test de validation.
	 */
	@Test
	public final void testValidationFile() {
		FileConformity conform = xtjc.fileValidation(file);
		assertEquals(conform, FileConformity.OK);
	}

	/**
	 * Test de transformation.
	 */
	@Test
	public final void testTransformFile() {
		int ret = xtjc.transformXmlToJsonViaXSLT(
				inputFile, xslt, outputFile);
		assertEquals(ret, 0);

	}

	/**
	 * Test de conversion.
	 */
	@Test
	public final void testConvert() {
		int ret = xtjc.convert(
				"ressources/examples/rightmoodlexml.xml",
				"exemple-moodle-test-result.json");
		assertEquals(ret, 0);
	}
}
