package com.clienttestmobile.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.clienttestmobile.ClientTestMobile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javax.swing.JOptionPane;

public class SecondaryPresenter {

    @FXML
    private View secondary;
    
    @FXML
    private VBox chatListBox;
    
    private String username = "";
    private Connection connection;
    private int userID;

    public void initialize() {
        secondary.setShowTransitionFactory(BounceInRightTransition::new);
        
        secondary.getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text, 
            e -> System.out.println("Info")));
        
        secondary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(ClientTestMobile.MENU_LAYER)));
                appBar.setTitleText("Secondary");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> 
                        System.out.println("Favorite")));
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

        

        
    }
    
    @FXML
    private void loginButtonClicked(ActionEvent event) throws Exception{

        

        
    }
    
    @FXML
    private void newGroupButton(ActionEvent event) {

    }
    
    public void setValues(String[] grp, Connection conn, String username, int userID, int[] groupIDList){
        try{
            //JOptionPane.showConfirmDialog(null,groupList[1]);
            this.username = username;
            this.connection = conn;
            this.userID= userID;
            for(int i=0; i<grp.length;i++){
                addGroup(grp[i],groupIDList[i]);
            }
        }
        catch(Exception e){System.out.println("secondarypresenter line 272");}
    }
    
    private void addGroup(String group, int groupID){
        HBox hbox = new HBox();
        hbox.getStyleClass().add("chatListHBox");
        hbox.setId(Integer.toString(groupID));

        ImageView icon = new ImageView(new Image(ClientTestMobile.class.getResourceAsStream("/anon.jpg")));
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
                //connectButton.setText(hbox.getId());
                hbox.getStyleClass().add("active");
            }
        });
        chatListBox.getChildren().add(hbox);
    }
}
