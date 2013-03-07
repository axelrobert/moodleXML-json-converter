package dcll_project.mdrlv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import dcll_project.mdrlv.tools.FileConformity;

public abstract class WebStandardConverter {

	protected static final Logger LOGGER = 
			Logger.getLogger(WebStandardConverter.class);

	public final FileConformity rightMoodleFile(final String inputFileUri)
			throws FileNotFoundException, SAXException {
		final File inputFile = new File(inputFileUri);
		if (!accordanceWithStandard(inputFile)) {
			return FileConformity.WRONG_STANDARD;
		}
		if (!accordanceWithMoodleStandard(inputFile)) {
			return FileConformity.WRONG_MOODLE;
		}
		return FileConformity.OK;

		//test
	}

	public abstract boolean accordanceWithMoodleStandard(File file)
			throws FileNotFoundException, SAXException;

	public abstract boolean accordanceWithStandard(File file);

    public abstract int convert(final String inputFileUri, 
			final String outputFileUri)
		throws IOException, URISyntaxException, TransformerException;

}
