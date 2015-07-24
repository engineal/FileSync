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
     * Test of run method, of class SyncEngine.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIndex method, of class SyncEngine.
     */
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        SyncIndex expResult = null;
        SyncIndex result = instance.getIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isRunning method, of class SyncEngine.
     */
    @Test
    public void testIsRunning() {
        System.out.println("isRunning");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        boolean expResult = false;
        boolean result = instance.isRunning();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pauseSync method, of class SyncEngine.
     */
    @Test
    public void testPauseSync() {
        System.out.println("pauseSync");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        instance.pauseSync();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resumeSync method, of class SyncEngine.
     */
    @Test
    public void testResumeSync() {
        System.out.println("resumeSync");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        instance.resumeSync();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stopSync method, of class SyncEngine.
     */
    @Test
    public void testStopSync() {
        System.out.println("stopSync");
        SyncIndex index = new SyncIndex("Test");
        SyncEngine instance = index.getSyncEngine();
        instance.stopSync();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
