package dcll.mdrlv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import dcll.mdrlv.tools.FileConformity;

public abstract class WebStandardConverter {

	protected static final Logger LOGGER =
			Logger.getLogger(WebStandardConverter.class);

	public abstract FileConformity validationXML(final File xmlFile)
			throws FileNotFoundException, SAXException;

	public abstract boolean accordanceWithMoodleStandard(File file)
			throws FileNotFoundException, SAXException;

	public abstract boolean accordanceWithStandard(File file);

    public abstract int convert(final String inputFileUri,
			final String outputFileUri)
		throws IOException, URISyntaxException, TransformerException;

}
