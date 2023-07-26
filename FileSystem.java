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
        if (name.length() > MAX_NAME_LENGTH) {
            System.out.println("Name is too long. Maximum length is 32 characters.");
            return false;
        }

        if (name.equals("/")) {
            System.out.println("Root directory cannot be deleted.");
            return false;
        }

        if (!delete(root, name)) {
            System.out.println("File or directory not found.");
            return false;
        }

        return true;
    }
}