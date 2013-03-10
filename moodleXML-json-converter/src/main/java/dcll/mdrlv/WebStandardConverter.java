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

	public FileConformity fileValidation(final File file) throws FileNotFoundException, SAXException {
		//On teste si le fichier est bien valide selon son format (XML ou JSON)
		if(!accordanceWithStandard(file)) {
			LOGGER.warn("Le fichier n'est pas conforme au format attendu.");
			return FileConformity.WRONG_STANDARD;
		} else {
			//On teste si le fichier est bien conforme à un Moodle format
			if(!accordanceWithMoodleStandard(file)) {
				LOGGER.warn("Le fichier n'est pas conforme au format Moodle.");
				return FileConformity.WRONG_MOODLE;
			}
		}
		return FileConformity.OK;
	}

	public abstract boolean accordanceWithMoodleStandard(File file)
			throws FileNotFoundException, SAXException;

	public abstract boolean accordanceWithStandard(File file);

    public abstract int convert(final String inputFileUri,
			final String outputFileUri)
		throws IOException, URISyntaxException, TransformerException;

}
