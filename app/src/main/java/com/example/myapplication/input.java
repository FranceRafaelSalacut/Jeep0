package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class input extends AppCompatActivity implements OnMapReadyCallback, OnMapsSdkInitializedCallback {

    DatabaseHelper myDB = new DatabaseHelper(this);
    private MapView mapView;
    private GoogleMap googleMap;
    AutoCompleteTextView location, destination;
    Button findJeep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);

        location = (AutoCompleteTextView) findViewById(R.id.initialInputText);
        destination = (AutoCompleteTextView) findViewById(R.id.initialDesiredText);
        findJeep = findViewById(R.id.calculateFare);

        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT Location FROM Jeepney", null);
        String[] locations = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            String locName = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
            locations[i] = locName;
            i++;
        }

        // use ArrayAdapter for the autocomplete text fields to use the locations string array
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
        location.setAdapter(adapter);
        destination.setAdapter(adapter);

        cursor.close();
        db.close();

        mapView = findViewById(R.id.mapView3);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text1 = charSequence.toString();
                //String text2 = destination.getText().toString();
                //String concatenatedText = text1 + " " + text2;
                //testing.setText(concatenatedText);
                checkLocation(location.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text1 = location.getText().toString();
                //String text2 = charSequence.toString();
                //String concatenatedText = text1 + " " + text2;
                //testing.setText(concatenatedText);
                checkLocation(destination.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //not used
            }
        });

        findJeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Suggest_jeeps.class);
                intent.putExtra("start",location.getText().toString());
                intent.putExtra("end",destination.getText().toString());
                startActivity(intent);
            }
        });

    }



    private void checkLocation(String Locate){
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT Location FROM Jeepney", null);

        String userInputLocation = Locate;
        boolean locationExists = false;


        if (cursor != null && cursor.moveToFirst()) {
            do {
                String location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
                if (location.equalsIgnoreCase(userInputLocation)) {
                    locationExists = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if (locationExists) {
            // Location exists in the database
            getLocationCoordinates(Locate
            );
        }
    }

    private void getLocationCoordinates(String locate) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList1 = geocoder.getFromLocationName(locate + ", Cebu City", 1);
            if (!addressList1.isEmpty()) {
                Address address1 = addressList1.get(0);
                LatLng mark = new LatLng(address1.getLatitude(), address1.getLongitude());
                putMarker(mark);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putMarker(LatLng mark){
        if (mark != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(mark)
                    .title(location.getText().toString()));
        }
    }


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


    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set the camera position to Cebu City
        LatLng cebuCity = new LatLng(10.3157, 123.8854);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cebuCity, 12f));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

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
