package dcll.mdrlv.jsontoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.io.IOUtils;

import dcll.mdrlv.WebStandardConverter;
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
            final String outputFileUri)
            throws IOException, URISyntaxException,
            TransformerException {
        InputStream is = new FileInputStream(inputFileUri);
        String jsonData = IOUtils.toString(is);

        XMLSerializer serializer = new XMLSerializer();
        serializer.setTypeHintsEnabled(false);
        JSON json = JSONSerializer.toJSON( jsonData );
        String xml = serializer.write(json);
        Tools.writeStringIntoFile(xml, outputFileUri);

        return 0;
    }

    public static void main(String args[]) throws IOException, URISyntaxException, TransformerException{
        JsonToXmlConverter converter = new JsonToXmlConverter();
        converter.convert("results/quiz-moodle-exemple.json", "results/output.xml");
    }

}