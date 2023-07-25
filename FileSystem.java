import java.util.*;

class FileSystem {
    private static final int MAX_NAME_LENGTH = 32;

    class File {
        String name;
        long size;
        Date creationDate;

        File(String name, long size) {
            this.name = name;
            this.size = size;
            this.creationDate = new Date();
        }
    }

    class Directory {
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

    private Directory root;

    public FileSystem() {
        root = new Directory("/");
    }

    private Directory findDirectory(Directory current, String parentDirName) {
        if (current.name.equals(parentDirName)) {
            return current;
        }

        for (Directory dir : current.directories) {
            Directory foundDir = findDirectory(dir, parentDirName);
            if (foundDir != null) {
                return foundDir;
            }
        }
        return null;
    }

    public void addFile(String parentDirName, String fileName, long fileSize) {
        if (fileName.length() > MAX_NAME_LENGTH) {
            System.out.println("File name is too long. Maximum length is 32 characters.");
            return;
        }

        if (fileSize <= 0) {
            System.out.println("File size not valid. Size file must be positive number");
            return;
        }

        Directory parentDir = findDirectory(root, parentDirName);
        if (parentDir == null) {
            System.out.println("Parent directory not found.");
            return;
        }

        File newFile = new File(fileName, fileSize);
        parentDir.files.add(newFile);
    }

    public void addDir(String parentDirName, String dirName) {
        if (dirName.length() > MAX_NAME_LENGTH) {
            System.out.println("Directory name is too long. Maximum length is 32 characters.");
            return;
        }

        Directory parentDir = findDirectory(root, parentDirName);
        if (parentDir == null) {
            System.out.println("Parent directory not found.");
            return;
        }

        Directory newDir = new Directory(dirName);
        parentDir.directories.add(newDir);
    }

    private long getFileSize(Directory current, String fileName) {
        for (File file : current.files) {
            if (file.name.equals(fileName)) {
                return file.size;
            }
        }

        for (Directory dir : current.directories) {
            long size = getFileSize(dir, fileName);
            if (size != -1) {
                return size;
            }
        }

        return -1;
    }

    public long getFileSize(String fileName) {
        return getFileSize(root, fileName);
    }

    private File getBiggestFile(Directory current, File maxFile) {
        for (File file : current.files) {
            if (maxFile == null || file.size > maxFile.size) {
                maxFile = file;
            }
        }

        for (Directory dir : current.directories) {
            maxFile = getBiggestFile(dir, maxFile);
        }

        return maxFile;
    }

    public File getBiggestFile() {
        return getBiggestFile(root, null);
    }

    private void showFileSystem(Directory current, String indent) {
        System.out.println(indent + "|-- " + current.name + " (Dir)");
        indent += "   ";
        for (File file : current.files) {
            System.out.println(indent + "|-- " + file.name + " (File, Size: " + file.size + ")");
        }
        for (Directory dir : current.directories) {
            showFileSystem(dir, indent);
        }
    }

    public void showFileSystem() {
        System.out.println("File System:");
        showFileSystem(root, "");
    }
}