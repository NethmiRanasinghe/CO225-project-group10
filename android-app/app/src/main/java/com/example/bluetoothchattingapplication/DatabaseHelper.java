package com.example.bluetoothchattingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private Context context;
    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "message_table";
    private static final String ID = "ID";
    private static final String DEVICE = "device";
    private static final String MESSAGE = "message";
    private static final String DATETIME = "date_time";
    private static final String STATE = "state";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " + DEVICE +
                " TEXT, " + MESSAGE + " TEXT, " + DATETIME + " TEXT, " + STATE + " INTETGER) ";

        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String deviceAddress, String receivedMessage, String dateAndTime, int state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE, deviceAddress);
        contentValues.put(MESSAGE, receivedMessage);
        contentValues.put(DATETIME, dateAndTime);
        contentValues.put(STATE, state);

        Log.d(TAG, "addData: Adding "+"Device Address:" + deviceAddress + "Message:" + receivedMessage
                +"Date and Time:"+dateAndTime+ " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents(){
        //method to get the data from the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }


}
