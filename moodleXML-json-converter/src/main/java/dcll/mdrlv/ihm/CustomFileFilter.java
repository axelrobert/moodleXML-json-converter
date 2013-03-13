package dcll.mdrlv.ihm;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author :Emilien DUBOIS, Romain LECHIEN, Francois MANCIET, Axel ROBERT, David
 *         VILLARD.
 * Permet de créer des filtres pour
 * les selecteurs de Fichiers
 * et les selecteurs de dossiers.
 *
 */
public class CustomFileFilter extends FileFilter {

    /**
     * description : donne un nom au filtre.
     */
    private String description;

    /**
     * extensions : définis les type de fichiers sélectionnables.
     */
    private final String[] extensions;

    /**
     * Constructeur.
     * @param aDescription : une description
     * @param extension : une liste d'extensions
     */
    public CustomFileFilter(final String aDescription, final String extension) {
        this(aDescription, new String[]{extension});
    }

    /**
     * Constructeur principal...
     *
     * @param aDescription L'intitulé affiché dans le selecteur de fichier.
     * @param newExtensions La liste des extensions à filtrer.
     */
    public CustomFileFilter(final String aDescription,
    		final String[] newExtensions) {
        String separator = "";
        this.description = aDescription.concat("(");
        this.extensions = new String[newExtensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.extensions[i] = newExtensions[i].toLowerCase();
            this.description = this.description.concat(separator)
                                  .concat("*.")
                                  .concat(extensions[i]);
            separator = ", ";
        }
        this.description = this.description.concat(")");
    }

    @Override
    public final boolean accept(final File file) {
    	boolean retour = false; 
        if (file.isDirectory()) {
            retour = true;
        } else {
            final String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0; i < extensions.length; i++) {
                final String extension = '.' + extensions[i];
                if (path.endsWith(extension)) {
                	retour = true;
                }
            }
        }
        return retour;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    /**
     * @return : la liste des extensions autorisées
     */
    public final String[] getExtensions() {
        return extensions;
    }

}



