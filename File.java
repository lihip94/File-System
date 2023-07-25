import java.util.Date;

public class File {
    String name;
    long size;
    Date creationDate;

    File(String name, long size) {
        this.name = name;
        this.size = size;
        this.creationDate = new Date();
    }
}