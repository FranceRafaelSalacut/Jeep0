package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class SuggestedRoutes extends AppCompatActivity {

    DatabaseHelper myDB = new DatabaseHelper(this);
    EditText location, destination;

    TextView testing;
    //Button Display;
    ScrollView Disp;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_routes);
        location = (EditText) findViewById(R.id.Location);
        destination = (EditText) findViewById(R.id.Destination);
        Disp = (ScrollView) findViewById(R.id.scrollView2);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //testing = (TextView) findViewById(R.id.textView);
        //Display = (Button) findViewById(R.id.RouteButton);

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
                executeQuery(location.getText().toString(), destination.getText().toString());
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
                executeQuery(location.getText().toString(), destination.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });
    }

    //This is where the query happens. Two locations is accepted and outputs suggestions
    private void executeQuery(String location1, String location2) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT CODE FROM Jeepney WHERE Location IN (?, ?) GROUP BY CODE HAVING COUNT(DISTINCT location) = 2";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);
        //StringBuilder result = new StringBuilder();

        if (Disp.getChildCount() > 0) {
            Disp.removeAllViews();
        }

        while (cursor.moveToNext()) {
            //Outputs for unit testing are jeepneyCode, dist, fare.
            String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
            String dist = calc_distance(jeepneyCode, location1, location2);
            float t_fare = calculateFare((float) Integer.parseInt(dist), (float) 12.00, (float) 1.80, (float) 4);
            String fare = Float.toString(t_fare);

            Button button = new Button(this);
            button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
            layout.addView(button);
            //result.append(jeepneyCode).append("\t");
            //result.append(dist).append("\t");
            //result.append(fare).append("\n");
            //For every result create button.
        }
        cursor.close();
        db.close();
        Disp.addView(layout);
        //String resultRoute = result.toString();
        //testing.setText(resultRoute);
    }

    public String calc_distance(String jeep_code, String location1, String location2){
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT * FROM Jeepney WHERE CODE = ?";
        String[] args = {jeep_code};
        Cursor cursor = db.rawQuery(query, args);
        ArrayList<String[]> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String[] row = new String[4];
            row[0] = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
            row[1] = cursor.getString(cursor.getColumnIndexOrThrow("Route_No"));
            row[2] = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
            row[3] = cursor.getString(cursor.getColumnIndexOrThrow("Distance"));
            data.add(row);
        }
        cursor.close();

        //finding the staring location's route_no
        int start = 0;
        for(int i = 0; i< data.size(); i++){
            String[] temp = data.get(i);
            if(temp[2].equals(location1)){
                start = Integer.parseInt(temp[1]);
            }
        }

        int sum = 0;
        do{
            if(start == data.size()){
                start = 1;
            }
            String[] temp = data.get(start++);
            sum = sum + Integer.parseInt(temp[3]);
            if(temp[2].equals(location2)){
                break;
            }
        }while(true);

        return Integer.toString(sum);
    }

    // Function that calculates the fare given travel distance, base fare, and increase per km
    public float calculateFare(float distance, float baseFare, float perKm, float firstKm){
        // set totalFare to baseFare
        float totalFare = baseFare;
        // if travel distance greater than 5 km, increase totalFare
        if(distance > firstKm){
            float excessDistance = distance - firstKm;
            totalFare = baseFare + perKm * (int) excessDistance;
            // type cast excessDistance to int since we don't include decimals in calculation;
            // if excessDistance is 3.56 for instance, we discard the 0.56
        }
        return totalFare;
    }

    // for now this function also does nothing
    public void SuggestRoutes(){
        // Here make the RouteButton display the code, km, and fare
    }
}