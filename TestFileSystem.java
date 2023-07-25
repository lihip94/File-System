public class TestFileSystem {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        fileSystem.addDir("/", "Documents");
        fileSystem.addFile("Documents", "report.txt", 1024);
        fileSystem.addFile("Documents", "image.jpg", 2048);
        fileSystem.addDir("/", "Projects");
        fileSystem.addFile("Projects", "app.exe", 4096);
        fileSystem.addFile("Projects", "data.csv", 8192);
        fileSystem.addDir("Projects", "Backup");
        fileSystem.addFile("Backup", "backup.zip", 16384);

        fileSystem.showFileSystem();

        System.out.println("Size of 'data.csv': " + fileSystem.getFileSize("data.csv"));
        System.out.println("Biggest file: " + fileSystem.getBiggestFile().name);
    }
}
