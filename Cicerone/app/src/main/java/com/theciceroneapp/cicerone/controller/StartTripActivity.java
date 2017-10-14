package com.theciceroneapp.cicerone.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.APIHelper;
import com.theciceroneapp.cicerone.model.LocationService;
import com.theciceroneapp.cicerone.model.TripService;

public class StartTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Creating Start Trip Activity");

        setContentView(R.layout.activity_start_trip);

        Button mBegin = (Button) findViewById(R.id.btnBegin);
        mBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLocationPermissions()) {
                    // We have permission to run location services
                    //enableLocationServices();
                }
                Intent modePage = new Intent(getApplicationContext(), ModeSelectorActivity.class);
                startActivity(modePage);
            }
        });

        if (getLocationPermissions()) {
            // We have permission to run location services
            enableLocationServices();
        }

        //Speech service
        Intent speechService = new Intent(getApplicationContext(), TripService.class);
        startService(speechService);
    }

    private void enableLocationServices() {
        APIHelper.init(this);
    }

    private boolean getLocationPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enableLocationServices();
            } else {
                //Handle permission denied
            }
        }
    }
}
