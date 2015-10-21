/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author Amish Naik
 */
import java.io.Serializable;

/**
 *
 * @author Amish Naik
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 5950169519310163575L;
    public String type="", cmd="", groupName="", clientName="", message="";
    public String[] groupList, clientList;
    
    //Send client list to newly joined client
    public Message(String type, String cmd, String groupName, String[] clientList){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientList = clientList;
    }
    //Send add message to a group when a new client joins or leaves
    public Message(String type, String cmd, String groupName, String clientName){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
    }
    //Send message to a group of people
    public Message(String type, String cmd, String groupName, String clientName, String msg){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
        this.message = msg;
    }
    //Send group list to newly joined client
    public Message(String type, String cmd, String[] groupList){
        this.type = type;
        this.cmd = cmd;
        this.groupList = groupList;
    }
    //Create new group
    public Message(String type, String cmd, String groupName){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
    }
    
    public String toString(){
        return type+cmd+groupName+clientName+message;
    }
}

