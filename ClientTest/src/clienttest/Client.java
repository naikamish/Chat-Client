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
    private ClientChat chatWindow;
    
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
       /* userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendMessage(clientName + " - " + e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
      */ 
        String myip="";
        try{
        myip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e){}

        serverIP = JOptionPane.showInputDialog(this, "What IP do you want to connect to", myip+"");
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
                chatWindow = new ClientChat(connection, groupsList.getSelectedValue().toString(), clientName);
                chatWindow.startRunning();
            }
            
        });
        
        setSize(10,200);
        setVisible(true);
    }
    
    public void startRunning(){
         connectToServer();
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
        }
        catch(Exception e){}
    }

}
