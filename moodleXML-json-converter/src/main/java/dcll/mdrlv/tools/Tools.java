package dcll_project.mdrlv.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Tools {

	protected static final Logger LOGGER = Logger.getLogger(Tools.class);

	public static void writeStringIntoFile(final String chaine,
			final String pathname) throws IOException {
		final FileWriter fwriter = new FileWriter(new File(pathname));
		//le BufferedWriter output auquel on donne comme argument
		//le FileWriter fw cree juste au dessus
		final BufferedWriter output = new BufferedWriter(fwriter);
		//on marque dans le fichier ou plutot dans
		//le BufferedWriter qui sert comme un tampon(stream)
		try {
			output.write(chaine);
		} catch (Exception e1) {
			LOGGER.error("Erreur d'écriture du fichier");
		}
		//on peut utiliser plusieurs fois la methode write
		try {
			output.flush();
		} catch (Exception e) {
			LOGGER.error("Flush erreur");
		}
		//ensuite flush envoie dans le fichier,
		//ne pas oublier cette methode pour le BufferedWriter
		output.close();
	}
}
