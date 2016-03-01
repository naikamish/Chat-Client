package com.example.amishnaik.clienttestandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import message.Message;

public class ChatWindow extends AppCompatActivity {

    private int groupID;
    private Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_chat_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView attachButton = (ImageView) findViewById(R.id.attachButton);
        ImageView doodleButton = (ImageView) findViewById(R.id.doodleButton);
        attachButton.setImageResource(R.drawable.attach);
        doodleButton.setImageResource(R.drawable.doodle);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null){
            groupID = (int) b.get("groupID");
            chat = Connection.bindChat(groupID);
            recreateMessages();
            Connection.setActiveChat(this);
        }



        final EditText sendMessageBox = (EditText) findViewById(R.id.sendMessageBox);
        sendMessageBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(new Message("MSG", "SEND", groupID, chat.username, sendMessageBox.getText().toString()));//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
                    sendMessageBox.clearFocus();
                    sendMessageBox.setText("");
                    return true;
                }
                return false;
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void recreateMessages(){
        for(Message message:chat.messages){
            showMessage(message);
        }
    }

    public void sendMessage(Message message){
        Connection.sendMessage(message);
    }

    public int getGroupID() {
        return groupID;
    }

    public void showMessage(final Message message) {
        final TextView text = new TextView(this);
        final TextView clientMessage = new TextView(this);
        final LinearLayout messageBox = new LinearLayout(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String clientInfo = message.clientName+" ("+dateFormat.format(date)+"):\n";
                text.setText(clientInfo);
                clientMessage.setText(message.message);
                messageBox.addView(text);
                messageBox.addView(clientMessage);

                LinearLayout chatMessagesBox = (LinearLayout) findViewById(R.id.chatMessagesBox);
                chatMessagesBox.addView(messageBox);

            }
        });
    }
}
