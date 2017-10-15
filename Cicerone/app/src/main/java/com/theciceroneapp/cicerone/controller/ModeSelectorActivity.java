package com.theciceroneapp.cicerone.controller;

import android.content.Context;
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
import android.widget.Toast;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Mode;
import com.theciceroneapp.cicerone.model.NotificationService;
import com.theciceroneapp.cicerone.model.Trip;

import java.util.ArrayList;
import java.util.LinkedList;

public class ModeSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selector);

        // Declare View objects
        final Switch mSilentStatus = (Switch) findViewById(R.id.swtSilentMode);
        final CheckBox mCultureStatus = (CheckBox) findViewById(R.id.chkCulture);
        final CheckBox mFoodStatus = (CheckBox) findViewById(R.id.chkFood);
        final CheckBox mEntStatus = (CheckBox) findViewById(R.id.chkEntertainment);
        final CheckBox mBusinessStatus = (CheckBox) findViewById(R.id.chkBusiness);
        final CheckBox[] chkGroup = {mBusinessStatus, mCultureStatus, mEntStatus, mFoodStatus};
        final RadioButton mInVehicle = (RadioButton) findViewById(R.id.radVehicle);
        final RadioButton mInPlane = (RadioButton) findViewById(R.id.radFlying);

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
                    Context context = getApplicationContext();
                    CharSequence msg = "At least one category must be selected!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, msg, duration);
                    toast.show();
                } else {
                    Trip trip;
                    if (mInVehicle.isChecked()) {
                        trip = new Trip(modes, Trip.MODE_RIDING);
                    } else if (mInPlane.isChecked()) {
                        trip = new Trip(modes, Trip.MODE_FLYING);
                    } else {
                        trip = new Trip(modes, Trip.MODE_WALKING);
                    }
                    final Trip finalTrip = trip;

                    trip.silentMode(mSilentStatus.isChecked());
                    Thread tripRunner = new Thread() {
                        @Override
                        public void run() {
                            finalTrip.startTrip();
                        }
                    };
                    view.post(tripRunner);

                    Intent tripPage = new Intent(getApplicationContext(),
                            TripHomeActivity.class);
                    startActivity(tripPage);
                }
            }
        });
    }
}
