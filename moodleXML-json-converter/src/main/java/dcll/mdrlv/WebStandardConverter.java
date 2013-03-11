package dcll.mdrlv;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import dcll.mdrlv.tools.FileConformity;

/**
 * @author David Villard
 *
 */
public abstract class WebStandardConverter {

	/**
	 *
	 */
	protected static Logger lOGGER;

	/**
	 *
	 */
	public WebStandardConverter() {
		lOGGER = Logger.getLogger(WebStandardConverter.class);
	}

	/**
	 * @param file : fichier en entree
	 * @return enum
	 */
	public final FileConformity fileValidation(final File file) {
		//On teste si le fichier est bien valide
		//selon son format (XML ou JSON)
		if (!accordanceWithStandard(file)) {
			lOGGER.warn("Le fichier n'est pas conforme"
					+ " au format attendu.");
			return FileConformity.WRONG_STANDARD;
		} else {
			//On teste si le fichier est bien
			//conforme à un Moodle format
			if (!accordanceWithMoodleStandard(file)) {
				lOGGER.warn("Le fichier n'est pas conforme"
			         + " au format Moodle.");
				return FileConformity.WRONG_MOODLE;
			}
		}
		return FileConformity.OK;
	}

	/**
	 * @param file :
	 * @return :
	 */
	public abstract boolean accordanceWithMoodleStandard(File file);

	/**
	 * @param file :
	 * @return :
	 */
	public abstract boolean accordanceWithStandard(File file);

    /**
     * @param inputFileUri :
     * @param outputFileUri :
     * @return :
     * @throws IOException :
     * @throws URISyntaxException :
     * @throws TransformerException :
     */
    public abstract int convert(final String inputFileUri,
			final String outputFileUri)
		throws IOException, URISyntaxException, TransformerException;

}
