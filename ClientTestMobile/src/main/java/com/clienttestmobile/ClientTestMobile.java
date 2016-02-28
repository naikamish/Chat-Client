package com.clienttestmobile;

import com.clienttestmobile.views.ChatWindowView;
import com.clienttestmobile.views.Connection;
import com.clienttestmobile.views.PrimaryView;
import com.clienttestmobile.views.SecondaryView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientTestMobile extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String CHAT_WINDOW_VIEW = "Chat Window View";
    public static final String MENU_LAYER = "Side Menu";
    private static PrimaryView primaryView;
    private static SecondaryView secondaryView;
    private static ChatWindowView chatWindowView;
    private static Scene scene;
    
    @Override
    public void init() {
        primaryView = new PrimaryView(PRIMARY_VIEW);
        secondaryView = new SecondaryView(SECONDARY_VIEW);
        chatWindowView = new ChatWindowView(CHAT_WINDOW_VIEW);
        addViewFactory(PRIMARY_VIEW, () -> primaryView.getView());
        addViewFactory(SECONDARY_VIEW, () -> secondaryView.getView());
        addViewFactory(CHAT_WINDOW_VIEW, () -> chatWindowView.getView());
        
        NavigationDrawer drawer = new NavigationDrawer();
        
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(ClientTestMobile.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);
        
        final Item primaryItem = new Item("Primary", MaterialDesignIcon.HOME.graphic());
        final Item secondaryItem = new Item("Secondary", MaterialDesignIcon.DASHBOARD.graphic());
        drawer.getItems().addAll(primaryItem, secondaryItem);
        
        drawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            switchView(newItem.equals(primaryItem) ? PRIMARY_VIEW : SECONDARY_VIEW);
        });
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
    }

    @Override
    public void postInit(Scene scene) {
        this.scene=scene;
        Swatch.getRandom().assignTo(scene);

        scene.getStylesheets().add(ClientTestMobile.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(ClientTestMobile.class.getResourceAsStream("/icon.png")));
    }
    
    public void openChannelList(String[] grp, Connection conn, String username, int userID, int[] groupIDList){
        switchView(SECONDARY_VIEW);
        secondaryView.setValues(grp, conn, username, userID, groupIDList);
    }
    
    public Stage getStage(){
        return (Stage) scene.getWindow();
    }
}