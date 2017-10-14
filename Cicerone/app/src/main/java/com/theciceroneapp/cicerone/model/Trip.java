package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by crsch on 10/14/2017.
 */

public class Trip {

    private HashSet<Location> locations = new HashSet<>();
    private List<Mode> modes = new ArrayList<>();
    final LocationsPromise lPromise;
    final TalkPromise tPromise;

    private int radius = 500;

    public Trip(Mode mode, Mode...otherModes) {
        modes.add(mode);
        for (Mode m: otherModes) {
            modes.add(m);
        }

        final Trip thisTrip = this;

        lPromise =  new LocationsPromise() {
            @Override
            public void locationsFound(com.theciceroneapp.cicerone.model.Location[] locations) {
                /*Arrays.sort(locations, new Comparator<Location>() {
                    @Override
                    public int compare(Location o1, Location o2) {

                        return 0;
                    };
                });*/
                for(com.theciceroneapp.cicerone.model.Location l: locations) {
                    if (!thisTrip.locations.contains(l)) {
                        thisTrip.locations.add(l);
                        TripService.say(l.getName(), tPromise);
                        break;
                    }
                }
            }
        };

        tPromise = new TalkPromise() {
            @Override
            public void talkingDone() {
                APIHelper.getLocations(500, Mode.CULTURE, lPromise);
            }
        };
    }

    public void startTrip() {
        APIHelper.getLocations(500, Mode.CULTURE, lPromise);
    }

}
