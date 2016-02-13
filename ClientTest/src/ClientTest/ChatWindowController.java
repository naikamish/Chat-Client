/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import message.DoodlePath;
import message.Message;

/**
 *
 * @author Amish Naik
 */
public class ChatWindowController implements Initializable {
    
    @FXML private VBox chatBox, usersBox;
    @FXML private ScrollPane chatBoxScrollPane;
    @FXML private ImageView attachIcon, doodleIcon;
    @FXML private HBox chatTextHBox;
    private String username="hello";
        private JTextField userText;
    private JTextPane chatWindow;
    private String message = ""; 
    private Connection connection;
    public String clientName;
   // public String groupName;
    private int userID, groupID, creatorID;
    private DefaultListModel listModel = new DefaultListModel();
    
    private JList<String> clientList;
    private JScrollPane clientListPane = new JScrollPane();
    
    private String fullText = "";
    private Scene scene;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DoubleProperty hProperty = new SimpleDoubleProperty();
        hProperty.bind(chatBox.heightProperty());
        hProperty.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
              chatBoxScrollPane.setVvalue(chatBoxScrollPane.getVmax());
            }
        });
        Image attachImage = new Image(ClientTest.class.getResourceAsStream("images/attach.png"));
        attachIcon.setImage(attachImage);
        
        Image doodleImage = new Image(ClientTest.class.getResourceAsStream("images/doodle.png"));
        doodleIcon.setImage(doodleImage);
    }  
    
    public void setValues(Connection connection, int groupID, String user, int userID){
        this.connection = connection;
        clientName = user;
        this.groupID = groupID;
        this.userID = userID;
    }
    
    @FXML
    private void enterPressed(ActionEvent event) {
        TextField source = (TextField)event.getSource();
        sendMessage(new Message("MSG", "SEND", groupID, clientName, source.getText()));//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
        source.clear();
        
        //createMessage(source.getText());
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
    
    private void addUser(String username, int id){
        HBox hbox = new HBox();
        ImageView icon = new ImageView(new Image(ClientTest.class.getResourceAsStream("images/anon.jpg")));
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
            ImageView banIcon = new ImageView(new Image(ClientTest.class.getResourceAsStream("images/ban.png")));
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
    
        public ChatWindowController getThis(){
        return this;
    }
    
    public void sendDoodle(LinkedList<DoodlePath> generalPath){
        sendMessage(new Message("MSG", "DOODLE", groupID, clientName,generalPath));
    }
    
    public void showDoodle(Message message){
        if(!clientName.equals(message.clientName)){
            DoodleFrame doodleFrame;
            doodleFrame = new DoodleFrame(this);
            doodleFrame.setDoodle(message.doodle);
        }
    }
    
    public void showFile(Message message){
        if(!clientName.equals(message.clientName)){
            /*JButton l=new JButton(message.message);
            chatWindow.insertComponent(l);*/
            String dialogMessage = message.clientName+" has sent you a file.\n"+message.filename+"\nWould you like to download this file?";
            String dialogTitle = "Download File";
            int reply = JOptionPane.showConfirmDialog(null, dialogMessage, dialogTitle, JOptionPane.YES_NO_OPTION);
            if(reply==JOptionPane.YES_OPTION){
                byte [] bytearray  = message.file;
                try{
                    FileOutputStream fos = new FileOutputStream(message.filename);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(bytearray, 0 , bytearray.length);
                    bos.flush();
                    bos.close();
                    JOptionPane.showMessageDialog(null,"Download Complete");
                }
                catch(Exception e)
                {
                    //JOptionPane.showMessageDialog(this,e.toString());
                }
            }
        }
    }
    
    public void closeWindow(){
        
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                TextFlow text = new TextFlow();
                text.getStyleClass().add("message");

                Text activeUser=new Text("YOU HAVE BEEN BANNED FROM THIS CHAT");
                activeUser.getStyleClass().add("activeUser");
                text.getChildren().addAll(activeUser);

                chatBox.getChildren().add(text);
                Message message = new Message("CMD", "EXIT", getGroupID(), clientName);
                message.userID = userID;
                sendMessage(message);
                chatTextHBox.setDisable(true);
            }
        });
        System.out.println("hello");

    }
    
    public void startRunning(){
        sendMessage(new Message("CMD", "JOIN", groupID, clientName));//CMD JOIN " + groupName+","+clientName);
    }
    
    public void deleteFromList(int id){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(Node node:usersBox.getChildren()){
                    if(node.getId().equals(Integer.toString(id))){
                        usersBox.getChildren().remove(node);
                    }
                }
            }
        });
    }
    
    public void setGroupList(String[] list, int[] idList, int creatorID){
        this.creatorID = creatorID;
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<list.length; i++){
                    addUser(list[i],idList[i]);
                }
                /*for(String s : list){
                    addUser(s);
                }*/
            }
        });
    } 
    
    @FXML
    private void changeCursorHand(MouseEvent event) {
        scene.setCursor(Cursor.HAND);
    }
    
    @FXML
    private void changeCursorPointer(MouseEvent event) {
        scene.setCursor(Cursor.DEFAULT);
    }
    
    public void setScene(Scene scene){
        this.scene = scene;
    }
    
    @FXML
    private void openDoodleFrame(ActionEvent event) {
        DoodleFrame doodleFrame;
        doodleFrame = new DoodleFrame(this);
    }
    
    @FXML
    private void openFileDialog(ActionEvent event) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
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
        }
    }
}
