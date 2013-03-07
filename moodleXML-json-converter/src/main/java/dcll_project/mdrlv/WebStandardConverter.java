package dcll_project.mdrlv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import dcll_project.mdrlv.tools.FileConformity;

public abstract class WebStandardConverter {

	public final FileConformity rightMoodleFile(final String inputFileUri)
			throws FileNotFoundException, SAXException {
		File inputFile = new File(inputFileUri);
		if (!accordanceWithStandard(inputFile)) {
			return FileConformity.WRONG_STANDARD;
		}
		if (!accordanceWithMoodleStandard(inputFile)) {
			return FileConformity.WRONG_MOODLE;
		}
		return FileConformity.OK;
		
		//test
	}

	public abstract boolean accordanceWithMoodleStandard(File f)
			throws FileNotFoundException, SAXException;

	public abstract boolean accordanceWithStandard(File f);

	public abstract int convert(String inputFileUri, String outputFileUri)
			throws IOException, URISyntaxException,
			TransformerException;

}
