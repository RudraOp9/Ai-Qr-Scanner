package com.leo.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class showData extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        textView = findViewById(R.id.textView3);
        button = findViewById(R.id.btnPrimary);


        Intent i = getIntent();
        String text = i.getStringExtra("text");
        String btnText = i.getStringExtra("btnText");


        textView.setText(text);
        button.setText(btnText);

    }
}