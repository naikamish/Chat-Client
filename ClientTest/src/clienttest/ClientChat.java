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
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    private JList<String> clientList;
    private JScrollPane clientListPane = new JScrollPane();
    
    public ClientChat(Connection conn, String grp, String name){
        super("Client Chat");
        connection = conn;       
        clientName = name;
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
        chatWindow.setLineWrap(true);
        chatWindow.setWrapStyleWord(true);
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        
        clientList = new JList(listModel);
        add(clientListPane, BorderLayout.EAST);
        clientListPane.setViewportView(clientList);
        //setGroupList();
        
        
        
        setSize(500,500);
        setVisible(true);
    }
    
    public void startRunning(){
        sendMessage(groupName+","+clientName);
       // sendMessage(clientName);
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            String message = (String) connection.input.readObject();
                            String source = message.substring(0,6);
                           // SERVER - add amish
                            if(source.equals("SERVER")){
                                String action = message.substring(9,12);
                                if(action.equals("SRT")&&message.length()>12){
                                    String groupList=message.substring(13);
                                    String[] data = groupList.split(",");

                                    for(String s : data){
                                        s = "<html><font color=green>"+s+"</font></html>";
                                        listModel.addElement(s);
                                        clientListPane.updateUI();
                                    }
                                }
                                else if(action.equals("ADD")){
                                    String s = message.substring(13);
                                    s = "<html><font color=green>"+s+"</font></html>";
                                    listModel.addElement(s);
                                }
                                else if(action.equals("DEL")){
                                    
                                }
                            }
                           // else{
                            Date date = new Date();
                            SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
                            String justTime = stf.format(date);
                            showMessage("\n"+justTime+"   "+message);
                           // }
                        }
                        catch(Exception e){//showMessage("\nline103 clientchat\n");
                        }
                    }
                }
            });
        thread.start();
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
