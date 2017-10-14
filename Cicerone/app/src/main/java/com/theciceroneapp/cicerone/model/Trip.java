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

    private int radius = 5000;

    public Trip(Mode mode, Mode...otherModes) {
        modes.add(mode);
        for (Mode m: otherModes) {
            modes.add(m);
        }

        final Trip thisTrip = this;

        lPromise =  new LocationsPromise() {
            @Override
            public void locationsFound(com.theciceroneapp.cicerone.model.Location[] locations) {
                Arrays.sort(locations, new Comparator<Location>() {
                    @Override
                    public int compare(Location o1, Location o2) {
                        for (Mode m: modes) {
                            int comp = m.compareLocation(o1, o2);
                            if (comp != 0) {
                                return comp;
                            }
                        }

                        return 0;
                    };
                });
                for(com.theciceroneapp.cicerone.model.Location l: locations) {
                    if (!thisTrip.locations.contains(l)) {
                        thisTrip.locations.add(l);
                        TripService.say(l.getName(), tPromise);
                        System.out.println(l.getName());
                        break;
                    }
                }
            }
        };

        tPromise = new TalkPromise() {
            @Override
            public void talkingDone() {
                APIHelper.getLocations(radius, modes.get(0), lPromise);
            }
        };
    }

    public void startTrip() {
        APIHelper.getLocations(radius, Mode.CULTURE, lPromise);
    }

}
