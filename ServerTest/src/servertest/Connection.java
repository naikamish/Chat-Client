/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import message.Message;

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
        Server.showMessage("\nNow connected to "+connection.getInetAddress().getHostName()+"\n");
        try{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        }
        catch(Exception e){//Server.showMessage("\nline32 connection\n");
        }
        setUpThread();
    }
    
    public void sendMessage(Message message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){//Server.showMessage("\nline40 connection\n");
        }
    }
    
    private void setUpThread(){
        thread= new Thread(
            new Runnable(){
                public void run(){
                    while(true){
                        try{
                            Message message = (Message) input.readObject();
                            if(message.type.equals("CMD")){
                                if(message.cmd.equals("JOIN")){
                                    Server.showMessage(message.toString());
                                    addToGroup(message.groupName,message.clientName);
                                }
                                else if(message.cmd.equals("EXIT")){
                                    leaveGroup(message.groupName);
                                }
                                else if(message.cmd.equals("CRTE")){
                                    Server.createGroup(message.groupName);
                                }
                            }
                            else if(message.type.equals("MSG")){
                                Server.sendGroupMessage(message);//MSG", "SEND", message[2],message[3],message[4]});
                            }
                         /*   StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < message.length; i++) {
                                strBuilder.append( message[i]+" " );
                             }*/
                            
                            
                            Server.showMessage(message.toString());
                        }
                        catch(Exception e){//Server.showMessage(e.toString());
                        }
                    }
                }
            });
        thread.start();
    }
    
    public void leaveGroup(String g){
        Server.removeFromGroup(this, g);
    }
    
    public void addToGroup(String group, String user){
        clientName = user;
        Group g = Server.addToGroup(this, group);
        String[] clientList = g.getClientList();
       // if(!clientList.equals("")){
            sendMessage(new Message("CMD", "STRT", g.getName(), clientList));//{"CMD", "STRT", g.getName(), "", clientList});
       // }
    }

    public String getName(){
        return clientName;
    }
}
