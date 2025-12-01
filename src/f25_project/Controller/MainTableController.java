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
    private final double TIMEFRAME = 50; //in Milliseconds
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
        cue.getBall().setTranslateX(cue.getBall().getLayoutX());
        cue.getBall().setTranslateY(cue.getBall().getLayoutY());
        object.getBall().setTranslateX(0);
        object.getBall().setTranslateY(0);
        
        writePositions();
        
        bounceOffWallProperty(cue);
        bounceOffWallProperty(object);
        collide(object);
        
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
        cue.getBall().setTranslateX(100);
        cue.getBall().setTranslateY(200);
        
        object.getBall().setTranslateX(550);
        object.getBall().setTranslateY(200);
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
                frictionCoefficient = Double.parseDouble(massLabel.getText());
            } catch (Exception e) {
                frictionSlider.setValue(0.1);
                frictionCoefficient = 1;
            }
        });
    }
    
    @FXML
    void push(MouseEvent event) {
        double arbitraryAmplifier = 10; //Try to get a coefficient that even out the power of the push
        
        double power = arbitraryAmplifier * pythagorean(event.getX(), event.getY());
        
        
        calculateAppliedPower(power, -event.getX(), -event.getY(), cue);
        resetButton.setDisable(true);
        cue.getBall().setDisable(true);
    }
    
    private void bounceOffWallProperty(Ball ball) {
        ball.getBall().translateXProperty().addListener(cl -> {
            double left = -ball.getBall().getLayoutX() + Ball.BALLRADIUS;
            double right = table.getWidth() - ball.getBall().getLayoutX() - Ball.BALLRADIUS;
            
            if (!ball.isBouncedX() &&
                    (ball.getPosX() < left 
                    || ball.getPosX() > right)) {
                    
                ball.setBouncedX(true);
                ball.setVelocityX(-ball.getVelocityX());
            }
            if (ball.isBouncedX() && (ball.getPosX() > left && ball.getPosX() < right)) {
                ball.setBouncedX(false);
            }
        });
        
        ball.getBall().translateYProperty().addListener(cl -> {
            double top = -ball.getBall().getLayoutY() + Ball.BALLRADIUS;
            double bottom = table.getHeight() - ball.getBall().getLayoutY() - Ball.BALLRADIUS;
            
            if (!ball.isBouncedY() &&
                    (ball.getPosY() < top 
                    || ball.getPosY() > bottom)) {
                
                ball.setBouncedY(true);
                ball.setVelocityY(-ball.getVelocityY());
            }
            if (ball.isBouncedY() && (ball.getPosY() > top && ball.getPosY() < bottom)) {
                ball.setBouncedY(false);
            }
        });
    }
    
    private boolean collide(Ball ball) {
        cue.getBall().translateXProperty().addListener(cl -> {
            double distanceX = (ball.getPosX() + cue.getBall().getLayoutX()) - cue.getPosX();
            double distanceY = (ball.getPosY()) - cue.getPosY();
            double rad = axisToRad(distanceX, distanceY);
            double distance = pythagorean(distanceX, distanceY);
            System.out.println(distance + "Distance\n");
            
            
            if (distance < 2 * Ball.BALLRADIUS) {
                double kEnergyTransfert = cue.getKineticEnergy() * energyTransferRatio(rad, cue.getVelocityX(), cue.getVelocityY());
                System.out.println(kEnergyTransfert + "Object Kinetic");
                
                cue.setKineticEnergy(cue.getKineticEnergy() - kEnergyTransfert);
                System.out.println(cue.getKineticEnergy() + "Cue ball Kinetic");
                calculateAppliedPower(kEnergyTransfert / TIMEFRAMESEC, distanceX, distanceY, ball);
            }
        });
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
        double newX = ball.getPosX() + x;
        double newY = ball.getPosY() + y;
        
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
        move.setToX(x);
        move.setToY(y);
        move.setCycleCount(1);

        move.play();

        move.setOnFinished(eh -> {
            ball.setPosX(move.getNode().getTranslateX());
            ball.setPosY(move.getNode().getTranslateY());
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
        double velocityRad = axisToRad(velocityX, velocityY);
        
        if (velocityRad == Math.atan2(1, 0) || velocityRad == Math.atan2(-1, 0)) {
            return 0;
        }
        
        double ratio = velocityRad / rad;
        System.out.println(ratio + "ratio");
        
        return ratio;
    }
    
    private void writePositions() {
        cue.getBall().translateXProperty().addListener(cl->{
            System.out.println("Cue");
            System.out.println(cue.getPosX() + " x, " + cue.getPosY() + " y");
            System.out.println("Object");
            System.out.println(object.getPosX() + " x, " + object.getPosY() + " y");
            System.out.println("");
        });
    }
}
