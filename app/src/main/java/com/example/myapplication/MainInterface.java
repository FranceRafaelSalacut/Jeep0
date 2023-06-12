package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainInterface extends AppCompatActivity {

    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininterface);
        myDB = new DatabaseHelper(this);

        //database is already loaded, don't load data yet
        //loadData();

        try {
            myDB.createDatabase();
        } catch (IOException e) {
            // handle error
        }

        new Handler().postDelayed(() -> {
            //Show beginning interface for 5 seconds
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        }, 1000);
    }

}
