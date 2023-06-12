package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Suggest_jeeps extends AppCompatActivity {

    ScrollView disp;
    DatabaseHelper myDB = new DatabaseHelper(this);
    LinearLayout layout;
    TextView test, list;
    ImageView back;

    Intent receive;
    String start, end;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofjeeps);

        disp = (ScrollView) findViewById(R.id.scrollView3);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //test = (TextView) findViewById(R.id.textView4);
        back = findViewById(R.id.back_button);
        //list = findViewById(R.id.textView4);

        receive = getIntent();
        start = receive.getStringExtra("start");
        end = receive.getStringExtra("end");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), input.class);
                startActivity(intent);
            }
        });
        executeQuery(start, end);
        //list.setText(start + " + " + end);
    }

    private void executeQuery(String location1, String location2) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String query = "SELECT CODE FROM Jeepney WHERE Location IN (?, ?) GROUP BY CODE HAVING COUNT(DISTINCT location) = 2";
        String[] selectionArgs = { location1, location2 };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        //StringBuilder result = new StringBuilder();

        layout.removeAllViews();
        disp.removeAllViews();

        // if nothing pops up from first query, do this instead
        if (cursor.getCount() == 0){
            executeQuery_v2(location1, location2);
        }
        else {
            layout.removeAllViews();
            disp.removeAllViews();

            while (cursor.moveToNext()) {
                //Outputs for unit testing are jeepneyCode, dist, fare.
                String jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                //String dist = calc_distance(jeepneyCode, location1, location2);
                //float t_fare = calculateFare((float) Integer.parseInt(dist), (float) 12.00, (float) 1.80, (float) 4);
                //String fare = Float.toString(t_fare);

                Button button = new Button(this);
                //button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);

                ShapeDrawable shapeDrawable = new ShapeDrawable();
                shapeDrawable.getPaint().setColor(Color.parseColor("#C1EAD9"));
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                shapeDrawable.getPaint().setStrokeWidth(5);

// Create a ShapeDrawable for the border
                ShapeDrawable borderDrawable = new ShapeDrawable();
                borderDrawable.getPaint().setColor(Color.BLACK);
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
        disp.addView(layout);
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
        disp.removeAllViews();

        if (cursor.getCount() > 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There are no DIRECT ROUTES on your inputs. However we are working on it, so stay tuned!")
                    .setTitle("Sorry");

// Add a button to dismiss the dialog
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button, do something
                    dialog.dismiss();
                }
            });

// Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There are no ROUTES on your inputs. Try placing new inputs.")
                    .setTitle("Sorry");

// Add a button to dismiss the dialog
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button, do something
                    dialog.dismiss();
                }
            });

// Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        /**
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
         **/
        cursor.close();
        db.close();
//      Disp.addView(layout);
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
            disp.removeAllViews();

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
        disp.addView(layout);
        //String resultRoute = result.toString();
        //testing.setText(resultRoute);
    }

}
