import java.util.Date;

/**
 * Responsible for creating a file in the FileStystem.
 */
public class File {
    String name;
    long size;
    Date creationDate;

    /**
     * Constructs a new File object with the given name and size.
     *
     * @param name The name of the file (up to 32 characters long).
     * @param size The size of the file (positive long integer).
     */
    File(String name, long size) {
        this.name = name;
        this.size = size;
        this.creationDate = new Date();
    }
}