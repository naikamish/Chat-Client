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
                            //String source = message.substring(0,3);
                            //CMD STRT group1 a,b,c,d
                            if(message[0].equals("CMD")){
                               // String action = message.substring(4,8);
                                if(message[1].equals("STRT")||message[1].equals("ADDS")){
                                 //   String groupName=message.substring(9,15);
                                 //   String groupList = message.substring(16);
                                    
                                    Client.sendGroupList(message[2],message[4]);//groupName, groupList);
                                   // Client.showMessage(groupName,message);
                                }
                                else if(message[1].equals("RMOV")){
                                    Client.deleteFromList(message[2],message[3]);
                                }
                            }
                            //MSG group1 hello
                          //  else if(message[0].equals("MSG")){
                               // String groupName = message.substring(4,10);
                                Client.showMessage(message);
                                //Client.showMessage(message[2],message[3], message[4]);//groupName,message);
                         //   }
                            //Server.showMessage("\n"+message);
                           // g.sendMessage(""+message);
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
