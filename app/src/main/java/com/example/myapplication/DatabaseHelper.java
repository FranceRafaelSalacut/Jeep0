package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    /*
        Database now works...
    */

    public static final String DB_NAME = "jeepneyroutes.db";
    public static final String TABLE_NAME = "Routes";
    public static final String col1 = "ID";
    public static final String col2 = "code";
    public static final String col3 = "locationID";
    public static final String col4 = "location";
    public Context context;
    //static SQLiteDatabase sqlDB;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER, code TEXT, locationID INTEGER, " +
                "location TEXT, PRIMARY KEY(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String code, int locationID, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, code);
        contentValues.put(col3, locationID);
        contentValues.put(col4, location);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Routes", null);
    }

}
