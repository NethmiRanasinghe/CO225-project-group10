package com.example.bluetoothchattingapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ListPairedDevices extends AppCompatActivity {

    Button showPairedButton;
    ListView listOfPairedDevices;
    // RecyclerView listOfAvailableDevices;
    BluetoothAdapter bluetoothAdapter;


    private final int LOCATION_REQUEST_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_paired);
        showPairedButton = (Button) findViewById(R.id.show_pairedDevices);
        listOfPairedDevices = (ListView) findViewById(R.id.paired_devices);

        //pairedDevices.add("jadsfk");
        // listOfAvailableDevices = (RecyclerView) findViewById(R.id.);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        showPairedDevices();
    }

    ;

    private void showPairedDevices() {

        showPairedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                checkPermission();
                Set<BluetoothDevice> pairedBTdevices = bluetoothAdapter.getBondedDevices();
                String[] strings = new String[pairedBTdevices.size()];
                int index = 0;

                if(pairedBTdevices.size()>0){
                    for (BluetoothDevice device : pairedBTdevices){

                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> pairedDevices = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listOfPairedDevices.setAdapter(pairedDevices);
                }else{
                    Toast.makeText(getApplicationContext(), "no paired devices", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_REQUEST_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(getApplicationContext(), ListPairedDevices.class);
            }else{
                new AlertDialog.Builder(getApplicationContext()).setCancelable(false).setMessage("Location permission required\n Please grant")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListPairedDevices.this.finish();
                            }
                        }).create();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ListPairedDevices.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION);

        }
    }

}
