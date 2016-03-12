package com.example.amishnaik.clienttestandroid;

import java.util.ArrayList;

import message.Message;

/**
 * Created by Amish Naik on 2/29/2016.
 */
public class Chat {
    public int groupID, creatorID, userID;
    public String username, groupName;
    public ArrayList<Message> messages;
    public ArrayList<User> users = new ArrayList<User>();

    public Chat(){
        groupID=0;
        creatorID=0;
        username="";
        userID=0;
        groupName = "";
        messages = new ArrayList<Message>();
    }

    public Chat(int groupID, String[] clientList, int[] groupUserIDs, int creatorID, String username, int userID, String groupName) {
        this.groupID = groupID;
        this.creatorID = creatorID;
        this.username = username;
        this.userID = userID;
        this.groupName = groupName;
        for(int i=0; i<groupUserIDs.length; i++){
            users.add(new User(clientList[i],groupUserIDs[i]));
        }
        messages = new ArrayList<Message>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(int userID){
        for(User user:users){
            if(user.userID==userID){
                users.remove(user);
            }
        }
    }
}
