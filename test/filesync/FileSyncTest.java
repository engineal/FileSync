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
package filesync;

import filesync.ui.UIEvent;
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
public class FileSyncTest {
    
    public FileSyncTest() {
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
     * Test of getInstance method, of class FileSync.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        FileSync expResult = null;
        FileSync result = FileSync.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class FileSync.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        FileSync.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sync method, of class FileSync.
     */
    @Test
    public void testSync() {
        System.out.println("sync");
        FileSync instance = null;
        instance.sync();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pauseSchedule method, of class FileSync.
     */
    @Test
    public void testPauseSchedule() {
        System.out.println("pauseSchedule");
        FileSync instance = null;
        instance.pauseSchedule();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of statusUpdated method, of class FileSync.
     */
    @Test
    public void testStatusUpdated() {
        System.out.println("statusUpdated");
        StatusEvent event = null;
        FileSync instance = null;
        instance.statusUpdated(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actionPerformed method, of class FileSync.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        UIEvent event = null;
        FileSync instance = null;
        instance.actionPerformed(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
