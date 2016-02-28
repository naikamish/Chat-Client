package com.clienttestmobile.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class SecondaryView {

    private final String name;
    private SecondaryPresenter controller;

    public SecondaryView(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            View view = fxmlLoader.load();
            controller = fxmlLoader.<SecondaryPresenter>getController();
            
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
    
    public void setValues(String[] grp, Connection conn, String username, int userID, int[] groupIDList){
        controller.setValues(grp, conn, username, userID, groupIDList);
        conn.setChannelListController(controller);
    }
}
