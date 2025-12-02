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
    private final double TIMEFRAME = 20; //in Milliseconds
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
        
//        System.out.println(event.getX() + " MouseX, " + event.getY() + " MouseY");
        calculateAppliedPower(power, -event.getX(), -event.getY(), cue);
        resetButton.setDisable(true);
        cue.getBall().setDisable(true);
    }
    
    private void bounceOffWallProperty(Ball ball) {
        ball.getBall().translateXProperty().addListener(cl -> {
            double preventWallX = ball.getVelocityX() * TIMEFRAMESEC;
            double left = Ball.BALLRADIUS + preventWallX;
            double right = table.getWidth() - Ball.BALLRADIUS - preventWallX;
            
            if (!ball.isBouncedX() &&
                    (ball.getPosX() < left 
                    || ball.getPosX() > right)) {
                    
                ball.setBouncedX(true);
                ball.setVelocityX(-ball.getVelocityX());
                System.out.println(ball.isBouncedX());
            }
            if (ball.isBouncedX() && (ball.getPosX() > left + Ball.BALLRADIUS   && ball.getPosX() < right + Ball.BALLRADIUS)) {
                System.out.println(ball.isBouncedY());
                ball.setBouncedX(false);
            }
        });
        
        ball.getBall().translateYProperty().addListener(cl -> {
            double preventWallY = ball.getVelocityY() * TIMEFRAMESEC;
            double top =  Ball.BALLRADIUS + preventWallY;
            double bottom = table.getHeight() - Ball.BALLRADIUS - preventWallY;
            
            if ((!ball.isBouncedY() &&
                    (ball.getPosY() < top 
                    || ball.getPosY() > bottom))) {
                
                ball.setBouncedY(true);
                ball.setVelocityY(-ball.getVelocityY());
            }
            if (ball.isBouncedY() && (ball.getPosY() > top * (2 + (TIMEFRAME/10)) && ball.getPosY() < bottom * (2 + (TIMEFRAME/10)))) {
                ball.setBouncedY(false);
            }
        });
    }
    
    private boolean collide(Ball c, Ball o) {
        
        boolean singleActif = false;
        c.getBall().translateXProperty().addListener(cl -> {
            double distanceX = (o.getPosX())  - c.getPosX();
            double distanceY = (o.getPosY()) - c.getPosY();
            double radAbs = axisToRad(Math.abs(distanceX), Math.abs(distanceY));
            double rad = axisToRad(distanceX, distanceY);
            double distance = pythagorean(distanceX, distanceY);
            
            double ballDetectionRange = 2 * Ball.BALLRADIUS + pythagorean(c.getVelocityX(), c.getVelocityY()) * TIMEFRAMESEC;
            
            if (!c.isCollided() && !o.isCollided() && distance <= ballDetectionRange) {
                double kEnergyTransfert = o.getKineticEnergy() + c.getKineticEnergy() * energyTransferRatio(radAbs, c.getVelocityX(), c.getVelocityY());
                c.setCollided(true);
                o.setCollided(true);
                c.setKineticEnergy(c.getKineticEnergy() - kEnergyTransfert);
                c.setVelocityX(c.getVelocityX() * Math.cos(rad + Math.PI/2 + radAbs));
                c.setVelocityY(c.getVelocityY() * Math.sin(rad + Math.PI/2 + radAbs));
                calculateAppliedPower(kEnergyTransfert / TIMEFRAMESEC, distanceX, distanceY, o);
            }
            if (c.isCollided() && o.isCollided() && distance > ballDetectionRange) {
                c.setCollided(false);
                o.setCollided(false);
            }
        });
        
//        c.getBall().translateYProperty().addListener(cl -> {
//            if (singleActif) {
//                double distanceX = (o.getPosX())  - c.getPosX();
//                double distanceY = (o.getPosY()) - c.getPosY();
//                double radAbs = axisToRad(Math.abs(distanceX), Math.abs(distanceY));
//                double rad = axisToRad(distanceX, distanceY);
//                double distance = pythagorean(distanceX, distanceY);
//
//                double ballDetectionRange = 2 * Ball.BALLRADIUS + pythagorean(c.getVelocityX(), c.getVelocityY()) * TIMEFRAMESEC;
//
//                if (!c.isCollided() && !o.isCollided() && distance <= ballDetectionRange) {
//                    double kEnergyTransfert = o.getKineticEnergy() + c.getKineticEnergy() * energyTransferRatio(radAbs, c.getVelocityX(), c.getVelocityY());
//                    c.setCollided(true);
//                    o.setCollided(true);
//                    c.setKineticEnergy(c.getKineticEnergy() - kEnergyTransfert);
//                    c.setVelocityX(c.getVelocityX() * Math.cos(rad + Math.PI/2 + radAbs));
//                    c.setVelocityY(c.getVelocityY() * Math.sin(rad + Math.PI/2 + radAbs));
//                    calculateAppliedPower(kEnergyTransfert / TIMEFRAMESEC, distanceX, distanceY, o);
//                }
//                if (c.isCollided() && o.isCollided() && distance > ballDetectionRange) {
//                    c.setCollided(false);
//                    o.setCollided(false);
//                }
//            }
//        });
        return true;
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
        
        predictDisplacement(displacementX, displacementY, ball);
    }
    
    private void measureDistance(Ball ball) {        
        double displacementX = ball.getVelocityX() * TIMEFRAMESEC;
        double displacementY = ball.getVelocityY() * TIMEFRAMESEC;
        
        predictDisplacement(displacementX, displacementY, ball);
    }
    
    private void predictDisplacement(double x, double y, Ball ball) {
        double newX = x;
        double newY = y;
        
        displaceBall(newX, newY, ball);
    }
    
    private void displaceBall(double x, double y, Ball ball) {
        if (ball.getKineticEnergy() <= 0) {
            resetButton.setDisable(false);
            cue.getBall().setDisable(false);
            return;
        }
        
        
        TranslateTransition move = new TranslateTransition(Duration.millis(TIMEFRAME), ball.getBall());
        move.setInterpolator(Interpolator.LINEAR); 
        move.setToX(ball.getPosX() - ball.getBall().getLayoutX() + x);
        move.setToY(ball.getPosY() - ball.getBall().getLayoutY() + y);
        move.setCycleCount(1);

        move.play();

        move.setOnFinished(eh -> {
            ball.setPosX(ball.getBall().getLayoutX() + move.getNode().getTranslateX());
            ball.setPosY(ball.getBall().getLayoutY() + move.getNode().getTranslateY());
            calculateNewMotion(ball);
        });
    }
    
    private double frictionDecay(double displacement, Ball ball) {
        return frictionCoefficient * ball.getBallMass() * GRAVITYA * displacement;
    }
    
    private double axisToRad(double x, double y) {
        double rad = Math.atan2(y, x);
        return rad;
    }
    
    private double pythagorean(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }
    
    private double kineticToSpeed(double kinetic, Ball ball) {
        return Math.sqrt((kinetic * 2) / ball.getBallMass());
    }
    
    private double energyTransferRatio(double rad, double velocityX, double velocityY) {
        double velocityRad = axisToRad(Math.abs(velocityX), Math.abs(velocityY));
        
        double ratio = 1 - (Math.max(velocityRad, rad) - Math.min(velocityRad, rad)) / (Math.PI/2);
        
        return ratio;
    }
    
    private void writePositions() {
        cue.getBall().translateXProperty().addListener(cl->{
            System.out.println("Cue");
            System.out.println(cue.getPosX() + " x, " + cue.getPosY() + " y");
            System.out.println(cue.getVelocityX() + " Vx, " + cue.getVelocityY()+ " Vy");
            System.out.println(cue.getKineticEnergy() + " Kinetic");
            System.out.println("Real Cue");            
            System.out.println(cue.getBall().getTranslateX() + " x, " + cue.getBall().getTranslateY() + " y");

            System.out.println("Object");
            System.out.println(object.getPosX() + " x, " + object.getPosY() + " y");
            System.out.println("Real Object");            
            System.out.println(object.getBall().getTranslateX() + " x, " + object.getBall().getTranslateY() + " y");
            System.out.println("");
        });
    }
}
