package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Map_view extends AppCompatActivity {

    Button back;
    Intent recieve;
    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        back = (Button) findViewById(R.id.back_away);
        recieve = getIntent();
        test = (TextView) findViewById(R.id.textView4);
        String jeepneyCode = recieve.getStringExtra("Code");
        String dist = recieve.getStringExtra("Dist");
        String fare = recieve.getStringExtra("Fare");
        test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
                startActivity(intent);
            }
        });

    }

}
