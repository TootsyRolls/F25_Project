/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package f25_project.Controller;

import f25_project.Ball;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author tonyg
 */
public class MainTableControllerTest {
    
    private static double delta = 0.01; // Tolerance margin for double values needed for asserEquals()
//    
    private static MainTableController instance;
//    private static Method pythagorean;
//    private static Method axisToRad;
//    private static Method kineticToSpeed;
//    private static Method frictionDecay;

    @Before
    public void setUp() throws Exception {
        instance = new MainTableController();
    }
    
    
//    /**
//     * Uses reflection to access private methods in MainTableController before testing
//     * @throws Exception 
//     */
//    @BeforeAll
//    public static void setUpClass() throws Exception {
//        instance = new MainTableController();
//        
//        pythagorean = MainTableController.class.getDeclaredMethod("pythagorean", double.class, double.class);
//        pythagorean.setAccessible(true);
//        
//        axisToRad = MainTableController.class.getDeclaredMethod("axisToRad", double.class, double.class);
//        axisToRad.setAccessible(true);
//        
//        kineticToSpeed = MainTableController.class.getDeclaredMethod("kineticToSpeed", double.class, Ball.class);
//        kineticToSpeed.setAccessible(true);
//        
//        frictionDecay = MainTableController.class.getDeclaredMethod("kineticToSpeed", double.class, double.class);
//        frictionDecay.setAccessible(true);
//    }

    @Test
    public void testPythagorean1() throws Exception {
        System.out.println("Test Pythagorean 1");
        double x = 3;
        double y = 5;
        double results = instance.pythagorean(x, y);
        double expected = Math.sqrt(34);
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testPythagorean2() throws Exception {
        System.out.println("Test Pythagorean 2");
        double x = 0;
        double y = 5;
        double results = instance.pythagorean(x, y);
        double expected = 5;
        Assert.assertEquals(expected, results, delta);
    }

    @Test 
    public void testAxisToRad1() throws Exception {
        System.out.println("Test Rad 1");
        double x = 0;
        double y = 1;
        double results = instance.axisToRad(x, y);
        double expected = Math.PI/2;
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testAxisToRad2() throws Exception {
        System.out.println("Test Rad 2");
        double x = -1;
        double y = 1;
        double results = instance.axisToRad(x, y);
        double expected = 3 * Math.PI/4;
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testAxisToRad3() throws Exception {    
        System.out.println("Test Rad 3");
        double x = -1;
        double y = -1;
        double results = instance.axisToRad(x, y);
        double expected = 5 * Math.PI/4 - 2 * Math.PI;
        Assert.assertEquals(expected, results, delta);

    }
    
    @Test
    public void testAxisToRad4() throws Exception {
        System.out.println("Test Rad 3");
        double x = 1;
        double y = -1;
        double results = instance.axisToRad(x, y);
        double expected = 7 * Math.PI/4 - 2 * Math.PI;
        Assert.assertEquals(expected, results, delta);
    }

    @Test
    public void testKineticToSpeed1() throws Exception {
        System.out.println("Test KineticToSpeed Method");

        Ball testBall = new Ball(new Circle(Ball.BALLRADIUS), 0, 0);
        testBall.setBallMass(20);
        double kinetic = 200;
        
        double results = instance.kineticToSpeed(kinetic, testBall);
        double expected = Math.sqrt(20);
        
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testKineticToSpeed2() throws Exception {
        System.out.println("Test KineticToSpeed Method");

        Ball testBall = new Ball(new Circle(Ball.BALLRADIUS), 0, 0);
        testBall.setBallMass(50);
        double kinetic = 200;
        
        double results = instance.kineticToSpeed(kinetic, testBall);
        double expected = Math.sqrt(8);
        Assert.assertEquals(expected, results, delta);
    }

    @Test
    public void testFrictionDecay1() throws Exception {
        System.out.println("Test FrictionDecay Method");

        Ball testBall = new Ball(new Circle(Ball.BALLRADIUS), 0, 0);
        testBall.setBallMass(20);
        double results = instance.frictionDecay(100, testBall);
        double expected = 0.1 * 20 * 9.8 * 100;
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testFrictionDecay2() throws Exception {
        System.out.println("Test FrictionDecay Method");

        Ball testBall = new Ball(new Circle(Ball.BALLRADIUS), 0, 0);
        testBall.setBallMass(10);
        double results = instance.frictionDecay(50, testBall);
        double expected = 0.1 * 10 * 9.8 * 50;
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testEnergyTransfertRatio1() {
        System.out.println("Test Energy Transferred Ratio 1");
        double rad = 0;
        double vX = 0;
        double vY = 1;
        double results = instance.energyTransferRatio(rad, vX, vY);
        double expected = 0;
        Assert.assertEquals(expected, results, delta);
    }
    
    @Test
    public void testEnergyTransfertRatio2() {
        System.out.println("Test Energy Transferred Ratio 2");
        double rad = Math.PI/4;
        double vX = -1;
        double vY = -1;
        double results = instance.energyTransferRatio(rad, vX, vY);
        double expected = 1;
        Assert.assertEquals(expected, results, delta);
    }
}
