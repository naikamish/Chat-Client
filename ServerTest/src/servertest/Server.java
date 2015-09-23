/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

/**
 *
 * @author Amish Naik
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

//separate connection,output, and input into their own connection class
    

public class Server extends JFrame{
    
    private static JTextArea chatWindow;
    
    private ServerSocket server; 
    private static ArrayList<Connection> connections = new ArrayList<Connection>();

    private Thread t1;
    
    //constructor
    public Server(){
        super("Server Chat");
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(500,500);
        setVisible(true);
    }
    
    //Set upand run the server
    public void startRunning(){
        try{
            server = new ServerSocket(5000,100); //First number is port and second number is backlog of how many people can access server
            showMessage("Waiting for someone to connect...\n");
            waitForConnection();
        }
        
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    //Wait for a connection, then display connection information
    private void waitForConnection(){
        t1 = new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                    try{
                        Connection tempConn = new Connection(server.accept());
                        connections.add(tempConn);
                    }
                    catch(Exception e){}
                }
                }
            });
        t1.start();    
    }
    
    //Send message to client
    public static void sendMessage(String message){
        for(Connection socket:connections){
            socket.sendMessage(message);
        }
    }
    
    //Updates chat window
    public static void showMessage(final String text){
        
        //Update the GUI instead of totally recreating the chat window
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
                }
            }
        );
    }
}
