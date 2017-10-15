package com.theciceroneapp.cicerone.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.provider.Settings;

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
    private static final String GEOCODE_API_KEY = "AIzaSyANVyKNAsgFOjLRSiSHjsGcfcgEE4U47pY";

    private static BroadcastReceiver locationReceiver;
    public static double latitude;
    public static double longitude;

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

    /**
     * Location's description is populated here
     */
    public static void getWikiInformation(final com.theciceroneapp.cicerone.model.Location location, final InformationPromise promise) {
        String locName = location.getName();
        String url ="https://en.wikipedia.org/w/api.php?action=parse&page=" + locName + "&format=json&redirects";
        //final LocationsPromise promise = prom;
        //System.out.println(url);

        final String locationName = locName;

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println(response);
                            String res = response.getJSONObject("parse").getJSONObject("text").getString("*");
                            int start = res.indexOf("<p>");
                            int end = res.indexOf("</p>", start);
                            String text = res.substring(start, end);
                            text = text.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
                            text = text.replaceAll("\\[.*\\]", "");
                            text = text.replaceAll("\\(.*\\)", "");
                            //System.out.println(text);

                            String regex = "<span class=\"geo\">";
                            int locationIndex = res.indexOf(regex) + regex.length();
                            int endLocation = res.indexOf(";", locationIndex);
                            double lat = Double.parseDouble(res.substring(locationIndex, endLocation));
                            locationIndex = endLocation + 1;
                            endLocation = res.indexOf("<", locationIndex);
                            double lon = Double.parseDouble(res.substring(locationIndex, endLocation));

                            if (Math.abs(location.getLatitude() - lat) < 5 && Math.abs(location.getLongitude() - lon) < 5) {
                                location.setDescription(text);
                                if (location.getWebsiteURL().equals("")) {
                                    location.setWebsiteURL("wikipedia.org/wiki/" + locationName.replaceAll("\\s", "%20"));
                                }
                                String distStr = String.format("%.1f", DistanceCalculator.distance(latitude, longitude, lat, lon, "M"));
                                promise.foundInformation(location, String.format("%s is %s miles to the %s. %s",
                                        locationName, distStr,
                                        BearingCalculator.calculateBearing(latitude, longitude, lat, lon), text));
                            } else {
                                promise.foundInformation(location, "");
                            }

                        } catch (JSONException e) {
                            System.out.println("JSON element not found");
                            promise.foundInformation(location, "");
                        } catch (StringIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            promise.foundInformation(location, "");
                        } catch (NumberFormatException e) {
                            System.out.println("Lat or Long mis formatted");
                            promise.foundInformation(location, "");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error! Request PLACE API failed");
                promise.foundInformation(location, "");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public static void getLocations(double radius, Mode[] modes, LocationsPromise prom) {

        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + (float)latitude + "," + (float)longitude + "&radius=" + (float)radius + "&type=" + Mode.getCombinedAPIString(modes)
                + "&rankby=prominence&key=" + PLACE_API_KEY;
        final LocationsPromise promise = prom;
        //System.out.println(url);

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println(response);
                            JSONArray res = response.getJSONArray("results");
                            com.theciceroneapp.cicerone.model.Location[] locations = new com.theciceroneapp.cicerone.model.Location[res.length()];
                            //System.out.println("Response Length: " + res.length());
                            //System.out.println(res.getJSONObject(0));
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject object = res.getJSONObject(i);
                                double longi = -3000;
                                double lat = -3000;
                                String name = "";
                                String address = "";
                                String[] types = null;
                                String websiteURL = "";
                                String mapsURL = "";
                                double rating = -1;

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

                                if (object.has("website")) {
                                    websiteURL = object.getString("website");
                                }

                                if (object.has("url")) {
                                    mapsURL = object.getString("url");
                                }

                                if (object.has("rating")) {
                                    rating = object.getDouble("rating");
                                }

                                if (types != null) {
                                    locations[i] = new com.theciceroneapp.cicerone.model.Location(longi, lat, name, address, types,
                                            websiteURL, mapsURL, rating);
                                }
                            }
                            promise.locationsFound(locations);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            promise.locationsFound(new com.theciceroneapp.cicerone.model.Location[]{});
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error! Request PLACE API failed");
                promise.locationsFound(new com.theciceroneapp.cicerone.model.Location[]{});
            }
        });

        // Add the request to the RequestQueue.
        queue.add(request);
    }

}
