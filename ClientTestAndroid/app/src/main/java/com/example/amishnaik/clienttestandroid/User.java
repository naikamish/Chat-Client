package com.example.amishnaik.clienttestandroid;

/**
 * Created by Amish Naik on 3/12/2016.
 */
public class User {
    int userID;
    String userName;
    byte[] profileImage;

    public User(String userName, int userID, byte[] profileImage){
        this.userName = userName;
        this.userID = userID;
        this.profileImage = profileImage;
    }
}
