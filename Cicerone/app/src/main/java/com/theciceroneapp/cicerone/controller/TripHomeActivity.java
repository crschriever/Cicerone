package com.theciceroneapp.cicerone.controller;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.theciceroneapp.cicerone.R;

public class TripHomeActivity extends AppCompatActivity implements LocationFragment.OnFragmentInteractionListener {

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
    }

    public void updateFragments() {
        lfpAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        // Maybe do something maybe not, it's for communicating with fragments
    }
}
