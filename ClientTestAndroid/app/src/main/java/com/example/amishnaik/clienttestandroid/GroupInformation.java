package com.example.amishnaik.clienttestandroid;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import message.Message;

public class GroupInformation extends AppCompatActivity {
    Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

        chat = Connection.getActiveChat().getChat();
        for(User user: chat.users){
            addUser(user.userID, user.userName);
        }
        setActionBar(chat.groupName);
    }

    public void setActionBar(String heading) {
        // TODO Auto-generated method stub
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_window_background));
        actionBar.setTitle(heading);
        actionBar.show();

    }

    private void addUser(int userID, String userName){
        try {
            LinearLayout hbox = new LinearLayout(this);
            hbox.setWeightSum(100);

            ImageView icon = new ImageView(this);
            icon.setImageResource(R.drawable.anon);
            icon.setAdjustViewBounds(true);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20);
            iconParams.setMargins(30,30,30,30);
            icon.setLayoutParams(iconParams);



            TextView textFlow = new TextView(this);
            //textFlow.getStyleClass().add("chatListText");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 60);
            textFlow.setLayoutParams(params);
            textFlow.setGravity(Gravity.CENTER);
            textFlow.setText(userName);

            hbox.addView(icon);
            hbox.addView(textFlow);

            System.out.println(Connection.userID + "," + chat.creatorID + "," + chat.userID);
            if(Connection.userID==chat.creatorID) {
                LinearLayout.LayoutParams banParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20);
                banParams.setMargins(30, 30, 30, 30);

                final ImageView ban = new ImageView(this);
                ban.setLayoutParams(banParams);
                ban.setImageResource(R.drawable.ban);
                ban.setAdjustViewBounds(true);
                ban.setBackgroundResource(android.R.drawable.list_selector_background);
                ban.setId(userID);
                ban.setClickable(true);
                ban.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Message message = new Message();
                        message.type="BAN";
                        message.userID=ban.getId();
                        message.groupID=chat.groupID;
                        Connection.sendMessage(message);;
                    }
                });

                hbox.addView(ban);
            }

            LinearLayout userListBox = (LinearLayout) findViewById(R.id.userListBox);
            userListBox.addView(hbox);

            hbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //connectButtonClick(v.getId());
                }
            });
        }
        catch(Exception e){System.out.println(e.toString());
        }
    }
}
