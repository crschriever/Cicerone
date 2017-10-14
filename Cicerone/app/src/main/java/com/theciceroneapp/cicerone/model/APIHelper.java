package com.theciceroneapp.cicerone.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by crsch on 10/13/2017.
 */

public class APIHelper {
    private static BroadcastReceiver locationReceiver;
    private static long latitude;
    private static long longitude;

    public static void listenForLocation(Context context) {

        Intent locationService = new Intent(context.getApplicationContext(), LocationService.class);
        context.startService(locationService);

        if (locationReceiver == null) {
            locationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (long) intent.getExtras().get("latitude");
                    longitude = (long) intent.getExtras().get("longitude");
                    System.out.printf("Lat: %d, Long: %d%n", latitude, longitude);
                }
            };
        }

        context.registerReceiver(locationReceiver, new IntentFilter("location_update"));
    }
}
