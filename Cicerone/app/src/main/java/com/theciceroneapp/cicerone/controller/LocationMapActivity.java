package com.theciceroneapp.cicerone.controller;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.APIHelper;
import com.theciceroneapp.cicerone.model.Location;
import com.theciceroneapp.cicerone.model.Trip;
import com.theciceroneapp.cicerone.model.TripService;

import java.util.List;
import android.os.Handler;

public class LocationMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trip_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mHandler = new Handler(Looper.getMainLooper()) {
          @Override
          public void handleMessage(Message m) {
              createMarker((Location) m.obj);
          }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<Location> locations = Trip.getLocations();

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(APIHelper.latitude, APIHelper.longitude), 15));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            createMarker(location);
        }
    }

    private void createMarker(Location location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(ll).title(location.getName()));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(APIHelper.latitude, APIHelper.longitude)));
    }

}