package com.example.amishnaik.clienttestandroid;

import java.util.ArrayList;

/**
 * Created by Amish Naik on 3/1/2016.
 */
public class Group {
    String groupName;
    int groupID;
    byte[] groupImage;

    public Group(String groupName, int groupID, byte[] groupImage){
        this.groupName = groupName;
        this.groupID = groupID;
        this.groupImage = groupImage;
    }

    public Group(String groupName, int groupID){
        this.groupName = groupName;
        this.groupID = groupID;
    }
}
