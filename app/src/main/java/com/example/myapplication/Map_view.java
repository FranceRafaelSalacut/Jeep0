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

public class Map_view extends AppCompatActivity implements OnMapReadyCallback, OnMapsSdkInitializedCallback {

    Button back;
    Intent recieve;
    TextView test;
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

        back = (Button) findViewById(R.id.back_away);
        recieve = getIntent();
        test = (TextView) findViewById(R.id.textView4);
        jeepneyCode = recieve.getStringExtra("Code");
        String dist = recieve.getStringExtra("Dist");
        String fare = recieve.getStringExtra("Fare");
        start = recieve.getStringExtra("Location");
        end = recieve.getStringExtra("Destination");
        //test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);

        getRoutePath();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationCoordinates(start,end);

        //test.setText(location1.toString() + " " + location2.toString());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
                startActivity(intent);
            }
        });

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
    }

    private LatLng getspecificLocationCoordinates(String location) {
        /**Geocoder geocoder = new Geocoder(this);
        LatLng coordinates = null;
        try {
            List<Address> addressList1 = geocoder.getFromLocationName(location + ", Cebu City", 1);
            if (!addressList1.isEmpty()) {
                Address address1 = addressList1.get(0);
                coordinates = new LatLng(address1.getLatitude(), address1.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }**/

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


    /**
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Draw route between the locations
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyDljg5Fe3T6V3iieR6fCIKQS12M-iTg68o").build();
        DirectionsApiRequest directionsApiRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(location1.latitude, location1.longitude))
                .destination(new com.google.maps.model.LatLng(location2.latitude, location2.longitude))
                .mode(TravelMode.DRIVING);

        directionsApiRequest.setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    DirectionsLeg leg = route.legs[0];

                    // Add polyline for the route
                    EncodedPolyline encodedPolyline = route.overviewPolyline;
                    List<LatLng> points = PolyUtil.decode(encodedPolyline.getEncodedPath());
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.addAll(points);
                    polylineOptions.width(8f);
                    polylineOptions.color(Color.BLUE);
                    googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle error
            }
        });

        // Set camera position to show both locations
        LatLng boundsSouthwest = new LatLng(Math.min(location1.latitude, location2.latitude),
                Math.min(location1.longitude, location2.longitude));
        LatLng boundsNortheast = new LatLng(Math.max(location1.latitude, location2.latitude),
                Math.max(location1.longitude, location2.longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                new com.google.android.gms.maps.model.LatLngBounds(boundsSouthwest, boundsNortheast), 100));
    }

    **/

    /**
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
    }
     **/

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

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDljg5Fe3T6V3iieR6fCIKQS12M-iTg68o")
                .build();
        /**
        //DrawPolyLines(location1, location2);
        int size = route.size();
        int index1 = 0;
        int index2 = 1;
        while(true) {
            if(index2 == size){
                break;
            }
            LatLng loca1 = getspecificLocationCoordinates(route.get(index1));
            LatLng loca2 = getspecificLocationCoordinates(route.get(index2));
            String coordinates1 = loca1.latitude + "," + loca1.longitude;
            test.setText(coordinates1);
            //DrawPolyLines(loca1, loca2, context);
            index1+=1;
            index2+=1;
        }
        **/
       // test.setText("YES?");

        drawTest();


        //DrawPolyLines(loca1,loca2);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));
    }

    private void DrawPolyLines(LatLng Loc1, LatLng Loc2,GeoApiContext context){

        String coordinates1 = Loc1.latitude + "," + Loc1.longitude;
        String coordinates2 = Loc2.latitude + "," + Loc2.longitude;
        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request

        DirectionsApiRequest req = DirectionsApi.getDirections(context, coordinates1, coordinates2);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            //Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(1);
            googleMap.addPolyline(opts);
        }

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

        PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(10);
        googleMap.addPolyline(opts);


    }


    /**
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
    **/
}
