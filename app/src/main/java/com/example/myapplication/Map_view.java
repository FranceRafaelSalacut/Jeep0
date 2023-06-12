package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.EncodedPolyline;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;



import java.io.IOException;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class Map_view extends AppCompatActivity implements OnMapReadyCallback, OnMapsSdkInitializedCallback {

    //Button back;
    Intent recieve;
    TextView code, distance, fare, test;
    ImageView back;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private MapView mapView;
    private GoogleMap googleMap;
    private String TAG = "so47492459";
    private FusedLocationProviderClient fusedLocationClient;
    LatLng location1;
    LatLng location2;
    Button save;
    ArrayList<String> route = new ArrayList<>();
    String start, end, jeepneyCode, from;
    Double Distance, Fare;
    public static Double before = 0.0;

    Boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_route);

        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);

        recieve = getIntent();
        back = (ImageView) findViewById(R.id.back_button2);
        //test = (TextView) findViewById(R.id.textView3);
        save = findViewById(R.id.button4);
        jeepneyCode = recieve.getStringExtra("Code");
        String dist = recieve.getStringExtra("Dist");
        String fare = recieve.getStringExtra("Fare");
        from = recieve.getStringExtra("From");
        start = recieve.getStringExtra("Location");
        end = recieve.getStringExtra("Destination");
        //test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
        isSaved = checkSAVED();

        code = (TextView) findViewById(R.id.codeInfo2);
        code.setText(jeepneyCode);
        //test.setText(from);
        getRoutePath();

        mapView = findViewById(R.id.mapView3);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationCoordinates(start,end);

        //test.setText(location1.toString() + " " + location2.toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), input.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSaved) {
                    SQLiteDatabase database = databaseHelper.getReadableDatabase();

                    ContentValues values = new ContentValues();

                    values.put("CODE", jeepneyCode);
                    values.put("START", start);
                    values.put("END", end);

                    long newRowId = database.insert("HISTORY", null, values);

                    isSaved = true;
                    database.close();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Map_view.this);
                    builder.setMessage("Your Route has been saved in History")
                            .setTitle("Route already Saved");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button, do something
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

    private Boolean checkSAVED(){
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] projection = {"CODE", "START", "END"};

        String selection = "CODE = ? AND START = ? AND END = ?";
        String[] selectionArgs = {jeepneyCode, start, end};

        Cursor cursor = database.query(
                "HISTORY",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isDataAlreadySaved = cursor.moveToFirst();

        cursor.close();
        database.close();

        return isDataAlreadySaved;
    }
    private void getRoutePath(){
        // Assuming you have a DatabaseHelper class for managing your SQLite database

        // Query and store locations in an array
        String targetJeepCode = jeepneyCode ;
        ArrayList<String> locationsArray = new ArrayList<>();

        // Replace 'context' with your activity or application context
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] columns = {"Location"};
        String selection = "CODE = ?";
        String[] selectionArgs = {targetJeepCode};
        String orderBy = "Route_No";
        String result = "";

        Cursor cursor = database.query("Jeepney", columns, selection, selectionArgs, null, null, orderBy);

        while (cursor.moveToNext()) {
            String location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
            locationsArray.add(location);

        }
        cursor.close();
        database.close();
        boolean add = false;
        int size = locationsArray.size();
        int index = 0;

        while(true){
            if(index == size){
                index = 0;
            }

            if(locationsArray.get(index).equals(start)){
                add = true;
            }

            if(add){
                route.add(locationsArray.get(index));
            }

            if(locationsArray.get(index).equals(end) && add){
                break;
            }
            index+=1;
        }

        //test.setText(route.toString());
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

    private LatLng getspecificLocationCoordinates(String location) {

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve from the database
        String[] projection = { "latitude", "longitude" };

        // Define the selection criteria for the query
        String selection = "location = ?";
        String[] selectionArgs = { location };

        // Execute the query and retrieve the result as a Cursor
        Cursor cursor = database.query("Jeepney", projection, selection, selectionArgs, null, null, null);

        LatLng coordinates = null;

        if (cursor.moveToFirst()) {
            // Retrieve the latitude and longitude values from the cursor
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

            // Create a LatLng object with the retrieved coordinates
            coordinates = new LatLng(latitude, longitude);
        }

        cursor.close();
        database.close();

        return coordinates;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set the camera position to Cebu City
        LatLng cebuCity = new LatLng(10.3157, 123.8854);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cebuCity, 12f));

        // Add markers for the locations
        if (location1 != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(location1)
                    .title(start));
        }

        if (location2 != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(location2)
                    .title(end));
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);



       // test.setText("YES?");

        drawTest();

    }


    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    private void drawTest(){
        List<LatLng> path = new ArrayList();
        getRoute(path);

        int[] startLatlng = findFiveClosestCoordinateIndices(location1, path);
        int[] endLatlng = findFiveClosestCoordinateIndices(location2, path);

        Log.d(TAG, Arrays.toString(startLatlng));
        Log.d(TAG, Arrays.toString(endLatlng));
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
            putMarker(path.get(pair[0]));
            putMarker(path.get(pair[1]));
            Log.d(TAG, pair[0] + "---" + pair[1]);
            List<LatLng> routed = tracePath(path, pair[0], pair[1]);
            double totalDistance = getDistance(routed);

            if(totalDistance<maxDistance){
                maxDistance = totalDistance;
                route = routed;
            }

        }

        PolylineOptions opts = new PolylineOptions().addAll(route).color(Color.rgb(1, 183, 0)).width(10);
        googleMap.addPolyline(opts);



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

        distance = findViewById(R.id.codeInfo3);
        distance.setText(String.format("%.2f", totalDistance/1000));

        double fared = calculateFare((float) totalDistance/1000, (float) 12.00, (float) 1.80, (float) 4);

        Fare = fared;
        fare = findViewById(R.id.fareInfo3);
        fare.setText(String.format("%.2f", fared));

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

        int[] closestIndices = new int[8];
        int count = 0;

        while (count < 8 && !pq.isEmpty()) {
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
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
