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
import java.awt.event.*;

/**
 *
 * @author Amish Naik
 */
public class ClientChat extends JFrame{
    private JTextField userText;
   // private JTextPane userText;
   // private JTextArea chatWindow;
    private JTextPane chatWindow;
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
    
    private String fullText = "";
    
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
                    //MSG group1 amish - hello
                    sendMessage(new String[]{"MSG", "SEND", groupName, clientName, e.getActionCommand()});//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
      //  chatWindow = new JTextArea();
        chatWindow = new JTextPane();
      //  chatWindow.setLineWrap(true);
     //   chatWindow.setWrapStyleWord(true);
        chatWindow.setContentType("text/html");
        chatWindow.setAutoscrolls(true);
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        
        clientList = new JList(listModel);
        add(clientListPane, BorderLayout.EAST);
        clientListPane.setViewportView(clientList);
        //setGroupList();
        
        addWindowListener( new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                       closeWindow();
                    }
                } );
        
        setSize(500,500);
        setVisible(true);
    }
    
    public void closeWindow(){
        sendMessage(new String[]{"CMD", "EXIT", groupName, clientName, ""});
        Client.removeFromGroup(this);
        this.dispose();
    }
    
    public void startRunning(){
        sendMessage(new String[]{"CMD", "JOIN", groupName, clientName,""});//CMD JOIN " + groupName+","+clientName);
       // sendMessage(clientName);
    /*    thread= new Thread(
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
        thread.start();*/
    }
    
    private void sendMessage(String[] message){
        connection.sendMessage(message);
    }
    
    public void showMessage(final String[] text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < text.length; i++) {
                                strBuilder.append( text[i]+" " );
                             }
                    //chatWindow.append(text);
                    fullText+="<font color=green>"+strBuilder.toString()+"</font><br>";
                    chatWindow.setText(fullText);
                }
            }
        );
    }
    
    public void deleteFromList(String user){
        // showMessage(new String[]{"removed",user});
        listModel.removeElement(user);
       // showMessage(new String[]{"removed",user});
        clientListPane.updateUI();
    }
    
    public String getGroup(){
        return groupName;
    }
    
    public void setGroupList(String list){
        String[] data = list.split(",");
        for(String s : data){
            //s = "<html><font color=green>"+s+"</font></html>";
            listModel.addElement(s);
            clientListPane.updateUI();
        }
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
