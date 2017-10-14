package com.theciceroneapp.cicerone.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

/**
 * Created by crsch on 10/13/2017.
 */

public class APIHelper {
    private static BroadcastReceiver locationReceiver;
    private static double latitude;
    private static double longitude;

    public static void listenForLocation(Context context) {

        Intent locationService = new Intent(context.getApplicationContext(), LocationService.class);
        context.startService(locationService);

        if (locationReceiver == null) {
            locationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");
                    System.out.printf("Lat: %f, Long: %f%n", latitude, longitude);
                }
            };
        }

        context.registerReceiver(locationReceiver, new IntentFilter("location_update"));
    }

    public static void setFirstLocation(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        System.out.printf("Initial Lat: %f, Long: %f%n", latitude, longitude);
    }
}
