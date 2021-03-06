package com.example.amishnaik.clienttestandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.io.ByteArrayOutputStream;

import message.Message;

public class DoodleActivity extends AppCompatActivity {
    private DoodleView doodleView;
    private ImageButton currPaint;
    private int groupID, userID;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        doodleView = (DoodleView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageResource(R.drawable.paint_pressed);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null){
            groupID = (int) b.get("groupID");
            userID = (int) b.get("userID");
            if (b.containsKey("bitmap")) {
                byte[] bytes = (byte[]) b.get("bitmap");
                System.out.println(bytes.length+"howlong");
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

               // bitmap = (Bitmap) b.get("bitmap");
                doodleView.setBitmap(bmp);
            }
        }



        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberpicker);
        numberPicker.setMaxValue(40);
        numberPicker.setMinValue(1);
        numberPicker.setValue(20);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                doodleView.setBrushSize(newVal);
            }
        });
    }

    public void eraseClicked(View view){
        doodleView.setErase(true);
    }

    public void brushClicked(View view){
        doodleView.setErase(false);
    }

    public void sendClicked(View view){
        doodleView.setDrawingCacheEnabled(true);
        //Bitmap bitmap = doodleView.getDrawingCache();
        Bitmap bitmap = doodleView.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Message message = new Message("MSG", "DOODLE", groupID, Connection.username, byteArray, "png");
        message.userID = this.userID;
        Connection.sendMessage(message);
        finish();
    }

    public void paintClicked(View view){
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            doodleView.setColor(color);
            imgView.setImageResource(R.drawable.paint_pressed);
            currPaint.setImageResource(R.drawable.paint);
            currPaint=(ImageButton)view;
        }
    }
}
