/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Amish Naik
 */
public class ClientTest extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Login.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage login = new Stage();
        login.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});
        login.setScene(scene);
        login.setResizable(false);
        login.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
