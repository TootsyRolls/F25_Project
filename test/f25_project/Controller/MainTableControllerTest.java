/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package f25_project.Controller;

import f25_project.Ball;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author tonyg
 */
public class MainTableControllerTest {
    
    private static double delta = 0.0001; // Tolerance margin for double values needed for asserEquals()
    
    private static MainTableController instance;
    private static Method pythagorean;
    private static Method axisToRad;
    private static Method kineticToSpeed;
    private static Method frictionDecay;
    
    
    /**
     * Uses reflection to access private methods in MainTableController before testing
     * @throws Exception 
     */
    @BeforeAll
    public static void setUpClass() throws Exception {
        instance = new MainTableController();
        
        pythagorean = MainTableController.class.getDeclaredMethod("pythagorean", double.class, double.class);
        pythagorean.setAccessible(true);
        
        axisToRad = MainTableController.class.getDeclaredMethod("axisToRad", double.class, double.class);
        axisToRad.setAccessible(true);
        
        kineticToSpeed = MainTableController.class.getDeclaredMethod("kineticToSpeed", double.class, Ball.class);
        kineticToSpeed.setAccessible(true);
        
        frictionDecay = MainTableController.class.getDeclaredMethod("kineticToSpeed", double.class, double.class);
        frictionDecay.setAccessible(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testPythagorean() throws Exception {
        System.out.println("Test Pythagorean Method");
        
        double test1 = (double) pythagorean.invoke(instance, 3, 4);
        assertEquals(5, test1, delta, "Test of 3-4-5 triangle failed");
    }
    
    @Test public void testAxisToRad() throws Exception {
        System.out.println("Test AxisToRad Method");
        
        double test1 = (double) axisToRad.invoke(instance, 1, 1); // 45° coordinates in quadrant 1 
        assertEquals(Math.PI / 4, test1, delta, "Test quadrant 1 failed");
        
        double test2 = (double) axisToRad.invoke(instance, -1, 1); // 135° coordinates in quadrant 2
        assertEquals(3 * Math.PI / 4, test2, delta, "Test quadrant 2 failed");
        
        double test3 = (double) axisToRad.invoke(instance, -1, -1); // 225° coordinates in quadrant 3 
        assertEquals(5 * Math.PI / 4, test3, delta, "Test quadrant 3 failed");
        
        double test4 = (double) axisToRad.invoke(instance, 1, -1); // 315° coordinates in quadrant 4
        assertEquals(7 * Math.PI / 4, test4, delta, "Test quadrant 4 failed");
    }
    
    @Test
    public void testKineticToSpeed() throws Exception {
        System.out.println("Test KineticToSpeed Method");
        
        Ball testBall = new Ball(null, 0, 0);
        
        testBall.setBallMass(1);
        double test1 = (double) kineticToSpeed.invoke(instance, 8, testBall);
        assertEquals(4, test1, delta, "Test 1 failed with mass = 1");
        
        testBall.setBallMass(2);
        double test2 = (double) kineticToSpeed.invoke(instance, 25, testBall);
        assertEquals(5, test2, delta, "Test 2 failed with mass = 2");
    }
    
    @Test
    public void testFrictionDecay() throws Exception {
        System.out.println("Test FrictionDecay Method");
        
        Ball testBall = new Ball(null, 0, 0);
        
        testBall.setBallMass(1);
        double result1 = 0.1 * 1 * 9.8 * 2;
        double test1 = (double) frictionDecay.invoke(instance, 2, testBall);
        assertEquals(result1, test1, delta, "Test 1 failed with mass = 1, displacement = 2");
        
        testBall.setBallMass(5);
        double result2 = 0.1 * 5 * 9.8 * 4;
        double test2 = (double) frictionDecay.invoke(instance, 4, testBall);
        assertEquals(result2, test2, delta, "Test 2 failed with mass = 5, displacement = 4");
    }
    
//    /**
//     * Test of initialize method, of class MainTableController.
//     */
//    @Test
//    public void testInitialize() {
//        System.out.println("initialize");
//        URL url = null;
//        ResourceBundle rb = null;
//        MainTableController instance = new MainTableController();
//        instance.initialize(url, rb);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of aim method, of class MainTableController.
//     */
//    @Test
//    public void testAim() {
//        System.out.println("aim");
//        MouseEvent event = null;
//        MainTableController instance = new MainTableController();
//        instance.aim(event);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of handleExit method, of class MainTableController.
//     */
//    @Test
//    public void testHandleExit() {
//        System.out.println("handleExit");
//        ActionEvent event = null;
//        MainTableController instance = new MainTableController();
//        instance.handleExit(event);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of handleReset method, of class MainTableController.
//     */
//    @Test
//    public void testHandleReset() {
//        System.out.println("handleReset");
//        ActionEvent event = null;
//        MainTableController instance = new MainTableController();
//        instance.handleReset(event);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of push method, of class MainTableController.
//     */
//    @Test
//    public void testPush() {
//        System.out.println("push");
//        MouseEvent event = null;
//        MainTableController instance = new MainTableController();
//        instance.push(event);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
