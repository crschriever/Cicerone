package com.theciceroneapp.cicerone.controller;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.theciceroneapp.cicerone.model.Trip;

/**
 * Created by keshr on 10/14/2017.
 */

public class LocationFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Overview", "Current Location"};
    private Context context;

    public LocationFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            // This is the page with OVERVIEW
            return LocationFragment.newInstance(-2);
        } else {
            // This is the page with CURRENT LOCATION
            return LocationFragment.newInstance(-1);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
