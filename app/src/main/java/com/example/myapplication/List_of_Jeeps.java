package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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

            ConstraintLayout constraintLayout = makeLayout(jeepneyCode, loca, "");

            layout.addView(constraintLayout);
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

    private ConstraintLayout makeLayout(String code, String start, String end){
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setId(R.id.constraintLayout3);
        //constraintLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(97)));
        constraintLayout.setBackgroundResource(R.drawable.rounded_border);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, dpToPx(10), 0, 0); // Set margin bottom of 16 pixels
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
        textView2.setId(R.id.location);
        textView2.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(85), dpToPx(21)));
        textView2.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView2.setText("Route :");
        textView2.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView2.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) textView2.getLayoutParams();
        layoutParams2.bottomToBottom = R.id.constraintLayout3;
        layoutParams2.endToEnd = R.id.constraintLayout3;
        layoutParams2.horizontalBias = 0.453f;
        layoutParams2.startToStart = R.id.constraintLayout3;
        layoutParams2.topToTop = R.id.constraintLayout3;
        layoutParams2.verticalBias = 0.075f;

        textView2.setLayoutParams(layoutParams2);

        constraintLayout.addView(textView2);

        TextView textView3 = new TextView(this);
        textView3.setId(R.id.fare);
        textView3.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(102), dpToPx(22)));
        textView3.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView3.setText("");
        textView3.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView3.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) textView3.getLayoutParams();
        layoutParams3.bottomToBottom = R.id.constraintLayout3;
        layoutParams3.endToEnd = R.id.constraintLayout3;
        layoutParams3.horizontalBias = 0.478f;
        layoutParams3.startToStart = R.id.constraintLayout3;
        layoutParams3.topToTop = R.id.constraintLayout3;
        layoutParams3.verticalBias = 0.645f;

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
        textView5.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView5.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView5.setText(start);
        textView5.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView5.setTextSize(15);
        textView5.setTypeface(textView4.getTypeface(), Typeface.BOLD);

        textView5.setAutoSizeTextTypeUniformWithConfiguration(
                12,
                24,
                2,
                TypedValue.COMPLEX_UNIT_SP // unit for sizes
        );

        ConstraintLayout.LayoutParams layoutParams5 = (ConstraintLayout.LayoutParams) textView5.getLayoutParams();
        layoutParams5.bottomToTop = R.id.fare;
        layoutParams5.startToStart = R.id.constraintLayout3;
        layoutParams5.topToBottom = R.id.location;
        layoutParams5.verticalBias = 1.0f;


        layoutParams5.leftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                123,
                getResources().getDisplayMetrics()
        );

        textView5.setLayoutParams(layoutParams5);

        constraintLayout.addView(textView5);

        TextView textView6 = new TextView(this);
        textView6.setId(R.id.codeInfo);
        textView6.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView6.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView6.setText("");
        textView6.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView6.setTextSize(15);
        textView6.setTypeface(textView4.getTypeface(), Typeface.BOLD);

        textView6.setAutoSizeTextTypeUniformWithConfiguration(
                12,
                24,
                2,
                TypedValue.COMPLEX_UNIT_SP // unit for sizes
        );

        ConstraintLayout.LayoutParams layoutParams6 = (ConstraintLayout.LayoutParams) textView6.getLayoutParams();
        layoutParams6.bottomToBottom = R.id.constraintLayout3;
        layoutParams6.startToStart = R.id.constraintLayout3;
        layoutParams6.topToBottom = R.id.fare;
        layoutParams6.verticalBias = 0.0f;

        layoutParams6.leftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                123,
                getResources().getDisplayMetrics()
        );

        textView6.setLayoutParams(layoutParams6);

        constraintLayout.addView(textView6);



        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Route_view.class);
                intent.putExtra("Code", code);
                intent.putExtra("Route", start);
                startActivity(intent);
            }
        });

        return constraintLayout;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
