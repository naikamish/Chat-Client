package clienttest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Amish Naik
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private String clientName;
    
    public Client(){
        super("Client Chat");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendMessage(e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        
        clientName = JOptionPane.showInputDialog(this, "What's your name?");
        serverIP = JOptionPane.showInputDialog(this, "What IP do you want to connect to", "148.166.154.46");
        
        setSize(500,500);
        setVisible(true);      
    }
    
    public void startRunning(){
        try{
            connectToServer();
            setupStream();
            whileChatting();
        }
        catch(EOFException eofException){
            showMessage("\nClient terminated connection");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            closeStreams();
        }
    }
    
    //Connect to Server
    private void connectToServer() throws IOException{
        showMessage("Attemption to connect to server...\n");
        connection = new Socket(InetAddress.getByName(serverIP),5000);
        showMessage("Connected to "+connection.getInetAddress().getHostName());
    }
    
    //Set up streams to send and receive messages
    private void setupStream() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are now set up\n");
    }
    
    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage(message);
            }
            catch(ClassNotFoundException classNotFoundException){
                showMessage("\nClass not found exception");
            }
        }
        while(!message.equals("SERVER - END"));
    }
    
    private void closeStreams(){
        showMessage("\nClosing streams...");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    private void sendMessage(String message){
        try{
            output.writeObject(clientName + " - " + message);
            output.flush();
        }
        catch(IOException ioException){
            chatWindow.append("\nUnable to send message");
        }
    }
    
    //Update chat window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
                }
            }
        );
    }
    
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(tof);
                }
            }
        );
    }
}
