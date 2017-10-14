package com.theciceroneapp.cicerone.model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by crsch on 10/13/2017.
 */

public class LocationService extends Service {

    private static LocationListener locationListener;
    private static LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Intent locationUpdate = new Intent("location_update");
                locationUpdate.putExtra("longitude", location.getLongitude());
                locationUpdate.putExtra("latitude", location.getLatitude());
                sendBroadcast(locationUpdate);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        String providerName = locationManager.getBestProvider(new Criteria(),
                true);

        if (providerName != null) {
            try {
                APIHelper.setFirstLocation(locationManager.getLastKnownLocation(providerName));
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
