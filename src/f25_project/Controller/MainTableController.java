/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package f25_project.Controller;

import f25_project.UI;
import java.net.URL;
import java.util.ResourceBundle;
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

/**
 * FXML Controller class
 *
 * @author Natha
 */
public class MainTableController implements Initializable, UI {
    
    private final int BALLRADIUS = 20;
    private final double GRAVITYA = 9.8;
    private final double TIMEFRAME = 50; //in Milliseconds
    
    private double ballMass = 1;
    private double frictionCoefficient = 0.1;

    @FXML
    private Circle cueBall;
    
    @FXML
    private Circle objectBall;
    
    @FXML
    private Button exitButton;
    
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
        cueBall.setRadius(BALLRADIUS);
        objectBall.setRadius(BALLRADIUS);
        
        massLabel.setText("1.00");
        
        massSlider.valueProperty().addListener(cl -> {
            massLabel.setText(String.format("%.2f",massSlider.getValue()));
        });
        
        massLabel.textProperty().addListener(cl -> {
            try {
                ballMass = Double.parseDouble(massLabel.getText());
            } catch (Exception e) {
                massSlider.setValue(1);
                ballMass = 1;
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
        System.out.println(event.getX());
    }
    
    @FXML
    void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
    @FXML
    void push(MouseEvent event) {

    }
}
