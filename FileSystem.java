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

    /**
     * Find and return the directory
     * Complexity: O(N)
     * N - the number of directorys in the file system
     * 
     * @param name The name of directory we seaching for.
     **/
    private Directory findDirectory(Directory current, String dirName) {
        if (current.name.equals(dirName)) {
            return current;
        }

        for (Directory dir : current.directories) {
            Directory foundDir = findDirectory(dir, dirName);
            if (foundDir != null) {
                return foundDir;
            }
        }
        return null;
    }

    /**
     * Checks if the name exist in the file system.
     * Complexity: O(N + M)
     * N - the number of directorys in the file system
     * M - the number of files in the file system
     * 
     * @param current The directory that we check in.
     * @param name    The name of the file or directory.
     **/
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

    /**
     * Adds a new file with the specified name and size to the given parent
     * directory.
     * Complexity: O(N + M)
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

    /**
     * Adds a new directory with the specified name to the given parent directory.
     * Complexity: O(N + M)
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

    /**
     * Fet the file size
     * Complexity: O(N + M)
     * 
     * @param current  The name of the parent directory (inital root)
     * @param fileName The name of the file
     * 
     */
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

    /**
     * Get the file size
     * 
     * @param fileName The name of the file
     * 
     */
    public long getFileSize(String fileName) {
        return getFileSize(root, fileName);
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