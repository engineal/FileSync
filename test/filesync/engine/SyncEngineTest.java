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

import filesync.SyncIndex;
import filesync.engine.DirectoryCrawler.CrawlState;
import junit.framework.Assert;
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
public class SyncEngineTest {
    
    public SyncEngineTest() {
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
     * Test of startCrawl method, of class SyncEngine.
     */
    @Test
    public void testStartCrawl() {
        System.out.println("startCrawl");
        SyncIndex index = new SyncIndex("Test Index");
        SyncEngine engine = new SyncEngine(index);
        engine.startCrawl();
        assertEquals(engine.getState(), CrawlState.Running);
    }

    /**
     * Test of pauseCrawl method, of class SyncEngine.
     */
    @Test
    public void testPauseCrawl() {
        System.out.println("pauseCrawl");
        SyncIndex index = new SyncIndex("Test Index");
        SyncEngine engine = new SyncEngine(index);
        engine.startCrawl();
        engine.pauseCrawl();
        //assertEquals(CrawlState.Paused, engine.getState());
        engine.pauseCrawl();
        //assertEquals(CrawlState.Running, engine.getState());
    }

    /**
     * Test of stopCrawl method, of class SyncEngine.
     */
    @Test
    public void testStopCrawl() {
        System.out.println("stopCrawl");
        SyncIndex index = new SyncIndex("Test Index");
        SyncEngine engine = new SyncEngine(index);
        engine.startCrawl();
        engine.stopCrawl();
        assertEquals(CrawlState.Stopped, engine.getState());
    }
}
