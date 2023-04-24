package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SuggestedRoutes extends AppCompatActivity {

    DatabaseHelper myDB = new DatabaseHelper(this);
    EditText location, destination;
    Button Display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_routes);
        location = (EditText) findViewById(R.id.Location);
        destination = (EditText) findViewById(R.id.Destination);
        Display = (Button) findViewById(R.id.RouteButton);

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
    private void executeQuery(String location1, String location2, Button button) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT CODE FROM Jeepney WHERE Location IN (?, ?) GROUP BY CODE HAVING COUNT(DISTINCT location) = 2";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext()) {
            String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
            result.append(jeepneyCode).append("\n");
            //For every result create button.
        }
        cursor.close();
        db.close();
        String resultRoute = result.toString();
        button.setText(resultRoute);
    }

    // Function that calculates the fare given travel distance, base fare, and increase per km
    public float calculateFare(float distance, float baseFare, float perKm){
        // set totalFare to baseFare
        float totalFare = baseFare;
        // if travel distance greater than 5 km, increase totalFare
        if(distance > 5){
            float excessDistance = distance - 5;
            totalFare = baseFare + perKm * (int) excessDistance;
            // type cast excessDistance to int since we don't include decimals in calculation;
            // if excessDistance is 3.56 for instance, we discard the 0.56
        }
        return totalFare;
    }

    public void SuggestRoutes(){
        // Here make the RouteButton display the code, km, and fare
    }
}