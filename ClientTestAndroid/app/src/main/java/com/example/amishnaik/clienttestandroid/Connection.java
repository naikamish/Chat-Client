package com.example.amishnaik.clienttestandroid;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private static ArrayList<Chat> chats = new ArrayList<Chat>();
    public static ArrayList<Group> groups = new ArrayList<Group>();
    private static ChatWindow activeChatWindow;
    public static String username;
    public static int userID;
    private Context _CONTEXT;

    public Connection(Socket socket, LoginActivity loginForm, Context c){
        this._CONTEXT = c;
        login = loginForm;
        connection = socket;
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
            // sendMessage(new Message("LOGIN","hello", "hello"));
        }
        catch(Exception e){}
    }

    public static void setChannelListController(GroupList controller){
        channelListController = controller;
    }

    public static void addChat(Chat chat){
        chats.add(chat);
    }

    public static Chat bindChat(int groupID){
        for(Chat chat:chats){
            if(chat.groupID==groupID){
                return chat;
            }
        }
        return new Chat();
    }

    private void showMessage(Message message){
        for(Chat chat:chats){
            if(chat.groupID==message.groupID){
                if(message.cmd.equals("SEND")){
                    chat.messages.add(message);
                    if(activeChatWindow.getGroupID()==message.groupID){
                        activeChatWindow.showMessage(message);
                    }
                }
                else if(message.cmd.equals("DOODLE")){
                    //chat.showDoodle(message);
                }
                else if(message.cmd.equals("FILE")){
                    //chat.showFile(message);
                }
            }
        }
    }

    public static void setActiveChat(ChatWindow chatWindow){
        activeChatWindow = chatWindow;
    }

    public static boolean checkExistingChatWindow(int selectedGroupID){
        for(Chat chat:chats){
            if(chat.groupID==selectedGroupID){
                return true;
            }
        }
        return false;
    }

    public static void setUsername(String name){
        username = name;

    }

    public void setUserID(int id){
        userID = id;
        System.out.println("abc"+userID+"conn");
    }

    public void createGroupList(String[] groupNames, int[] groupIDs){
        for(int i=1; i<groupNames.length; i++){
            groups.add(new Group(groupNames[i], groupIDs[i]));
        }
    }

    private void showNotification(){
        ActivityManager activityManager = (ActivityManager) _CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(_CONTEXT.getPackageName().toString())) {
            isActivityFound = true;
        }

        if (isActivityFound) {
        } else {
            buildNotification();
        }


    }

    private void buildNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(_CONTEXT)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(_CONTEXT, GroupList.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(_CONTEXT);
        stackBuilder.addParentStack(GroupList.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) _CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, mBuilder.build());
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
                                        channelListController.sendGroupList(message.groupID, message.clientList, message.groupUserIDs, message.creatorID);//groupName, groupList);
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
                                    showMessage(message);
                                    showNotification();
                                }
                                else if(message.type.equals("LOGIN SUCCESSFUL")){
                                    createGroupList(message.groupList, message.groupIDList);
                                    setUsername(message.username);
                                    setUserID(message.userID);
                                    login.successfullyLoggedIn();
                                }
                                else if(message.type.equals("LOGIN UNSUCCESSFUL")){
                                    login.unsuccessfulLogin(message.message);
                                }
                          /*  else if(message.type.equals("BAN")){
                                channelListController.enforceBan(message.userID, message.groupID);
                            }*/
                                //Client.showMessage(message);
                            }
                            catch(Exception e){}
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
