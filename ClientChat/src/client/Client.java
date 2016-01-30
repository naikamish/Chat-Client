/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Amish Naik
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import message.Message;

public class Client extends JFrame{
    
    private static ArrayList<ClientChat> chats = new ArrayList<ClientChat>();
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP;
    private String clientName;
    private String groupName;
    private String[] data;
    private static DefaultListModel listModel = new DefaultListModel();
    static Notification notifications;
    
    Connection connection;
    JButton connectButton, createGroupButton;
    private static JScrollPane groupsListPane = new JScrollPane();
    JList groupsList;
    
    public Client(){
        super("Client Chat");
        groupsList = new JList(listModel);
        add(groupsListPane, BorderLayout.CENTER);
        groupsListPane.setViewportView(groupsList);
        
        connectButton = new JButton("Connect");
        add(connectButton,BorderLayout.SOUTH);
        connectButton.setEnabled(false);
        
        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientChat chatWindow = new ClientChat(connection, groupsList.getSelectedValue().toString(), clientName);
                
                chatWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                
                
                chatWindow.startRunning();
                chats.add(chatWindow);
            }
            
        });
        
        createGroupButton = new JButton("Create New Group");
        add(createGroupButton,BorderLayout.NORTH);
        
        createGroupButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String newGroup = JOptionPane.showInputDialog("What is the name of the group you wish to create?");
                connection.sendMessage(new Message("CMD","CREATE",newGroup));
            }
            
        });
        
        try{
            notifications = new Notification();
        }
        catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            listModel.addElement(sw.toString());
            groupsListPane.updateUI();
        }
        
        setSize(100,400);
        setVisible(true);
    }
    
    public void startRunning(String[] groupList, Connection connection, String username){
        try{
            //JOptionPane.showConfirmDialog(null,groupList[1]);
            clientName = username;
            this.connection = connection;
            for(String s : groupList){
                listModel.addElement(s);
                groupsListPane.updateUI();
            }
            connectButton.setEnabled(true);
        }
        catch(Exception e){}
    }
    
    public static void sendGroupList(String g, String[] list){
        for(ClientChat chat:chats){
            if(chat.getGroup().equals(g)){
                chat.setGroupList(list);
            }
        }
    }
    
    public static void showMessage(Message message){
        for(ClientChat chat:chats){
            if(chat.getGroup().equals(message.groupName)){
                if(message.cmd.equals("SEND")){
                    chat.showMessage(message);
                }
                else if(message.cmd.equals("DOODLE")){
                    chat.showDoodle(message);
                }
                else if(message.cmd.equals("FILE")){
                    chat.showFile(message);
                }
            }
        }
    }
    
    public static void removeFromGroup(ClientChat c){
        chats.remove(c);
    }
    
    public static void deleteFromList(String g, String user){
        for(ClientChat chat:chats){
            if((chat.getGroup()).equals(g)){
                chat.deleteFromList(user);
            }
        }
    }
    
    public static void addGroup(String groupName){
        listModel.addElement(groupName);
        groupsListPane.updateUI();
    }
}

