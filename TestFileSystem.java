import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileSystem {
    private FileSystem fileSystem;

    @BeforeEach
    public void setup() {
        fileSystem = new FileSystem();
    }

    @Test
    public void testAddFileSuccessfully() {
        fileSystem.addDir("/", "Documents");
        fileSystem.addFile("Documents", "report.txt", 1024);
        assertEquals(1024, fileSystem.getFileSize("report.txt"));
    }

    @Test
    public void testAddFileWithInvalidName() {
        // Attempt to add a file with a name longer than the allowed limit(32characters)
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addFile("/", "ThisFileNameIsTooLongForTheLimit.txt", 1024));
        assertEquals("File name is too long. Maximum length is 32 characters.", exception.getMessage());
    }

    @Test
    public void testAddFileWithExistingName() {
        // Attempt to add a file with a name that exist in the file system
        fileSystem.addDir("/", "Documents");
        fileSystem.addFile("Documents", "OldName.txt", 1024);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addFile("/", "OldName.txt", 1024));
        assertEquals("File name is not valid. File with the same name already exists.", exception.getMessage());
    }

    @Test
    public void testAddFileWithNegativeSize() {
        // Attempt to add a file with a negative size
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addFile("/", "file.txt", -1024));
        assertEquals("File size not valid. Size file must be positive number", exception.getMessage());
    }

    @Test
    public void testAddFileToNonExistsParanetDir() {
        // Attempt to add a file to non exists directory
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addFile("FakeDir", "file.txt", 512));
        assertEquals("Parent directory not found.", exception.getMessage());
    }

    @Test
    public void testAddDirSuccessfully() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.delete("Projects"));
        fileSystem.addDir("/", "Projects");
        // Testing delete in this test sccueded after adding
        assertTrue(fileSystem.delete("Projects"));
    }

    @Test
    public void testAddDirWithInvalidName() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addDir("/", "ThisDirectoryNameIsTooLongForTheLimit"));
        assertEquals("Directory name is too long. Maximum length is 32 characters.", exception.getMessage());

    }

    @Test
    public void testAddDirWithExistingName() {
        // Attempt to add a file with a name that exist in the file system
        fileSystem.addDir("/", "Documents");
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addDir("/", "Documents"));
        assertEquals("Directory name is not valid. Directory with the same name already exists.",
                exception.getMessage());
    }

    @Test
    public void testAddDirtoNonExistsParanetDir() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.addDir("FakeDir", "RealDir"));
        assertEquals("Parent directory not found.", exception.getMessage());
    }

    @Test
    public void testGetFileSize() {
        fileSystem.addFile("/", "file1.txt", 512);
        fileSystem.addFile("/", "file2.txt", 1024);

        assertEquals(512, fileSystem.getFileSize("file1.txt"));
        assertEquals(1024, fileSystem.getFileSize("file2.txt"));
    }

    @Test
    public void testGetBiggestFile() {
        fileSystem.addFile("/", "file1.txt", 512);
        fileSystem.addFile("/", "file2.txt", 1024);
        fileSystem.addFile("/", "file3.txt", 256);

        assertEquals("file2.txt", fileSystem.getBiggestFile().name);
    }

    @Test
    public void testDeleteFileSuccessfully() {
        fileSystem.addFile("/", "file.txt", 512);
        assertTrue(fileSystem.delete("file.txt"));
        // file cannot be deleted again
        assertThrows(IllegalArgumentException.class, () -> fileSystem.delete("file.txt"));
    }

    @Test
    public void testDeleteDirectorySuccessfully() {
        fileSystem.addDir("/", "TestDir");
        assertTrue(fileSystem.delete("TestDir"));
        // dir cannot be deleted again
        assertThrows(IllegalArgumentException.class, () -> fileSystem.delete("TestDir"));
    }

    @Test
    public void testDeleteRootDirectory() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.delete("/"));
        assertEquals("Root directory cannot be deleted.", exception.getMessage());
    }

    @Test
    public void testDeleteDirectory() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> fileSystem.delete("FakeName"));
        assertEquals("File or directory not found.", exception.getMessage());
    }
}