/*
 * Copyright (C) 2015 Aaron Lucia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package filesync.engine;

import filesync.SyncFile;
import static filesync.engine.FileCompare.SyncAction.Modified;
import static filesync.engine.FileCompare.SyncAction.Removed;
import static filesync.engine.FileCompare.SyncAction.Unchanged;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Aaron Lucia
 */
@RunWith(Parameterized.class)
public class FileCompareTest {

    private static final File testDir = new File("JUnitTestFiles");
    private static final String TEST_CONTENT = "Lorem ipsum dolor sit amet, consectetur cras amet.";
    private static final String NEW_TEST_CONTENT = "Nam interdum augue sapien. Aenean venenatis sodales nibh, in pellentesque metus porta vel cras amet.";
    private static final long LAST_MODIFIED = System.currentTimeMillis() - System.currentTimeMillis() % 1000;

    @Parameters
    public static Collection<File[][]> generateFiles() {
        final int[] testCases = {1, 2, 3, 10};
        Collection<File[][]> fileCollection = new ArrayList<>();

        for (int num : testCases) {
            File[][] files = new File[1][num];
            for (int i = 0; i < num; i++) {
                files[0][i] = new File(testDir, "Test" + i + ".txt");
            }
            fileCollection.add(files);
        }

        return fileCollection;
    }

    @BeforeClass
    public static void setUpClass() {
        if (!testDir.exists()) {
            if (!testDir.mkdir()) {
                fail(testDir.getName() + " cannot be created.");
            }
        } else {
            fail(testDir.getName() + " already exists.");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        if (testDir.exists()) {
            if (!testDir.delete()) {
                fail(testDir.getName() + " cannot be deleted.");
            }
        } else {
            fail(testDir.getName() + " does not exist.");
        }
    }

    private final File[] files;

    public FileCompareTest(File[] files) {
        this.files = files;
        assertTrue(files.length > 0);
    }

    @Before
    public void setUp() {
        for (File testFile : files) {
            try (PrintWriter out = new PrintWriter(testFile)) {
                out.print(TEST_CONTENT);
            } catch (FileNotFoundException ex) {
                fail(testFile.getName() + " cannot be created.");
            }
            assertTrue(testFile.setLastModified(LAST_MODIFIED));
        }
    }

    @After
    public void tearDown() {
        for (File testFile : files) {
            if (testFile.exists() && !testFile.delete()) {
                fail(testFile.getName() + " cannot be deleted.");
            }
        }
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionRemoved() {
        assertTrue(files[0].delete());

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Removed, instance.getAction());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionModified() {
        try (PrintWriter out = new PrintWriter(files[0])) {
            out.print(NEW_TEST_CONTENT);
        } catch (FileNotFoundException ex) {
            fail(files[0].getName() + " cannot be modified.");
        }

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Modified, instance.getAction());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionUnchanged() {
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Unchanged, instance.getAction());
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testResolveConflictRemoved() throws Exception {
        assertTrue(files[0].delete());

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Removed, instance.resolveConflict());

        for (File testFile : files) {
            assertFalse(testFile.exists());
        }
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testResolveConflictModified() throws Exception {
        try (PrintWriter out = new PrintWriter(files[0])) {
            out.print(NEW_TEST_CONTENT);
        } catch (FileNotFoundException ex) {
            fail(files[0].getName() + " cannot be modified.");
        }
        long NEW_LAST_MODIFIED = files[0].lastModified();

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Modified, instance.resolveConflict());

        for (File testFile : files) {
            assertEquals(NEW_TEST_CONTENT.getBytes().length, testFile.length());
            assertEquals(NEW_LAST_MODIFIED, testFile.lastModified());

            try (Scanner in = new Scanner(testFile)) {
                assertEquals(NEW_TEST_CONTENT, in.nextLine());
            } catch (FileNotFoundException ex) {
                fail(testFile.getName() + " cannot be read.");
            }
        }

        assertEquals(Unchanged, instance.getAction());
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testResolveConflictUnchanged() throws Exception {
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(Unchanged, instance.resolveConflict());
    }

    /**
     * Test of compareFiles method, of class FileCompare.
     */
    @Test
    public void testCompareFiles() {
        try (PrintWriter out = new PrintWriter(files[0])) {
            out.print(NEW_TEST_CONTENT);
        } catch (FileNotFoundException ex) {
            fail(files[0].getName() + " cannot be modified.");
        }

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(files[0], instance.compareFiles());
    }
}
