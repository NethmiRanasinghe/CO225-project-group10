package com.example.bluetoothchattingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

public class FirstSplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    Handler h = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_splash);

        getSupportActionBar().hide();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =  new Intent(FirstSplashScreen.this, HomePage.class);
                startActivity(i);
                finish();
                Log.d(TAG,"Is is working");
            }
        },2000);

    }
}
