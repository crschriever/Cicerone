package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public class Location {
    private final double longitude;
    private final double latitude;
    private final String name;
    private final String address;
    private final String[] types;

    public Location(double longitude, double latitude, String name, String address, String[] types) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address = address;
        this.types = types;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(longitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
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

    public String[] getTypes() {
        return types;
    }
}
