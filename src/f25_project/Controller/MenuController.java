/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package f25_project.Controller;

import f25_project.UI;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    }    

    /**
     * starts the program by switching the scene to a Scene object with the MainTable UI
     * @param event the event when the user activates the Start button
     * @throws IOException 
     */
    @FXML
    private void handleStart(ActionEvent event) throws IOException {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setScene(toMainTable());
    }

    /**
     * closes the program
     * @param event the event when the user activates the Exit button
     */
    @FXML
    private void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }
    
    /**
     * loads the MainTable .fxml file into a scene
     * @return the Scene object containing the MainTable UI
     * @throws IOException an IOException indicating the presence of the file
     */
    private Scene toMainTable() throws IOException {
        Parent mainTable = mainLoader.load();
        return new Scene(mainTable);
    }
}
