package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ImageView about = findViewById(R.id.imageView7);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), aboutus.class);
                startActivity(intent);
            }
        });



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

        ImageView imageView5 = findViewById(R.id.commute_news);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/LTFRB7/"; // Replace with your desired website URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(browserIntent);

                openWebsite(url);
            }
        });
    }

    private void openWebsite(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
