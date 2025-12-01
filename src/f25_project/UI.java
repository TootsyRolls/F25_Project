/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package f25_project;


import f25_project.F25_Project;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 *
 * @author Natha
 */
public interface UI {
    final URL MENU = F25_Project.class.getResource("View/Menu.fxml");
    final URL MAIN = F25_Project.class.getResource("View/MainTable.fxml");
    final URL DATA = F25_Project.class.getResource("View/Menu.fxml");
    
    FXMLLoader menuLoader = new FXMLLoader(MENU);
    FXMLLoader mainLoader = new FXMLLoader(MAIN);
    FXMLLoader dataLoader = new FXMLLoader(DATA);
}
