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
                    sendMessage(clientName + " - " + e.getActionCommand());
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
                            showMessage("\n"+message);
                        }
                        catch(Exception e){}
                    }
                }
            });
        thread.start();
        
        
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
    
    
}
