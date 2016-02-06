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
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Amish Naik
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 5950169519310163575L;
    public String type="", cmd="", groupName="", clientName="", message="";
    public String name="", username="", email="";
    public String fullMessage="";
    public int code;
    public String password;
    public String[] groupList, clientList;
    public LinkedList<DoodlePath> doodle;
    public byte[] file;
    public String extension="", filename="";
    public char[] pass;
    
    public Message(){
        
    }
    //Send client list to newly joined client
    public Message(String type, String cmd, String groupName, String[] clientList){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientList = clientList;
        fullMessage = "Send client list for group "+this.groupName+" to "+this.groupName;
    }
    //Send add message to a group when a new client joins or leaves
    public Message(String type, String cmd, String groupName, String clientName){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
        if(cmd.equals("JOIN"))
            fullMessage = this.clientName + " joined group " + this.groupName;
        else if(cmd.equals("EXIT"))
            fullMessage = this.clientName + " left group " + this.groupName;
    }
    //Send message to a group of people
    public Message(String type, String cmd, String groupName, String clientName, String msg){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
        this.message = msg;
        fullMessage = this.groupName + "- " + this.clientName + ": " + this.message;
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
        fullMessage = "New group created: " + this.groupName;
    }
    
    //Send registration activation code
    public Message(String type, String email, int code){
        this.type = type;
        this.email = email;
        this.code = code;
        fullMessage = "Sent activation code " + this.code + " to " + this.email + ".";
    }
    
    //Send registration information from client to server
    public Message(String type, String name, String username, char[] password, String email, int code){
        this.type = type;
        this.name = name;
        this.username = username;
        this.pass = password;
        this.email = email;
        this.code = code;
        fullMessage = this.username + " attempted to create a new account.";
    }
    
    //Send login information from client to server
    public Message(String type, String username, char[] password){
        this.type = type;
        this.username = username;
        this.pass = password;
        fullMessage = this.username + " logged in.";
    }
    
    //Send unsuccessful login message from server to client
    public Message(String type, String message){
        this.type = type;
        this.message = message;
    }
    
    //Send doodle
    public Message(String type, String cmd, String groupName, String clientName, LinkedList<DoodlePath> doodle){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
        this.doodle=doodle;
    }
    
    //Send File
    public Message(String type, String cmd, String groupName, String clientName, byte[] file, String extension){
        this.type = type;
        this.cmd = cmd;
        this.groupName = groupName;
        this.clientName = clientName;
        this.file=file;
        this.extension = extension;
    }
    
    public String toString(){
        return fullMessage;
    }
}