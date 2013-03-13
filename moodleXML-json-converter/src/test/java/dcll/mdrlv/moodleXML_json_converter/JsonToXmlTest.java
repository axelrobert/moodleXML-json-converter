package dcll.mdrlv.moodleXML_json_converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dcll.mdrlv.jsontoxml.JsonToXmlConverter;
import dcll.mdrlv.tools.Tools;

public class JsonToXmlTest {

    private static JsonToXmlConverter jtxc;
    private static File file;
    private static String text;

    @BeforeClass
    public static void initialisation() {
        jtxc = new JsonToXmlConverter();
        file = new File("ressources/exemple.json");
        text = Tools.readStringFromFile(file);
    }

    @AfterClass
    public static void apresTests() {
        jtxc = null;
    }

    @Test
	public final void testAccordanceWithStandard() {
		boolean isOk = jtxc.accordanceWithStandard(file);
        assertTrue(isOk);
    }

    @Test
	public final void testConvert() {
		int ret = jtxc.convert(
				"ressources/exemple.json",
				"exemple-moodle-test-result.xml");
		assertEquals(ret, 0);
	}

    @Test
    public final void testJsonStringToCompactedXml() {
    	String resultat = jtxc.convertJsonStringToCompactedXmlString(text);
    	assertNotEquals(resultat, "error");
    	assertNotNull(resultat);
    }
}
