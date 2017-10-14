package com.theciceroneapp.cicerone.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Mode;
import com.theciceroneapp.cicerone.model.Trip;

import java.util.LinkedList;

public class ModeSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selector);

        // Declare View objects
        Switch mTourStatus = (Switch) findViewById(R.id.swtTourMode);
        CheckBox mCultureStatus = (CheckBox) findViewById(R.id.chkCulture);
        CheckBox mFoodStatus = (CheckBox) findViewById(R.id.chkFood);
        CheckBox mEntStatus = (CheckBox) findViewById(R.id.chkEntertainment);
        CheckBox mBusinessStatus = (CheckBox) findViewById(R.id.chkBusiness);
        final CheckBox[] chkGroup = {mBusinessStatus, mCultureStatus, mEntStatus, mFoodStatus};

        // When tour mode is flipped, switch checkboxes being enabled
        mTourStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox chk : chkGroup) {
                        chk.setChecked(false);
                        chk.setEnabled(false);
                    }
                } else {
                    for (CheckBox chk : chkGroup) {
                        chk.setEnabled(true);
                    }
                }
            }
        });



        Button mTour = (Button) findViewById(R.id.btnTour);
        mTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pull the modes selected from checkboxes and pass them into Trip()

                Trip trip = new Trip(Mode.CULTURE);
                trip.startTrip();


                Intent tripPage = new Intent(getApplicationContext(),
                        ThisTripActivity.class);
                startActivity(tripPage);
            }
        });
    }
}
