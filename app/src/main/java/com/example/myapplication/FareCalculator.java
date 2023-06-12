package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FareCalculator extends AppCompatActivity {
    ImageView back;
    Button calculateFare;
    TextInputEditText distance;
    TextView textKm, textFare, textDiscount;
    Switch hasDiscount, isModern;
    double baseFare = 12.00, firstKm = 4.0, perKm = 1.80;
    boolean hasCalculated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare_calculator);
        back = findViewById(R.id.back_button);
        calculateFare = findViewById(R.id.calculateFare);
        distance = findViewById(R.id.inputDistance);
        textKm = findViewById(R.id.textKm);
        textFare = findViewById(R.id.textFare);
        textDiscount = findViewById(R.id.textDiscount);
        hasDiscount = findViewById(R.id.hasDiscount);
        isModern = findViewById(R.id.isModern);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hasCalculated = false;
            }
        });

        hasDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(hasCalculated){
                    double distance_val = Double.parseDouble(distance.getText().toString());
                    double fare = calculate(distance_val, hasDiscount.isChecked());
                    BigDecimal bd = new BigDecimal(distance_val).setScale(1, RoundingMode.HALF_UP);
                    if(isChecked){
                        double discount = fare * 0.2;
                        BigDecimal bd2 = new BigDecimal(fare - discount).setScale(1, RoundingMode.HALF_UP);
                        BigDecimal bd3 = new BigDecimal(discount).setScale(1, RoundingMode.HALF_UP);
                        textKm.setText(Double.toString(bd.doubleValue()));
                        textFare.setText(Double.toString(bd2.doubleValue()));
                        textDiscount.setText(Double.toString(bd3.doubleValue()));
                    }else{
                        textKm.setText(Double.toString(bd.doubleValue()));
                        textFare.setText(Double.toString(fare));
                        textDiscount.setText("0");
                    }
                }
            }
        });

        isModern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(hasCalculated) {
                    double distance_val = Double.parseDouble(distance.getText().toString());
                    double fare = calculate(distance_val, hasDiscount.isChecked());
                    double discount = hasDiscount.isChecked()? fare*0.2 : 0;
                    BigDecimal bd = new BigDecimal(distance_val).setScale(1, RoundingMode.HALF_UP);
                    BigDecimal bd2 = new BigDecimal(fare - discount).setScale(1, RoundingMode.HALF_UP);
                    BigDecimal bd3 = new BigDecimal(discount).setScale(1, RoundingMode.HALF_UP);
                    textKm.setText(Double.toString(bd.doubleValue()));
                    textFare.setText(Double.toString(bd2.doubleValue()));
                    textDiscount.setText(hasDiscount.isChecked()? Double.toString(bd3.doubleValue()):"0");
                }
            }
        });

        calculateFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(distance.getText().toString().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                    double distance_val = Double.parseDouble(distance.getText().toString());
                    double fare = calculate(distance_val, hasDiscount.isChecked());
                    double discount = hasDiscount.isChecked()? fare*0.2 : 0;
                    BigDecimal bd = new BigDecimal(distance_val).setScale(1, RoundingMode.HALF_UP);
                    BigDecimal bd2 = new BigDecimal(fare - discount).setScale(1, RoundingMode.HALF_UP);
                    BigDecimal bd3 = new BigDecimal(discount).setScale(1, RoundingMode.HALF_UP);
                    textKm.setText(Double.toString(bd.doubleValue()));
                    textFare.setText(Double.toString(bd2.doubleValue()));
                    textDiscount.setText(hasDiscount.isChecked()? Double.toString(bd3.doubleValue()):"0");
                }
            }
        });
    }

    private double calculate(double distance, boolean discounted) {

        if(discounted){
            baseFare = baseFare - (baseFare * 0.2);
        }else{
            baseFare = 12.00;
        }

        if(isModern.isChecked()){
            baseFare = 14.00;
            perKm = 2.20;
        }else{
            baseFare = 12.00;
            perKm = 1.80;
        }

        // set totalFare to baseFare
        double totalFare = baseFare;
        // if travel distance greater than firstKm, increase totalFare
        if(distance > firstKm){
            double excessDistance = distance - firstKm;
            totalFare = baseFare + perKm * (int) excessDistance;
            // type cast excessDistance to int since we don't include decimals in calculation;
            // if excessDistance is 3.56 for instance, we discard the 0.56
        }
        hasCalculated = true;
        BigDecimal bd = new BigDecimal(totalFare).setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
