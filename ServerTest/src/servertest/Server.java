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
import databaselibrary.DatabaseLibrary;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.*;
import message.Message;

 //create and delete group, store group names in txt file

public class Server extends JFrame{
    
    private static JTextPane chatWindow;
    
    private static ServerSocket server; 
    private static ArrayList<Group> groups = new ArrayList<Group>();
    private static ArrayList<Connection> connections = new ArrayList<Connection>();
    private static String fullText = "";
    private DatabaseLibrary dbLib;

    private Thread t1;
    
    public Server(){
        super("Server Chat");
        
        chatWindow = new JTextPane();
        chatWindow.setContentType("text/html");
        chatWindow.setAutoscrolls(true);
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(500,500);
        setVisible(true);
        
        try{
            dbLib = new DatabaseLibrary("jdbc:mysql://localhost:3306/chat","root","password");
        }
        catch(Exception e){
            //Server.showMessage(e.toString());
        }
    }
    
    public void startRunning(){
        try{
            server = new ServerSocket(5000,100); //First number is port and second number is backlog of how many people can access server
            showMessage("Waiting for someone to connect...\n");
            createGroupList();
            waitForConnection();
        }
        
        catch(Exception e){}
    }
    
    private void createGroupList(){
        String query = "select groupName, groupID, userID from groups;";
        
        try{
            ResultSet resultSet = dbLib.selectQuery(query);
            while (resultSet.next()) {
                groups.add(new Group(server, resultSet.getString("groupName"), resultSet.getInt("groupID"), resultSet.getInt("userID")));
                System.out.println(resultSet.getString("groupName"));
            }
        }
        catch(Exception e){}
        // The name of the file to open.
    }
    
    public static void createGroup(String groupName){
        groups.add(new Group(server,groupName,0,0));
        for(Connection connection:connections){
            connection.sendMessage(new Message("CMD", "CREATE", groupName));//{"CMD", "CRTE", groupName,"",""});
        }
    }
    
    private void waitForConnection(){
        t1 = new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                    try{
                        Connection tempConn = new Connection(server.accept(),dbLib);
                        connections.add(tempConn);
                    }
                    catch(Exception e){}
                }
                }
            });
        t1.start();    
    }
    
    public static Group addToGroup(Connection c, int g){
        for(Group group:groups){
            if(group.getID()==g){
                Message message = new Message("CMD", "ADD", group.getID(),c.getName());
                message.creatorID = group.getCreatorID();
                message.userID = c.getID();
                group.sendMessage(message);//[]{"CMD", "ADDS", group.getName(), "", c.getName()});//CMD ADDS "+group.getName()+" "+c.getName());
                group.addConnection(c);
                return group;
            }
        }
        return null;
    }
    
    public static void sendGroupMessage(Message msg){
        for(Group group:groups){
            if(group.getID()==msg.groupID){
                group.sendMessage(msg);
            }
        }
    }
    
    public static String[] getGroupList(){
        String[] groupList = new String[groups.size()];
        int i = 0;
        for(Group group:groups){
            groupList[i]=group.getName();
            i++;
        }
        return groupList;
    }
    
    public static int[] getGroupID(){
        int[] groupID = new int[groups.size()];
        int i = 0;
        for(Group group:groups){
            groupID[i]=group.getID();
            i++;
        }
        return groupID;
    }
    
    public static void removeFromGroup(Connection user, int g){
        for(Group group:groups){
            if(g==group.getID()){
                group.removeFromGroup(user);
            }
        }
    }
    
    public static void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    fullText+="<font color=green>"+text+"</font><br>";
                    chatWindow.setText(fullText);
                }
            }
        );
    }
}
