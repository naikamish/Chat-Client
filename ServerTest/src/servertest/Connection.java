/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Amish Naik
 */
public class Connection{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private Thread thread;
    private Group group;
    private String clientName;
    
    public Connection(Socket socket){
        connection = socket;
        Server.showMessage("Now connected to "+connection.getInetAddress().getHostName()+"\n");
        try{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        }
        catch(Exception e){}
    }
    
    public void sendMessage(String message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){}
    }
    
    private void setUpThread(Group g){
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            String message = (String) input.readObject();
                            Server.showMessage("\n"+message);
                            g.sendMessage("\n"+message);
                        }
                        catch(Exception e){}
                    }
                }
            });
        thread.start();
    }
    
    public void setGroup(Group g){
        group = g;
        sendClientList();
        setUpThread(group);
        Server.showMessage("\n"+group.getName()+"\n");
    }
    
    public void getInfo(){
        try{
            //Read the string sent by the client which says what group 
            //they want to be part of then get the server to set 
            //this connections group
            String message = (String) input.readObject();
            Server.setGroup(this, message);
            String message2 = (String) input.readObject();
            clientName = message2;
            group.sendMessage("SERVER - ADD "+clientName);
        }
        catch(Exception e){}
    }
    
    public String getName(){
        return clientName;
    }
    
    public void sendClientList(){
        sendMessage("SERVER - SRT "+group.getClientList());
    }
}
