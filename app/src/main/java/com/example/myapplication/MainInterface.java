package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininterface);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(getApplicationContext(), SuggestedRoutes.class);
            startActivity(intent);
        }, 1000);
    }


}
