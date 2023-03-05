package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "jeepney_routes.db";
    public static final String TABLE_NAME = "Routes";
    public static final String col1 = "ID";
    public static final String col2 = "code";
    public static final String col3 = "start_point";
    public static final String col4 = "mid_point_1";
    public static final String col5 = "mid_point_2";
    public static final String col6 = "end_point";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER, code TEXT, start_point TEXT, mid_point_1 TEXT, mid_point_2 TEXT, end_point TEXT, PRIMARY KEY(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String code, String start, String mid1, String mid2, String end){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, code);
        contentValues.put(col3, start);
        contentValues.put(col4, mid1);
        contentValues.put(col5, mid2);
        contentValues.put(col6, end);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
}
