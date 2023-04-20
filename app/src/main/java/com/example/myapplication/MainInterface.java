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
            Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
            startActivity(intent);
        }, 1000);
    }

    // I'll put the loading of data here

    /*
    public void loadData(){
        DBData d = new DBData();
        ArrayList<Object> routeData = d.routeInit();
        boolean inserted = true;
        for(int i = 0; i < routeData.size(); i += 3){
            if(!myDB.insertData((String) routeData.get(i), (Integer) routeData.get(i+1),
                    (String) routeData.get(i+2))){
                Toast.makeText(MainInterface.this, "Failed to add", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
    
     */
}
