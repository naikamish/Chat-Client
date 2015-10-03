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
    private static ArrayList<Group> groups = new ArrayList<Group>();
   // private static Group group1, group2, group3;
//    private String groupList = "group1,group2,group3";

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
            groups.add(new Group(server,"group1"));
            groups.add(new Group(server,"group2"));
            groups.add(new Group(server,"group3"));
        //    group1 = new Group(server,"group1");
        //    group2 = new Group(server,"group2");
        //    group3 = new Group(server,"group3");
            waitForConnection();
        }
        
        catch(Exception e){
            //showMessage("\nline62 server\n");
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
                        tempConn.sendMessage(getGroupList());
                        tempConn.getInfo();
                       // tempConn.sendClientList();
                    }
                    catch(Exception e){//showMessage("\nline80 server\n");
                    }
                }
                }
            });
        t1.start();    
    }
    
    private String getGroupList(){
        String groupList = "";
        for(Group group:groups){
            groupList+=","+group.getName();
        }
        return groupList;
    }
    
    
    //Send the client the group that they want to be part of
    //as a parameter so they can create a thread
    //that lets them send their messages to everyone in that group
    //Also add the client to that group through addConnection method
    public static void setGroup(Connection conn, String s){
        for(Group group:groups){
            if(s.equals(group.getName())){
                conn.setGroup(group);
                //conn.sendClientList();
                group.addConnection(conn);
            }
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
