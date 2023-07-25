import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory {
    String name;
    Date creationDate;
    List<File> files;
    List<Directory> directories;

    Directory(String name) {
        this.name = name;
        this.creationDate = new Date();
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
    }
}