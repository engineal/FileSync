/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync;

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
        FileSync instance = new FileSync();
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
        FileSync instance = new FileSync();
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
        FileSync instance = new FileSync();
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
        FileSync instance = new FileSync();
        instance.actionPerformed(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
