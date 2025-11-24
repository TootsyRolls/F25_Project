/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package f25_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author 2376190
 */
public class F25_Project extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage menu = new Stage();
        Stage data = new Stage();
        
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("view/Menu.fxml"));
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("view/MainTable.fxml"));
        FXMLLoader dataLoader = new FXMLLoader(getClass().getResource("view/Data.fxml"));
        
        stage.setScene(new Scene(mainLoader.load()));
        stage.setTitle("Playground");
        stage.show();
        
        menu.setTitle("Menu");
        menu.setScene(new Scene(menuLoader.load()));
        menu.show();
        
        data.setTitle("Data Table");
        data.setScene(new Scene(dataLoader.load()));
        data.show();
    }
}
