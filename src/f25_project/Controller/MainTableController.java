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
        cue = new Ball(cueBall, cueBall.getCenterX(), cueBall.getCenterY());
        object = new Ball(objectBall, objectBall.getCenterX(), objectBall.getCenterY());
        
        bounceOffWallProperty(cue);
//        bounceOffWallProperty(object);
        
        massLabel.setText("1.00");
        
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
        
        
        frictionLabel.setText("0.10");
        
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
    void aim(MouseEvent event) {
    }
    
    @FXML
    void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
    @FXML
    void handleReset(ActionEvent event) {
        cue.setPosX(0);
        cue.setPosY(0);
        cue.getBall().setTranslateX(0);
        cue.getBall().setTranslateY(0);
        
        object.getBall().setTranslateX(0);
        object.getBall().setTranslateY(0);
        object.setPosX(0);
        object.setPosY(0);
    }
    
    @FXML
    void push(MouseEvent event) {
        System.out.println(event.getX());
        
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
    
    private void calculateAppliedPower(double power, double x, double y, Ball ball) {
        double rad = axisToRad(x, y);
        double kEnergy = power * TIMEFRAMESEC;
        double speed = kineticToSpeed(kEnergy, ball);
        double velocityX = speed * Math.cos(rad);
        double velocityY = speed * Math.sin(rad);
        
        ball.setVelocityX(velocityX);
        ball.setVelocityY(velocityY);
        ball.setKineticEnergy(kEnergy);
        
        System.out.println(rad + "pi rad");
        System.out.println(kEnergy + " Kinetic");
        System.out.println(speed + " speed");
        System.out.println(velocityX + " x speed");
        System.out.println(velocityY + " y speed");
        
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
        move.setFromX(ball.getPosX());
        move.setFromY(ball.getPosY());
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
}
