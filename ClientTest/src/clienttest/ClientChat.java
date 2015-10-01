/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.*;

/**
 *
 * @author Amish Naik
 */
public class ClientChat extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Connection connection;
    private String clientName;
    private String groupName;
    private Thread thread;
    private DefaultListModel listModel = new DefaultListModel();
    
    public ClientChat(Connection conn, String grp){
        super("Client Chat");
        connection = conn;
        
        clientName = JOptionPane.showInputDialog(this, "What's your name?");
        groupName = grp;
        userText = new JTextField();
        userText.setEditable(true);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendMessage("CLIENT - "+clientName + " - " + e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            String message = (String) connection.input.readObject();
                            String source = message.substring(0,6);
                           // SERVER - add awoiefwa
                            if(source.equals("SERVER")){
                                String action = message.substring(9,12);
                                if(action.equals("SRT")){
                                    message=message.substring(13);
                                    String[] data = message.split(",");

                                    for(String s : data){
                                        listModel.addElement(s);
                                    }
                                }
                                else if(action.equals("ADD")){
                                    listModel.addElement(message.substring(13));
                                }
                                else if(action.equals("DEL")){
                                    
                                }
                            }
                            else{
                            showMessage("\n"+message.substring(9));
                            }
                        }
                        catch(Exception e){}
                    }
                }
            });
        thread.start();
        
        sendMessage(groupName);
        sendMessage(clientName);
        
        JList clientList = new JList(listModel);
        add(new JScrollPane(clientList), BorderLayout.EAST);
        //setGroupList();
        
        
        
        setSize(500,500);
        setVisible(true);
    }
    
    private void sendMessage(String message){
        connection.sendMessage(message);
    }
    
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
                }
            }
        );
    }
    
  /*  private void setGroupList(){
        try{
            String message = (String) connection.input.readObject();
            String[] data = message.split(",");
            
            for(String s : data){
                listModel.addElement(s);
            }
        }
        catch(Exception e){}
    }*/
    
    
}
