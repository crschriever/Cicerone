package com.theciceroneapp.cicerone.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.APIHelper;
import com.theciceroneapp.cicerone.model.Location;
import com.theciceroneapp.cicerone.model.Trip;
import com.theciceroneapp.cicerone.model.TripService;

/**
 * Created by crsch on 10/15/2017.
 */

public class LocationDisplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_location);

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        Location location = Trip.locationAt(0);

        TextView locationName = (TextView) findViewById(R.id.tvLocationName);
        ImageView locationImage = (ImageView) findViewById(R.id.imvLocationPhoto);
        TextView locationDescription = (TextView) findViewById(R.id.tvDescription);
        TextView locationDescriptionTitle = (TextView) findViewById(R.id.tvDescriptionTitle);
        TextView locationAddress = (TextView) findViewById(R.id.tvAddress);
        TextView locationRating = (TextView) findViewById(R.id.tvRating);
        TextView locationRatingTitle = (TextView) findViewById(R.id.tvRatingTitle);
        TextView locationWebsite = (TextView) findViewById(R.id.tvWebsite);

        System.out.println(location);

        // Avoid using an empty location
        if (location != null) {
            locationName.setText(location.getName());
            locationDescription.setText(location.getDescription());
            locationAddress.setText(location.getAddress());
            locationRating.setText(location.getRating());
            locationWebsite.setText(location.getWebsiteURL());
            if (location.getName() == null || location.getName().equals("")) {
                locationName.setVisibility(View.GONE);
            }
            if (location.getDescription() == null || location.getDescription().equals("")) {
                locationDescription.setVisibility(View.GONE);
                locationDescriptionTitle.setVisibility(View.GONE);
            }
            if (location.getAddress() == null || location.getAddress().equals("")) {
                locationAddress.setVisibility(View.GONE);
            }
            if (location.getRating() == null || location.getRating().equals("-1.0")|| location.getRating().equals("")) {
                locationRating.setVisibility(View.GONE);
                locationRatingTitle.setVisibility(View.GONE);
            }
            if (location.getWebsiteURL() == null || location.getWebsiteURL().equals("")) {
                locationWebsite.setVisibility(View.GONE);
            }
        } else {
            // I want these invisible
            locationName.setVisibility(View.GONE);
            locationDescription.setVisibility(View.GONE);
            locationDescriptionTitle.setVisibility(View.GONE);
            locationAddress.setVisibility(View.GONE);
            locationRating.setVisibility(View.GONE);
            locationRatingTitle.setVisibility(View.GONE);
            locationWebsite.setVisibility(View.GONE);
        }

        Fragment myFrag = LocationFragment.newInstance(0);
        fragTransaction.add(myFrag.getId(), myFrag, null);
        fragTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
