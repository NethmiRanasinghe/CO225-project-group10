package com.example.bluetoothchattingapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private static final String TAG = "HomePage";
    Button btnONOFF;
    Button proceed;

    BluetoothAdapter mBluetoothAdapter;

    //first broadcast receiver
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceiver: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };



    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_enable);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnONOFF = (Button) findViewById(R.id.btnOnOff);
        proceed = (Button) findViewById(R.id.btn_login);


        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "enabling/disabling bluetooth...");
                enableDisableBT();
            }
        });

        // proceed to next activity once the button is clicked
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent proceedToNext = new Intent(HomePage.this, NewSecondPage.class);
                startActivity(proceedToNext);
            }
        });


    }

    public void enableDisableBT() {
        //there are three scenarios here...
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have bluetooth capability");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //if the bluetooth is not enabled we are using intent to turn on the bluetooth
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            //Intent filter is used to catch the state change
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable(); //turning off the bluetooth if bt is turned on

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }



}