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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Map_view extends AppCompatActivity {

    Button back;
    Intent recieve;
    TextView test;
    private MapView mapView;
    private MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setOsmdroidTileCache(new File(Environment.getExternalStorageDirectory(), "osmdroid/tiles"));

        back = (Button) findViewById(R.id.back_away);
        recieve = getIntent();
        test = (TextView) findViewById(R.id.textView4);
        String jeepneyCode = recieve.getStringExtra("Code");
        String dist = recieve.getStringExtra("Dist");
        String fare = recieve.getStringExtra("Fare");
        test.setText("Jeep Code: " + jeepneyCode + "\t Distance: " + dist + "\t Approx Fare: " + fare);
        mapView = findViewById(R.id.mapview);
        mapController = (MapController) mapView.getController();
        GeoPoint cebuCity = new GeoPoint(10.3157, 123.8854);
        mapController.setCenter(cebuCity);
        mapController.setZoom(12);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
                startActivity(intent);
            }
        });

    }

}
