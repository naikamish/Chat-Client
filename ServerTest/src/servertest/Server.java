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
import javax.swing.*;

//separate connection,output, and input into their own connection class
    

public class Server extends JFrame{
    
    private JTextArea chatWindow;
    
    //This is how computers communicate with each other
    private ArrayList<ObjectOutputStream> output = new ArrayList<ObjectOutputStream>(); //Output is messages we send out
    private ArrayList<ObjectInputStream> input = new ArrayList<ObjectInputStream>(); //Input is messages we receive
    
    
    private ServerSocket server;
    private ArrayList<Socket> connection = new ArrayList<Socket>();
    
    //constructor
    public Server(){
        super("Server Chat");
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow));
        setSize(300,150);
        setVisible(true);
    }
    
    //Set upand run the server
    public void startRunning(){
        try{
            server = new ServerSocket(5000, 100); //First number is port and second number is backlog of how many people can access server
            while(true){
                try{
                    waitForConnection();
                    setupStream();
                    whileChatting();
                }
                
                catch(EOFException eofException){//EOF Exception represents end of a stream
                    showMessage("\n Server ended the connection!");
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
    private void waitForConnection() throws IOException{
        showMessage("Waiting for someone to connect...\n");
        
        //Listens for a connection to be made accepts it
        connection.add(server.accept()); //Throw ioException if no connection is made
        showMessage("Now connect to "+connection.get(0).getInetAddress().getHostName());
        showMessage("\nWaiting for Second connection");
        connection.add(server.accept());
        //If ioException occurs when there is no connection, the try 
        //block will end and the while loop will start again
        showMessage("Now connect to "+connection.get(1).getInetAddress().getHostName());
        
    }
    
    //Get stream to send and receive data
    private void setupStream() throws IOException{
        output.add(new ObjectOutputStream(connection.get(0).getOutputStream()));
        output.get(0).flush();
        
        input.add(new ObjectInputStream(connection.get(0).getInputStream()));
        showMessage("\n Streams1 are now setup! \n");   
        
        
        output.add(new ObjectOutputStream(connection.get(1).getOutputStream()));
        output.get(1).flush();
        
        input.add(new ObjectInputStream(connection.get(1).getInputStream()));
        showMessage("\n Streams1 are now setup! \n");
    }
    
    //Occurs during the conversation
    private void whileChatting() throws IOException{
        String message = "Connection Successful";
        sendMessage(message);
        
        do{
            try{
                message = (String) input.get(0).readObject();
                showMessage("\n"+message);
                sendMessage("\n"+message);
                
                message = (String) input.get(1).readObject();
                showMessage("\n"+message);
                sendMessage("\n"+message);
            }
            catch(ClassNotFoundException classNotFoundException){
                showMessage("\n Class Not Found Exception");
            }           
        }
        while(!message.equals("CLIENT - END"));
    }
    
    //Close streams and sockets at the end of chat
    public void closeStreams(){
        showMessage("\n Closing connection... \n");
        try{
            //Close Streams and Connection
            output.get(0).close();
            input.get(0).close();
            connection.get(0).close();
            
            output.get(1).close();
            input.get(1).close();
            connection.get(1).close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    //Send message to client
    private void sendMessage(String message){
        try{
            
            output.get(0).writeObject(message);
            output.get(0).flush();
            
            output.get(1).writeObject(message);
            output.get(1).flush();
            
            showMessage(message);
        }
        catch(IOException ioException){
            chatWindow.append("\n ERROR: COULDN'T SEND MESSAGE");
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
    
    //Toggle users ability to type into message box
}
