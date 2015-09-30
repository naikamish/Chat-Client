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
    
    public Connection(Socket socket){
        connection = socket;
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
    
    public void close(){
        try{
            input.close();
            output.close();
            connection.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
}
