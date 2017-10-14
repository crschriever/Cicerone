package com.theciceroneapp.cicerone.model;

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

        queue = Volley.newRequestQueue(context);
    }

    public static void setFirstLocation(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        System.out.printf("Initial Lat: %f, Long: %f%n", latitude, longitude);
    }

    public static void getLocations(double radius, Mode mode, LocationsPromise prom) {

        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + (float)latitude + "," + (float)longitude + "&radius=" + radius + "&type=" + mode.getAPISTRING()
                + "&rankby=prominence&key=" + PLACE_API_KEY;
        final LocationsPromise promise = prom;
        System.out.println(url);

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
                            System.out.println(res.getJSONObject(0));
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject object = res.getJSONObject(i);
                                double longi = -3000;
                                double lat = -3000;
                                String name = "";
                                String address = "";
                                String[] types = null;
                                if (object.has("geometry") && object.getJSONObject("geometry").has("location")) {
                                    longi = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                    lat = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                }

                                if (object.has("name")) {
                                    name = object.getString("name");
                                }
                                if (object.has("vicinity")) {
                                    address = object.getString("vicinity");
                                }

                                if (object.has("types")) {
                                    JSONArray JSONTypes = object.getJSONArray("types");
                                    types = new String[JSONTypes.length()];
                                    for (int j = 0; j < JSONTypes.length(); j++) {
                                        types[j] = (String) JSONTypes.get(j);
                                    }
                                }

                                if (types != null) {
                                    locations[i] = new com.theciceroneapp.cicerone.model.Location(longi, lat, name, address, types);
                                }
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
