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
import javafx.scene.text.TextFlow;
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
    
    private String username="hello";
        private JTextField userText;
    private JTextPane chatWindow;
    private String message = ""; 
    private Connection connection;
    public String clientName;
    public String groupName;
    private DefaultListModel listModel = new DefaultListModel();
    
    private JList<String> clientList;
    private JScrollPane clientListPane = new JScrollPane();
    private JButton  uploadFileButton;
    private JPanel optionsPanel;
    
    private String fullText = "";
    private HTMLEditorKit kit;
    private HTMLDocument doc;
    private StyleSheet styleSheet;
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
    
    public void setValues(Connection connection, String groupName, String user){
        this.connection = connection;
        clientName = user;
        this.groupName = groupName;
        sendMessage(new Message("CMD", "JOIN", groupName, clientName));
    }
    
    @FXML
    private void enterPressed(ActionEvent event) {
        TextField source = (TextField)event.getSource();
        sendMessage(new Message("MSG", "SEND", groupName, clientName, source.getText()));//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
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
    
    private void addUser(String username){
        HBox hbox = new HBox();
        ImageView icon = new ImageView(new Image(ClientTest.class.getResourceAsStream("images/anon.jpg")));
        icon.fitHeightProperty().set(55.0);
        icon.fitWidthProperty().set(53.0);
        Text text = new Text(username);
        hbox.getChildren().addAll(icon,text);
        hbox.setId(username);
        usersBox.getChildren().add(hbox);
    }
    
    public String getGroup(){
        return groupName;
    }
    
     public void sendMessage(Message message){
        connection.sendMessage(message);
    }
    
        public ChatWindowController getThis(){
        return this;
    }
    
    public void sendDoodle(LinkedList<DoodlePath> generalPath){
        sendMessage(new Message("MSG", "DOODLE", groupName, clientName,generalPath));
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
        sendMessage(new Message("CMD", "EXIT", groupName, clientName));
        ChannelListController.removeFromGroup(this);
    }
    
    public void startRunning(){
        sendMessage(new Message("CMD", "JOIN", groupName, clientName));//CMD JOIN " + groupName+","+clientName);
    }
    
    public void deleteFromList(String user){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(Node node:usersBox.getChildren()){
                    if(node.getId().equals(user)){
                        usersBox.getChildren().remove(node);
                    }
                }
            }
        });
    }
    
    public void setGroupList(String[] list){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(String s : list){
                    addUser(s);
                }
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
                sendMessage(new Message("MSG", "FILE", groupName, clientName, bytearray, extension));
            }
            catch(Exception ex){}
        }
    }
}
