package com.example.bluetoothchattingapplication;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.UUID;

public class NewThirdPage extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothCommunication mBluetoothConnection;
    Button btnStartConnection;
    Button btnSend;
    EditText etSend;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    BluetoothDevice mBTDevice;

   // public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
   // public DeviceListAdapter mDeviceListAdapter;
  //  ListView lvNewDevices;

    ListView listView;
    //************************************************************for database***********************************************************
    DatabaseHelper mDatabaseHelper; //for database
    //***********************************************************************************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_third_page);

         //mBluetoothConnection = new BluetoothCommunication(NewThirdPage.this);

        btnStartConnection = (Button) findViewById(R.id.startConnection);
        btnSend = (Button) findViewById(R.id.send);
        etSend = (EditText) findViewById(R.id.typeMsg);

//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("incomingMessage"));
//        //Broadcasts when bond state changes (ie:pairing)
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        registerReceiver(mBroadcastReceiver4, filter);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("incomingMessage"));
        //lvNewDevices.setOnItemClickListener(MainActivity.this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Parcelable user = getIntent().getParcelableExtra("bluetoothDevice");
        mBTDevice = (BluetoothDevice) user;
        mBluetoothConnection = new BluetoothCommunication(NewThirdPage.this);

        //***************************************************************for database**********************************************
        mDatabaseHelper = new DatabaseHelper(NewThirdPage.this);

        listView = (ListView) findViewById(R.id.chatScreen);
        //***************************************************************************************************************************

//        btnONOFF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "enabling/disabling bluetooth...");
//                enableDisableBT();
//            }
//        });



        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Parcelable user = getIntent().getParcelableExtra("bluetoothDevice");
//                    mBTDevice = (BluetoothDevice) user;
                    startConnection();
                    //adding data to the database

                    //AddData(mBTDevice.getAddress(),sentMessage, String.valueOf(LocalDateTime.now()), 1); //here took a default string as a data and time ;-)
                    listView.setAdapter(mDatabaseHelper.getListContents(mBTDevice,getApplicationContext()));


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                //startConnection();
            }
        });

        //******************************************************************check this: updated for the database insertion************************************
        btnSend.setOnClickListener(new View.OnClickListener() {
            //sent state : 1 (when the message is a sent message it is indicated by value 1 in the database STATE column)


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                String sentMessage = etSend.getText().toString(); //taking the typed message as a String to database

                mBluetoothConnection.write(bytes);

//                //adding data to the database
//
                  AddData(mBTDevice.getAddress(),sentMessage, String.valueOf(LocalDateTime.now()), 1); //here took a default string as a data and time ;-)
//                listView.setAdapter(mDatabaseHelper.getListContents(mBTDevice,getApplicationContext()));

                //after message is sent the editText field be empty
                etSend.setText("");
            }
        });
        //*************************************************************************************************************************************************

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        //received state : 2 (when the message is a received message it is indicated by value 2 in the database STATE column)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");
            AddData(mBTDevice.getAddress(),text,String.valueOf(LocalDateTime.now()),2); //here took a default string as a data and time ;-)
            listView.setAdapter(mDatabaseHelper.getListContents(mBTDevice,getApplicationContext()));
        }
    };


    //create method for starting connection
//***remember the connection will fail and app will crash if you haven't paired first
    public void startConnection() {
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);
    }


    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        //first cancel discovery because its very memory intensive.
//        mBluetoothAdapter.cancelDiscovery();
//
//        Log.d(TAG, "onItemClick: You Clicked on a device.");
//        String deviceName = mBTDevices.get(i).getName();
//        String deviceAddress = mBTDevices.get(i).getAddress();
//
//        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
//        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
//
//        //create the bond.
//        //NOTE: Requires API 17+? I think this is JellyBean
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Log.d(TAG, "Trying to pair with " + deviceName);
//
//
//            mBTDevices.get(i).createBond();
//
//            mBTDevice = mBTDevices.get(i);
//            mBluetoothConnection = new BluetoothCommunication(NewThirdPage.this);
//        }
//    }

    //*******************************************************for database********************************************************************
    public void AddData(String deviceAddress, String message, String dateTime, int status){
        //this method will send the data to the database
        boolean insertData = mDatabaseHelper.addData(deviceAddress, message, dateTime, status); //inserting data

        //checking whether the data insertion is completed is successful or not
        if(insertData){
            Log.d(TAG, "database part activated");
            //toastMessage("Inserting data: Successful!");
        }else{
            toastMessage("Inserting data: Not Successful!");
        }
    }

    private void toastMessage(String textMessage) {
        Toast.makeText(this,textMessage, Toast.LENGTH_SHORT).show();
    }
    //**************************************************************************************************************************************
}