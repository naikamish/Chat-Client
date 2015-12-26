/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import databaselibrary.DatabaseLibrary;
import gmaillibrary.GmailLibrary;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
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
    private DatabaseLibrary dbLib;
    
    public Connection(Socket socket, DatabaseLibrary dbLib){
        connection = socket;
        Server.showMessage("\nNow connected to "+connection.getInetAddress().getHostName()+"\n");
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
        }
        catch(Exception e){//Server.showMessage("\nline32 connection\n");
        }
        setUpThread();
        
        try{
            this.dbLib=dbLib;
            //this.dbLib = new DatabaseLibrary("jdbc:mysql://localhost:3306/chat","root","password");
        }
        catch(Exception e){
            //Server.showMessage(e.toString());
        }
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
                                    addToGroup(message.groupName,message.clientName);
                                }
                                else if(message.cmd.equals("EXIT")){
                                    leaveGroup(message.groupName);
                                }
                                else if(message.cmd.equals("CREATE")){
                                    String query = "insert into groups(groupName) values('"+message.groupName+"');";
                                    try{
                                        dbLib.insertQuery(query);
                                        Server.createGroup(message.groupName);
                                    }
                                    catch(Exception e){
                                        Server.showMessage(e.toString()+"line 82");
                                    }
                                    //Server.createGroup(message.groupName);
                                }
                            }
                            else if(message.type.equals("MSG")){
                                Server.sendGroupMessage(message);//MSG", "SEND", message[2],message[3],message[4]});
                            }
                            else if(message.type.equals("SEND CODE")){
                                String query = "insert into registrationCodes(email,code) values('"+message.email+"',"+message.code+");";
                                try{
                                    dbLib.insertQuery(query);
                                    new GmailLibrary("mailToSecureYou","mailstodeliver",message.email,"Passcode","Code: "+message.code);
                                }
                                catch(Exception e){
                                    Server.showMessage(e.toString());
                                }
                            }
                            
                            else if(message.type.equals("REGISTER")){
                                String query = "select code from registrationcodes where email='"+message.email+"';";
                                try{
                                    ResultSet resultSet = dbLib.selectQuery(query);
                                    while (resultSet.next()) {
                                        if(resultSet.getInt("code")==message.code){
                                            String pass = new String(message.password);
                                            String query3 = "insert into users(name,username,password,email) values('"+message.name+"','"+message.username+"','"+pass+"','"+message.email+"');";
                                            try{
                                                dbLib.insertQuery(query3);
                                            }
                                            catch(Exception e){
                                                Server.showMessage(e.toString());
                                            }
                                        }
                                    }
                                }
                                catch(Exception e){}
                            }
                            
                            else if(message.type.equals("LOGIN")){
                                String pass = new String(message.password);
                                String query = "select username from users where username='"+message.username+"' and password='"+pass+"';";
                                try{
                                    ResultSet resultSet = dbLib.selectQuery(query);
                                    if (!resultSet.next() ) {
                                        sendMessage(new Message("LOGIN UNSUCCESSFUL","Invalid username/password"));
                                    }
                                    else{
                                        sendMessage(new Message("LOGIN SUCCESSFUL","LIST",Server.getGroupList()));
                                    }
                                }
                                catch(Exception e){
                                    Server.showMessage(e.toString()+"line 120");
                                }
                            }
                            Server.showMessage(message.toString());
                        }
                        catch(Exception e){
                            
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
            sendMessage(new Message("CMD", "START", g.getName(), clientList));//{"CMD", "STRT", g.getName(), "", clientList});
       // }
    }

    public String getName(){
        return clientName;
    }
}
