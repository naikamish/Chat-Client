/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Amish Naik
 */
public class Connection {
    private ObjectOutputStream output;
    public ObjectInputStream input;
    private Socket connection;
    private Thread thread;
    
    public Connection(Socket socket){
        connection = socket;
        try{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        }
        catch(Exception e){}
    }
    
    public void setUpThread(){
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            String[] message = (String[]) input.readObject();
                            if(message[0].equals("CMD")){
                                if(message[1].equals("STRT")||message[1].equals("ADDS")){
                                    Client.sendGroupList(message[2],message[4]);//groupName, groupList);
                                }
                                else if(message[1].equals("RMOV")){
                                    Client.deleteFromList(message[2],message[3]);
                                }
                                else if(message[1].equals("CRTE")){
                                    Client.addGroup(message[2]);
                                }
                            }
                            Client.showMessage(message);
                        }
                        catch(Exception e){//Server.showMessage("\nline53 connection\n");
                        }
                    }
                }
            });
        thread.start();
    }
    
    public void sendMessage(String[] message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){}
    }   
}
