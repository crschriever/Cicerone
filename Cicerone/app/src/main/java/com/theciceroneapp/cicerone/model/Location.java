package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public class Location {
    private final double longitude;
    private final double latitude;
    private final String name;
    private final String address;

    public Location(double longitude, double latitude, String name, String address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.longitude, longitude) != 0) return false;
        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (!name.equals(location.name)) return false;
        return address.equals(location.address);

    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
