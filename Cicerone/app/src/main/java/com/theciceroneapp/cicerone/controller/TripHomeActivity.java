package com.theciceroneapp.cicerone.controller;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Trip;

public class TripHomeActivity extends AppCompatActivity implements LocationFragment.OnFragmentInteractionListener {

    public static TripHomeActivity singleton;

    LocationFragmentPagerAdapter lfpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        lfpAdapter = new LocationFragmentPagerAdapter(getSupportFragmentManager(),
                TripHomeActivity.this);

        // Get the ViewPager and setup adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(lfpAdapter);

        // Give TabLayout the pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        singleton = this;
    }

    public void updateFragments() {
        lfpAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Maybe do something maybe not, it's for communicating with fragments
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trip_end, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.show_map:
                Intent map = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(map);
                break;
            // action with ID action_settings was selected
            case R.id.end_tour:
                Trip.stopTrip();
                Intent returnHome = new Intent(getApplicationContext(), StartTripActivity.class);
                startActivity(returnHome);
                break;
            default:
                break;
        }

        return true;
    }
}
