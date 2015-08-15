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

import filesync.SyncDirectory;
import java.nio.file.Path;
import java.util.List;
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
public class DirectoryCrawlerTest {

    /**
     * Create a new test for DirectoryCrawler
     */
    public DirectoryCrawlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compareDirs method, of class DirectoryCrawler.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCompareDirs() throws Exception {
        SyncDirectory directory = null;
        List<Path> directories = null;
        DirectoryCrawler instance = null;
        instance.compareDirs(directory, directories);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class DirectoryCrawler.
     */
    @Test
    public void testGetState() {
        DirectoryCrawler instance = null;
        DirectoryCrawler.CrawlState expResult = null;
        DirectoryCrawler.CrawlState result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testStartCrawl() {
        DirectoryCrawler instance = null;
        instance.startCrawl();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pauseCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testPauseCrawl() {
        DirectoryCrawler instance = null;
        instance.pauseCrawl();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stopCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testStopCrawl() {
        DirectoryCrawler instance = null;
        instance.stopCrawl();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
