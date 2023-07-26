/**
 * Represents a file system that manages files and directories.
 */
class FileSystem {

    private static final int MAX_NAME_LENGTH = 32;
    private Directory root;

    /**
     * Constructs a new FileSystem object with a root directory ("/").
     */
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

    private boolean NameExist(Directory current, String name) {
        if (current.name.equals(name))
            return true;

        for (File file : current.files)
            if (file.name.equals(name))
                return true;

        for (Directory dir : current.directories)
            return NameExist(dir, name);
        return false;
    }

    public void addFile(String parentDirName, String fileName, long fileSize) {
        if (fileName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("File name is too long. Maximum length is 32 characters.");
        }

        if (NameExist(root, fileName)) {
            throw new IllegalArgumentException("File name is not valid. File with the same name already exists.");

        }

        if (fileSize <= 0) {
            throw new IllegalArgumentException("File size not valid. Size file must be positive number");

        }

        Directory parentDir = findDirectory(root, parentDirName);
        if (parentDir == null) {
            throw new IllegalArgumentException("Parent directory not found.");
        }

        File newFile = new File(fileName, fileSize);
        parentDir.files.add(newFile);
    }

    public void addDir(String parentDirName, String dirName) {
        if (dirName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Directory name is too long. Maximum length is 32 characters.");
        }

        if (NameExist(root, dirName)) {
            throw new IllegalArgumentException(
                    "Directory name is not valid. Directory with the same name already exists.");

        }

        Directory parentDir = findDirectory(root, parentDirName);
        if (parentDir == null) {
            throw new IllegalArgumentException("Parent directory not found.");
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
        System.out.println(indent + "|-- " + current.name + " (Dir, Creation Date: " + current.creationDate + ")");
        indent += "   ";
        for (File file : current.files) {
            System.out.println(indent + "|-- " + file.name + " (File, Size: " + file.size + ", Creation Date: "
                    + file.creationDate + ")");
        }
        for (Directory dir : current.directories) {
            showFileSystem(dir, indent);
        }
    }

    public void showFileSystem() {
        System.out.println("File System:");
        showFileSystem(root, "");
    }

    private boolean delete(Directory current, String name) {
        for (int i = 0; i < current.files.size(); i++) {
            File file = current.files.get(i);
            if (file.name.equals(name)) {
                current.files.remove(i);
                return true;
            }
        }

        for (int i = 0; i < current.directories.size(); i++) {
            Directory dir = current.directories.get(i);
            if (dir.name.equals(name)) {
                current.directories.remove(i);
                return true;
            } else {
                if (delete(dir, name)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean delete(String name) {
        if (name.equals("/")) {
            throw new IllegalArgumentException("Root directory cannot be deleted.");
        }

        if (!delete(root, name)) {
            throw new IllegalArgumentException("File or directory not found.");
        }

        return true;
    }
}