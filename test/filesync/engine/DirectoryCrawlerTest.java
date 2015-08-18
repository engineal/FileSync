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
import filesync.SyncIndex;
import filesync.engine.DirectoryCrawler.CrawlState;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
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
        List<File> directories = null;
        DirectoryCrawler instance = null;
        instance.compareDirs(directory, directories);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testStartCrawl() {
        SyncIndex index = new SyncIndex("Test Index");
        DirectoryCrawler instance = new DirectoryCrawler(index, new ArrayList<>());
        instance.startCrawl();
        assertThat(instance.getState(), is(CrawlState.Running));
    }

    /**
     * Test of pauseCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testPauseCrawl() {
        SyncIndex index = new SyncIndex("Test Index");
        DirectoryCrawler instance = new DirectoryCrawler(index, new ArrayList<>());
        instance.startCrawl();
        instance.pauseCrawl();
        //assertThat(instance.getState(), is(CrawlState.Paused));
        instance.pauseCrawl();
        //assertThat(instance.getState(), is(CrawlState.Running));
    }

    /**
     * Test of stopCrawl method, of class DirectoryCrawler.
     */
    @Test
    public void testStopCrawl() {
        SyncIndex index = new SyncIndex("Test Index");
        DirectoryCrawler instance = new DirectoryCrawler(index, new ArrayList<>());
        instance.startCrawl();
        instance.stopCrawl();
        assertThat(instance.getState(), is(CrawlState.Stopped));
    }
}
