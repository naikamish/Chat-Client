/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clienttestmobile.views;

/**
 *
 * @author Amish Naik
 */

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.clienttestmobile.ClientTestMobile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import message.Message;

public class ChatWindowController {
    @FXML private ImageView attachIcon, doodleIcon;
    
    @FXML private VBox chatBox, usersBox;
    
    private int creatorID, userID, groupID;
    public String clientName;
    private Connection connection;
    
    public void initialize() {
        Image attachImage = new Image(ClientTestMobile.class.getResourceAsStream("/attach.png"));
        attachIcon.setImage(attachImage);
        
        Image doodleImage = new Image(ClientTestMobile.class.getResourceAsStream("/doodle.png"));
        doodleIcon.setImage(doodleImage);
    }   
    
    @FXML
    private void enterPressed(ActionEvent event) {
        TextField source = (TextField)event.getSource();
        sendMessage(new Message("MSG", "SEND", groupID, clientName, source.getText()));//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
        source.clear();
    }
       
    @FXML
    private void changeCursorHand(MouseEvent event) {
        
    }
    
    @FXML
    private void changeCursorPointer(MouseEvent event) {
        
    }
    
    @FXML
    private void openDoodleFrame(ActionEvent event) {

    }
    
    @FXML
    private void openFileDialog(ActionEvent event) {
        ClientTestMobile platform = (ClientTestMobile) ClientTestMobile.getInstance();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(platform.getStage());
                
       // JFileChooser fc = new JFileChooser();
       // int returnVal = fc.showOpenDialog(null);
       // if (returnVal == JFileChooser.APPROVE_OPTION) {
           // File file = fc.getSelectedFile();
            byte [] bytearray  = new byte [(int)file.length()];
            String extension = "";

            int i = file.getPath().lastIndexOf('.');
            int p = Math.max(file.getPath().lastIndexOf('/'), file.getPath().lastIndexOf('\\'));

            if (i > p) {
                extension = file.getPath().substring(i+1);
            }
            try{
                FileInputStream fin = new FileInputStream(file);
                BufferedInputStream bin = new BufferedInputStream(fin);
                bin.read(bytearray,0,bytearray.length);
                sendMessage(new Message("MSG", "FILE", groupID, clientName, bytearray, extension));
            }
            catch(Exception ex){}
       // }
    }

    void setGroupList(String[] clientList, int[] groupUserIDs, int creatorID) {
        this.creatorID = creatorID;
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<clientList.length; i++){
                    addUser(clientList[i],groupUserIDs[i]);
                }
            }
        });
    }

    void setValues(Connection connection, int groupID, String username, int userID) {
        this.connection = connection;
        clientName = username;
        this.groupID = groupID;
        this.userID = userID;
    }
    
    public void deleteFromList(int id){
        for(Node node:usersBox.getChildren()){
            if(node.getId().equals(Integer.toString(id))){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run(){
                        usersBox.getChildren().remove(node);
                    }
                });
            }
        }
    }
    
    private void addUser(String username, int id){
        HBox hbox = new HBox();
        ImageView icon = new ImageView(new Image(ClientTestMobile.class.getResourceAsStream("/anon.jpg")));
        icon.fitHeightProperty().set(55.0);
        icon.fitWidthProperty().set(53.0);
        
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add("chatListText");
        textFlow.setPrefHeight(55.0);
        textFlow.setPrefWidth(225.0);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        
        Text text = new Text(username);
        text.getStyleClass().add("chatListTextObject");
        textFlow.getChildren().add(text);
        VBox vbox = new VBox();
        
        if(this.userID==this.creatorID){
            ImageView banIcon = new ImageView(new Image(ClientTestMobile.class.getResourceAsStream("/ban.png")));
            banIcon.setId(Integer.toString(id));
            banIcon.setOnMouseClicked(new EventHandler<MouseEvent>(){
 
                @Override
                public void handle(MouseEvent e) {
                    Message message = new Message();
                    message.type="BAN";
                    message.userID=Integer.parseInt(banIcon.getId());
                    message.groupID=getGroupID();
                    sendMessage(message);
                }

            });
            
            
            banIcon.fitHeightProperty().set(30.0);
            banIcon.fitWidthProperty().set(30.0);
            vbox.getChildren().addAll(banIcon);
        }
        
        hbox.getChildren().addAll(icon,textFlow,vbox);
        hbox.setId(Integer.toString(id));
        usersBox.getChildren().add(hbox);
    }
    
    public int getGroupID(){
        return groupID;
    }
    
    public void sendMessage(Message message){
        connection.sendMessage(message);
    }
    
    public void showMessage(Message message){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String clientInfo = message.clientName+" ("+dateFormat.format(date)+"):\n";

                TextFlow text = new TextFlow();
                text.getStyleClass().add("message");

                Text activeUser=new Text(clientInfo);
                String styleType = message.clientName.equals(clientName) ? "activeUser":"passiveUser";
                activeUser.getStyleClass().add(styleType);

                Text messageText=new Text(message.message);
                messageText.getStyleClass().add("messageText");

                text.getChildren().addAll(activeUser, messageText);

                chatBox.getChildren().add(text);
            }
        });
    }
}
