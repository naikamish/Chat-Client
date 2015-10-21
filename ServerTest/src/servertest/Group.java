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
import message.Message;

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
    public void sendMessage(Message msg){
        for(Connection socket:connections){
            socket.sendMessage(msg);
        }
    }
    
    public void addConnection(Connection conn){
        connections.add(conn);
    }
    
    public void removeFromGroup(Connection user){
        connections.remove(user);
        sendMessage(new Message("CMD", "REMOVE", groupName, user.getName()));//{"CMD","RMOV",groupName,user.getName(),""});
    }
    
    public String getName(){
        return groupName;
    }
    
    public String[] getClientList(){
        String[] groupList = new String[connections.size()];
        int i = 0;
      //  if(!connections.isEmpty()){
            for(Connection socket:connections){
                groupList[i]=socket.getName();
                i++;
            }
      //  }
        return groupList;
    }
}
