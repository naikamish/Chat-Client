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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
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
    private HTMLEditorKit kit;
    private HTMLDocument doc;
    private StyleSheet styleSheet;
    
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
        
        
        kit = new HTMLEditorKit();
        styleSheet = new StyleSheet();
        styleSheet.importStyleSheet(getClass().getResource("css.css"));
        kit.setStyleSheet(styleSheet);
        doc = (HTMLDocument) kit.createDefaultDocument();
        chatWindow.setEditorKit(kit);
        chatWindow.setDocument(doc);
        
        
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
    
    public void showFile(Message message){
        if(!clientName.equals(message.clientName)){
            /*JButton l=new JButton(message.message);
            chatWindow.insertComponent(l);*/
            String dialogMessage = message.clientName+" has sent you a file.\n"+message.filename+"\nWould you like to download this file?";
            String dialogTitle = "Download File";
            int reply = JOptionPane.showConfirmDialog(this, dialogMessage, dialogTitle, JOptionPane.YES_NO_OPTION);
            if(reply==JOptionPane.YES_OPTION){
                byte [] bytearray  = message.file;
                try{
                    FileOutputStream fos = new FileOutputStream(message.filename);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(bytearray, 0 , bytearray.length);
                    bos.flush();
                    bos.close();
                    JOptionPane.showMessageDialog(this,"Download Complete");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this,e.toString());
                }
            }
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
                    
                    String style;
                    if(message.clientName.equals(clientName)){
                        style = "<p class='personOne'>";
                    }
                    else{
                        style = "<p class='personTwo'>";
                    }
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    
                    //fullText+=style+message.clientName+" ("+dateFormat.format(date)+"):</span> "+message.message+"<br>";
                    fullText=style+"<span>"+message.clientName+" ("+dateFormat.format(date)+"):</span> "+message.message+"</p>";
                    

                    try {
                        kit.insertHTML(doc, doc.getLength(), fullText, 0, 0, HTML.Tag.P);
                        kit.insertHTML(doc, doc.getLength(), "<p></p>", 0, 0, null);
                    } catch(Exception e){}
                    //chatWindow.setText(fullText);
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
