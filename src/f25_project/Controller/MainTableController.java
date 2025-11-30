/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package f25_project.Controller;

import f25_project.UI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Natha
 */
public class MainTableController implements Initializable, UI {

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
        

    }
    
}
