package com.theciceroneapp.cicerone.model;

import java.util.HashSet;

/**
 * Created by crsch on 10/14/2017.
 */

public class Location {
    private final double longitude;
    private final double latitude;
    private final String name;
    private final String address;
    private final HashSet<String> types = new HashSet<>();
    private String description;
    private String websiteURL;
    private String navURL;
    private double rating;

    public Location(double longitude, double latitude, String name, String address, String[] types,
                    String websiteURL, String navURL, double rating) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address = address;
        for (String s: types) {
            this.types.add(s);
        }
        this.websiteURL = websiteURL;
        this.navURL = navURL;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", types=" + types +
                ", description='" + description + '\'' +
                ", websiteURL='" + websiteURL + '\'' +
                ", navURL='" + navURL + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public int hashCode() {
        int result = 32;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.longitude, longitude) == 0) return true;
        if (Double.compare(location.latitude, latitude) == 0) return true;
        if (name.equals(location.name)) return true;
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

    public HashSet<String> getTypes() {
        return types;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String s) {
        websiteURL = s;
    }

    public String getNavURL() {
        return navURL;
    }

    public double getRating() {
        return rating;
    }
}
