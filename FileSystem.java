import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents a file system that manages files and directories.
 */
class FileSystem {

    private static final int MAX_NAME_LENGTH = 32;
    private Directory root;
    private Dictionary<String, Object> namesInFileSystem;

    /**
     * Constructs a new FileSystem object with a root directory ("/").
     */
    public FileSystem() {
        root = new Directory("/");
        namesInFileSystem = new Hashtable<>();
        namesInFileSystem.put("/", root);
    }

    /**
     * Adds a new file with the specified name and size to the given parent
     * directory.
     * Complexity: O(1)
     * 
     * @param parentDirName The name of the parent directory where the file will be
     *                      added.
     * @param fileName      The name of the file to be added (up to 32 characters
     *                      long).
     * @param fileSize      The size of the file to be added (positive long
     *                      integer).
     */
    public void addFile(String parentDirName, String fileName, long fileSize) {
        if (fileName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("File name is too long. Maximum length is 32 characters.");
        }

        if (namesInFileSystem.get(fileName) != null) {
            throw new IllegalArgumentException("File name is not valid. File with the same name already exists.");
        }
        if (fileSize <= 0) {
            throw new IllegalArgumentException("File size not valid. Size file must be positive number");

        }

        Object parentDir = namesInFileSystem.get(parentDirName);
        if (parentDir == null) {
            throw new IllegalArgumentException("Parent directory not found.");
        }

        File newFile = new File(fileName, fileSize);
        ((Directory) parentDir).files.add(newFile);
        namesInFileSystem.put(fileName, newFile);
    }

    /**
     * Adds a new directory with the specified name to the given parent directory.
     * Complexity: O(1))
     *
     * @param parentDirName The name of the parent directory where the directory
     *                      will be added.
     * @param dirName       The name of the directory to be added (up to 32
     *                      characters long).
     */
    public void addDir(String parentDirName, String dirName) {
        if (dirName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Directory name is too long. Maximum length is 32 characters.");
        }

        if (namesInFileSystem.get(dirName) != null) {
            throw new IllegalArgumentException(
                    "Directory name is not valid. Directory with the same name already exists.");
        }

        Object parentDir = namesInFileSystem.get(parentDirName);
        if (parentDir == null) {
            throw new IllegalArgumentException("Parent directory not found.");
        }

        Directory newDir = new Directory(dirName);
        ((Directory) parentDir).directories.add(newDir);
        namesInFileSystem.put(dirName, newDir);
    }

    /**
     * Get the file size
     * Complexity: O(1)
     * 
     * @param fileName The name of the file
     * 
     */
    public long getFileSize(String fileName) {
        Object currentFile = namesInFileSystem.get(fileName);
        long fileSize = ((File) currentFile).size;
        return fileSize;
    }

    /**
     * Get the biggest file in the file system
     * Complexity: O(N + M)
     * 
     * @param current The name of the parent directory (inital value is root)
     * @param maxFile The file with the max size (inital values is null)
     * 
     */
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

    /**
     * Get the biggest file in the file system
     * Complexity: O(N + M)
     * 
     */
    public File getBiggestFile() {
        return getBiggestFile(root, null);
    }

    /**
     * Displays all files & directories with their hierarchical structure
     * Complexity: O(N + M)
     * 
     * @param current The name of the parent directory (inital value is root)
     * @param indent  The file with the max size (inital values is empty string)
     * 
     */
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

    /**
     * Displays all files & directories with their hierarchical structure.
     * The file and the directory show all their properties (name, size, time
     * creation)
     * Complexity: O(N + M)
     * 
     */
    public void showFileSystem() {
        System.out.println("File System:");
        showFileSystem(root, "");
    }

    /**
     * Delete a file or a Directory with the specific name
     * If delete directory, delete all the files in the directory
     * Complexity: O(N + M)
     * 
     * @param current The name of the parent directory (inital value is root)
     * @param name    The name of the file or directory to delete
     */
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

    /**
     * Delete a file or a Directory with the specific name
     * Complexity: O(N + M)
     * 
     * @param name The name of the file or directory to delete
     */
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