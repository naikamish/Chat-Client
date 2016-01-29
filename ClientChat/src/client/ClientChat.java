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
import java.awt.geom.GeneralPath;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import message.DoodlePath;
import message.Message;

/**
 *
 * @author Amish Naik
 */
public class ClientChat extends JFrame{
    private JTextField userText;
    private JTextPane chatWindow;
    private String message = ""; 
    private Connection connection;
    private String clientName;
    private String groupName;
    private DefaultListModel listModel = new DefaultListModel();
    
    private JList<String> clientList;
    private JScrollPane clientListPane = new JScrollPane();
    private JButton doodleButton, uploadFileButton;
    private JPanel optionsPanel;
    
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
                    sendMessage(new Message("MSG", "SEND", groupName, clientName, e.getActionCommand()));//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
        chatWindow = new JTextPane();
        chatWindow.setContentType("text/html");
        chatWindow.setAutoscrolls(true);
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        
        clientList = new JList(listModel);
        add(clientListPane, BorderLayout.EAST);
        clientListPane.setViewportView(clientList);
                
        doodleButton = new JButton("Doodle");
        doodleButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DoodleFrame doodleFrame;
                doodleFrame = new DoodleFrame(getThis());
            }
            
        });
        
        uploadFileButton = new JButton("Upload File");
        uploadFileButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(rootPane);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    byte [] bytearray  = new byte [(int)file.length()];
                    String extension = "";

                    int i = file.getPath().lastIndexOf('.');
                    int p = Math.max(file.getPath().lastIndexOf('/'), file.getPath().lastIndexOf('\\'));

                    if (i > p) {
                        extension = file.getPath().substring(i+1);
                    }
                    try{
                        FileInputStream fin = new FileInputStream(file);
                        BufferedInputStream bin = new BufferedInputStream(fin);
                        bin.read(bytearray,0,bytearray.length);
                        sendMessage(new Message("MSG", "FILE", groupName, clientName, bytearray, extension));
                    }
                    catch(Exception ex){}
                }
            }
            
        });
        
        optionsPanel = new JPanel();
        optionsPanel.add(doodleButton);
        optionsPanel.add(uploadFileButton);
        add(optionsPanel, BorderLayout.SOUTH);
        
        addWindowListener( new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                       closeWindow();
                    }
                } );
        
        setSize(500,500);
        setVisible(true);
    }
    
    public ClientChat getThis(){
        return this;
    }
    
    public void sendDoodle(LinkedList<DoodlePath> generalPath){
        sendMessage(new Message("MSG", "DOODLE", groupName, clientName,generalPath));
    }
    
    public void showDoodle(Message message){
        if(!clientName.equals(message.clientName)){
            DoodleFrame doodleFrame;
            doodleFrame = new DoodleFrame(this);
            doodleFrame.setDoodle(message.doodle);
        }
    }
    
    public void closeWindow(){
        sendMessage(new Message("CMD", "EXIT", groupName, clientName));
        Client.removeFromGroup(this);
        this.dispose();
    }
    
    public void startRunning(){
        sendMessage(new Message("CMD", "JOIN", groupName, clientName));//CMD JOIN " + groupName+","+clientName);
    }
    
    public void sendMessage(Message message){
        connection.sendMessage(message);
    }
    
    public void showMessage(final Message message){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    try{
                        Client.notifications.displayMessage(message.clientName, message.message);
                    }
                    catch(Exception e){
                        chatWindow.setText(e.toString());
                    }
                   /* StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < text.length; i++) {
                                strBuilder.append( text[i]+" " );
                             }*/
                    String style;
                    if(message.clientName.equals(clientName)){
                        style = "<span style='color:#ff0000; font-weight:bold;'>";
                    }
                    else{
                        style = "<span style='color:#0000ff; font-weight:bold;'>";
                    }
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    
                    fullText+=style+message.clientName+" ("+dateFormat.format(date)+"):</span> "+message.message+"<br>";
                   // fullText+=message.toString()+"<br>";
                    chatWindow.setText(fullText);
                }
            }
        );
    }
    
    public void deleteFromList(String user){
        listModel.removeElement(user);
        clientListPane.updateUI();
    }
    
    public String getGroup(){
        return groupName;
    }
    
    public void setGroupList(String[] list){
       // String[] data = list.split(",");
        for(String s : list){
            listModel.addElement(s);
            clientListPane.updateUI();
        }
    } 
}
