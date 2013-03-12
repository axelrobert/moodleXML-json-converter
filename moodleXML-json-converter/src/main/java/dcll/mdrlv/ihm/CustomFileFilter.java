package dcll.mdrlv.ihm;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author :
 *
 */
public class CustomFileFilter extends FileFilter {

    /**
     *
     */
    private String description;

    /**
     *
     */
    private final String[] extensions;

    /**
     * @param aDescription :
     * @param extension :
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

        if (file.isDirectory()) {
            return true;
        } else {
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0; i < extensions.length; i++) {
                String extension = '.' + extensions[i];
                if (path.endsWith(extension)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    /**
     * @return :
     */
    public final String[] getExtensions() {
        return extensions;
    }

}



