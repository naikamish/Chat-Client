/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

/**
 *
 * @author Amish Naik
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import message.Message;


public class Connection {
    private static ObjectOutputStream output;
    public static ObjectInputStream input;
    private static Socket connection;
    private Thread thread;
    private LoginController login;
    private ChannelListController channelListController;
    
    public Connection(Socket socket, LoginController loginForm){
        login = loginForm;
        connection = socket;
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
            //sendMessage(new Message("LOGIN","hello", "hello"));
        }
        catch(Exception e){System.out.println("Error Connection line 38");}
    }
    
    public void setChannelListController(ChannelListController controller){
        channelListController = controller;
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
                                    channelListController.sendGroupList(message.groupName, message.clientList);//groupName, groupList);
                                }
                                else if(message.cmd.equals("ADD")){
                                    String[] client = {message.clientName};
                                    channelListController.sendGroupList(message.groupName,client);
                                }
                                else if(message.cmd.equals("REMOVE")){
                                    channelListController.deleteFromList(message.groupName, message.clientName);
                                }
                                else if(message.cmd.equals("CREATE")){
                                    channelListController.addGroup(message.groupName);
                                }
                            }
                            else if(message.type.equals("MSG")){
                                channelListController.showMessage(message);
                            }
                            else if(message.type.equals("LOGIN SUCCESSFUL")){
                                System.out.println("login successful");
                                login.successfullyLoggedIn(message.groupList);
                            }
                            else if(message.type.equals("LOGIN UNSUCCESSFUL")){
                                login.unsuccessfulLogin(message.message);
                            }
                            //Client.showMessage(message);
                        }
                        catch(Exception e){System.out.println(e.toString());}
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
        catch(Exception e){System.out.println("Error Connection line 87");}
    }   
}
