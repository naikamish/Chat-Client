/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author Amish Naik
 */
public class Group {
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private Thread t1;
    private ServerSocket server;
    private String groupName;
    
    public Group(ServerSocket serv, String name){
        server=serv;
        groupName = name;
    }
    
    //Send message to client
    public void sendMessage(String message){
        for(Connection socket:connections){
            socket.sendMessage(message);
        }
    }
    
    public void addConnection(Connection conn){
        connections.add(conn);
    }
    
    public String getName(){
        return groupName;
    }
    
    public String getClientList(){
        String groupList = "";
        if(!connections.isEmpty()){
        for(Connection socket:connections){
            groupList += ","+socket.getName();
        }
        }
        return groupList;
    }
}
