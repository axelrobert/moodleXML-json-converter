package dcll_project.mdrlv.ihm;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter {
    
    private String description;
    private String[] extensions;

    public CustomFileFilter(String description, String extension) {
        this(description, new String[]{extension});
    }

    /**
     * Constructeur principal.
     *
     * @param description L'intitulé affiché dans le selecteur de fichier.
     * @param extensions La liste des extensions à filtrer.
     */
    public CustomFileFilter(String description, String[] extensions) {
        String separator = "";
        this.description = description.concat("(");
        this.extensions = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.extensions[i] = extensions[i].toLowerCase();
            this.description = this.description.concat(separator)
                                  .concat("*.")
                                  .concat(extensions[i]);
            separator = ", ";
        }
        this.description = this.description.concat(")");
    }

    @Override
    public boolean accept(File file) {

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
    public String getDescription() {
        return description;
    }
    
    public String[] getExtensions(){
        return extensions;
    }

}



