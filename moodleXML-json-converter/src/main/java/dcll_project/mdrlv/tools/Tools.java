package dcll_project.mdrlv.moodleXML_json_converter.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tools {
	public static void writeStringIntoFile(String ch,String pathname) throws IOException{
		FileWriter fw = new FileWriter(new File(pathname));
		// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
		BufferedWriter output = new BufferedWriter(fw);
		//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
		try {
			output.write(ch);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//on peut utiliser plusieurs fois methode write
		try {
			output.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
	}
}
