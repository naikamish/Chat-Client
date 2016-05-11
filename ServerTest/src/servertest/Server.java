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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import message.Message;

 //create and delete group, store group names in txt file

public class Server extends JFrame{
    
    private static JTextPane chatWindow;
    
    private static ServerSocket server; 
    private static ArrayList<Group> groups = new ArrayList<Group>();
    private static ArrayList<Connection> connections = new ArrayList<Connection>();
    private static String fullText = "";
    private static LoadDriver dbLib;
    public static TrieTree dictionary;

    private Thread t1;
    
    public Server(){
        super("Server Chat");
        createTrie();
        chatWindow = new JTextPane();
        chatWindow.setContentType("text/html");
        chatWindow.setAutoscrolls(true);
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(500,500);
        setVisible(true);
        
        try{
            dbLib = new LoadDriver("jdbc:mysql://localhost:3306/chat","root","password");
        }
        catch(Exception e){
            Server.showMessage("server line 49" + e.toString());
        }
    }
    
    public void startRunning(){
        try{
            server = new ServerSocket(5000,100); //First number is port and second number is backlog of how many people can access server
            showMessage("Waiting for someone to connect...\n");
            createGroupList();
            waitForConnection();
        }
        
        catch(Exception e){Server.showMessage("server line 61" + e.toString());}
    }
    
    private void createGroupList(){
        String query = "select groupName, groupID, userID, groupImage from groups;";
        
        try{
            ResultSet resultSet = dbLib.selectQuery(query);
            while (resultSet.next()) {
                byte[] file;
                String groupImage = resultSet.getString("groupImage");
                try{
                    Path imagePath = Paths.get(groupImage);
                    file = java.nio.file.Files.readAllBytes(imagePath);
                }
                catch(Exception e){
                    file=new byte[0];
                }
                groups.add(new Group(server, resultSet.getString("groupName"), resultSet.getInt("groupID"), resultSet.getInt("userID"), file));
                //System.out.println(resultSet.getString("groupName"));
            }
        }
        catch(Exception e){Server.showMessage("server line 74" + e.toString());}
        // The name of the file to open.
    }
    
    public static void createGroup(String groupName, int groupID, int creatorID, byte[] file){
        groups.add(new Group(server,groupName,groupID,creatorID, file));
        for(Connection connection:connections){
            Message message = new Message("CMD", "CREATE", groupName);
            message.groupID = groupID;
            message.creatorID = creatorID;
            message.file = file;
            connection.sendMessage(message);//{"CMD", "CRTE", groupName,"",""});
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
                    catch(Exception e){Server.showMessage("server line 98" + e.toString());}
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
                message.file = c.getImage();
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
    
    public static byte[][] getGroupImages(){
        byte[][] groupImages = new byte[groups.size()][];
        int i=0;
        for(Group group:groups){
            groupImages[i]=group.getImage();
            i++;
        }
        return groupImages;
    }
    
    public static void removeFromGroup(Connection user, int g){
        for(Group group:groups){
            if(g==group.getID()){
                group.removeFromGroup(user);
            }
        }
    }
    
    public static void removeConnection(Connection connection){
        for(Group group:groups){
            if(group.connections.contains(connection)){
                group.removeFromGroup(connection);
                //group.connections.remove(connection);
                Server.showMessage("removed user from group"+group.getName());
                String insertQuery = "insert into groupJoins(groupID, userID, status) values(?,?,?);";
                try{
                    dbLib.prepareJoinExitQuery(insertQuery, group.getID(), connection.getID(), 0);
                    //Server.showMessage(query);
                }
                catch(Exception e){
                    Server.showMessage("conn line 106"+e.toString());
                }
            }
        }
        Server.showMessage("hello");
        if(connections.contains(connection)){
            connections.remove(connection);
            Server.showMessage("removed user from server");
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
    
    public static void createTrie(){
        dictionary = new TrieTree();
        
        populateDictionary("negative-words.txt",-1);
        populateDictionary("positive-words.txt",1);
    }
    
    public static void populateDictionary(String string, int value){
        try {
            InputStream is = ServerTest.class.getResourceAsStream(string);  
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while (line != null) {
                dictionary.addWord(line, value);
                line = br.readLine();
            }
            br.close();
        } 
        catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public static int calculateEmotion(String string){
        int emotion=0;
        String[] sentence = string.split(" ");
        for(String str:sentence){
            emotion+=dictionary.findWord(str);
        }
        return emotion;
    }
}
