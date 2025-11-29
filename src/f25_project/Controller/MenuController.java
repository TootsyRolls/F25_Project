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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Natha
 */
public class MenuController implements Initializable, UI {

    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleStart(ActionEvent event) {
    }

    @FXML
    private void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
    
    
}
