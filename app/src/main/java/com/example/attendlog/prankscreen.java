package com.example.attendlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class prankscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prankscreen);


        getSupportActionBar().hide();
        Handler hn=new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent in = new Intent(prankscreen.this, MainActivity.class);
                startActivity(in);
                finish();

            }
        },2000);



    }
}