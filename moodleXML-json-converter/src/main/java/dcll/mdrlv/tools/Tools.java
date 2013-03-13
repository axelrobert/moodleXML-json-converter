package dcll.mdrlv.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * @author : Axel Robert
 *
 */
public final class Tools {

    /**
     * Un logger.
     **/
    private static final Logger LOGGER = Logger.getLogger(Tools.class);

    /**
    * Constructeur privé.
    **/
    private Tools() {

    }

    /**
    * @param chaine : chaîne à écrire dans le ficher
    *
    * @param pathname : path du fichier
    *
    */
    public static void writeStringIntoFile(final String chaine,
        final String pathname) {
        // On crée un FileWriter à partir du pathname
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(new File(pathname));
            } catch (IOException e2) {
            // TODO Auto-generated catch block
            LOGGER.error("Erreur d'ouverture");
        }
        // On crée un BufferedWriter output
		// auquel on donne comme argument
		// le FileWriter fwriter cree juste au dessus
		final BufferedWriter output = new BufferedWriter(fwriter);
		try {
			// On écrit la chaine dans le BufferedWriter
			// qui sert de tampon(buffer)
			output.write(chaine);
		} catch (Exception e1) {
			LOGGER.error("Erreur d'écriture du fichier");
		}

		try {
			// On fait appel à la méthode flush() qui permet
			// d'écrire le contenu du tampon dans le fichier
			output.flush();
		} catch (Exception e) {
			LOGGER.error("Flush erreur");
		}

		// On ferme le BufferedWriter
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Erreur de fermeture");
		}
	}

	/**
	 * @param file : fichier à lire
	 * @return : le contenu du fichier sous forme de chaîne
	 */
	public static String readStringFromFile(final File file) {
		Scanner scan = null;
		String text = null;
		try {
			scan = new Scanner(file);
			text = scan.useDelimiter("\\A").next();
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Fichier non trouve");
		}
		return text;
	}

	/**
	 * @return : un enum correspondant à l'OS
	 */
	public static OS determineOS() {
		final String osName =
				System.getProperty("os.name").toLowerCase();
		OS osOfUser = null;
		if (isWindows(osName)) {
			osOfUser = OS.WINDOWS_OS;
		} else if (isMac(osName)) {
			osOfUser = OS.MAC_OS;
		} else if (isUnix(osName) || isSolaris(osName)) {
			osOfUser = OS.UNIX_OS;
		}
		return osOfUser;
	}

	/**
	 * @param oSystem : chaîne contenant le nom de l'OS
	 * @return : boolean, true si c'est Windows, false sinon
	 */
	public static boolean isWindows(final String oSystem) {
		return (oSystem.indexOf("win") >= 0);
	}

	/**
	 * @param oSystem : chaîne contenant le nom de l'OS
	 * @return : boolean, true si c'est Mac, false sinon
	 */
	public static boolean isMac(final String oSystem) {
		return (oSystem.indexOf("mac") >= 0);
	}

	/**
	 * @param oSystem : chaîne contenant le nom de l'OS
	 * @return : boolean, true si c'est Unix, false sinon
	 */
	public static boolean isUnix(final String oSystem) {
		return (oSystem.indexOf("nix") >= 0
				|| oSystem.indexOf("nux") >= 0
				|| oSystem.indexOf("aix") > 0);
	}

	/**
	 * @param oSystem : chaîne contenant le nom de l'OS
	 * @return : boolean, true si c'est Solaris, false sinon
	 */
	public static boolean isSolaris(final String oSystem) {
		return (oSystem.indexOf("sunos") >= 0);
	}

}
