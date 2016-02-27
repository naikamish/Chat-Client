package com.clienttestmobile.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.clienttestmobile.ClientTestMobile;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import message.Message;

public class PrimaryPresenter {

    @FXML
    private View primary;

    @FXML
    private Label label;
    
    @FXML private TextField UsernameTextField, PasswordTextField;
    @FXML private Text errorMessageLabel;
    @FXML private Button loginButton;
    @FXML private ImageView chatIcon, doodleIcon, fileIcon;
    
    private String serverIP;
    static Connection connection;
    static String username = "";
    public Stage stage;

    public void initialize() {
        serverIP = "73.69.133.137";
        //serverIP = "localhost";
        startRunning();
        
        Image doodleImage = new Image(ClientTestMobile.class.getResourceAsStream("/doodle.png"));
        Image chatImage = new Image(ClientTestMobile.class.getResourceAsStream("/chat.png"));
        Image fileImage = new Image(ClientTestMobile.class.getResourceAsStream("/attach.png"));
        
        doodleIcon.setImage(doodleImage);
        chatIcon.setImage(chatImage);
        fileIcon.setImage(fileImage);
        
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(ClientTestMobile.MENU_LAYER)));
                appBar.setTitleText("Primary");
                appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> 
                        System.out.println("Search")));
            }
        });
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
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                //MobileApplication.getInstance().switchView("Secondary View");
                ClientTestMobile platform = (ClientTestMobile) ClientTestMobile.getInstance();
                platform.openChannelList(grp,connection, username,userID, groupIDList);
                
            }
        });
        //MobileApplication.getInstance().switchView("Secondary View");
    }
    
    public void unsuccessfulLogin(String message){
        errorMessageLabel.setText(message);        
    }
}
