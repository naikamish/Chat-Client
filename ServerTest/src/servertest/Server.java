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
import message.Message;

 //create and delete group, store group names in txt file

public class Server extends JFrame{
    
    private static JTextPane chatWindow;
    
    private static ServerSocket server; 
    private static ArrayList<Group> groups = new ArrayList<Group>();
    private static ArrayList<Connection> connections = new ArrayList<Connection>();
    private static String fullText = "";

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
        // The name of the file to open.
        String fileName = "grouplist.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                groups.add(new Group(server,line));
            } 

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }
    
    public static void createGroup(String groupName){
        String fileName = "grouplist.txt";
        
        try{
        FileWriter fileWriter =
                new FileWriter(fileName,true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.newLine();
            bufferedWriter.write(groupName);

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        groups.add(new Group(server,groupName));
        for(Connection connection:connections){
            connection.sendMessage(new Message("CMD", "CRTE", groupName));//{"CMD", "CRTE", groupName,"",""});
        }
    }
    
    private void waitForConnection(){
        t1 = new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                    try{
                        Connection tempConn = new Connection(server.accept());
                        tempConn.sendMessage(new Message("CMD","LIST",getGroupList()));//[]{"CMD","LIST","","",getGroupList()});
                        connections.add(tempConn);
                    }
                    catch(Exception e){}
                }
                }
            });
        t1.start();    
    }
    
    public static Group addToGroup(Connection c, String g){
        for(Group group:groups){
            if(group.getName().equals(g)){
                group.sendMessage(new Message("CMD", "ADDS", group.getName(),c.getName()));//[]{"CMD", "ADDS", group.getName(), "", c.getName()});//CMD ADDS "+group.getName()+" "+c.getName());
                group.addConnection(c);
               // showMessage(group.getName());
                return group;
            }
        }
        return null;
    }
    
    public static void sendGroupMessage(Message msg){
        for(Group group:groups){
            if(group.getName().equals(msg.groupName)){
                group.sendMessage(msg);
            }
        }
    }
    
    private String[] getGroupList(){
        String[] groupList = new String[groups.size()];
        int i = 0;
        for(Group group:groups){
            groupList[i]=group.getName();
            i++;
        }
        return groupList;
    }
    
    public static void removeFromGroup(Connection user, String g){
        for(Group group:groups){
            if(g.equals(group.getName())){
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
