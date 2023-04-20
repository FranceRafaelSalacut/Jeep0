package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class SuggestedRoutes extends AppCompatActivity {

    DatabaseHelper myDB = new DatabaseHelper(this);
    EditText location, destination;
    TextView Display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_routes);
        location = (EditText) findViewById(R.id.Location);
        destination = (EditText) findViewById(R.id.Destination);
        Display = (TextView) findViewById(R.id.textView4);

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text1 = charSequence.toString();
                //String text2 = destination.getText().toString();
                //String concatenatedText = text1 + " " + text2;
                //Display.setText(concatenatedText);
                executeQuery(location.getText().toString(), destination.getText().toString(), Display);
                //displayTables();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text1 = location.getText().toString();
                //String text2 = charSequence.toString();
                //String concatenatedText = text1 + " " + text2;
                //Display.setText(concatenatedText);
                executeQuery(location.getText().toString(), destination.getText().toString(), Display);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });
    }

    //This is where the query happens. Two locations is accepted and outputs suggestions
    private void executeQuery(String location1, String location2, TextView textView) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT J_Code FROM Jeepney WHERE Location IN (?, ?) GROUP BY J_Code HAVING COUNT(DISTINCT location) = 2";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext()) {
            String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("J_Code"));
            result.append(jeepneyCode).append("\n");
        }
        cursor.close();
        db.close();
        textView.setText(result.toString());
    }

    public void displayTables() {
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);

        //TextView textView = findViewById(R.id.textView);


        if (cursor.moveToFirst()) {

            do {
                String tableName = cursor.getString(0);
                Display.setText(tableName + "\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }


    public void SuggestRoutes(){
        // Here make the RouteButton display the code, km, and fare
    }
}