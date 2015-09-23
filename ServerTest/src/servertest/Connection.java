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
    
    public Connection(Socket socket){
        connection = socket;
        Server.showMessage("Now connected to "+connection.getInetAddress().getHostName()+"\n");
        try{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        }
        catch(Exception e){}
        sendMessage("a");
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            String message = (String) input.readObject();
                            Server.showMessage("\n"+message);
                            Server.sendMessage("\n"+message);
                        }
                        catch(Exception e){}
                    }
                }
            });
        thread.start();
    }
    
    public void sendMessage(String message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){}
    }
}
