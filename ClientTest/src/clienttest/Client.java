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
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Client extends JFrame{
    
  //  private JTextField userText;
    private static ArrayList<ClientChat> chats = new ArrayList<ClientChat>();
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
 //   private Socket connection;
    private String clientName;
    private String groupName;
   // private ArrayList<String> data;
    private String[] data;
    private DefaultListModel listModel = new DefaultListModel();
    
    Connection connection;
    JButton connectButton;
    
    public Client(){
        super("Client Chat");

        serverIP = JOptionPane.showInputDialog(this, "What IP do you want to connect to?");
        clientName = JOptionPane.showInputDialog(this, "What's your name?");
        //groupName = JOptionPane.showInputDialog(this, "What Group do you want to connect to?", "group1");

        JList groupsList = new JList(listModel);
        add(new JScrollPane(groupsList), BorderLayout.CENTER);
        
        connectButton = new JButton("Connect");
        add(connectButton,BorderLayout.SOUTH);
        connectButton.setEnabled(false);
        
        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientChat chatWindow = new ClientChat(connection, groupsList.getSelectedValue().toString(), clientName);
                chatWindow.startRunning();
                chats.add(chatWindow);
            }
            
        });
        
        setSize(10,200);
        setVisible(true);
    }
    
    public void startRunning(){
         connectToServer();
    }
    
    public static void sendGroupList(String g, String list){
        for(ClientChat chat:chats){
            if(chat.getGroup().equals(g)){
                chat.setGroupList(list);
            }
        }
    }
    
    public static void showMessage(String g, String message){
        for(ClientChat chat:chats){
            if(chat.getGroup().equals(g)){
                chat.showMessage("\n"+message);
            }
        }
    }

    
    //Connect to Server
    private void connectToServer(){
        try{
            connection = new Connection(new Socket(InetAddress.getByName(serverIP),5000));
            message = (String) connection.input.readObject();
            data = message.split(",");
            for(String s : data){
                listModel.addElement(s);
            }
            connectButton.setEnabled(true);
            connection.setUpThread();
        }
        catch(Exception e){}
    }

}
