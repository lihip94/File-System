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
    public void testAddFile() {
        fileSystem.addDir("/", "Documents");
        fileSystem.addFile("Documents", "report.txt", 1024);
        assertEquals(1024, fileSystem.getFileSize("report.txt"));
    }

    @Test
    public void testAddDir() {
        assertFalse(fileSystem.delete("Projects"));
        fileSystem.addDir("/", "Projects");
        assertTrue(fileSystem.delete("Projects")); // Testing delete in this test
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
    public void testDeleteFile() {
        fileSystem.addFile("/", "file.txt", 512);
        assertTrue(fileSystem.delete("file.txt"));
        assertFalse(fileSystem.delete("file.txt")); // Verify it's deleted and cannot be deleted again
    }

    @Test
    public void testDeleteDirectory() {
        fileSystem.addDir("/", "TestDir");
        assertTrue(fileSystem.delete("TestDir"));
        assertFalse(fileSystem.delete("TestDir")); // Verify it's deleted and cannot be deleted again
    }
}