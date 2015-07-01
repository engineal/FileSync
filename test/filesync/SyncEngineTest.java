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
        SyncEngine instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class SyncEngine.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        SyncEngine instance = null;
        boolean expResult = false;
        boolean result = instance.start();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pause method, of class SyncEngine.
     */
    @Test
    public void testPause() {
        System.out.println("pause");
        SyncEngine instance = null;
        instance.pause();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPaused method, of class SyncEngine.
     */
    @Test
    public void testIsPaused() {
        System.out.println("isPaused");
        SyncEngine instance = null;
        boolean expResult = false;
        boolean result = instance.isPaused();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIndex method, of class SyncEngine.
     */
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        SyncEngine instance = null;
        SyncIndex expResult = null;
        SyncIndex result = instance.getIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addStatusListener method, of class SyncEngine.
     */
    @Test
    public void testAddStatusListener() {
        System.out.println("addStatusListener");
        StatusListener listener = null;
        SyncEngine instance = null;
        instance.addStatusListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeStatusListener method, of class SyncEngine.
     */
    @Test
    public void testRemoveStatusListener() {
        System.out.println("removeStatusListener");
        StatusListener listener = null;
        SyncEngine instance = null;
        instance.removeStatusListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
