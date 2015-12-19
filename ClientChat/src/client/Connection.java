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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import message.Message;


public class Connection {
    private static ObjectOutputStream output;
    public static ObjectInputStream input;
    private static Socket connection;
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
                            Message message = (Message) input.readObject();
                            if(message.type.equals("CMD")){
                                if(message.cmd.equals("START")){
                                    Client.sendGroupList(message.groupName, message.clientList);//groupName, groupList);
                                }
                                else if(message.cmd.equals("ADD")){
                                    String[] client = {message.clientName};
                                    Client.sendGroupList(message.groupName,client);
                                }
                                else if(message.cmd.equals("REMOVE")){
                                    Client.deleteFromList(message.groupName, message.clientName);
                                }
                                else if(message.cmd.equals("CREATE")){
                                    Client.addGroup(message.groupName);
                                }
                            }
                            else if(message.type.equals("MSG")){
                                Client.showMessage(message);
                            }
                            else if(message.type.equals("LOGIN SUCCESSFUL")){
                                Login.successfullyLoggedIn(message.groupList);
                            }
                            //Client.showMessage(message);
                        }
                        catch(Exception e){//Server.showMessage("\nline53 connection\n");
                        }
                    }
                }
            });
        thread.start();
    }
    
    public static void sendMessage(Message message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){}
    }   
}
