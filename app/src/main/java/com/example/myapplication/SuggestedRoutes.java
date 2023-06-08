package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SuggestedRoutes extends AppCompatActivity {

    DatabaseHelper myDB = new DatabaseHelper(this);
    EditText location, destination;

    ConstraintLayout testss;
    TextView testing;
    //Button Display;
    ScrollView Disp;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_routes);

        // location and destination fields now have autocomplete features
        AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.initialInputText);
        AutoCompleteTextView destination = (AutoCompleteTextView) findViewById(R.id.initialDesiredText);
        Disp = (ScrollView) findViewById(R.id.scrollView4);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        testss = creates(testss);
        layout.removeAllViews();
        Disp.removeAllViews();
        layout.addView(testss);
        Disp.addView(layout);

        //testing = (TextView) findViewById(R.id.textView3);

        // generate string query of all locations
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT Location FROM Jeepney", null);
        String[] locations = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            String locName = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
            locations[i] = locName;
            i++;
        }

        // use ArrayAdapter for the autocomplete text fields to use the locations string array
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
        location.setAdapter(adapter);
        destination.setAdapter(adapter);

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
                //testing.setText(concatenatedText);
                executeQuery(location.getText().toString(), destination.getText().toString());
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
                //testing.setText(concatenatedText);
                executeQuery(location.getText().toString(), destination.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });
    }

    private void testingattentionplease(String location1, String location2) {
        testing.setText(location1 +" " + location2);
    }

    //This is where the query happens. Two locations is accepted and outputs suggestions
    private void executeQuery(String location1, String location2) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT CODE FROM Jeepney WHERE Location IN (?, ?) GROUP BY CODE HAVING COUNT(DISTINCT location) = 2";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        //StringBuilder result = new StringBuilder();

        // if nothing pops up from first query, do this instead
        if (cursor.getCount() == 0){
            executeQuery_v2(location1, location2);
        }
        else {
            layout.removeAllViews();
            Disp.removeAllViews();

            while (cursor.moveToNext()) {
                //Outputs for unit testing are jeepneyCode, dist, fare.
                String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                //String dist = calc_distance(jeepneyCode, location1, location2);
                //float t_fare = calculateFare((float) Integer.parseInt(dist), (float) 12.00, (float) 1.80, (float) 4);
                //String fare = Float.toString(t_fare);

                Button button = new Button(this);
                //button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
                button.setText("Jeep Code: " + jeepneyCode);

                //ConstraintLayout test = null;

                //test = creates(test);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Map_view.class);
                        intent.putExtra("Code", jeepneyCode);
                        //intent.putExtra("Dist", dist);
                        //intent.putExtra("Fare", fare);
                        intent.putExtra("Location", location1);
                        intent.putExtra("Destination", location2);
                        startActivity(intent);
                    }
                });
                layout.addView(button);
                //result.append(jeepneyCode).append("\t");//("\n");
                //result.append(dist).append("\t");
                //result.append(fare).append("\n");
                //For every result create button.
            }
        }

        cursor.close();
        db.close();
        Disp.addView(layout);
        //String resultRoute = result.toString();
        //testing.setText(resultRoute);
    }
    // use of this function is to determine a midpoint between two locations when there is no direct route
    private void executeQuery_v2(String location1, String location2) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String[] selectionArgs = { location1, location2 };
        String query = "SELECT Loc1 AS Midpoint FROM (SELECT DISTINCT Location AS Loc1 FROM Jeepney" +
                " WHERE CODE IN (SELECT CODE FROM Jeepney WHERE Location = ?)) " +
                "INNER JOIN (SELECT DISTINCT Location AS Loc2 FROM Jeepney WHERE CODE IN " +
                "(SELECT CODE FROM Jeepney WHERE Location = ?)) ON Loc1 = Loc2";
        Cursor cursor = db.rawQuery(query, selectionArgs);

        layout.removeAllViews();
        Disp.removeAllViews();

        while (cursor.moveToNext()) {
            String midpoint = cursor.getString(cursor.getColumnIndexOrThrow("Midpoint"));

            Button button = new Button(this);
            button.setText("Midpoint: " + midpoint);
//            TextView text1 = new TextView(this);
//            TextView text2 = new TextView(this);
//            text1.setText("From start to midpoint " + midpoint);
//            executeQuery(location1, midpoint);
//            text2.setText("From midpoint " + midpoint + " to destination");
//            executeQuery(midpoint, location2);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executeQuery_midpoint(location1, location2);
                }
            });
//
            layout.addView(button);
//            layout.addView(text2);
        }
