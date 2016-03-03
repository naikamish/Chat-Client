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
        messages = new ArrayList<Message>();
    }
}
