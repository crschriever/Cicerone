package com.theciceroneapp.cicerone.model;

import java.util.HashSet;

/**
 * Created by crsch on 10/14/2017.
 */

public enum Mode {
    CULTURE("park|art_gallery|stadium|aquarium|campground|zoo|university|local_government_office|library|casino|cemetery"),
    FOOD("restaurant|meal_takeaway|meal_delivery|cafe|bakery|bar|liquor_store"),
    ENTERTAINMENT("amusement_park|night_club|casino|aquarium|movie_theater|bowling_alley|museum|zoo|shopping_mall|stadium|park"),
    BUSINESS("");

    private String placesAPIString;
    private String[] types;

    Mode(String placesAPIString) {
        this.placesAPIString = placesAPIString;
        types = placesAPIString.split("\\|");
    }

    private String getAPISTRING() {
        return placesAPIString;
    }

    public int compareLocation(Location l1, Location l2) {
        for (String s: types) {
            if (l1.getTypes().contains(s) && !l2.getTypes().contains(s)) {
                return -1;
            } else if (l2.getTypes().contains(s) && !l1.getTypes().contains(s)) {
                return 1;
            }
        }

        return 0;
    }

    public boolean hasType(String type) {
        return placesAPIString.contains(type);
    }

    public static String[] getCombinedTypes(Mode[] modes) {
        HashSet<String> types = new HashSet<>();
        for (Mode m: modes) {
            for (String s: m.types) {
                types.add(s);
            }
        }

        String[] typesArray = new String[types.size()];

        types.toArray(typesArray);

        return typesArray;
    }

    public static String getCombinedAPIString(Mode[] modes) {
        String ret = "";
        String[] all = getCombinedTypes(modes);
        for (int i = 0; i < all.length - 1; i++) {
            ret += all[i] + "|";
        }
        return ret + all[all.length - 1];
    }
}
