package dcll.mdrlv;

import java.io.File;

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
	private static Logger lOGGER;

	/**
	 * @param log : le logger
	 */
	public final void setlOGGER(final Logger log) {
		lOGGER = log;
	}

	/**
	 * @return : le logger
	 */
	public final Logger getlOGGER() {
		return lOGGER;
	}

	/**
	 * Constructeur : initialisation du logger
	 */
	public WebStandardConverter() {
		lOGGER = Logger.getLogger(WebStandardConverter.class);
	}

	/**
	 * @param file : fichier en entrée
	 * @return une FileConformity enumération
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
	 * @param file : fichier à vérifier
	 * @return : boolean
	 */
	public abstract boolean accordanceWithMoodleStandard(File file);

	/**
	 * @param file : fichier à vérifier
	 * @return : boolean
	 */
	public abstract boolean accordanceWithStandard(File file);

    /**
     * @param inputFileUri : URL du fichier en entrée
     * @param outputFileUri : URL du fichier en sortie
     * @return :

     */
    public abstract int convert(final String inputFileUri,
			final String outputFileUri);

}
