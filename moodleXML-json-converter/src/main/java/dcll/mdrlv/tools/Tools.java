package dcll.mdrlv.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * @author :
 *
 */
public class Tools {

	/**
	 *
	 */
	protected static final Logger LOGGER = Logger.getLogger(Tools.class);

	/**
	 * @param chaine :
	 * @param pathname :
	 */
	public static void writeStringIntoFile(final String chaine,
			final String pathname) {
		//On crée un FileWriter à partir du pathname
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(new File(pathname));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//On crée un BufferedWriter output
		//auquel on donne comme argument
		//le FileWriter fwriter cree juste au dessus
		final BufferedWriter output = new BufferedWriter(fwriter);
		try {
			//On écrit la chaine dans le BufferedWriter
			//qui sert de tampon(buffer)
			output.write(chaine);
		} catch (Exception e1) {
			LOGGER.error("Erreur d'écriture du fichier");
		}

		try {
			//On fait appel à la méthode flush() qui permet
			//d'écrire le contenu du tampon dans le fichier
			output.flush();
		} catch (Exception e) {
			LOGGER.error("Flush erreur");
		}

		//On ferme le BufferedWriter
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param f :
	 * @return :
	 */
	public static String readStringFromFile(final File f) {
		Scanner scan = null;
		String text = null;
		try {
			scan = new Scanner(f);
			text = scan.useDelimiter("\\A").next();
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
