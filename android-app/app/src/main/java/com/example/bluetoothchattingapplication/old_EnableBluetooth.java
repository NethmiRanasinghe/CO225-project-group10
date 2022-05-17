package com.example.bluetoothchattingapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class old_EnableBluetooth extends AppCompatActivity {

    Button bluetoothOn;
    Button proceed;
    BluetoothAdapter bluetoothAdapter;
    Intent bluetoothEnableIntent;
    int requestedEnableSignal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_bluetooth_enable);
        // to check whether Bluetooth is supported on device, use object of BluetoothAdapter class
        bluetoothOn = (Button) findViewById(R.id.enable_BT);
        proceed = (Button) findViewById(R.id.btn_login);
        // if this method returns null, then it means Bluetooth is not supported on the device
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // call this method to on bluetooth on the device
        bluetoothEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestedEnableSignal = 1;
        enableBluetooth();

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestedEnableSignal) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is successfully enabled!", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Oops! Bluetooth enabling cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void enableBluetooth() {
        // anonymous instance of an anonymous inner class
        bluetoothOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                if(bluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(), "This device does not support on this device", Toast.LENGTH_LONG).show();

                }else{
                    // To check Bluetooth is enabled or not, use isEnabled() method in BluetoothAdapter class
                    // before adding this, have to give permission to access Bluetooth settings in the manifest file
                    if (!bluetoothAdapter.isEnabled()){
                        // method is deprecated and in future versions this will be removed (?)
                        startActivityForResult(bluetoothEnableIntent,requestedEnableSignal);
                    } else {
                        Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_LONG).show();

                    }
                }

            }

        }
        );
        // proceed to next activity once the button is clicked
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent proceedToNext = new Intent(old_EnableBluetooth.this, old_ListPairedDevices.class);
                startActivity(proceedToNext);
            }
        });
    }
}
