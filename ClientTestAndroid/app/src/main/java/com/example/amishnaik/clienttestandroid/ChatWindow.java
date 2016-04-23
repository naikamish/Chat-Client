package com.example.amishnaik.clienttestandroid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import message.Message;

public class ChatWindow extends AppCompatActivity {

    private int groupID;
    private Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Connection.clearNotifications();
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
                    Message newMessage = new Message("MSG", "SEND", groupID, chat.username, sendMessageBox.getText().toString());
                    newMessage.groupName = chat.groupName;
                    newMessage.userID = chat.userID;
                    sendMessage(newMessage);//MSG "+groupName+" "+clientName + " - " + e.getActionCommand());
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
        setActionBar(chat.groupName);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setActionBar(String heading) {
        // TODO Auto-generated method stub
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_window_background));
        actionBar.setTitle(heading);
        actionBar.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_window_menu, menu);
        return true;
    }

    private void recreateMessages(){
        for(Message message:chat.messages){
            showMessage(message);
        }
    }

    public void showGroupInfo(MenuItem item){
        Intent intent = new Intent(this, GroupInformation.class);
        intent.putExtra("groupID", groupID);
        intent.putExtra("userID", chat.userID);
        startActivity(intent);
    }

    public void sendMessage(Message message){
        Connection.sendMessage(message);
    }

    public int getGroupID() {
        return groupID;
    }

    public void showMessage(final Message message) {
        final TextView text = new TextView(this);
        int styleType = message.userID==chat.userID ? R.style.ChatMessageSenderYou:R.style.ChatMessageSenderOther;
        text.setTextAppearance(this,styleType);

        final TextView clientMessage = new TextView(this);
        final ImageView clientImage = new ImageView(this);
        final LinearLayout messageBox = new LinearLayout(this);
        messageBox.setOrientation(LinearLayout.VERTICAL);
        messageBox.setBackgroundColor(Color.WHITE);
        messageBox.setPadding(20, 20, 0, 20);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String clientInfo = message.clientName + " (" + dateFormat.format(date) + "):\n";
                text.setText(clientInfo);
                messageBox.addView(text);

                if (message.cmd.equals("SEND")) {
                    clientMessage.setText(message.message);
                    messageBox.addView(clientMessage);
                }
                else if (message.cmd.equals("FILE") || message.cmd.equals("DOODLE")) {
                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(500, 500);
                    clientImage.setLayoutParams(imgParams);

                    byte[] bytearray = message.file;
                    try {
                        String baseFolder;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            baseFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
                        } else {
                            baseFolder = getContext().getFilesDir().getAbsolutePath();
                        }

                        File file = new File(baseFolder + "/ChatClient");

                        if (!file.exists()) {
                            file.mkdir();
                        }

                        FileOutputStream fos = new FileOutputStream(file + "/" + message.filename);
                        fos.write(bytearray);
                        fos.close();

                        final Bitmap bMap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                        clientImage.setImageBitmap(bMap);
                        if (message.cmd.equals("DOODLE")) {
                            clientImage.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    doodleButtonClick(bMap);
                                }
                            });
                        }

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                    messageBox.addView(clientImage);
                }
                else if(message.cmd.equals("ADD")){
                    showSpecialMessage(message.clientName+" has joined the chat");
                }
                else if(message.cmd.equals("REMOVE")){
                    showSpecialMessage(message.clientName+" has left the chat");
                }

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(1000,LinearLayout.LayoutParams.WRAP_CONTENT, .75f);
                param.setMargins(30,30,30,10);
                LinearLayout chatMessagesBox = (LinearLayout) findViewById(R.id.chatMessagesBox);
                chatMessagesBox.addView(messageBox,param);

                final ScrollView scroll = (ScrollView) findViewById(R.id.chatScrollView);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }
        });
    }

    public Context getContext(){
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,GroupList.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void doodleButtonClick(View view){
        Intent intent = new Intent(this, DoodleActivity.class);
        intent.putExtra("groupID", groupID);
        intent.putExtra("userID", chat.userID);
        startActivity(intent);
    }

    public void doodleButtonClick(Bitmap bitmap){
        Intent intent = new Intent(this, DoodleActivity.class);
        intent.putExtra("groupID", groupID);
        intent.putExtra("userID", chat.userID);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent.putExtra("bitmap",bytes);

        startActivity(intent);
    }

    public void attachButtonClick(View view) {
        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.show();

        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            public void onFileSelected(Dialog source, File file) {
                source.hide();
                sendFile(file);
            }

            public void onFileSelected(Dialog source, File folder, String name) {
                source.hide();
            }
        });
    }

    private void sendFile(File file){
        byte [] bytearray  = new byte [(int)file.length()];
        String extension = "";

        int i = file.getPath().lastIndexOf('.');
        int p = Math.max(file.getPath().lastIndexOf('/'), file.getPath().lastIndexOf('\\'));

        if (i > p) {
            extension = file.getPath().substring(i+1);
        }
        try{
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray, 0, bytearray.length);
            Message newMessage = new Message("MSG", "FILE", groupID, chat.username, bytearray, extension);
            newMessage.userID = chat.userID;
            sendMessage(newMessage);
        }
        catch(Exception ex){}
    }

    public Chat getChat(){
        return chat;
    }

    public void enforceBan(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSpecialMessage("YOU HAVE BEEN BANNED FROM THIS CHAT");
                EditText sendMessageBox = (EditText) findViewById(R.id.sendMessageBox);
                sendMessageBox.setEnabled(false);
            }
        });


    }

    public void showSpecialMessage(String message){
        Context context = Connection._CONTEXT;
        final TextView text = new TextView(context);
        text.setTextAppearance(context,R.style.ChatMessageSenderYou);
        text.setText(message);
        final LinearLayout messageBox = new LinearLayout(context);
        messageBox.setOrientation(LinearLayout.VERTICAL);
        messageBox.setBackgroundColor(Color.WHITE);
        messageBox.setPadding(20, 20, 0, 20);
        messageBox.addView(text);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(1000,LinearLayout.LayoutParams.WRAP_CONTENT, .75f);
        param.setMargins(30, 30, 30, 10);
        LinearLayout chatMessagesBox = (LinearLayout) findViewById(R.id.chatMessagesBox);
        chatMessagesBox.addView(messageBox,param);
    }
}
