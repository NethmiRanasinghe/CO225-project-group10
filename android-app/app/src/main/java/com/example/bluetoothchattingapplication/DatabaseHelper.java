package com.example.bluetoothchattingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 2/28/2017.
 */

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

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param // name
     * @return
     */
    public Cursor getMessage(String device){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + MESSAGE + " FROM " + TABLE_NAME +
                " WHERE " + DEVICE + " = '" + device + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    /*
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }
    */
    /**
     * Delete from database
     * @param id
     * @param name
     */
    /*
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
*/
}
