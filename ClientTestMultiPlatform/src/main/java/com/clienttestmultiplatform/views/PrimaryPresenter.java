package com.clienttestmultiplatform.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.clienttestmultiplatform.ClientTestMultiPlatform;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import message.Message;

public class PrimaryPresenter {

    @FXML
    private View primary;

    @FXML
    private Label label;
    
    @FXML private TextField UsernameTextField, PasswordTextField;
    @FXML private Text errorMessageLabel;
    @FXML private Button loginButton;
    
    private String serverIP;
    static Connection connection;
    static String username = "";
    public Stage stage;
    
    private ClientTestMultiPlatform app;

    public void initialize() {
       /* try{
            String serverIP = "73.69.133.137";
            connection = new Socket(InetAddress.getByName(serverIP),5000);
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
        }
        catch(Exception e){
            errorMessageLabel.setText(e.toString());
        }*/
        serverIP = "73.69.133.137";
        //serverIP = "localhost";
        startRunning();
        
        primary.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    AppBar appBar = MobileApplication.getInstance().getAppBar();
                    appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                            MobileApplication.getInstance().showLayer(ClientTestMultiPlatform.MENU_LAYER)));
                    appBar.setTitleText("Primary");
                    appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e ->
                            System.out.println("Search")));
                }
            }
        });
    }
    
    @FXML
    void buttonClick() {
        label.setText("Hello JavaFX Universe!");
    }
    

    
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
        catch(Exception e){System.out.println("primarypresenter line 108");}
    }
    
    public void setStageValue(Stage stage){
        this.stage = stage;
    }
    
    public void successfullyLoggedIn(String[] grp, int userID, int[] groupIDList){
    /*    Platform.runLater(new Runnable() {
            @Override
            public void run(){
                //ClientTestMultiPlatform platform = (ClientTestMultiPlatform) ClientTestMultiPlatform.getInstance();
                //platform.openChannelList(grp,connection, username,userID, groupIDList);
                
            }
        });*/
        MobileApplication.getInstance().switchView("Secondary View");
    }
    
    public void unsuccessfulLogin(String message){
        errorMessageLabel.setText(message);        
    }
}
