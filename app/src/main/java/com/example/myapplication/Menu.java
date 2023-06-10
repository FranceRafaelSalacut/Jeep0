package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ImageView imageView = findViewById(R.id.suggestedRoute);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), input.class);
                startActivity(intent);
            }
        });

        ImageView imageView2 = findViewById(R.id.fare_calc);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FareCalculator.class);
                startActivity(intent);
            }
        });

        ImageView imageView3 = findViewById(R.id.travel_hist);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
            }
        });

        ImageView imageView4 = findViewById(R.id.jeepneyList);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), List_of_Jeeps.class);
                startActivity(intent);
            }
        });
    }
}
