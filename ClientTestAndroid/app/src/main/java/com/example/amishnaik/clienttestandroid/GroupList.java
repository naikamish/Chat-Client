package com.example.amishnaik.clienttestandroid;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

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

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null){
            username =(String) b.get("username");
            userID = (int) b.get("userID");
            String[] grp = (String[]) b.get("grp");
            int[] groupIDList = (int[]) b.get("groupIDList");
            for(int i=0; i<grp.length;i++){
                addGroup(grp[i],groupIDList[i]);
            }
        }
        connectButton = (Button) findViewById(R.id.button);
        Connection.setChannelListController(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        //Intent intent = new Intent(this, ChatWindow.class);
        //intent.putExtra("grp", grp);
        //intent.putExtra("userID", userID);
        //intent.putExtra("username", username);
        //intent.putExtra("groupIDList", groupIDList);
        //startActivity(intent);
    }

    public void sendMessage(Message message){
        Connection.sendMessage(message);
    }

    public void addGroup(String group, int groupID){
        try {
            LinearLayout hbox = new LinearLayout(this);

            //hbox.getStyleClass().add("chatListHBox");
            hbox.setId(groupID);

            ImageView icon = new ImageView(this);
            icon.setImageResource(R.drawable.anon);

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
                    connectButton.setId(v.getId());
                }
            });
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void sendGroupList(int groupID, String[] clientList, int[] groupUserIDs, int creatorID) {
        Chat chat = new Chat(groupID, clientList, groupUserIDs, creatorID, username, userID);
        Connection.addChat(chat);
        startChatActivity(groupID);
       /* Intent intent = new Intent(this, ChatWindow.class);
        intent.putExtra("groupID", groupID);
        intent.putExtra("clientList", clientList);
        intent.putExtra("groupUserIDs", groupUserIDs);
        intent.putExtra("creatorID", creatorID);
        intent.putExtra("username", username);
        intent.putExtra("userID", userID);
        startActivity(intent);*/
    }

    public void startChatActivity(int groupID){
        Intent intent = new Intent(this, ChatWindow.class);
        intent.putExtra("groupID", groupID);
        startActivity(intent);
    }
}
