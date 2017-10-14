package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public enum Mode {
    CULTURE("park|stadium|aquarium|campground|zoo|university"), FOOD("aquarium|campground|zoo|university|stadium|park"),
    ENTERTAINMENT("aquarium|campground|zoo|university|stadium|park"), BUSINESS("aquarium|campground|zoo|university|stadium|park");

    private String placesAPIString;

    Mode(String placesAPIString) {
        this.placesAPIString = placesAPIString;
    }

    public String getAPISTRING() {
        return placesAPIString;
    }
}
