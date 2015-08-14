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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Aaron Lucia
 */
public class FileCompareTest {

    private static final String TEST_DIR = "JUnitTestFiles";
    private static final String TEST_FILE_1 = TEST_DIR + "\\Test1.txt";
    private static final String TEST_FILE_2 = TEST_DIR + "\\Test2.txt";
    private static final String TEST_CONTENT = "Lorem ipsum dolor sit amet, consectetur cras amet.\n";

    public FileCompareTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        File testDir = new File(TEST_DIR);
        if (!testDir.exists()) {
            if (!testDir.mkdir()) {
                fail(TEST_DIR + " cannot be created.");
            }
        } else {
            fail(TEST_DIR + " already exists.");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            if (!testDir.delete()) {
                fail(TEST_DIR + " cannot be deleted.");
            }
        } else {
            fail(TEST_DIR + " does not exist.");
        }
    }

    @Before
    public void setUp() {
        File testFile1 = new File(TEST_FILE_1);
        if (!testFile1.exists()) {
            try (PrintWriter out = new PrintWriter(TEST_FILE_1)) {
                out.print(TEST_CONTENT);
            } catch (FileNotFoundException ex) {
                fail(TEST_FILE_1 + " cannot be created.");
            }
        } else {
            fail(TEST_FILE_1 + " already exists.");
        }

        File testFile2 = new File(TEST_FILE_2);
        if (!testFile2.exists()) {
            try (PrintWriter out = new PrintWriter(TEST_FILE_2)) {
                out.print(TEST_CONTENT);
            } catch (FileNotFoundException ex) {
                fail(TEST_FILE_2 + " cannot be created.");
            }
        } else {
            fail(TEST_FILE_2 + " already exists.");
        }
    }

    @After
    public void tearDown() {
        File testFile1 = new File(TEST_FILE_1);
        if (testFile1.exists() && !testFile1.delete()) {
            fail(TEST_FILE_1 + " cannot be deleted.");
        }

        File testFile2 = new File(TEST_FILE_2);
        if (testFile2.exists() && !testFile2.delete()) {
            fail(TEST_FILE_2 + " cannot be deleted.");
        }
    }

    /**
     * Test of getSyncFile method, of class FileCompare.
     */
    @Test
    public void testGetSyncFile() {
        System.out.println("getSyncFile");
        File[] files = new File[2];
        files[0] = new File(TEST_FILE_1);
        files[1] = new File(TEST_FILE_2);
        FileCompare instance = new FileCompare(files);
        SyncFile expResult = new SyncFile("Test", TEST_CONTENT.getBytes().length, files[0].lastModified());
        SyncFile result = instance.getSyncFile();
        assertEquals(expResult.getSize(), result.getSize());
        assertEquals(expResult.getLastModified(), result.getLastModified());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionAdded() {
        System.out.println("getActionAdded");
        File testFile2 = new File(TEST_FILE_2);
        if (!testFile2.delete()) {
            fail(TEST_FILE_2 + " cannot be deleted.");
        }

        File[] files = new File[2];
        files[0] = new File(TEST_FILE_1);
        files[1] = new File(TEST_FILE_2);
        FileCompare instance = new FileCompare(files);
        assertEquals(SyncAction.Added, instance.getAction());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionRemoved() {
        System.out.println("getActionRemoved");
        File testFile2 = new File(TEST_FILE_2);
        if (!testFile2.delete()) {
            fail(TEST_FILE_2 + " cannot be deleted.");
        }

        File[] files = new File[2];
        files[0] = new File(TEST_FILE_1);
        files[1] = new File(TEST_FILE_2);
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, files[0].lastModified());
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(SyncAction.Removed, instance.getAction());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionModified() {
        System.out.println("getActionModified");
        try (PrintWriter out = new PrintWriter(TEST_FILE_2)) {
            out.print("This content has changed");
        } catch (FileNotFoundException ex) {
            fail(TEST_FILE_2 + " cannot be modified.");
        }

        File[] files = new File[2];
        files[0] = new File(TEST_FILE_1);
        files[1] = new File(TEST_FILE_2);
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, files[0].lastModified());
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(SyncAction.Modified, instance.getAction());
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionUnchanged() {
        System.out.println("getActionUnchanged");
        File[] files = new File[2];
        files[0] = new File(TEST_FILE_1);
        files[1] = new File(TEST_FILE_2);
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, files[0].lastModified());
        FileCompare instance = new FileCompare(syncFile, files);
        assertEquals(SyncAction.Unchanged, instance.getAction());
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     */
    @Test
    public void testResolveConflict() throws Exception {
        System.out.println("resolveConflict");
        FileCompare instance = null;
        SyncAction expResult = null;
        SyncAction result = instance.resolveConflict();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
