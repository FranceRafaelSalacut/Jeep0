package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class List_of_Jeeps extends AppCompatActivity {

    ScrollView disp;
    DatabaseHelper myDB = new DatabaseHelper(this);
    LinearLayout layout;
    TextView test;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofjeeps);

        disp = (ScrollView) findViewById(R.id.disp);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        test = (TextView) findViewById(R.id.textView4);
        displayAllJeep();
    }

    private void displayAllJeep(){
        // Assuming you have a class that extends SQLiteOpenHelper and has a readable database instance
        layout.removeAllViews();
        disp.removeAllViews();
        // Define the table name and column name
        String tableName = "jeepney";
        String columnName = "code";

        SQLiteDatabase database = myDB.getReadableDatabase();
        // Query the database for distinct codes
        String query = "SELECT * FROM DisplayNames";
        Cursor cursor = database.rawQuery(query, null);

        List<String> show = new ArrayList<>();
        // Iterate through the cursor to retrieve the codes
        while (cursor.moveToNext()) {

            String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
            String loca = cursor.getString(cursor.getColumnIndexOrThrow("Locations"));

            Button button = new Button(this);
            //button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
            button.setText(jeepneyCode + " - " + loca);

            //ConstraintLayout test = null;

            //test = creates(test);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            layout.addView(button);
            //result.append(jeepneyCode).append("\t");//("\n");
            //result.append(dist).append("\t");
            //result.append(fare).append("\n");
            //For every result create button.
        }

        // Close the cursor after retrieving the codes
        cursor.close();
        myDB.close();
        //test.setText(show.toString());
        disp.addView(layout);
        // Now, you have all the distinct codes in the "codes" ArrayList
        // You can perform any further operations on the codes as needed

    }

}
