package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class SuggestedRoutes extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText location, destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_routes);
        location = (EditText) findViewById(R.id.Location);
        destination = (EditText) findViewById(R.id.Destination);
    }

    public void SuggestRoutes(){
        // Here make the RouteButton display the code, km, and fare
    }
}