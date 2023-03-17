package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    /* App crashes when attempting to access database, like in RouteSelect. Maybe you can find a way
       to fix this...
     */

    public static final String DB_NAME = "jeepneyroutes.db";
    public static final String DB_PATH = "/data/data/com.example.myapplication/databases/";
    public static final String TABLE_NAME = "Routes";
    public static final String col1 = "ID";
    public static final String col2 = "code";
    public static final String col3 = "locationID";
    public static final String col4 = "location";
    public Context context;
    static SQLiteDatabase sqlDB;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER, code TEXT, locations TEXT, PRIMARY KEY(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //onCreate(db);
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
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    // These functions help facilitate the opening of a local SQLite database
    public void createDB() throws IOException {
        boolean dbExists = checkDB();
        if(!dbExists){
            this.getWritableDatabase();
            copyDB();
        }
    }

    public boolean checkDB(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public void copyDB() throws IOException {
        // setting up
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        // byte transfer from input file to output file
        byte[] bfr = new byte[1024];
        int length;
        while((length = myInput.read(bfr)) > 0){
            myOutput.write(bfr, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDB() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        sqlDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close(){
        if(sqlDB != null){
            sqlDB.close();
        }
        super.close();
    }
}
