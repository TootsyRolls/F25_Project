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
    
    @FXML
    void aim(MouseEvent event) {
    }
    
    @FXML
    void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
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
    
    private void measureDistance(Ball ball) {        
        double displacementX = ball.getVelocityX() * TIMEFRAMESEC;
        double displacementY = ball.getVelocityY() * TIMEFRAMESEC;
        
        displaceBall(ball);
    }
    
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
            calculateNewMotion(ball);
        });
    }
    
    protected double frictionDecay(double displacement, Ball ball) {
        return frictionCoefficient * ball.getBallMass() * GRAVITYA * displacement;
    }
    
    protected double axisToRad(double x, double y) {
        if (x == Double.NaN || y == Double.NaN) {
            return 0;
        }
        double rad = Math.atan2(y, x);
        return rad;
    }
    
    protected double pythagorean(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }
    
    protected double kineticToSpeed(double kinetic, Ball ball) {
        return Math.sqrt((kinetic * 2) / ball.getBallMass());
    }
    
    protected double energyTransferRatio(double rad, double velocityX, double velocityY) {
        double velocityRad = axisToRad(Math.abs(velocityX), Math.abs(velocityY));
        
        double ratio = 1 - (Math.max(velocityRad, rad) - Math.min(velocityRad, rad)) / (Math.PI/2);
        return ratio;
    }
    
    
//    protected void writePositions() {
//        cue.getBall().translateXProperty().addListener(cl->{
//            System.out.println("Cue");
//            System.out.println(cue.getPosX() + " x, " + cue.getPosY() + " y");
//            System.out.println(cue.getVelocityX() + " Vx, " + cue.getVelocityY()+ " Vy");
//            System.out.println(cue.getKineticEnergy() + " Cue Kinetic");
//        });
//        
//        object.getBall().translateXProperty().addListener(cl -> {
//            System.out.println("Object");
//            System.out.println(object.getPosX() + " x, " + object.getPosY() + " y");
//            System.out.println(object.getVelocityX() + " Vx, " + object.getVelocityY()+ " Vy");
//            System.out.println(object.getKineticEnergy() + " Object Kinetic");
//        });
//    }
}
