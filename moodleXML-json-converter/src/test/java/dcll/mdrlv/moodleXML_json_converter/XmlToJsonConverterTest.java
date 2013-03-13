package dcll.mdrlv.moodleXML_json_converter;

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

public class XmlToJsonConverterTest {

	private static File file;
	private static File result;
	private static File stylesheet;
	private static XmlToJsonConverter xtjc;
	private static StreamSource inputFile;
	private static StreamSource xslt;
	private static StreamResult outputFile;
	private static FileWriter writer;

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

	@AfterClass
    public static void apresTests() {
		xtjc.getXMLFile().delete();
		file = null;
        xtjc = null;
    }

	@Test
	public final void testAccordanceWithStandard() {
		boolean isOk = xtjc.accordanceWithStandard(file);
        assertTrue(isOk);
    }

	@Test
	public final void testAccordanceWithMoodleStandard() {
		boolean isOk = xtjc.accordanceWithMoodleStandard(file);
		assertTrue(isOk);
	}

	@Test
	public final void testValidationFile() {
		FileConformity conform = xtjc.fileValidation(file);
		assertEquals(conform, FileConformity.OK);
	}

	@Test
	public final void testTransformFile() {
		int ret = xtjc.transformXmlToJsonViaXSLT(
				inputFile, xslt, outputFile);
		assertEquals(ret, 0);

	}

	@Test
	public final void testConvert() {
		int ret = xtjc.convert(
				"ressources/examples/rightmoodlexml.xml",
				"exemple-moodle-test-result.json");
		assertEquals(ret, 0);
	}
}
