import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Responsible for creating a file in the FileStystem.
 */
public class Directory {
    String name;
    Date creationDate;
    List<File> files;
    List<Directory> directories;

    /**
     * Constructs a new Directory object with the given name.
     *
     * @param name The name of the directory (up to 32 characters long).
     */
    Directory(String name) {
        this.name = name;
        this.creationDate = new Date();
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
    }
}