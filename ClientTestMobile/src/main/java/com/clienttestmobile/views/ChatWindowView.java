/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clienttestmobile.views;


import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
/**
 *
 * @author Amish Naik
 */
public class ChatWindowView {
    private final String name;
    private ChatWindowController controller;
    private View view;
    
    public ChatWindowView(String name){
        this.name=name;
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chatWindow.fxml"));
            view = fxmlLoader.load();
            controller = fxmlLoader.<ChatWindowController>getController();
            
            
        } catch (IOException e) {
        }
    }
    
    public View getView() {
        view.setName(name);
        return view;
    }
    
    public ChatWindowController getController(){
        return controller;
    }
}
