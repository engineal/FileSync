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
import filesync.engine.FileCompare.SyncAction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

    /**
     * Generate lists of files to run the tests against
     *
     * @return
     */
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

    /**
     * Create directory to run tests in
     */
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

    /**
     * Remove directory used by tests
     */
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

    /**
     * Create a new test with specified list of files
     *
     * @param files the files to test against
     */
    public FileCompareTest(File[] files) {
        this.files = files;
        assertThat(files.length, greaterThan(0));
    }

    /**
     * Create the files to test against
     */
    @Before
    public void setUp() {
        for (File testFile : files) {
            try (PrintWriter out = new PrintWriter(testFile)) {
                out.print(TEST_CONTENT);
            } catch (FileNotFoundException ex) {
                fail(testFile.getName() + " cannot be created.");
            }
            assertThat(testFile.setLastModified(LAST_MODIFIED), is(true));
        }
    }

    /**
     * Remove the files used by tests
     */
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
    public void testGetActionAdded() {
        for (int i = 1; i < files.length; i++) {
            assertThat(files[i].delete(), is(true));
        }

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED, true);
        FileCompare instance = new FileCompare(syncFile, files);
        assertThat(instance.getAction(), is(SyncAction.Added));
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionRemoved() {
        assertThat(files[0].delete(), is(true));

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertThat(instance.getAction(), is(SyncAction.Removed));
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
        assertThat(instance.getAction(), is(SyncAction.Modified));
    }

    /**
     * Test of getAction method, of class FileCompare.
     */
    @Test
    public void testGetActionUnchanged() {
        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertThat(instance.getAction(), is(SyncAction.Unchanged));
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testResolveConflictAdded() throws Exception {
        for (int i = 1; i < files.length; i++) {
            assertThat(files[i].delete(), is(true));
        }

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED, true);
        FileCompare instance = new FileCompare(syncFile, files);
        assertThat(instance.resolveConflict(), is(SyncAction.Added));

        for (File testFile : files) {
            assertThat(testFile.exists(), is(true));
        }
    }

    /**
     * Test of resolveConflict method, of class FileCompare.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testResolveConflictRemoved() throws Exception {
        assertThat(files[0].delete(), is(true));

        SyncFile syncFile = new SyncFile("Test", TEST_CONTENT.getBytes().length, LAST_MODIFIED);
        FileCompare instance = new FileCompare(syncFile, files);
        assertThat(instance.resolveConflict(), is(SyncAction.Removed));

        for (File testFile : files) {
            assertThat(testFile.exists(), is(false));
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
        assertThat(instance.resolveConflict(), is(SyncAction.Modified));

        for (File testFile : files) {
            assertThat(testFile.length(), equalTo(new Integer(NEW_TEST_CONTENT.getBytes().length).longValue()));
            assertThat(testFile.lastModified(), equalTo(NEW_LAST_MODIFIED));

            try (Scanner in = new Scanner(testFile)) {
                assertThat(in.nextLine(), equalTo(NEW_TEST_CONTENT));
            } catch (FileNotFoundException ex) {
                fail(testFile.getName() + " cannot be read.");
            }
        }

        assertThat(instance.getAction(), is(SyncAction.Unchanged));
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
        assertThat(instance.resolveConflict(), is(SyncAction.Unchanged));
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
        assertThat(files[0], equalTo(instance.compareFiles()));
    }
}
