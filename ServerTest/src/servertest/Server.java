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
            
            while(true){
                try{
                    waitForConnection();
                    server.setSoTimeout(100);
                    setupStream();
                    whileChatting();
                }
                
              /*  catch(EOFException eofException){//EOF Exception represents end of a stream
                    showMessage("\n Server ended the connection!");
                }*/
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
        showMessage("Waiting for someone to connect...\n");
        try{
            connection.add(server.accept());
            connection.get(0).setSoTimeout(100);
        }
        catch(Exception e){}
        showMessage("Now connected to "+connection.get(0).getInetAddress().getHostName());
        
        //Listens for a connection to be made accepts it
     /*   connection.add(server.accept()); //Throw ioException if no connection is made
        showMessage("Now connect to "+connection.get(0).getInetAddress().getHostName());
        showMessage("\nWaiting for Second connection");
        connection.add(server.accept());
        showMessage("Now connect to "+connection.get(1).getInetAddress().getHostName());*/
        
    }
    
    //Get stream to send and receive data
    private void setupStream() throws IOException{
        output.add(new ObjectOutputStream(connection.get(0).getOutputStream()));
        output.get(0).flush();
        
        input.add(new ObjectInputStream(connection.get(0).getInputStream()));
        
        
   /*     output.add(new ObjectOutputStream(connection.get(1).getOutputStream()));
        output.get(1).flush();
        
        input.add(new ObjectInputStream(connection.get(1).getInputStream()));*/
    }
    
    //Occurs during the conversation
    private void whileChatting() throws IOException{
        String message="hi";
        do{
            try{
                checkAdditionalConnections();
                checkNewMessages();
                
             /*   message = (String) input.get(1).readObject();
                showMessage("\n"+message);
                sendMessage("\n"+message);*/
            }
            catch(Exception e){
                showMessage("\n Class Not Found Exception");
            }           
        }
        while(!message.equals("CLIENT - END"));
    }
    
    private void checkAdditionalConnections() throws IOException{
     /*   executor.submit(new Runnable(){
            public void run(){
                try{
                connection.add(server.accept());
                showMessage("Now connected to "+connection.get(0).getInetAddress().getHostName()+"\n");
                }
                catch(Exception e){}
            }
        });
        executor.shutdown();*/
        try{
        connection.add(server.accept());
        connection.get(connection.size()-1).setSoTimeout(100);
        showMessage("\nNow connected to "+connection.get(connection.size()-1).getInetAddress().getHostName());
        
        output.add(new ObjectOutputStream(connection.get(connection.size()-1).getOutputStream()));
        output.get(0).flush();
        
        input.add(new ObjectInputStream(connection.get(connection.size()-1).getInputStream()));
        }
        catch(Exception e){}
    }
    
    private void checkNewMessages(){
        for(ObjectInputStream socket:input){
            try{
            String message = (String) socket.readObject();
            showMessage("\n"+message);
            sendMessage("\n"+message);
            }
            catch(Exception e){}
        }
    }
    
    //Close streams and sockets at the end of chat
    public void closeStreams(){
        showMessage("\n Closing connection... \n");
        try{
            //Close Streams and Connection
            output.get(0).close();
            input.get(0).close();
            connection.get(0).close();
            
          /*  output.get(1).close();
            input.get(1).close();
            connection.get(1).close();*/
        }
        catch(IOException ioException){
            ioException.printStackTrace();
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
