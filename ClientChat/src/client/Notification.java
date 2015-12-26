/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 *
 * @author Amish Naik
 */
public class Notification {
    TrayIcon trayIcon;
    
    public Notification() throws Exception{
        initComponents();
    }
    
    private void initComponents() throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        //retieve icon form url and scale it to 32 x 32
        //trayIcon = new TrayIcon(resizeImage( ImageIO.read(new URL("http://files.softicons.com/download/game-icons/super-mario-icons-by-sandro-pereira/png/16/Mushroom%20-%20Life.png")), BufferedImage.TYPE_INT_ARGB, 32, 32));
        trayIcon = new TrayIcon(ImageIO.read(new URL("http://files.softicons.com/download/game-icons/super-mario-icons-by-sandro-pereira/png/16/Mushroom%20-%20Life.png")));

        //get the system tray
        final SystemTray tray = SystemTray.getSystemTray();

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
    
    public void displayMessage(String title, String message){
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.NONE);
    }
}
