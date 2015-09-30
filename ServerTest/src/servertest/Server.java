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

//make it so that connection in group closes properly when client exits chat
//will probably have to catch an ioexception error and remove connection from
//the group

public class Server extends JFrame{
    
    private static JTextArea chatWindow;
    
    private ServerSocket server; 
    //private ArrayList<Group> group = new ArrayList<Group>();
    private static Group group1, group2, group3;
    private String groupList = "group1,group2,group3";

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
            group1 = new Group(server,"group1");
            group2 = new Group(server,"group2");
            group3 = new Group(server,"group3");
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
                        //Create a temporary connection and find out which group 
                        //the connection wants to be a part of
                        Connection tempConn = new Connection(server.accept());
                        tempConn.sendMessage(groupList);
                        tempConn.getGroup();
                    }
                    catch(Exception e){}
                }
                }
            });
        t1.start();    
    }
    
    
    //Send the client the group that they want to be part of
    //as a parameter so they can create a thread
    //that lets them send their messages to everyone in that group
    //Also add the client to that group through addConnection method
    public static void setGroup(Connection conn, String s){
        if(s.equals("group1")){
            conn.setGroup(group1);
            group1.addConnection(conn);
        }
        else if(s.equals("group2")){
            conn.setGroup(group2);
            group2.addConnection(conn);
        }
        else{
            conn.setGroup(group3);
            group3.addConnection(conn);
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
