package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public enum Mode {
    CULTURE("park|stadium|aquarium|campground|zoo|university"), FOOD("aquarium|campground|zoo|university|stadium|park"),
    ENTERTAINMENT("aquarium|campground|zoo|university|stadium|park"), BUSINESS("aquarium|campground|zoo|university|stadium|park");

    private String placesAPIString;
    private String[] types;

    Mode(String placesAPIString) {
        this.placesAPIString = placesAPIString;
        types = placesAPIString.split("\\|");
    }

    public String getAPISTRING() {
        return placesAPIString;
    }

    public int compareLocation(Location l1, Location l2) {
        System.out.println(types.length);
        for (String s: types) {
            if (l1.getTypes().contains(s) && !l2.getTypes().contains(s)) {
                return -1;
            } else if (l2.getTypes().contains(s) && !l1.getTypes().contains(s)) {
                return 1;
            }
        }

        return 0;
    }
}
