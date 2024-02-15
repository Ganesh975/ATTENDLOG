package com.example.attendlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        getSupportActionBar().hide();
        Handler hn=new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent in = new Intent(admin.this, MainActivity.class);
                startActivity(in);
                finish();

            }
        },2000);
    }
}