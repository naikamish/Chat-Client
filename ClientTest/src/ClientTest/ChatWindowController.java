/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    @FXML private Button doodleButton;
    private String username, groupName;
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
        chatBox.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    chatBox.layout();
                    chatBoxScrollPane.layout();
                    chatBoxScrollPane.setVvalue(1.0f);
                }));
        Image attachImage = new Image(ClientTest.class.getResourceAsStream("images/attach.png"));
        attachIcon.setImage(attachImage);
        
        Image doodleImage = new Image(ClientTest.class.getResourceAsStream("images/doodle.png"));
        doodleIcon.setImage(doodleImage);
        chatBox.getChildren().clear();
    }  
    
    public void setValues(Connection connection, int groupID, String user, int userID, String groupName){
        this.connection = connection;
        clientName = user;
        this.groupID = groupID;
        this.userID = userID;
        this.groupName = groupName;
        System.out.println(groupName+"hello");
    }
    
    @FXML
    private void enterPressed(ActionEvent event) {
        TextField source = (TextField)event.getSource();
        Message message = new Message("MSG", "SEND", groupID, clientName, source.getText());
        message.groupName = getGroupName();
        message.userID = this.userID;
        sendMessage(message);//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
        source.clear();
    }
    
    public String getGroupName(){
        return groupName;
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

                text.getChildren().add(activeUser);
                
                if(message.cmd.equals("SEND")){
                Text messageText=new Text(message.message);
                messageText.getStyleClass().add("messageText");
                text.getChildren().add(messageText);
                }
                
                else if(message.cmd.equals("FILE")||message.cmd.equals("DOODLE")){
                    try{
                        saveFile(message);
                        String ext = message.extension.substring(message.extension.indexOf('.')+1).toLowerCase();
                        if(ext.equals("png")||ext.equals("jpg")||ext.equals("bmp")||ext.equals("ico")||ext.equals("jpeg")||ext.equals("gif")) {
                            WritableImage writableImage;
                            writableImage = showImage(message.file);
                            //writableImage = new WritableImage(writableImage.getPixelReader(), 0,0,(int) writableImage.getWidth(),500);
                            Rectangle2D r = new Rectangle2D(0,0,400,400);
                            ImageView imageView = new ImageView(writableImage);
                            imageView.setFitWidth(450);
                            imageView.setSmooth(true);
                            imageView.setPreserveRatio(true);
                            imageView.getStyleClass().add("imageView");
                            text.getChildren().add(imageView);

                            if(message.cmd.equals("DOODLE")){
                                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent t) {
                                        showDoodle(writableImage);
                                    }
                                });
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, message.filename + " has been saved to your device");
                            return;
                        }
                    }
                    catch(Exception e){                        
                    }                    
                }
                chatBox.getChildren().add(text); 
            }
        });
    } 
    
    private void saveFile(Message message){
        byte [] bytearray  = message.file;
        try{
            FileOutputStream fos = new FileOutputStream(message.filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(bytearray, 0 , bytearray.length);
            bos.flush();
            bos.close();
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(this,e.toString());
        }
    }
    
    private WritableImage showImage(byte[] byteArray) throws IOException{
        BufferedImage bf = ImageIO.read(new ByteArrayInputStream(byteArray));

        WritableImage wr = null;
        wr = new WritableImage(bf.getWidth(), bf.getHeight());
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < bf.getWidth(); x++) {
            for (int y = 0; y < bf.getHeight(); y++) {
                pw.setArgb(x, y, bf.getRGB(x, y));
            }
        }
        
        return wr;
    }
    
    private void addUser(String username, int id, byte[] profileImage){
        HBox hbox = new HBox();
        ImageView icon = new ImageView();
        if(profileImage.length>0){
            try{
                ByteArrayInputStream bais = new ByteArrayInputStream(profileImage);
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
        
        //ImageView icon = new ImageView(new Image(ClientTest.class.getResourceAsStream("images/anon.jpg")));
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
    
    public void sendDoodle(Canvas canvas){
        try{
            WritableImage image = canvas.snapshot(null, null);
            ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();
            ImageIO.write( SwingFXUtils.fromFXImage( image, null ), "png", byteOutput );
            byte[] bytearray = byteOutput.toByteArray();
            sendMessage(new Message("MSG", "DOODLE", groupID, clientName, bytearray, "png"));
        }
        catch(Exception e){}
    }
    
    public void closeWindow(){      
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                showSpecialMessage("YOU HAVE BEEN BANNED FROM THIS CHAT");
                Message message = new Message("CMD", "EXIT", getGroupID(), clientName);
                message.userID = userID;
                sendMessage(message);
                chatTextHBox.setDisable(true);
            }
        });
    }
    
    public void startRunning(){
        sendMessage(new Message("CMD", "JOIN", groupID, clientName));//CMD JOIN " + groupName+","+clientName);
    }
    
    public void deleteFromList(int id, String name){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(Node node:usersBox.getChildren()){
                    if(node.getId().equals(Integer.toString(id))){
                        usersBox.getChildren().remove(node);
                        showSpecialMessage(name+" has left the chat");
                    }
                }
            }
        });
    }
    
    public void setGroupList(String[] list, int[] idList, int creatorID, byte[][] profileImages){
        this.creatorID = creatorID;
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<list.length; i++){
                    addUser(list[i],idList[i], profileImages[i]);
                }
            }
        });
    } 
    
    public void addGroupMember(String[] list, int[] idList, byte[] profileImage){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<list.length; i++){
                    addUser(list[i],idList[i], profileImage);
                    showSpecialMessage(list[i]+" has joined the chat");
                }
            }
        });
    } 
    
    public void showSpecialMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                TextFlow text = new TextFlow();
                text.getStyleClass().add("message");

                Text activeUser=new Text(message);
                activeUser.getStyleClass().add("activeUser");
                text.getChildren().addAll(activeUser);

                chatBox.getChildren().add(text);
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
        showDoodle(null);
    }
    
    private void showDoodle(WritableImage writableImage){
        try{ 
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Doodle.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            DoodleController controller = fxmlLoader.<DoodleController>getController();
            Scene doodleScene = new Scene(root);
            controller.setChatWindow(getChatWindow());
            if(writableImage!=null){
                controller.setDoodle(writableImage);
            }
            Stage doodleWindow = new Stage();
            doodleWindow.setScene(doodleScene);
            doodleWindow.show();
           }
        catch(Exception e){e.printStackTrace();}
    }
    
    private ChatWindowController getChatWindow(){
        return this;
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