//        cursor.close();
//        db.close();
//        Disp.addView(layout);
    }

    // this just combines routes crossing through the midpoint, though this is temporary.
    // point is to suggest routes the user can take from start to finish, as long as there is a midpoint.
    private void executeQuery_midpoint(String location1, String location2) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT CODE FROM Jeepney WHERE Location = ? UNION " +
                "SELECT CODE FROM Jeepney WHERE Location = ?";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        //StringBuilder result = new StringBuilder();

        // if nothing pops up from first query, do this instead
        if (cursor.getCount() == 0){
            executeQuery_v2(location1, location2);
        }
        else {
            layout.removeAllViews();
            Disp.removeAllViews();

            while (cursor.moveToNext()) {
                //Outputs for unit testing are jeepneyCode, dist, fare.
                String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                //String dist = calc_distance(jeepneyCode, location1, location2);
                //float t_fare = calculateFare((float) Integer.parseInt(dist), (float) 12.00, (float) 1.80, (float) 4);
                //String fare = Float.toString(t_fare);

                Button button = new Button(this);
                //button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
                button.setText("Jeep Code: " + jeepneyCode);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Map_view.class);
                        intent.putExtra("Code", jeepneyCode);
                        //intent.putExtra("Dist", dist);
                        //intent.putExtra("Fare", fare);
                        intent.putExtra("Location", location1);
                        intent.putExtra("Destination", location2);
                        startActivity(intent);
                    }
                });
                layout.addView(button);
                //result.append(jeepneyCode).append("\t");//("\n");
                //result.append(dist).append("\t");
                //result.append(fare).append("\n");
                //For every result create button.
            }
        }

        cursor.close();
        db.close();
        Disp.addView(layout);
        //String resultRoute = result.toString();
        //testing.setText(resultRoute);
    }
    // Create a new ConstraintLayout
    private ConstraintLayout creates(ConstraintLayout test){
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
            constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        200));
            constraintLayout.setBackgroundColor(Color.WHITE);
            constraintLayout.setId(R.id.constraintLayout3);

        // Create a ShapeDrawable for the border
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(Color.RED);  // Set the border color
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);  // Set the border style
        shapeDrawable.getPaint().setStrokeWidth(10);  // Set the border width

        // Convert the ShapeDrawable to a Drawable
        Drawable borderDrawable = shapeDrawable;

        // Set the border Drawable as the background of the ConstraintLayout
        constraintLayout.setBackground(borderDrawable);

        // Create TextViews
        TextView codeTextView = createTextView("CODE");
        TextView kmTextView = createTextView("KM");
        TextView fareTextView = createTextView("FARE");
        TextView codeInfoTextView = createTextView("CODE INPUT");
        TextView kmInfoTextView = createTextView("KM INPUT");
        TextView fareInfoTextView = createTextView("15");

        // Set IDs for TextViews
            codeTextView.setId(R.id.code);
            kmTextView.setId(R.id.km);
            fareTextView.setId(R.id.fare);
            codeInfoTextView.setId(R.id.codeInfo);
            kmInfoTextView.setId(R.id.kmInfo);
            fareInfoTextView.setId(R.id.fareInfo);

        // Add TextViews to ConstraintLayout
            constraintLayout.addView(codeTextView);
            constraintLayout.addView(kmTextView);
            constraintLayout.addView(fareTextView);
            constraintLayout.addView(codeInfoTextView);
            constraintLayout.addView(kmInfoTextView);
            constraintLayout.addView(fareInfoTextView);

        // Create ConstraintSet to manage constraints
        ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

        // Set constraints for codeTextView
            constraintSet.connect(codeTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(codeTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(codeTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(codeTextView.getId(), 0.049f);
            constraintSet.setVerticalBias(codeTextView.getId(), 0.207f);

        // Set constraints for kmTextView
            constraintSet.connect(kmTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(kmTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(kmTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(kmTextView.getId(), 0.507f);
            constraintSet.setVerticalBias(kmTextView.getId(), 0.198f);

        // Set constraints for fareTextView
            constraintSet.connect(fareTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(fareTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(fareTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(fareTextView.getId(), 0.926f);
            constraintSet.setVerticalBias(fareTextView.getId(), 0.222f);

        // Set constraints for codeInfoTextView
            constraintSet.connect(codeInfoTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(codeInfoTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(codeInfoTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(codeInfoTextView.getId(), 0.055f);
            constraintSet.connect(codeInfoTextView.getId(), ConstraintSet.TOP, codeTextView.getId(), ConstraintSet.BOTTOM);
            //constraintSet.setVerticalBias(codeInfoTextView.getId(), 0.0f);

        // Set constraints for kmInfoTextView
            constraintSet.connect(kmInfoTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(kmInfoTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(kmInfoTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(kmInfoTextView.getId(), 0.544f);
            constraintSet.setVerticalBias(kmInfoTextView.getId(), 0.0f);

        // Set constraints for fareInfoTextView
            constraintSet.connect(fareInfoTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            constraintSet.connect(fareInfoTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(fareInfoTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.setHorizontalBias(fareInfoTextView.getId(), 0.916f);
            constraintSet.setVerticalBias(fareInfoTextView.getId(), 0.0f);

        // Apply constraints to the ConstraintLayout
            constraintSet.applyTo(constraintLayout);

        // Set the dynamically created ConstraintLayout as the content view
        //setContentView(constraintLayout);

        test = constraintLayout;
        return test;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        return textView;
    }

    // Function that calculates the fare given travel distance, base fare, and increase per km
    public float calculateFare(float distance, float baseFare, float perKm, float firstKm) {
        // set totalFare to baseFare
        float totalFare = baseFare;
        // if travel distance greater than firstKm, increase totalFare
        if(distance > firstKm){
            float excessDistance = distance - firstKm;
            totalFare = baseFare + perKm * (int) excessDistance;
            // type cast excessDistance to int since we don't include decimals in calculation;
            // if excessDistance is 3.56 for instance, we discard the 0.56
        }
        return totalFare;
    }

}
