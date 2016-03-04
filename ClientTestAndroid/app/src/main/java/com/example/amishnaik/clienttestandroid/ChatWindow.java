package com.example.amishnaik.clienttestandroid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
        Connection.clearNotifications();
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
                    Message newMessage = new Message("MSG", "SEND", groupID, chat.username, sendMessageBox.getText().toString());
                    newMessage.groupName = chat.groupName;
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
        final ImageView clientImage = new ImageView(this);
        final LinearLayout messageBox = new LinearLayout(this);

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
                } else if (message.cmd.equals("FILE")) {
                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(500, 500);
                    clientImage.setLayoutParams(imgParams);

                    byte[] bytearray = message.file;
                    try {
                        String baseFolder;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            baseFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
                            System.out.println("1111111111111");
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

                        Bitmap bMap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                        clientImage.setImageBitmap(bMap);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                    messageBox.addView(clientImage);
                }


                LinearLayout chatMessagesBox = (LinearLayout) findViewById(R.id.chatMessagesBox);
                chatMessagesBox.addView(messageBox);

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
            bin.read(bytearray,0,bytearray.length);
            sendMessage(new Message("MSG", "FILE", groupID, chat.username, bytearray, extension));
        }
        catch(Exception ex){}
    }
}
