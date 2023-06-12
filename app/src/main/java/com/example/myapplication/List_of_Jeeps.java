package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    ImageView
            back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofjeeps);

        disp = (ScrollView) findViewById(R.id.scrollView3);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //test = (TextView) findViewById(R.id.textView4);
        back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });
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
        String query = "SELECT * FROM DisplayNames ORDER BY CODE";
        Cursor cursor = database.rawQuery(query, null);

        List<String> show = new ArrayList<>();
        // Iterate through the cursor to retrieve the codes
        while (cursor.moveToNext()) {

            String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
            String loca = cursor.getString(cursor.getColumnIndexOrThrow("Locations"));

            Button button = new Button(this);

            // Set padding (left, top, right, bottom)


// Create a ShapeDrawable for the border
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.getPaint().setColor(Color.parseColor("#C1EAD9"));
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            shapeDrawable.getPaint().setStrokeWidth(5);

// Create a ShapeDrawable for the border
            ShapeDrawable borderDrawable = new ShapeDrawable();
            borderDrawable.getPaint().setColor(Color.parseColor("#5CC297"));
            borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
            borderDrawable.getPaint().setStrokeWidth(5);

// Create a LayerDrawable to combine background and border
            Drawable[] layers = {shapeDrawable, borderDrawable};
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            button.setBackground(layerDrawable);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            layoutParams.setMargins(0, 0, 0, 16); // Set margin bottom of 16 pixels
            button.setLayoutParams(layoutParams);

            button.setText(jeepneyCode + " - " + loca);

            //ConstraintLayout test = null;

            //test = creates(test);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Route_view.class);
                    intent.putExtra("Code", jeepneyCode);
                    startActivity(intent);
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
