package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public class BearingCalculator {
    public static String calculateBearing(double lat1, double long1, double lat2, double long2) {

//        endpoint.lat = x1;
//        endpoint.lng = y1;
//        startpoint.lat = x2;
//        startpoint.lng = y2;

        double radians = Math.atan2((long2 - long1), (lat2 - lat1));

//        function getAtan2(y, x) {
//            return Math.atan2(y, x);
//        };

        double compassReading = radians * (180 / Math.PI);

        String[] coordNames = {"North", "North East", "East", "Sout East", "South", "South West", "West", "North West", "North"};
        double coordIndex = Math.round(compassReading / 45);
        if (coordIndex < 0) {
            coordIndex = coordIndex + 8;
        };

        return coordNames[(int)coordIndex]; // returns the coordinate value
    }
}
