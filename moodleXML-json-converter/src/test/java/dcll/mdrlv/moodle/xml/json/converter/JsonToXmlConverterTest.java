package dcll.mdrlv.moodle.xml.json.converter;

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

/**
 * @author Axel Robert
 *
 */
public class JsonToXmlConverterTest {

    /**
     *
     */
    private static JsonToXmlConverter jtxc;

    /**
     *
     */
    private static File file;

    /**
     *
     */
    private static String text;

    /**
     * initialisation.
     */
    @BeforeClass
    public static void initialisation() {
        jtxc = new JsonToXmlConverter();
        file = new File("ressources/examples/rightmoodlejson.json");
        text = Tools.readStringFromFile(file);
    }

    /**
     * apresTest.
     */
    @AfterClass
    public static void apresTests() {
        jtxc = null;
    }

    /**
     * Test d'accord avec les standards.
     */
    @Test
	public final void testAccordanceWithStandard() {
		boolean isOk = jtxc.accordanceWithStandard(file);
        assertTrue(isOk);
    }

    /**
     * Test de conversion.
     */
    @Test
	public final void testConvert() {
		int ret = jtxc.convert(
				"ressources/examples/rightmoodlejson.json",
				"exemple-moodle-test-result.xml");
		assertEquals(ret, 0);
	}

    /**
     * Test de passage d'une string Json
     * a un fichier XML compacte.
     */
    @Test
    public final void testJsonStringToCompactedXml() {
    	String resultat = jtxc.convertJsonStringToCompactedXmlString(text);
    	assertNotEquals(resultat, "error");
    	assertNotNull(resultat);
    }
}
