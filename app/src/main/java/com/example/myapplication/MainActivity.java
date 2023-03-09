package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editCode, editLocation;
    Button btnAdd, btnRouteSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DatabaseHelper(this);

        editCode = (EditText) findViewById(R.id.text_code);
        editLocation = (EditText) findViewById(R.id.text_locations);
        btnAdd = (Button) findViewById(R.id.button_add);
        AddData();
        OnClickRouteSelect();
    }

    public void AddData(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isInserted = myDB.insertData(editCode.getText().toString(),
                                editLocation.getText().toString());
                        if (isInserted){
                            Toast.makeText(MainActivity.this, "Data successfully inserted",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Failed to insert data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void OnClickRouteSelect(){
        btnRouteSelect = (Button) findViewById(R.id.button_routeselect);
        btnRouteSelect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(".RouteSelect");
                        startActivity(intent);
                    }
                }
        );
    }
}