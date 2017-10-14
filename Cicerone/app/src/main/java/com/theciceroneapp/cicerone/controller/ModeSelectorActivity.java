package com.theciceroneapp.cicerone.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Mode;
import com.theciceroneapp.cicerone.model.Trip;

public class ModeSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selector);

        Button mTour = (Button) findViewById(R.id.btnTour);
        mTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip = new Trip(Mode.CULTURE);
                trip.startTrip();


                Intent tripPage = new Intent(getApplicationContext(),
                        ThisTripActivity.class);
                startActivity(tripPage);
            }
        });
    }
}
