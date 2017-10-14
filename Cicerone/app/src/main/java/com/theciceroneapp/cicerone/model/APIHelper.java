package com.theciceroneapp.cicerone.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by crsch on 10/13/2017.
 */

public class APIHelper {

    private static final String PLACE_API_KEY = "AIzaSyBInMdH8pnh1ylbZNnXCnLPndOeWJgk-Uc";

    private static BroadcastReceiver locationReceiver;
    private static double latitude;
    private static double longitude;

    private static RequestQueue queue;

    public static void init(Context context) {

        Intent locationService = new Intent(context.getApplicationContext(), LocationService.class);
        context.startService(locationService);

        final LocationsPromise promise =  new LocationsPromise() {
            @Override
            public void locationsFound(com.theciceroneapp.cicerone.model.Location[] locations) {
                for(com.theciceroneapp.cicerone.model.Location l: locations) {
                    System.out.println(l.getName());
                }
            }
        };

        if (locationReceiver == null) {
            locationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");
                    System.out.printf("Lat: %f, Long: %f%n", latitude, longitude);
                    getLocations(3000, null, promise);
                    TripService.say("Test");
                }
            };
        }

        context.registerReceiver(locationReceiver, new IntentFilter("location_update"));

        queue = Volley.newRequestQueue(context);
    }

    public static void setFirstLocation(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        System.out.printf("Initial Lat: %f, Long: %f%n", latitude, longitude);
    }

    public static void getLocations(double radius, String[] types, LocationsPromise prom) {

        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + (float)latitude + "," + (float)longitude + "&radius=" + radius + "&type=restaurant&key=" + PLACE_API_KEY;
        final LocationsPromise promise = prom;
//        System.out.println(url);

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            JSONArray res = response.getJSONArray("results");
                            com.theciceroneapp.cicerone.model.Location[] locations = new com.theciceroneapp.cicerone.model.Location[res.length()];
                            System.out.println("Response Length: " + res.length());
                            //System.out.println(res.getJSONObject(0).getJSONObject("geometry").getJSONObject("location"));
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject object = res.getJSONObject(i);
                                double longi = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                double lat = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                String name = object.getString("name");
                                String address = object.getString("vicinity");

                                locations[i] = new com.theciceroneapp.cicerone.model.Location(longi, lat, name, address);
                            }
                            promise.locationsFound(locations);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error! Request PLACE API failed");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

}
