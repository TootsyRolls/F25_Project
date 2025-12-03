/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package f25_project.Controller;

import f25_project.Ball;
import f25_project.UI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Natha
 */
public class MainTableController implements Initializable, UI {
    
    private final double GRAVITYA = 9.8;
    private final double TIMEFRAME = 10; //in Milliseconds
    private final double TIMEFRAMESEC = TIMEFRAME/1000; //in Milliseconds
    
    private double frictionCoefficient = 0.1;
    
    private Ball cue;
    private Ball object;

    @FXML
    private Circle cueBall;
    
    @FXML
    private Circle objectBall;
    
    @FXML
    private Button exitButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private Label frictionLabel;

    @FXML
    private Slider frictionSlider;

    @FXML
    private Label massLabel;

    @FXML
    private Slider massSlider;

    @FXML
    private Pane table;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cue = new Ball(cueBall, cueBall.getLayoutX(), cueBall.getLayoutY());
        object = new Ball(objectBall, objectBall.getLayoutX(), objectBall.getLayoutY());
        
//        writePositions();
        
        bounceOffWallProperty(cue);
        bounceOffWallProperty(object);
        collide(cue, object);
        collide(object, cue);
        
        massLabel.setText("1.00");
        setMassSlider();
        
        frictionLabel.setText("0.10");
        setFrictionSlider();
    }
    
    /**
     * shows the amount of power applied (optional)
     * @param event the event when the user drags around the Cue ball
     */
    @FXML
    void aim(MouseEvent event) {
    }
    
    /**
     * closes the program
     * @param event the event when the user activates the Exit button
     */
    @FXML
    void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
    /**
     * resets the position of the Ball objects
     * @param event the event when the user activates the Reset button
     */
    @FXML
    void handleReset(ActionEvent event) {
        cue.setPosX(100);
        cue.setPosY(200);
        cue.getBall().setTranslateX(0);
        cue.getBall().setTranslateY(0);
        
        object.getBall().setTranslateX(0);
        object.getBall().setTranslateY(0);
        object.setPosX(550);
        object.setPosY(200);
    }
    
    /**
     * sets the slider listeners for the masses of the Ball objects and the label
     */
    private void setMassSlider() {
        massSlider.valueProperty().addListener(cl -> {
            massLabel.setText(String.format("%.2f",massSlider.getValue()));
        });
        
        massLabel.textProperty().addListener(cl -> {
            try {
                cue.setBallMass(Double.parseDouble(massLabel.getText()));
                object.setBallMass(Double.parseDouble(massLabel.getText()));
            } catch (Exception e) {
                massSlider.setValue(1);
                cue.setBallMass(1);
            }
        });
    }
    
    /**
     * sets the slider listeners for the friction coefficient of the table and the label
     */
    private void setFrictionSlider() {
        frictionSlider.valueProperty().addListener(cl -> {
            frictionLabel.setText(String.format("%.2f", frictionSlider.getValue()));
        });
        
        frictionLabel.textProperty().addListener(cl -> {
            try {
                frictionCoefficient = Double.parseDouble(frictionLabel.getText());
            } catch (Exception e) {
                frictionSlider.setValue(0.1);
                frictionCoefficient = 1;
            }
        });
    }
    
    @FXML
    void push(MouseEvent event) {
        double arbitraryAmplifier = 100; //Try to get a coefficient that even out the power of the push
        
        double power = arbitraryAmplifier * pythagorean(event.getX(), event.getY());
        
        calculateAppliedPower(power, -event.getX(), -event.getY(), cue);
        resetButton.setDisable(true);
        cue.getBall().setDisable(true);
    }
    
    /**
     * sets the given ball the property that bounces when reaching the borders of the table/pane/parent
     * @param ball the selected ball - every ball should have this property
     */
    private void bounceOffWallProperty(Ball ball) {
        ball.getBall().translateXProperty().addListener(cl -> {
            double left = Ball.BALLRADIUS - ball.getBall().getLayoutX();
            double right = table.getWidth() - Ball.BALLRADIUS - ball.getBall().getLayoutX();
            
            if (!ball.isBouncedX() &&
                    (ball.getBall().getTranslateX() < left
                    || ball.getBall().getTranslateX() > right)) {
                    
                ball.setBouncedX(true);
                ball.setVelocityX(-ball.getVelocityX());
            }
            if (ball.isBouncedX() 
                    && (ball.getBall().getTranslateX() > left 
                    && ball.getBall().getTranslateX() < right)
                    && ball.getPosX() > Ball.BALLRADIUS 
                    && ball.getPosX() < table.getWidth() - Ball.BALLRADIUS) {
                ball.setBouncedX(false);;
            }
        });
        
        ball.getBall().translateYProperty().addListener(cl -> {
            double top =  Ball.BALLRADIUS - ball.getBall().getLayoutY();
            double bottom = table.getHeight() - Ball.BALLRADIUS - ball.getBall().getLayoutY();
            
            if ((!ball.isBouncedY() &&
                    (ball.getBall().getTranslateY() < top || ball.getBall().getTranslateY() > bottom))) {
                
                ball.setBouncedY(true);
                ball.setVelocityY(-ball.getVelocityY());
            }
            if (ball.isBouncedY() && ball.getBall().getTranslateY() > top && ball.getBall().getTranslateY() < bottom) {
                ball.setBouncedY(false);
            }
        });
    }
    
    /**
     * sets the property of the acting Ball object to collide with another Ball object
     * separates both the x and y translate listeners since the collision should only occur once
     * @param c the acting Ball object
     * @param o the colliding Ball object
     * @return a true or false value indicating that the function become active
     */
    private boolean collide(Ball c, Ball o) {
        c.getBall().translateXProperty().addListener(cl -> {
                colliding(c,o);
        });
        
        c.getBall().translateYProperty().addListener(cl -> {
            if ((int) c.getVelocityX() == 0) {
               colliding(c, o);
            }
        });
        return true;
    }
   
    private boolean once = true;  
    /**
     * main function
     * calculates the energy, velocities, and angle of the motion of the colliding Ball objects
     * @param c the acting Ball object
     * @param o the colliding Ball object
     */
    private void colliding(Ball c, Ball o) {
        double distanceX = (o.getPosX())  - c.getPosX();
        double distanceY = (o.getPosY()) - c.getPosY();
        double radAbs = axisToRad(Math.abs(distanceX), Math.abs(distanceY)); //For calculations
        double rad = axisToRad(distanceX, distanceY);
        double distance = pythagorean(distanceX, distanceY);

        double ballDetectionRange = 2 * Ball.BALLRADIUS;

        if (!c.isCollided() && !o.isCollided() && distance < ballDetectionRange) {
            double kEnergyTransfert = c.getKineticEnergy() * energyTransferRatio(radAbs, c.getVelocityX(), c.getVelocityY());
            c.setCollided(true);
            o.setCollided(true);
            c.setBouncedX(false);
            c.setBouncedY(false);
            o.setBouncedX(false);
            o.setBouncedY(false); 
            
            if (!o.isMoving()) {
                c.setKineticEnergy(c.getKineticEnergy() - kEnergyTransfert );
                c.setVelocityX(c.getVelocityX() * Math.cos(rad + Math.PI/2 + (Math.PI - rad)));
                c.setVelocityY(c.getVelocityY() * Math.sin(rad + Math.PI/2 + (Math.PI - rad)));
                calculateAppliedPower((o.getKineticEnergy() + kEnergyTransfert) / TIMEFRAMESEC, distanceX, distanceY, o);
            }
            else {
                if (once) {
                    c.setKineticEnergy(c.getKineticEnergy() - kEnergyTransfert );
                    c.setVelocityX(c.getVelocityX() * Math.cos(rad));
                    c.setVelocityY(c.getVelocityY() * Math.sin(rad));
                    o.setKineticEnergy(o.getKineticEnergy() + kEnergyTransfert);
                    double speedO = kineticToSpeed(o.getKineticEnergy(), o);
                    o.setVelocityX(speedO * Math.cos(rad));
                    o.setVelocityY(speedO * Math.sin(rad));  
                    once = false;
                }
                else {
                    once = true;
                }
            }
        }
        if (c.isCollided() && o.isCollided() && distance > ballDetectionRange) {
            c.setCollided(false);
            o.setCollided(false);
        }
    }
    
    /**
     * calculates the power applied to the Ball object, energy, velocities, and angle of the motion 
     * @param power the power applied on the Ball object
     * @param x the x distance between the Ball object and the applied power
     * @param y the y distance between the Ball object and the applied power
     * @param ball the selected Ball object
     */
    private void calculateAppliedPower(double power, double x, double y, Ball ball) {
        double rad = axisToRad(x, y);
        double kEnergy = power * TIMEFRAMESEC;
        double speed = kineticToSpeed(kEnergy, ball);
        double velocityX = speed * Math.cos(rad);
        double velocityY = speed * Math.sin(rad);
        
        ball.setVelocityX(velocityX);
        ball.setVelocityY(velocityY);
        ball.setKineticEnergy(kEnergy);

        
        measureDistance(ball);
    }
    
    /**
     * recalculates the trajectory of the motion of the Ball object, which includes friction
     * @param ball the selected Ball object that its trajectory is being adjusting
     */
    private void calculateNewMotion(Ball ball) {
        double displacementX = ball.getVelocityX() * TIMEFRAMESEC;
        double displacementY = ball.getVelocityY() * TIMEFRAMESEC;
        double rad = axisToRad(displacementX, displacementY);
        
        double kEnergy = ball.getKineticEnergy() - frictionDecay(pythagorean(displacementX, displacementY), ball);
        ball.setKineticEnergy(kEnergy);
        
        double speed = kineticToSpeed(kEnergy, ball);
        
        displacementX = speed * Math.cos(rad);
        displacementY = speed * Math.sin(rad);
        
        ball.setVelocityX(displacementX);
        ball.setVelocityY(displacementY);
        
        measureDistance(ball);
    }
    
    /**
     * calculates the future displacements of the Ball object according to its kinetic energy
     * @param ball the selected Ball object
     */
    private void measureDistance(Ball ball) {        
        double displacementX = ball.getVelocityX() * TIMEFRAMESEC;
        double displacementY = ball.getVelocityY() * TIMEFRAMESEC;
        
        displaceBall(ball);
    }
    
    /**
     * moves the Ball object based on its x and y velocities in a fixed time frame
     * it recalls the calculateNewMotion function to readjust the Ball object's motion
     * @param ball the selected Ball object
     */
    private void displaceBall(Ball ball) {
        if (ball.getKineticEnergy() <= 0) {
            resetButton.setDisable(false);
            cue.getBall().setDisable(false);
            ball.setMoving(false);
            return;
        }
        TranslateTransition move = new TranslateTransition(Duration.millis(TIMEFRAME), ball.getBall());
        move.setInterpolator(Interpolator.LINEAR); 
        move.setByX(ball.getVelocityX());
        move.setByY(ball.getVelocityY());
        move.setCycleCount(1);
        
        move.play();
        
        ball.setMoving(true);
        
        move.setOnFinished(eh -> {
            ball.setPosX(ball.getBall().getLayoutX() + move.getNode().getTranslateX());
            ball.setPosY(ball.getBall().getLayoutY() + move.getNode().getTranslateY());
            move.stop();
            calculateNewMotion(ball);
        });
    }
    
    /**
     * calculates the work done by the friction acted on the given Ball object
     * @param displacement the distance traveled by the given Ball object
     * @param ball the selected Ball object
     * @return the work done by the friction acted on the given Ball object
     */
    private double frictionDecay(double displacement, Ball ball) {
        return frictionCoefficient * ball.getBallMass() * GRAVITYA * displacement;
    }
    
    /**
     * calculates the angle in rad with the given x & y axis
     * it measures with any x & y data
     * @param x the x axis
     * @param y the y axis
     * @return the angle in rad
     */
    private double axisToRad(double x, double y) {
        if (x == Double.NaN || y == Double.NaN) {
            return 0;
        }
        double rad = Math.atan2(y, x);
        return rad;
    }
    
    /**
     * calculates the hypotenuse or magnitude of any given x & y axis
     * @param x the x axis
     * @param y the y axis
     * @return the hypotenuse of the given x & y axis
     */
    private double pythagorean(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }
    
    /**
     * calculates the speed according to the kinetic energy of the Ball object
     * @param kinetic the kinetic energy of the Ball object
     * @param ball the selected Ball object
     * @return the speed the Ball object
     */
    private double kineticToSpeed(double kinetic, Ball ball) {
        return Math.sqrt((kinetic * 2) / ball.getBallMass());
    }
    
    /**
     * calculations the portion of energy transferred according to the angle of the Ball object colliding with the other
     * @param rad the angle that the acting Ball object is colliding at
     * @param velocityX the initial x velocity of the acting Ball object
     * @param velocityY the initial y velocity of the acting Ball object
     * @return the ratio of the total energy that is transferred
     */
    private double energyTransferRatio(double rad, double velocityX, double velocityY) {
        double velocityRad = axisToRad(Math.abs(velocityX), Math.abs(velocityY));
        
        double ratio = 1 - (Math.max(velocityRad, rad) - Math.min(velocityRad, rad)) / (Math.PI/2);
        return ratio;
    }
    
    /**
     * only displays the data held in the Ball objects
     */
    private void writePositions() {
        cue.getBall().translateXProperty().addListener(cl->{
            System.out.println("Cue");
            System.out.println(cue.getPosX() + " x, " + cue.getPosY() + " y");
            System.out.println(cue.getVelocityX() + " Vx, " + cue.getVelocityY()+ " Vy");
            System.out.println(cue.getKineticEnergy() + " Cue Kinetic");
        });
        
        object.getBall().translateXProperty().addListener(cl -> {
            System.out.println("Object");
            System.out.println(object.getPosX() + " x, " + object.getPosY() + " y");
            System.out.println(object.getVelocityX() + " Vx, " + object.getVelocityY()+ " Vy");
            System.out.println(object.getKineticEnergy() + " Object Kinetic");
        });
    }
}
