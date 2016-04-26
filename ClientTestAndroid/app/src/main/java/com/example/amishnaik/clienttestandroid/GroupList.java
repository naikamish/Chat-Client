package com.example.amishnaik.clienttestandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import message.Message;

public class GroupList extends AppCompatActivity {
    private String username = "";
    private int userID;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = Connection.username;
        userID = Connection.userID;
        for(Group group:Connection.groups){
            addGroup(group.groupName, group.groupID, group.groupImage);
        }

        connectButton = (Button) findViewById(R.id.button);
        Connection.setChannelListController(this);
    }

    public void connectButtonClick(View view) {
        if(Connection.checkExistingChatWindow(connectButton.getId())){
            startChatActivity(connectButton.getId());
        }
        else {
            Message message = new Message("CMD", "JOIN", connectButton.getId(), username);
            message.userID = this.userID;
            sendMessage(message);
        }
    }

    public void connectButtonClick(int id) {
        if(Connection.checkExistingChatWindow(id)){
            startChatActivity(id);
        }
        else {
            Message message = new Message("CMD", "JOIN", id, username);
            message.userID = this.userID;
            sendMessage(message);
        }
    }

    public void sendMessage(Message message){
        Connection.sendMessage(message);
    }

    public void addGroup(String group, int groupID, byte[] groupImage){
        try {
            LinearLayout hbox = new LinearLayout(this);
            hbox.setClickable(true);
            hbox.setBackgroundResource(android.R.drawable.list_selector_background);
            //hbox.getStyleClass().add("chatListHBox");
            hbox.setId(groupID);

            ImageView icon = new ImageView(this);
            if(groupImage.length!=0) {
                Bitmap bm = BitmapFactory.decodeByteArray(groupImage, 0, groupImage.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                icon.setMinimumHeight(dm.heightPixels);
                icon.setMinimumWidth(dm.widthPixels);
                icon.setImageBitmap(bm);
            }


            else{
                icon.setImageResource(R.drawable.anon);
            }

            TextView textFlow = new TextView(this);
            //textFlow.getStyleClass().add("chatListText");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            textFlow.setLayoutParams(params);
            textFlow.setGravity(Gravity.CENTER);
            textFlow.setText(group);

            hbox.addView(icon);
            hbox.addView(textFlow);



            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(200, 200);
            icon.setLayoutParams(imgParams);

            LinearLayout chatListBox = (LinearLayout) findViewById(R.id.chatListBox);
            chatListBox.addView(hbox);

            hbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    connectButtonClick(v.getId());
                }
            });
        }
        catch(Exception e){
        }
    }

    public void sendGroupList(int groupID, String[] clientList, int[] groupUserIDs, int creatorID, String groupName, byte[][] profileImages) {
        Chat chat = new Chat(groupID, clientList, groupUserIDs, creatorID, username, userID, groupName, profileImages);
        Connection.addChat(chat);
        startChatActivity(groupID);
    }

    public void startChatActivity(int groupID){
        Intent intent = new Intent(this, ChatWindow.class);
        intent.putExtra("groupID", groupID);
        startActivity(intent);
    }

    public void enforceBan() {
        final GroupList activity = this;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(activity, "You have been banned from this chat.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }
        catch(Exception e){System.out.println(e.toString());}
    }

    public void newGroupButton(View view) {
        Intent intent = new Intent(this, NewGroup.class);
        //intent.putExtra("grp", grp);
        startActivity(intent);
    }
}
