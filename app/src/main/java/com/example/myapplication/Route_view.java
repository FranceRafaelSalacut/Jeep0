package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;



import java.io.IOException;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Route_view extends AppCompatActivity implements OnMapReadyCallback, OnMapsSdkInitializedCallback {

    //Button back;
    Intent recieve;
    TextView test;
    ImageView back;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private MapView mapView;
    private GoogleMap googleMap;
    private String TAG = "so47492459";
    private FusedLocationProviderClient fusedLocationClient;
    LatLng location1;
    LatLng location2;

    ArrayList<String> route = new ArrayList<>();
    String start, end, jeepneyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);

        recieve = getIntent();
        back = (ImageView) findViewById(R.id.back_button);
        test = (TextView) findViewById(R.id.textView3);
        jeepneyCode = recieve.getStringExtra("Code");
        //test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);;

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        //test.setText(location1.toString() + " " + location2.toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set the camera position to Cebu City
        LatLng cebuCity = new LatLng(10.3157, 123.8854);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cebuCity, 12f));

        googleMap.getUiSettings().setZoomControlsEnabled(true);

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

        PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(10);
        googleMap.addPolyline(opts);
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

    public static int findClosestCoordinateIndex(LatLng location, List<LatLng> path) {
        double closestDistance = Double.MAX_VALUE;
        int closestIndex = -1;

        for (int i = 0; i < path.size(); i++) {
            LatLng coordinate = path.get(i);
            double distance = calculateDistance(location, coordinate);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        return closestIndex;
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