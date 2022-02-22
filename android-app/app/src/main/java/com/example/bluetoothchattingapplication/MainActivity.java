package com.example.bluetoothchattingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMain = (Button) findViewById(R.id.button);
        goToNextActivity();
    }

    private void goToNextActivity() {

        buttonMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // how to direct to another activity on button click
                // select new -> Activity for the second activity ( not new -> Java File )
                Intent intent1 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent1);
            }
        });

    }


}