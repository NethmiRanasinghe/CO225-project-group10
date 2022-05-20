//package com.example.bluetoothchattingapplication;
//
////****************************   MOST RECENT UPDATES  ******************************************
///*
//18/05/2022_03:08_AM
//
//[TARGET] : once the send button is clicked the newly created ListView should be updated with the older messages from the database
//
//    In MainActivity.java -->
//                   *a ListView listView was declared and initialized [line 67 and line 211]
//                   *under the btnSend [from line 256]
//
//    In DatabaseHelper.java -->
//                   *new method getListContent()
//
//    In activity_main.xml -->
//                   *new ListView was added just above the TextEdit(typing area)--> to display the previous messages
//
//[TARGET: COMPLETED THEN,]:  New code under the btnSend might work for the receiver's side as well (with mReceiver)
//*/
////*****************************************************************************************************
//
//
//import android.Manifest;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.database.Cursor;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import java.nio.charset.Charset;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
//    private static final String TAG = "MainActivity";
//
//    BluetoothAdapter mBluetoothAdapter;
//
//    Button btnEnableDisable_Discoverable;
//
//    BluetoothCommunication mBluetoothConnection;
//
//    Button btnStartConnection;
//    Button btnSend;
//
//    TextView incomingMessages;
//    StringBuilder messages;
//
//    EditText etSend;
//
//    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
//
//    BluetoothDevice mBTDevice;
//
//    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
//    public DeviceListAdapter mDeviceListAdapter;
//    ListView lvNewDevices;
//    ListView listView;
//    //************************************************************for database***********************************************************
//    DatabaseHelper mDatabaseHelper; //for database
//    //***********************************************************************************************************************************
//
//    // //first broadcast receiver
//    // private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
//    //     @Override
//    //     public void onReceive(Context context, Intent intent) {
//    //         String action = intent.getAction();
//    //         if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
//    //             final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
//
//    //             switch (state) {
//    //                 case BluetoothAdapter.STATE_OFF:
//    //                     Log.d(TAG, "onReceiver: STATE OFF");
//    //                     break;
//    //                 case BluetoothAdapter.STATE_TURNING_OFF:
//    //                     Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
//    //                     break;
//    //                 case BluetoothAdapter.STATE_ON:
//    //                     Log.d(TAG, "mBroadcastReceiver1: STATE ON");
//    //                     break;
//    //                 case BluetoothAdapter.STATE_TURNING_ON:
//    //                     Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
//    //                     break;
//    //             }
//    //         }
//    //     }
//    // };
//
//    //second broadcast receiver
//
//    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
//                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
//
//                switch (mode) {
//                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability enabled...");
//                        break;
//                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability disabled. Able to receive connections");
//                        break;
//                    case BluetoothAdapter.SCAN_MODE_NONE:
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability disabled. Not able to receive connections");
//                        break;
//                    case BluetoothAdapter.STATE_CONNECTING:
//                        Log.d(TAG, "mBroadcastReceiver2: Connecting...");
//                        break;
//                    case BluetoothAdapter.STATE_CONNECTED:
//                        Log.d(TAG, "mBroadcastReceiver2: Connected");
//                        break;
//                }
//            }
//        }
//    };
//
//    //third broadcast receiver
//    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            Log.d(TAG, "onReceive: ACTION FOUND.");
//
//            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                mBTDevices.add(device);
//                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
//                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
//                lvNewDevices.setAdapter(mDeviceListAdapter);
//            }
//        }
//    };
//
//    //forth broadcast receiver
//    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//
//            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                //3 cases:
//                //case1: bonded already
//                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
//                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
//                    mBTDevice = mDevice;
//                }
//                //case2: creating a bone
//                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
//                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
//                }
//                //case3: breaking a bond
//                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
//                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
//                }
//            }
//        }
//    };
//
//
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy called.");
//        super.onDestroy();
//        // unregisterReceiver(mBroadcastReceiver1);
//        unregisterReceiver(mBroadcastReceiver2);
//        unregisterReceiver(mBroadcastReceiver3);
//        unregisterReceiver(mBroadcastReceiver4);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //Button btnONOFF = (Button) findViewById(R.id.btnOnOff);
//
//        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverableONOFF);
//        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
//        mBTDevices = new ArrayList<>();
//        mBluetoothConnection = new BluetoothCommunication(MainActivity.this);
//
//        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
//        btnSend = (Button) findViewById(R.id.btnSend);
//        etSend = (EditText) findViewById(R.id.editText);
//
//        //incomingMessages = (TextView) findViewById(R.id.incomingMessage);
//        messages = new StringBuilder();
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("incomingMessage"));
//        //Broadcasts when bond state changes (ie:pairing)
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        registerReceiver(mBroadcastReceiver4, filter);
//
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        lvNewDevices.setOnItemClickListener(MainActivity.this);
//
//        //***************************************************************for database**********************************************
//        mDatabaseHelper = new DatabaseHelper(MainActivity.this);
//
//        listView = (ListView) findViewById(R.id.message_history);
//        //***************************************************************************************************************************
//
////        btnONOFF.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Log.d(TAG, "enabling/disabling bluetooth...");
////                enableDisableBT();
////            }
////        });
//
//        btnStartConnection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    startConnection();
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//                //startConnection();
//            }
//        });
//
//        //******************************************************************check this: updated for the database insertion************************************
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            //sent state : 1 (when the message is a sent message it is indicated by value 1 in the database STATE column)
//
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
//
//                String sentMessage = etSend.getText().toString(); //taking the typed message as a String to database
//
//                mBluetoothConnection.write(bytes);
//
//                //adding data to the database
//                // import androidx.annotation.RequiresApi; is used for now() part below
//                AddData(mBTDevice.getAddress(),sentMessage, String.valueOf(LocalDateTime.now()), 1); //here took a default string as a data and time ;-)
//
//                // display the sent message on the chat screen (could not align this)
//                messages.append("SENT : " + sentMessage + "\n");
//                Log.d(TAG, "message displaying at outgoing chat...");
//                //incomingMessages.setGravity(Gravity.RIGHT);
//              //  incomingMessages.setText(messages);
//
//                //________________________________________________latest update___________________________________________
//
//                //need to populate an Array list from the database
//
//                //ArrayList<String> dataList = new ArrayList<>();
//
//                //ListAdapter listAdapter1;
//                //Cursor data = mDatabaseHelper.getListForDevice(mBTDevice);
//                listView.setAdapter(mDatabaseHelper.getListContents(mBTDevice.getAddress(),getApplicationContext()));
//
//
//
//                //________________________________________________________________________________________________________
//
//                //after message is sent the editText field be empty
//                etSend.setText("");
//            }
//        });
//        //*************************************************************************************************************************************************
//
//    }
//
//    BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        //received state : 2 (when the message is a received message it is indicated by value 2 in the database STATE column)
//
//
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String text = intent.getStringExtra("theMessage");
//
//            AddData(mBTDevice.getAddress(),text,String.valueOf(LocalDateTime.now()),2); //here took a default string as a data and time ;-)
//
//            messages.append("RECEIVE : " + text + "\n");
//            Log.d(TAG, "message sending to incoming chat...");
//            listView.setAdapter(mDatabaseHelper.getListContents(mBTDevice.getAddress(),getApplicationContext()));
//
//           // incomingMessages.setText(messages);
//        }
//    };
//
//
//    //create method for starting connection
////***remember the connection will fail and app will crash if you haven't paired first
//    public void startConnection() {
//        startBTConnection(mBTDevice, MY_UUID_INSECURE);
//    }
//
//    /**
//     * starting chat service method
//     */
//    public void startBTConnection(BluetoothDevice device, UUID uuid) {
//        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
//
//        mBluetoothConnection.startClient(device, uuid);
//    }
//
//
//    // public void enableDisableBT() {
//    //     //there are three scenarios here...
//    //     if (mBluetoothAdapter == null) {
//    //         Log.d(TAG, "enableDisableBT: Does not have bluetooth capability");
//    //     }
//    //     if (!mBluetoothAdapter.isEnabled()) {
//    //         //if the bluetooth is not enabled we are using intent to turn on the bluetooth
//    //         Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//    //         startActivity(enableBTIntent);
//
//    //         //Intent filter is used to catch the state change
//    //         IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//    //         registerReceiver(mBroadcastReceiver1, BTIntent);
//    //     }
//    //     if (mBluetoothAdapter.isEnabled()) {
//    //         mBluetoothAdapter.disable(); //turning off the bluetooth if bt is turned on
//
//    //         IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//    //         registerReceiver(mBroadcastReceiver1, BTIntent);
//    //     }
//    // }
//
//    public void btnEnableDisable_Discoverable(View view) {
//        Log.d(TAG, "btnEnableDisable_Discoverable: making the device discoverable");
//        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//        startActivity(discoverableIntent);
//
//        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(mBroadcastReceiver2, intentFilter);
//
//    }
//
//    public void btnDiscover(View view) {
//        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
//
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//            Log.d(TAG, "btnDiscover: Canceling discovery.");
//
//            //check BT permissions in manifest
//            checkBTPermissions();
//
//            mBluetoothAdapter.startDiscovery();
//            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
//        }
//        if (!mBluetoothAdapter.isDiscovering()) {
//
//            //check BT permissions in manifest
//            checkBTPermissions();
//
//            mBluetoothAdapter.startDiscovery();
//            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
//        }
//    }
//
//    private void checkBTPermissions() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//            if (permissionCheck != 0) {
//
//                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
//            }
//        } else {
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//        }
//    }
//
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
//            //mBluetoothConnection = new BluetoothCommunication(MainActivity.this);
//        }
//    }
//
//    //*******************************************************for database********************************************************************
//    public void AddData(String deviceAddress, String message, String dateTime, int status){
//        //this method will send the data to the database
//        boolean insertData = mDatabaseHelper.addData(deviceAddress, message, dateTime, status); //inserting data
//
//        //checking whether the data insertion is completed is successful or not
//        if(insertData){
//            Log.d(TAG, "database part activated");
//            toastMessage("Inserting data: Successful!");
//        }else{
//            toastMessage("Inserting data: Not Successful!");
//        }
//    }
//
//    private void toastMessage(String textMessage) {
//        Toast.makeText(this,textMessage, Toast.LENGTH_SHORT).show();
//    }
//    //**************************************************************************************************************************************
//}