package com.clienttestmultiplatform.views;

import com.clienttestmultiplatform.ClientTestMultiPlatform;
import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PrimaryView {

    private final String name;
    private PrimaryPresenter controller;

    public PrimaryView(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"));
            View view = fxmlLoader.load();
            controller = fxmlLoader.<PrimaryPresenter>getController();
            
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
