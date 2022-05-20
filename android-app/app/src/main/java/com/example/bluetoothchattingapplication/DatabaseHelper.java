package com.example.bluetoothchattingapplication;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private Context context;
    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "message_table";
    private static final String ID = "ID";
    private static final String DEVICE = "device";
    private static final String DEVICE_NAME = "name";
    private static final String MESSAGE = "message";
    private static final String DATETIME = "date_time";
    private static final String STATE = "state";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " + DEVICE +
                " TEXT, " + DEVICE_NAME + " TEXT, " + MESSAGE + " TEXT, " + DATETIME + " TEXT, " + STATE + " INTETGER) ";

        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String deviceAddress, String deviceName, String receivedMessage, String dateAndTime, int state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE, deviceAddress);
            contentValues.put(DEVICE_NAME, deviceName);
        contentValues.put(MESSAGE, receivedMessage);
        contentValues.put(DATETIME, dateAndTime);
        contentValues.put(STATE, state);

        Log.d(TAG, "addData: Adding "+"Device Address:" + deviceAddress + "Device Name:" + deviceName + "Message:" + receivedMessage
                +"Date and Time:"+dateAndTime+ " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ListAdapter getListContents(String mBTDevice, Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        //Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + DEVICE + " = " + mBTDevice, null);
        //return data;

        ArrayList<String> dataList = new ArrayList<>();
        ListAdapter listAdapter = null;
        //Cursor data = mDatabaseHelper.getListForDevice(mBTDevice);

        if(data.getCount()==0){
            //no data in the database: put a toast
        }else{
            String s;
            while(data.moveToNext()){
                //1 - sent
                //2 - received-----------------state -4
                if (data.getString(1).equals(mBTDevice)) {
                    int i = Integer.parseInt(data.getString(5));
                    if (i == 1) {
                        s = data.getString(2).concat(" : ").concat(data.getString(3));
                    } else {
                        s = data.getString(2).concat(" : ").concat(data.getString(3));
                    }
                    dataList.add(s);

                    //below the context was going to pass by the keyword 'the', but it gave an error.
                    //then it was changed to getApplicationContext() which do the same thing I guess : [need to check this]
                    listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dataList);

                }
                //listView.setAdapter(listAdapter);
            }
        }

        return listAdapter;
    }



}
