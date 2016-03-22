package com.example.amishnaik.clienttestandroid;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import message.Message;

public class NewGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
    }

    public void attachFileButtonClick(View view) {
        FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.show();

        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            public void onFileSelected(Dialog source, File file) {
                source.hide();
                showFile(file);
                //sendFile(file);
            }

            public void onFileSelected(Dialog source, File folder, String name) {
                source.hide();
            }
        });
    }

    public void showFile(File file){
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
            final Bitmap bMap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bMap);
        }
        catch(Exception ex){}
    }
}
