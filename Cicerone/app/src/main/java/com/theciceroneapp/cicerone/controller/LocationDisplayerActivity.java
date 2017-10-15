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
import android.widget.LinearLayout;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.APIHelper;
import com.theciceroneapp.cicerone.model.TripService;

/**
 * Created by crsch on 10/15/2017.
 */

public class LocationDisplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onAttach(Context context) {
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        Fragment myFrag = new LocationFragment();
        fragTransaction.add(R.id.location_display, myFrag, null);
        fragTransaction.commit();
    }


}
