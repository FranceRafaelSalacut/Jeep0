package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Suggest_jeeps extends AppCompatActivity {

    ScrollView disp;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseHelper myDB = new DatabaseHelper(this);
    LinearLayout layout;
    TextView test, list;
    ImageView back;
    private GoogleMap googleMap;
    Intent receive;
    String start, end;
    LatLng location1;
    LatLng location2;
    Double Distance, Fare;
    String jeepneyCode;


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
        getLocationCoordinates(start,end);
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
                jeepneyCode = cursor.getString(cursor.getColumnIndexOrThrow("CODE"));
                //String dist = calc_distance(jeepneyCode, location1, location2);
                //float t_fare = calculateFare((float) Integer.parseInt(dist), (float) 12.00, (float) 1.80, (float) 4);
                //String fare = Float.toString(t_fare);

                drawTest();
                ConstraintLayout button = makeLayout(jeepneyCode, location1, location2);
                //button.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);



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
        layoutParams1.horizontalBias = 0.300f;
        layoutParams1.startToStart = R.id.constraintLayout3;
        layoutParams1.topToTop = R.id.constraintLayout3;
        layoutParams1.verticalBias = 0.21f;

        textView1.setLayoutParams(layoutParams1);

        constraintLayout.addView(textView1);

        TextView textView2 = new TextView(this);
        textView2.setId(R.id.location);
        textView2.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(85), dpToPx(21)));
        textView2.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView2.setText("KM :");
        textView2.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView2.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) textView2.getLayoutParams();
        layoutParams2.bottomToBottom = R.id.constraintLayout3;
        layoutParams2.endToEnd = R.id.constraintLayout3;
        layoutParams2.horizontalBias = 0.753f;
        layoutParams2.startToStart = R.id.constraintLayout3;
        layoutParams2.topToTop = R.id.constraintLayout3;
        layoutParams2.verticalBias = 0.075f;

        textView2.setLayoutParams(layoutParams2);

        constraintLayout.addView(textView2);

        TextView textView3 = new TextView(this);
        textView3.setId(R.id.fare);
        textView3.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(102), dpToPx(22)));
        textView3.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView3.setText("Fare :");
        textView3.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView3.setTextSize(16);

        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) textView3.getLayoutParams();
        layoutParams3.bottomToBottom = R.id.constraintLayout3;
        layoutParams3.endToEnd = R.id.constraintLayout3;
        layoutParams3.horizontalBias = 0.778f;
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
        layoutParams4.horizontalBias = 0.308f;
        layoutParams4.startToStart = R.id.constraintLayout3;
        layoutParams4.topToTop = R.id.constraintLayout3;
        layoutParams4.verticalBias = 0.77f;

        textView4.setLayoutParams(layoutParams4);

        constraintLayout.addView(textView4);

        TextView textView5 = new TextView(this);
        textView5.setId(R.id.codeInfo);
        textView5.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView5.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView5.setText(String.format("%.2f", Distance));
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
                200,
                getResources().getDisplayMetrics()
        );

        textView5.setLayoutParams(layoutParams5);

        constraintLayout.addView(textView5);

        TextView textView6 = new TextView(this);
        textView6.setId(R.id.codeInfo);
        textView6.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView6.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_new));
        textView6.setText(String.format("%.2f", Fare));
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
                200,
                getResources().getDisplayMetrics()
        );

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

    private void drawTest(){
        List<LatLng> path = new ArrayList();
        getRoute(path);

        int[] startLatlng = findFiveClosestCoordinateIndices(location1, path);
        int[] endLatlng = findFiveClosestCoordinateIndices(location2, path);

        //test.setText(startLatlng + "\n" + endLatlng);
        List<int[]> nonRepeatingPairs = new ArrayList<>();

        for (int i = 0; i < startLatlng.length; i++) {
            for (int j = 0; j < endLatlng.length; j++) {
                if (startLatlng[i] != endLatlng[j]) {
                    int[] pair = {startLatlng[i], endLatlng[j]};
                    nonRepeatingPairs.add(pair);
                }
            }
        }

        List<LatLng> route = new ArrayList();
        double maxDistance = Double.MAX_VALUE;

        for (int[] pair : nonRepeatingPairs) {
            List<LatLng> routed = tracePath(path, pair[0], pair[1]);
            double totalDistance = getDistance(routed);

            if(totalDistance<maxDistance){
                maxDistance = totalDistance;
                route = routed;
            }

        }

        PolylineOptions opts = new PolylineOptions().addAll(route).color(Color.rgb(1, 183, 0)).width(10);



        for (int i = 0; i < route.size(); i++) {
            //putMarker(route.get(i));
        }

        double totalDistance = 0.0;
        int i = 0;
        while(true) {
            if(i+1 == route.size()){
                break;
            }
            LatLng point1 = route.get(i);
            LatLng point2 = route.get(i + 1);

            double distance = SphericalUtil.computeDistanceBetween(point1, point2);
            totalDistance += distance;

            i++;
        }

        Distance = totalDistance/1000;


        double fared = calculateFare((float) totalDistance/1000, (float) 12.00, (float) 1.80, (float) 4);

        Fare = fared;

    }

    private double getDistance(List<LatLng> routed){
        int i = 0;
        double totalDistance = 0.0;
        while(true) {
            if(i+1 == routed.size()){
                break;
            }
            LatLng point1 = routed.get(i);
            LatLng point2 = routed.get(i + 1);

            double distance = SphericalUtil.computeDistanceBetween(point1, point2);
            totalDistance += distance;

            i++;
        }

        return totalDistance;
    }


    private List<LatLng> tracePath(List<LatLng> path, int start, int end){
        boolean add = false;
        int size = path.size();
        int index = start;
        List<LatLng> route = new ArrayList();

        while(true){
            if(index == size){
                index = 0;
            }

            route.add(path.get(index));

            if(index == end){
                break;
            }
            index+=1;
        }

        return route;
    }

    private void putMarker(LatLng mark){
        if (mark != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(mark)
                    .title(mark.toString()));
        }
    }

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


    private List<LatLng> getRoute(List<LatLng> path){
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve from the database
        String[] projection = { "Longitude", "Latitude" };

        // Define the selection criteria for the query
        String selection = "CODE = ?";
        String[] selectionArgs = { jeepneyCode };

        // Execute the query and retrieve the result as a Cursor
        Cursor cursor = database.query("RoutePaths", projection, selection, selectionArgs, null, null, null);

        // Iterate through the cursor to retrieve the coordinates
        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude"));
            LatLng coordinates = new LatLng(latitude, longitude);
            path.add(coordinates);
        }

        cursor.close();
        database.close();

        return path;
    }

    public static int[] findFiveClosestCoordinateIndices(LatLng location, List<LatLng> path) {
        Map<Integer, Double> distanceMap = new HashMap<>();

        for (int i = 0; i < path.size(); i++) {
            LatLng coordinate = path.get(i);
            double distance = calculateDistance(location, coordinate);
            distanceMap.put(i, distance);
        }

        PriorityQueue<Map.Entry<Integer, Double>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue));

        pq.addAll(distanceMap.entrySet());

        int[] closestIndices = new int[3];
        int count = 0;

        while (count < 3 && !pq.isEmpty()) {
            Map.Entry<Integer, Double> entry = pq.poll();
            closestIndices[count] = entry.getKey();
            count++;
        }

        return closestIndices;
    }



    public static double calculateDistance(LatLng location1, LatLng location2) {
        double earthRadius = 6371;  // Radius of the Earth in kilometers

        double lat1 = Math.toRadians(location1.latitude);
        double lon1 = Math.toRadians(location1.longitude);
        double lat2 = Math.toRadians(location2.latitude);
        double lon2 = Math.toRadians(location2.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double distance = earthRadius * c;

        return distance;
    }

    private void getLocationCoordinates(String start, String end) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList1 = geocoder.getFromLocationName(start + ", Cebu City", 1);
            if (!addressList1.isEmpty()) {
                Address address1 = addressList1.get(0);
                location1 = new LatLng(address1.getLatitude(), address1.getLongitude());
            }

            List<Address> addressList2 = geocoder.getFromLocationName(end +", Cebu City", 1);
            if (!addressList2.isEmpty()) {
                Address address2 = addressList2.get(0);
                location2 = new LatLng(address2.getLatitude(), address2.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // turns out many location coordinates in Jeepney table are way off
        //location1 = getspecificLocationCoordinates(start);
        //location2 = getspecificLocationCoordinates(end);
    }

}
