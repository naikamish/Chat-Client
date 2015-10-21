/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

/**
 *
 * @author Amish Naik
 */
public class Message {
    private String type, cmd, groupName, clientName, message;
    private String[] groupList, clientList;
    
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
}
