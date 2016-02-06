/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Color;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import message.Message;

/**
 * FXML Controller class
 *
 * @author Amish Naik
 */
public class LoginController implements Initializable {
    @FXML private TextField UsernameTextField, PasswordTextField;
    @FXML private Text errorMessageLabel;
    @FXML private Button loginButton;
    
    private String serverIP;
    static Connection connection;
    static String username = "";
    public Stage stage;
    
    @FXML
    private void loginButtonClicked(ActionEvent event){
        if(UsernameTextField.getText().equals("")||PasswordTextField.getText().equals("")){
            errorMessageLabel.setText("Please fill out all fields");
        }
        else{
            username = UsernameTextField.getText();
            Message message = new Message();
            message.type="LOGIN";
            message.username=UsernameTextField.getText();
            message.password=PasswordTextField.getText();
            message.fullMessage ="Login" + UsernameTextField.getText() + PasswordTextField.getText();
            Connection.sendMessage(message);
        }
    }   
    
    public void startRunning(){
        try{
            connection = new Connection(new Socket(InetAddress.getByName(serverIP),5000),this);
            connection.setUpThread();
        }
        catch(Exception e){}
    }
    
    public void setStageValue(Stage stage){
        this.stage = stage;
    }
    
    public void successfullyLoggedIn(String[] grp) throws Exception{
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ChannelList.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                ChannelListController controller = fxmlLoader.<ChannelListController>getController();
                connection.setChannelListController(controller);
                controller.startRunning(grp,connection, username);
                Scene groupList = new Scene(root);

                Stage groupListWindow = new Stage();
                groupListWindow.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});
                groupListWindow.setScene(groupList);
                groupListWindow.minWidthProperty().set(330.0);
                groupListWindow.minHeightProperty().set(440.0);
                groupListWindow.maxWidthProperty().set(330.0);
                groupListWindow.maxHeightProperty().set(440.0);
                //groupListWindow.setResizable(false);
                groupListWindow.show();
                
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();
                }
                catch(Exception e){System.out.println(e.toString());}
            }
        });
        
        

    }
    
    public void unsuccessfulLogin(String message){
        errorMessageLabel.setText(message);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        serverIP = JOptionPane.showInputDialog(this,"Enter IP you wish to connect to");
        startRunning();
    } 
}
