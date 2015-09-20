/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

/**
 *
 * @author Amish Naik
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

//separate connection,output, and input into their own connection class
    

public class Server extends JFrame{
    
    private JTextArea chatWindow;
    
    //This is how computers communicate with each other
    private ArrayList<ObjectOutputStream> output = new ArrayList<ObjectOutputStream>(); //Output is messages we send out
    private ArrayList<ObjectInputStream> input = new ArrayList<ObjectInputStream>(); //Input is messages we receive
    
    
    private ServerSocket server;
    private ArrayList<Socket> connection = new ArrayList<Socket>();
    
    ExecutorService executor = Executors.newCachedThreadPool();    
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private Thread t1;
    
    //constructor
    public Server(){
        super("Server Chat");
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(500,500);
        setVisible(true);
    }
    
    //Set upand run the server
    public void startRunning(){
        try{
            server = new ServerSocket(5000, 100); //First number is port and second number is backlog of how many people can access server
            showMessage("Waiting for someone to connect...\n");
            while(true){
                try{
                    waitForConnection();
                    setupStream();
                }
                catch(Exception e){
                    showMessage(e+"e");
                }
                
                finally{
                    closeStreams();
                }
            }
        }
        
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    //Wait for a connection, then display connection information
    private void waitForConnection(){
        t1 = new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                    try{
                        connection.add(server.accept());
                        showMessage("Now connected to "+connection.get(0).getInetAddress().getHostName()+"\n");
                        setupStream();
                    }
                    catch(Exception e){}
                }
                }
            });
        t1.start();    
    }
    
    //Get stream to send and receive data
    private void setupStream() throws IOException{
        try{    
        output.add(new ObjectOutputStream(connection.get(connection.size()-1).getOutputStream()));
        output.get(connection.size()-1).flush();
        
        input.add(new ObjectInputStream(connection.get(connection.size()-1).getInputStream()));
        threads.add(new Thread(
            new Runnable(){
                public int inputNum=input.size()-1;
                public void run(){
                    while(true){
                    try{
                        String message = (String) input.get(inputNum).readObject();
                        showMessage("\n"+message);
                        sendMessage("\n"+message);
                    }
                    catch(Exception e){}
                    }
                }
            }));
        threads.get(threads.size()-1).start();
        }
        catch(Exception e){}
    }
    
    //Close streams and sockets at the end of chat
    public void closeStreams(){
        try{
            connection.get(0).close();
        }
        catch(IOException ioException){
        }
    }
    
    //Send message to client
    private void sendMessage(String message){
        for(ObjectOutputStream socket:output){
            try{
                socket.writeObject(message);
                socket.flush();
            }
            catch(IOException ioException){
                chatWindow.append("\n ERROR: COULDN'T SEND MESSAGE");
            }
        }
    }
    
    //Updates chat window
    private void showMessage(final String text){
        
        //Update the GUI instead of totally recreating the chat window
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
                }
            }
        );
    }
}
