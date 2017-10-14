package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by crsch on 10/14/2017.
 */

public class Trip {

    private HashSet<Location> locations = new HashSet<>();
    private List<Location> locationsWithDecription = new ArrayList<>();
    private List<Mode> modes = new ArrayList<>();
    private final LocationsPromise<Location[]> lPromise;
    private final TalkPromise tPromise;
    private final InformationPromise dPromise;

    private double radius = 500;
    private final int MAX_RADIUS = 5000;
    private final int MIN_RADIUS = 200;
    private final float RADIUS_CHANGE = .25f;

    public Trip(ArrayList<Mode> modeList) {
        this.modes = modeList;

        final Trip thisTrip = this;

        lPromise =  new LocationsPromise<Location[]>() {
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
                int count = 0;
                for(com.theciceroneapp.cicerone.model.Location l: locations) {
                    if (!thisTrip.locations.contains(l)) {
                        if (count == 0) {
                            APIHelper.getWikiInformation(l, dPromise);
                            thisTrip.locations.add(l);
                            System.out.println(l.getName());
                        }
                        count++;
                    }
                }

                // Changing radius based on the amount or responses
                if (count > 10) {
                    radius *= (1.0 - RADIUS_CHANGE);
                } else if (count < 3) {
                    radius *= (1.0 + RADIUS_CHANGE);
                    System.out.println(1.0 + RADIUS_CHANGE);
                }

                System.out.println(radius + ", " + count);

                if (radius < MIN_RADIUS) {
                    radius = MIN_RADIUS;
                } else if (radius > MAX_RADIUS) {
                    radius = MAX_RADIUS;
                }
                System.out.println(radius + ", " + count);

                if (count == 0) {
                    try {
                        synchronized (this) {
                            wait(2000);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        System.out.println("Waiting didnt work!!");
                        e.printStackTrace();
                    }
                    System.out.println("Nothing found!");
                    APIHelper.getLocations(radius, Mode.CULTURE, lPromise);
                }
            }
        };

        dPromise = new InformationPromise() {
            @Override
            public void foundInformation(Location location, String text) {
                if (!text.equals("")) {
                    locationsWithDecription.add(location);
                    TripService.say(location.getName(), tPromise);
                } else {
                    APIHelper.getLocations(radius, modes.get(0), lPromise);
                }
                System.out.println(locations);
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
