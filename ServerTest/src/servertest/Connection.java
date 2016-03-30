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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.util.Random;
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
    private LoadDriver dbLib;
    private int userID;
    
    public Connection(Socket socket, LoadDriver dbLib){
        connection = socket;
        Server.showMessage("\nNow connected to "+connection.getInetAddress().getHostName()+"\n");
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
        }
        catch(Exception e){Server.showMessage("conn line 44" + e.toString());
        }
        setUpThread();
        
        try{
            this.dbLib=dbLib;
            //this.dbLib = new DatabaseLibrary("jdbc:mysql://localhost:3306/chat","root","password");
        }
        catch(Exception e){Server.showMessage("conn line 52" + e.toString());
            //Server.showMessage(e.toString());
        }
    }
    
    public void sendMessage(Message message){
        try{
        output.writeObject(message);
        output.flush();
        }
        catch(Exception e){Server.showMessage("conn line 62" + e.toString());
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
                                    String query = "select groupID from bans where groupID="+message.groupID+" and userID="+message.userID+";";
                                    try{
                                        ResultSet resultSet = dbLib.selectQuery(query);
                                        if (!resultSet.next() ) {
                                            addToGroup(message.groupID,message.clientName, message.userID);
                                        }
                                        else{          
                                            Message newMessage = new Message();
                                            newMessage.type = "BANNED";
                                            sendMessage(newMessage);                                            
                                        }
                                    }
                                    catch(Exception e){Server.showMessage("conn line 87" + e.toString());}
                                    
                                    
                                }
                                else if(message.cmd.equals("EXIT")){
                                    leaveGroup(message.groupID);
                                    Server.showMessage(Integer.toString(message.userID));
                                }
                                else if(message.cmd.equals("CREATE")){
                                    byte [] bytearray  = message.file;
                                    String filename;
                                    
                                    if(bytearray.length>0){
                                        Random rand = new Random();
                                        int  n = rand.nextInt(2000000);
                                        filename = n+"."+"png";
                                        message.filename = filename;
                                        FileOutputStream fos = new FileOutputStream(filename);
                                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                                        bos.write(bytearray, 0 , bytearray.length);
                                        bos.flush();
                                        bos.close();
                                    }
                                    else{ filename = "x.png";}
                                    Server.showMessage(filename);
                                    
                                    String query = "insert into groups(groupName, userID, groupImage) values(?,?,?);";
                    
                                    try{
                                        int groupID=dbLib.insertQueryReturnKey(query, message.groupName, message.userID, filename);
                                        Server.createGroup(message.groupName, groupID, message.userID, message.file);
                                    }
                                    catch(Exception e){
                                        Server.showMessage("conn line 104" + e.toString());
                                    }
                                    //Server.createGroup(message.groupName);
                                }
                            }
                            else if(message.type.equals("MSG")){
                                if(message.cmd.equals("FILE")||message.cmd.equals("DOODLE")){
                                    byte [] bytearray  = message.file;
                                    Random rand = new Random();
                                    int  n = rand.nextInt(2000000);
                                    String filename = n+"."+message.extension;
                                    message.filename = filename;
                                    FileOutputStream fos = new FileOutputStream(filename);
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    bos.write(bytearray, 0 , bytearray.length);
                                    bos.flush();
                                    bos.close();
                                    Server.sendGroupMessage(message);
                                }
                                else{
                                    Server.sendGroupMessage(message);//MSG", "SEND", message[2],message[3],message[4]});                           
                                }
                            }
                            else if(message.type.equals("SEND CODE")){
                                String query = "insert into registrationCodes(email,code) values('"+message.email+"',"+message.code+");";
                                try{
                                    dbLib.insertQuery(query);
                                    new GmailLibrary("mailToSecureYou","mailstodeliver",message.email,"Passcode","Code: "+message.code);
                                }
                                catch(Exception e){
                                    Server.showMessage("conn line 134" + e.toString());
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
                                                Server.showMessage("conn line 150" + e.toString());
                                            }
                                        }
                                    }
                                }
                                catch(Exception e){Server.showMessage("conn line 155" + e.toString());}
                            }
                            
                            else if(message.type.equals("LOGIN")){
                                String pass = message.password;
                                String query = "select username, userID, email from users where username='"+message.username+"' and password='"+pass+"';";
                                try{
                                    ResultSet resultSet = dbLib.selectQuery(query);
                                    if (!resultSet.next() ) {
                                        sendMessage(new Message("LOGIN UNSUCCESSFUL","Invalid username/password"));
                                        Server.showMessage("login unsuccessful\n");
                                    }
                                    else{
                                        int userID=0;
                                        //while(resultSet.next()){
                                        userID = resultSet.getInt("userID");
                                        clientName = resultSet.getString("username");
                                            //resultSet.getInt("code")==message.code
                                            
                                        /*query = "select code from codesss where email='"+resultSet.getString("email")+"';";
                                        resultSet = dbLib.selectQuery(query);
                                        resultSet.next();
                                        if(!resultSet.getString("code").matches("1"))
                                        {
                                            sendMessage(new Message("LOGIN UNSUCCESSFUL","Account activation pending"));
                                            Server.showMessage("login unsuccessful\n");
                                        }
                                        else
                                        {*/
                                        Server.showMessage(Integer.toString(userID));
                                        Message userMessage = new Message("LOGIN SUCCESSFUL","LIST",Server.getGroupList());
                                        userMessage.userID=userID;
                                        userMessage.username = clientName;
                                        userMessage.groupIDList=Server.getGroupID();
                                        userMessage.groupImages=Server.getGroupImages();
                                        sendMessage(userMessage);
                                        Server.showMessage("login successful\n");
                                        //}
                                    }
                                }
                                catch(Exception e){
                                    Server.showMessage(e.toString()+"line 120");
                                }
                            }
                            
                            else if(message.type.equals("BAN")){
                                String query = "insert into bans(userID, groupID) values("+message.userID+","+message.groupID+");";
                                try{
                                    dbLib.insertQuery(query);
                                }
                                catch(Exception e){
                                    Server.showMessage("conn line 206" + e.toString());
                                }
                                Server.sendGroupMessage(message);
                            }
                            Server.showMessage(message.toString());
                        }
                        catch(SocketException e){
                            removeConnection();
                        }
                        catch(Exception e){
                        }
                    }
                }
            });
        thread.start();
    }
    
    private void removeConnection(){
        Server.removeConnection(this, userID);
    }
    
    public void leaveGroup(int g){
        Server.removeFromGroup(this, g);
    }
    
    public void addToGroup(int groupID, String user, int userID){
        clientName = user;
        this.userID = userID;
        Group g = Server.addToGroup(this, groupID);
        String[] clientList = g.getClientList();
        int[] clientListIDs = g.getClientIDs();
        String groupName = g.getName();
        Message message = new Message("CMD", "START", g.getID(), clientList);
        message.groupUserIDs = clientListIDs;
        message.creatorID = g.getCreatorID();
        message.groupName = groupName;
        
       // if(!clientList.equals("")){
            sendMessage(message);//{"CMD", "STRT", g.getName(), "", clientList});
       // }
    }

    public String getName(){
        return clientName;
    }
    
    public int getID(){
        return userID;
    }
}
