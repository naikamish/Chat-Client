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
    
    public void sendMessage(String[] message){
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
                            String[] message = (String[]) input.readObject();
                           // String source = message.substring(0,3);
                            //CMD JOIN group1,amish
                            if(message[0].equals("CMD")){
                              //  String action = message.substring(4,8);
                                if(message[1].equals("JOIN")){
                                 //   String info = message.substring(9);
                                    addToGroup(message[2],message[3]);
                                   // sendMessage
                                }
                                else if(message[1].equals("EXIT")){
                                    leaveGroup(message[2]);
                                   // Server.removeFromGroup(message[2],this);
                                }
                            }
                            //MSG group1 hello
                            else if(message[0].equals("MSG")){
                              //  String groupName = message.substring(4,10);
                                Server.sendGroupMessage(new String[]{"MSG", "SEND", message[2],message[3],message[4]});
                            }
                            StringBuilder strBuilder = new StringBuilder();
                            for (int i = 0; i < message.length; i++) {
                                strBuilder.append( message[i]+" " );
                             }
                            Server.showMessage(strBuilder.toString());
                           // g.sendMessage(""+message);
                        }
                        catch(Exception e){//Server.showMessage("\nline53 connection\n");
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
       // String[] data = info.split(",");
        clientName = user;
        Group g = Server.addToGroup(this, group);
       // clientName = user;
        String clientList = g.getClientList();
        if(!clientList.equals("")){
            sendMessage(new String[]{"CMD", "STRT", g.getName(), "", clientList});
            //Server.showMessage("\nCMD - STRT "+g.getName()+" "+clientList);
        }
       // g.sendMessage(new String[]{"CMD", "ADDS", g.getName(), "", clientName});//CMD ADDS "+g.getName()+" "+clientName);
    }
    
    public void setGroup(Group g){
        group = g;
        sendClientList();
      //  setUpThread(group);
        Server.showMessage("\n"+group.getName()+"\n");
    }
    
    public void getInfo(){
        try{
            //Read the string sent by the client which says what group 
            //they want to be part of then get the server to set 
            //this connections group
            String message = (String) input.readObject();
            String[] data = message.split(",");
            
            Server.setGroup(this, data[0]);
           // String message2 = (String) input.readObject();
            clientName = data[1];
           // group.sendMessage("SERVER - ADD "+clientName);
        }
        catch(Exception e){//Server.showMessage("\nline80 connection\n");
        }
    }
    
    public String getName(){
        return clientName;
    }
    
    public void sendClientList(){
        String clientList = group.getClientList();
        if(!clientList.equals("")){
           // sendMessage("SERVER - SRT "+group.getClientList());
        }
    }
}
