package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class History extends AppCompatActivity {

    ScrollView disp;
    LinearLayout layout;

    DatabaseHelper myDB = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        disp = findViewById(R.id.scrollView3);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.removeAllViews();
        disp.removeAllViews();


        SQLiteDatabase database = myDB.getReadableDatabase();

        // Define the columns you want to retrieve from the History table
        String[] projection = {
                "CODE",
                "START",
                "END"
        };

        // Perform the database query
        Cursor cursor = database.query(
                "History",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Iterate through the cursor to retrieve the data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve the values from the cursor and store them in variables
                String code = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("START"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("END"));
                ConstraintLayout constraintLayout = makeLayout(code, start, end);

                layout.addView(constraintLayout);
            } while (cursor.moveToNext());

            cursor.close();
        }
 // Close the database connection after use
        database.close();

        disp.addView(layout);

    }

    private ConstraintLayout makeLayout(String code, String start, String end){
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setId(R.id.constraintLayout3);
        //constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(97)));
        constraintLayout.setBackgroundResource(R.drawable.round_border_white);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(120)
        );
        layoutParams.setMargins(0, 0, 0, 16); // Set margin bottom of 16 pixels
        constraintLayout.setLayoutParams(layoutParams);

        TextView textView1 = new TextView(this);
        textView1.setId(R.id.code);
        textView1.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(45), dpToPx(21)));
        textView1.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView1.setText("CODE");
        textView1.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView1.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) textView1.getLayoutParams();
        layoutParams1.bottomToBottom = R.id.constraintLayout3;
        layoutParams1.endToEnd = R.id.constraintLayout3;
        layoutParams1.horizontalBias = 0.07f;
        layoutParams1.startToStart = R.id.constraintLayout3;
        layoutParams1.topToTop = R.id.constraintLayout3;
        layoutParams1.verticalBias = 0.21f;

        textView1.setLayoutParams(layoutParams1);

        constraintLayout.addView(textView1);

        TextView textView2 = new TextView(this);
        textView2.setId(R.id.km);
        textView2.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(90), dpToPx(21)));
        textView2.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView2.setText("Location :");
        textView2.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView2.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) textView2.getLayoutParams();
        layoutParams2.bottomToBottom = R.id.constraintLayout3;
        layoutParams2.endToEnd = R.id.constraintLayout3;
        layoutParams2.horizontalBias = 0.523f;
        layoutParams2.startToStart = R.id.constraintLayout3;
        layoutParams2.topToTop = R.id.constraintLayout3;
        layoutParams2.verticalBias = 0.20f;

        textView2.setLayoutParams(layoutParams2);

        constraintLayout.addView(textView2);

        TextView textView3 = new TextView(this);
        textView3.setId(R.id.fare);
        textView3.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(100), dpToPx(21)));
        textView3.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView3.setText("Destination :");
        textView3.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView3.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) textView3.getLayoutParams();
        layoutParams3.bottomToBottom = R.id.constraintLayout3;
        layoutParams3.endToEnd = R.id.constraintLayout3;
        layoutParams3.horizontalBias = 0.523f;
        layoutParams3.startToStart = R.id.constraintLayout3;
        layoutParams3.topToTop = R.id.constraintLayout3;
        layoutParams3.verticalBias = 0.70f;

        textView3.setLayoutParams(layoutParams3);

        constraintLayout.addView(textView3);

        TextView textView4 = new TextView(this);
        textView4.setId(R.id.codeInfo);
        textView4.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(49)));
        textView4.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView4.setText(code);
        textView4.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView4.setTextSize(32);
        textView4.setTypeface(textView4.getTypeface(), Typeface.BOLD);

        ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) textView4.getLayoutParams();
        layoutParams4.bottomToBottom = R.id.constraintLayout3;
        layoutParams4.endToEnd = R.id.constraintLayout3;
        layoutParams4.horizontalBias = 0.08f;
        layoutParams4.startToStart = R.id.constraintLayout3;
        layoutParams4.topToTop = R.id.constraintLayout3;
        layoutParams4.verticalBias = 0.77f;

        textView4.setLayoutParams(layoutParams4);

        constraintLayout.addView(textView4);

        TextView textView5 = new TextView(this);
        textView5.setId(R.id.codeInfo);
        textView5.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(49)));
        textView5.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView5.setText(start);
        textView5.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView5.setTextSize(15);
        textView5.setTypeface(textView4.getTypeface(), Typeface.BOLD);

        ConstraintLayout.LayoutParams layoutParams5 = (ConstraintLayout.LayoutParams) textView5.getLayoutParams();
        layoutParams5.bottomToBottom = R.id.constraintLayout3;
        layoutParams5.endToEnd = R.id.constraintLayout3;
        layoutParams5.horizontalBias = 0.523f;
        layoutParams5.startToStart = R.id.constraintLayout3;
        layoutParams5.topToTop = R.id.constraintLayout3;
        layoutParams5.verticalBias = 0.60f;

        textView5.setLayoutParams(layoutParams5);

        constraintLayout.addView(textView5);

        TextView textView6 = new TextView(this);
        textView6.setId(R.id.codeInfo);
        textView6.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(49)));
        textView6.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView6.setText(end);
        textView6.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView6.setTextSize(15);
        textView6.setTypeface(textView4.getTypeface(), Typeface.BOLD);

        ConstraintLayout.LayoutParams layoutParams6 = (ConstraintLayout.LayoutParams) textView6.getLayoutParams();
        layoutParams6.bottomToBottom = R.id.constraintLayout3;
        layoutParams6.endToEnd = R.id.constraintLayout3;
        layoutParams6.horizontalBias = 0.523f;
        layoutParams6.startToStart = R.id.constraintLayout3;
        layoutParams6.topToTop = R.id.constraintLayout3;
        layoutParams6.verticalBias = 1.2f;

        textView6.setLayoutParams(layoutParams6);

        constraintLayout.addView(textView6);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Map_view.class);
                intent.putExtra("Code", code);
                //intent.putExtra("Dist", dist);
                //intent.putExtra("Fare", fare);
                intent.putExtra("Location", start);
                intent.putExtra("Destination", end);
                startActivity(intent);
            }
        });

        return constraintLayout;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private int dptosp(int sp){
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        return Math.round(sp * scaleDensity);
    }


}
