package com.example.amishnaik.clienttestandroid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;

/**
 * Created by Amish Naik on 2/28/2016.
 */
public class Connection {
    private static ObjectOutputStream output;
    public static ObjectInputStream input;
    private static Socket connection;
    private static Thread thread;
    private static LoginActivity login;
    private static GroupList channelListController;

    public Connection(Socket socket, LoginActivity loginForm){
        login = loginForm;
        connection = socket;
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
            // sendMessage(new Message("LOGIN","hello", "hello"));
        }
        catch(Exception e){System.out.println("connection line 40");}
    }

    public void setChannelListController(GroupList controller){
        channelListController = controller;
    }

    public void setUpThread(){
        thread= new Thread(
                new Runnable(){
                    public void run(){
                        while(true){
                            try{
                                Message message = (Message) input.readObject();
                                System.out.println(message.type);
                                if(message.type.equals("CMD")){
                                    if(message.cmd.equals("START")){
                                        //channelListController.sendGroupList(message.groupID, message.clientList, message.groupUserIDs, message.creatorID);//groupName, groupList);
                                    }
                                    else if(message.cmd.equals("ADD")){
                                        String[] client = {message.clientName};
                                        int[] clientID = {message.userID};
                                        //channelListController.addGroupMember(message.groupID, client, clientID, message.creatorID);
                                    }
                                    else if(message.cmd.equals("REMOVE")){
                                        //channelListController.deleteFromList(message.groupID, message.userID);
                                    }
                                    else if(message.cmd.equals("CREATE")){
                                        //channelListController.addGroup(message.groupName, message.groupID);
                                    }
                                }
                          /*  else if(message.type.equals("BANNED")){
                                JOptionPane.showMessageDialog(null,"You have been banned from this group");
                            }*/
                                else if(message.type.equals("MSG")){
                                    //channelListController.showMessage(message);
                                }
                                else if(message.type.equals("LOGIN SUCCESSFUL")){
                                    login.successfullyLoggedIn(message.groupList,message.userID, message.groupIDList);
                                }
                                else if(message.type.equals("LOGIN UNSUCCESSFUL")){
                                    login.unsuccessfulLogin(message.message);
                                }
                          /*  else if(message.type.equals("BAN")){
                                channelListController.enforceBan(message.userID, message.groupID);
                            }*/
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
        catch(Exception e){System.out.println("connection line 100");}
    }
}
