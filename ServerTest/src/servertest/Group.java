/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import java.io.File;
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
    public ArrayList<Connection> connections = new ArrayList<Connection>();
    private Thread t1;
    private ServerSocket server;
    private String groupName;
    private int groupID, creatorID;
    private byte[] groupImage;
    
    public Group(ServerSocket serv, String name, int groupID, int creatorID, byte[] groupImage){
        server=serv;
        groupName = name;
        this.groupID = groupID;
        this.creatorID = creatorID;
        this.groupImage = groupImage;
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
        Message message = new Message("CMD", "REMOVE", groupID, user.getName());
        message.userID = user.getID();
        sendMessage(message);//{"CMD","RMOV",groupName,user.getName(),""});
    }
    
    public String getName(){
        return groupName;
    }
    
    public int getID(){
        return groupID;
    }
    
    public byte[] getImage(){
        return groupImage;
    }
    
    public int getCreatorID(){
        return creatorID;
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
    
    public int[] getClientIDs(){
        int[] groupIDs = new int[connections.size()];
        int i = 0;
      //  if(!connections.isEmpty()){
            for(Connection socket:connections){
                groupIDs[i]=socket.getID();
                i++;
            }
      //  }
        return groupIDs;
    }
    
    public byte[][] getGroupImages(){
        byte[][] groupImages = new byte[connections.size()][];
        int i=0;
        for(Connection connection: connections){
            groupImages[i]=connection.getImage();
            i++;
        }
        return groupImages;
    }
}
