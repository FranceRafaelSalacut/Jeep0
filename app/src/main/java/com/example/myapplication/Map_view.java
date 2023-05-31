package com.example.myapplication;

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





import java.util.Arrays;
import java.util.List;



import java.io.IOException;
import java.util.List;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Map_view extends AppCompatActivity implements OnMapReadyCallback {

    Button back;
    Intent recieve;
    TextView test;
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng location1;
    LatLng location2;


    String start, end, jeepneyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        back = (Button) findViewById(R.id.back_away);
        recieve = getIntent();
        test = (TextView) findViewById(R.id.textView4);
        jeepneyCode = recieve.getStringExtra("Code");
        String dist = recieve.getStringExtra("Dist");
        String fare = recieve.getStringExtra("Fare");
        start = recieve.getStringExtra("Location");
        end = recieve.getStringExtra("Destination");
        //test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);

        // Initialize the Places SDK
        Places.initialize(getApplicationContext(), "AIzaSyDljg5Fe3T6V3iieR6fCIKQS12M-iTg68o");
        PlacesClient placesClient = Places.createClient(this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationCoordinates(start,end);

        test.setText(start + " " + end);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
        FetchPlaceRequest location1Request = FetchPlaceRequest.builder(location1.toString(), placeFields).build();
        FetchPlaceRequest location2Request = FetchPlaceRequest.builder(location2.toString(), placeFields).build();

        placesClient.fetchPlace(location1Request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FetchPlaceResponse response = task.getResult();
                Place place = response.getPlace();
                String location1Name = place.getName();
                // Update location1 marker with the fetched name
                googleMap.addMarker(new MarkerOptions().position(location1).title(location1Name));
            }
        });

        placesClient.fetchPlace(location2Request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FetchPlaceResponse response = task.getResult();
                Place place = response.getPlace();
                String location2Name = place.getName();
                // Update location2 marker with the fetched name
                googleMap.addMarker(new MarkerOptions().position(location2).title(location2Name));
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
                startActivity(intent);
            }
        });

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
    }**/
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
