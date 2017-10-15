package com.theciceroneapp.cicerone.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Mode;
import com.theciceroneapp.cicerone.model.Trip;

import java.util.ArrayList;
import java.util.LinkedList;

public class ModeSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selector);

        // Declare View objects
        Switch mTourStatus = (Switch) findViewById(R.id.swtTourMode);
        final CheckBox mCultureStatus = (CheckBox) findViewById(R.id.chkCulture);
        final CheckBox mFoodStatus = (CheckBox) findViewById(R.id.chkFood);
        final CheckBox mEntStatus = (CheckBox) findViewById(R.id.chkEntertainment);
        final CheckBox mBusinessStatus = (CheckBox) findViewById(R.id.chkBusiness);
        final CheckBox[] chkGroup = {mBusinessStatus, mCultureStatus, mEntStatus, mFoodStatus};
        final RadioButton mInVehicle = (RadioButton) findViewById(R.id.radVehicle);
        final RadioButton mInPlane = (RadioButton) findViewById(R.id.radFlying);

        // When tour mode is flipped, switch checkboxes being enabled
        mTourStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox chk : chkGroup) {
                        if (chk == mCultureStatus || chk == mEntStatus) {
                            chk.setChecked(true);
                        } else {
                            chk.setChecked(false);
                        }
                        chk.setEnabled(false);
                    }
                } else {
                    for (CheckBox chk : chkGroup) {
                        chk.setEnabled(true);
                    }
                }
            }
        });



        final Button mTour = (Button) findViewById(R.id.btnTour);
        mTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pull the modes selected from checkboxes and pass them into Trip()
                ArrayList<Mode> modes = new ArrayList<>(4);
                if (mCultureStatus.isChecked()) {
                    modes.add(Mode.CULTURE);
                }
                if (mFoodStatus.isChecked()) {
                    modes.add(Mode.FOOD);
                }
                if (mEntStatus.isChecked()) {
                    modes.add(Mode.ENTERTAINMENT);
                }
                if (mBusinessStatus.isChecked()) {
                    modes.add(Mode.BUSINESS);
                }

                if (modes.isEmpty()) {
                    // TODO: disable the button if nothing is selected
                } else {
                    Trip trip;
                    if (mInVehicle.isChecked()) {
                        trip = new Trip(modes, Trip.MODE_RIDING);
                    } else if (mInPlane.isChecked()) {
                        trip = new Trip(modes, Trip.MODE_FLYING);
                    } else {
                        trip = new Trip(modes, Trip.MODE_WALKING);
                    }
                    trip.startTrip();


                    Intent tripPage = new Intent(getApplicationContext(),
                            TripHomeActivity.class);
                    startActivity(tripPage);
                    /*Intent mapPage = new Intent(getApplicationContext(), LocationMapActivity.class);
                    startActivity(mapPage);*/
                }
            }
        });
    }
}
