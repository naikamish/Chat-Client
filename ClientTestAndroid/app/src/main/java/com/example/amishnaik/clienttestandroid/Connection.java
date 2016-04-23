package com.example.amishnaik.clienttestandroid;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

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
    public static Context _CONTEXT;
    private static int notificationCount, notificationGroupID;

    public Connection(Socket socket, LoginActivity loginForm, Context c){
        this._CONTEXT = c;
        notificationCount = 0;
        notificationGroupID = 0;
        login = loginForm;
        connection = socket;
        try{
            output = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            output.flush();
            input = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
            // sendMessage(new Message("LOGIN","hello", "hello"));
        }
        catch(Exception e){e.toString();}
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
                chat.messages.add(message);
                if(activeChatWindow.getGroupID()==message.groupID){
                    activeChatWindow.showMessage(message);
                }
            }
        }
    }

    public static void setActiveChat(ChatWindow chatWindow){
        activeChatWindow = chatWindow;
    }

    public static ChatWindow getActiveChat(){
        return activeChatWindow;
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

    public void createGroupList(String[] groupNames, int[] groupIDs, byte[][] groupImages){
        for(int i=0; i<groupNames.length; i++){
            groups.add(new Group(groupNames[i], groupIDs[i], groupImages[i]));
            System.out.println(groupImages[i].length);
        }
    }

    public void addGroup(String groupName, int groupID, byte[] groupImage){
        groups.add(new Group(groupName, groupID, groupImage));
    }

    public void createGroupList(String[] groupNames, int[] groupIDs){
        for(int i=1; i<groupNames.length; i++){
            groups.add(new Group(groupNames[i], groupIDs[i]));
        }
    }

    public void addGroup(String groupName, int groupID){
        groups.add(new Group(groupName, groupID));
    }

    private void addGroupMember(Message message){
        for(Chat chat:chats){
            if(chat.groupID==message.groupID){
                chat.addUser(new User(message.clientName, message.userID));
            }
        }
    }

    private void removeGroupMember(int groupID, int userID){
        for(Chat chat:chats){
            if(chat.groupID==groupID){
                chat.removeUser(userID);
            }
        }
    }

    private void enforceBan(int userID, int groupID){
        if(this.userID==userID){
            for(Chat chat:chats){
                if(chat.groupID==groupID){
                    chats.remove(chat);
                }
                if(activeChatWindow.getGroupID()==groupID){
                    activeChatWindow.enforceBan();
                }
            }
        }
    }

    private void showNotification(String message, String groupName, int groupID){
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
            buildNotification(message, groupName, groupID);
        }


    }

    private void buildNotification(String message, String groupName, int groupID){
        String notificationMessage;
        String notificationTitle;
        notificationCount++;
        if(notificationGroupID==0){
            notificationGroupID=groupID;
            notificationTitle = groupName;
        }
        else if(groupID!=notificationGroupID){
            notificationTitle = groupName + " and other groups";
        }
        else{
            notificationTitle = groupName;
        }
        if(notificationCount==1){
            notificationMessage=message;
        }
        else{
            notificationMessage=Integer.toString(notificationCount) + " new messages";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(_CONTEXT)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage);
        mBuilder.setVibrate(new long[] { 1000, 1000 });

        //LED
        mBuilder.setLights(Color.GREEN, 3000, 3000);
        mBuilder.setAutoCancel(true);
        Intent resultIntent = new Intent(_CONTEXT, ChatWindow.class);
        resultIntent.putExtra("groupID", groupID);
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

    public static void clearNotifications(){
        notificationCount=0;
        notificationGroupID=0;
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
                                        channelListController.sendGroupList(message.groupID, message.clientList, message.groupUserIDs, message.creatorID, message.groupName);//groupName, groupList);
                                    }
                                    else if(message.cmd.equals("ADD")){
                                        String[] client = {message.clientName};
                                        int[] clientID = {message.userID};
                                        addGroupMember(message);
                                        showMessage(message);
                                        //addGroupMember(message.groupID, client, clientID, message.creatorID);
                                    }
                                    else if(message.cmd.equals("REMOVE")){
                                        removeGroupMember(message.groupID, message.userID);
                                        showMessage(message);
                                        //channelListController.deleteFromList(message.groupID, message.userID);
                                    }
                                    else if(message.cmd.equals("CREATE")){
                                        //addGroup(message.groupName, message.groupID);
                                        addGroup(message.groupName, message.groupID, message.file);
                                    }
                                }
                                else if(message.type.equals("BANNED")){
                                    channelListController.enforceBan();
                                }
                                else if(message.type.equals("MSG")){
                                    showMessage(message);
                                    showNotification(message.message, message.groupName, message.groupID);
                                }
                                else if(message.type.equals("LOGIN SUCCESSFUL")){
                                    //createGroupList(message.groupList, message.groupIDList);
                                    createGroupList(message.groupList, message.groupIDList, message.groupImages);
                                    setUsername(message.username);
                                    setUserID(message.userID);
                                    login.successfullyLoggedIn();
                                }
                                else if(message.type.equals("LOGIN UNSUCCESSFUL")){
                                    login.unsuccessfulLogin(message.message);
                                }
                                else if(message.type.equals("BAN")){
                                    enforceBan(message.userID, message.groupID);
                                }
                                //Client.showMessage(message);
                            }
                            catch(Exception e){e.toString();}
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
        catch(Exception e){e.toString();}
    }
}
