/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import message.Message;

/**
 * FXML Controller class
 *
 * @author Amish Naik
 */
public class ChannelListController implements Initializable {

    @FXML private VBox chatListBox;
    
    private static ArrayList<ChatWindowController> chats = new ArrayList<ChatWindowController>();
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP;
    private String username;
    private String groupName;
    private String[] data;
    private static DefaultListModel listModel = new DefaultListModel();
    private static Notification notifications;
    private int userID;
    
    Connection connection;
    JButton connectButton, createGroupButton;
    private static JScrollPane groupsListPane = new JScrollPane();
    JList groupsList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void addGroup(String group, int groupID, byte[] groupImage){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                HBox hbox = new HBox();
                hbox.getStyleClass().add("chatListHBox");
                hbox.setId(Integer.toString(groupID));

                ImageView icon = new ImageView();
                
                if(groupImage.length>0){
                    try{
                        ByteArrayInputStream bais = new ByteArrayInputStream(groupImage);
                        BufferedImage bf = ImageIO.read(bais);

                        WritableImage wr = null;
                        if (bf != null) {
                            wr = new WritableImage(bf.getWidth(), bf.getHeight());
                            PixelWriter pw = wr.getPixelWriter();
                            for (int x = 0; x < bf.getWidth(); x++) {
                                for (int y = 0; y < bf.getHeight(); y++) {
                                    pw.setArgb(x, y, bf.getRGB(x, y));
                                }
                            }
                        }

                        icon = new ImageView(wr);
                    }
                    catch(Exception e){
                        
                    }
                }
                else{
                    icon = new ImageView(new Image(ClientTest.class.getResourceAsStream("images/anon.jpg")));
                }
                icon.fitHeightProperty().set(63.0);
                icon.fitWidthProperty().set(62.0);

                TextFlow textFlow = new TextFlow();
                textFlow.getStyleClass().add("chatListText");
                textFlow.setPrefHeight(63.0);
                textFlow.setPrefWidth(312.0);
                textFlow.setTextAlignment(TextAlignment.CENTER);

                Text text = new Text(group);
                text.getStyleClass().add("chatListTextObject");
                textFlow.getChildren().add(text);
                hbox.getChildren().addAll(icon,textFlow);
                hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent t) {
                        hbox.getStyleClass().add("active");
                    }
                });
                chatListBox.getChildren().add(hbox);
            }
        });
    }
    
    @FXML
    private void chatListClick(MouseEvent event) {
        for(Node node:chatListBox.getChildren()){
            node.getStyleClass().remove("active");
            if(node.isHover()){
                //node.getStyleClass().add("active");
                node.getStyleClass().add("active");
            }
        }
    }
    
    @FXML
    private void connectButtonClick(ActionEvent event) throws Exception{
        Message message = new Message("CMD", "JOIN", Integer.parseInt(getSelectedGroup()), username);
        message.userID = this.userID;
        sendMessage(message);
        

        
    }
    
    public void sendMessage(Message message){
        connection.sendMessage(message);
    }
    
    private String getSelectedGroup(){
        String selectedGroup="";
        for(Node node:chatListBox.getChildren()){
            if(node.getStyleClass().contains("active")){
                selectedGroup=node.getId();
            }
        }
        return selectedGroup;
    }
    
    
    public void startRunning(String[] groupList, Connection connection, String username, int userID, int[] groupIDList, byte[][] groupImages){
        try{
            //JOptionPane.showConfirmDialog(null,groupList[1]);
            this.username = username;
            this.connection = connection;
            this.userID= userID;
            for(int i=0; i<groupList.length;i++){
                addGroup(groupList[i],groupIDList[i], groupImages[i]);
            }
            /*for(String s : groupList){
                addGroup(s);
            }*/
        }
        catch(Exception e){}
    }
    
    public void sendGroupList(int g, String[] list, int[] idList, int creatorID, String groupName,byte[][] profileImages){
       /* for(ChatWindowController chat:chats){
            if(chat.getGroupID()==g){
                chat.setGroupList(list, idList, creatorID);
            }
        }*/
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                try{ FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ChatWindow.fxml"));
                    Parent root = (Parent)fxmlLoader.load();
                    ChatWindowController controller = fxmlLoader.<ChatWindowController>getController();
                   // String selectedGroup = getSelectedGroup();
                    controller.setValues(connection, g, username, userID, groupName);
                    chats.add(controller);
                    Scene chatWindowScene = new Scene(root);
                    controller.setScene(chatWindowScene);
                    Stage chatWindow = new Stage();
                    chatWindow.setScene(chatWindowScene);
                    chatWindow.minWidthProperty().set(800.0);
                    chatWindow.minHeightProperty().set(600.0);
                    chatWindow.show();
                    
                    controller.setGroupList(list, idList, creatorID, profileImages);
                    
                    chatWindow.setOnCloseRequest(e -> {
                        Message message = new Message("CMD", "EXIT", controller.getGroupID(), controller.clientName);
                        message.userID = userID;
                        controller.sendMessage(message);
                        removeFromGroup(controller);
                    });
                   }
                catch(Exception e){System.out.println(e.toString());}
            }
        });
            
    }
    
    public void addGroupMember(int g, String[] list, int[] idList, byte[] profileImage){
        for(ChatWindowController chat:chats){
            if(chat.getGroupID()==g){
                chat.addGroupMember(list, idList, profileImage);
            }
        }
    }
    
    public static void showMessage(Message message){
        for(ChatWindowController chat:chats){
            if(chat.getGroupID()==message.groupID){
                chat.showMessage(message);
            }
        }
    }
    
    public static void removeFromGroup(ChatWindowController c){
        chats.remove(c);
    }
    
    public static void deleteFromList(int g, int id, String name){
        for(ChatWindowController chat:chats){
            if(chat.getGroupID()==g){
                chat.deleteFromList(id, name);
            }
        }
    }   
    
    @FXML
    private void newGroupButton(ActionEvent event) {
        String newGroup = JOptionPane.showInputDialog("What is the name of the group you wish to create?");
        Message message = new Message("CMD","CREATE",newGroup);
        message.userID = this.userID;
        message.file = new byte[0];
        connection.sendMessage(message);
    }
    
    public void enforceBan(int userID, int groupID){
        if(this.userID==userID){
            for(ChatWindowController chat:chats){
                if(chat.getGroupID()==groupID){
                    chat.closeWindow();
                }
            }
        }
    }
}
