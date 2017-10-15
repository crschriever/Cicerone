package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.widget.ListAdapter;

import com.theciceroneapp.cicerone.controller.TripChangeListener;
import com.theciceroneapp.cicerone.controller.TripHomeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * Created by crsch on 10/14/2017.
 */

public class Trip {

    private HashSet<Location> locations = new HashSet<>();
    private List<Location> locationsWithDecription = new ArrayList<>();
    private Mode[] modes;

    private Location currentLocality;

    private final int modeOFTravel;
    private final LocationsPromise<Location[]> lPromise;
    private final TalkPromise tPromise;
    private final InformationPromise dPromise;

    private double radius = 500;
    private final int MAX_RADIUS_WALKING = 700;
    private final int MIN_RADIUS_WALKING = 50;
    private final float RADIUS_CHANGE_WALKING = .25f;
    private final int AFTER_SPEECH_WAIT_WALKING = 1000;
    private final int FAILED_FIND_WAIT_WALKING = 500;

    private final int MAX_RADIUS_RIDING = 5000;
    private final int MIN_RADIUS_RIDING = 200;
    private final float RADIUS_CHANGE_RIDING = .25f;
    private final int AFTER_SPEECH_WAIT_RIDING = 10000;
    private final int FAILED_FIND_WAIT_RIDING = 5000;

    private final int MAX_RADIUS_FLYING = 5000;
    private final int MIN_RADIUS_FLYING = 200;
    private final float RADIUS_CHANGE_FLYING = .25f;
    private final int AFTER_SPEECH_WAIT_FLYING = 10000;
    private final int FAILED_FIND_WAIT_FLYING = 5000;

    private final int LOCALITY_REFRESH = 1000;

    public static final int MODE_FLYING = 0;
    public static final int MODE_WALKING = 1;
    public static final int MODE_RIDING = 2;
    public boolean tripGoing = true;

    private static Trip singleton;
    private static TripChangeListener tripChangeListener;

    public Trip(ArrayList<Mode> modeList, int modeTravel) {
        singleton = this;

        this.modes = new Mode[modeList.size()];
        for (int i = 0; i < modeList.size(); i++) {
            modes[i] = modeList.get(i);
        }

        this.modeOFTravel = modeTravel;

        final Trip thisTrip = this;

        lPromise =  new LocationsPromise<Location[]>() {
            @Override
            public void locationsFound(com.theciceroneapp.cicerone.model.Location[] locations) {
                if (!tripGoing) {
                    return;
                }
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
                boolean firstSelected = false;
                for(com.theciceroneapp.cicerone.model.Location l: locations) {
                    if (!thisTrip.locations.contains(l) && !l.getTypes().contains("locality")) {
                        if (!firstSelected) {
                            APIHelper.getWikiInformation(l, dPromise);
                            thisTrip.locations.add(l);
                            System.out.println(l.getName());
                            firstSelected = true;
                        }
                    }
                }

                // Changing radius based on the amount or responses

                if (modeOFTravel == MODE_WALKING) {
                    if (locations.length > 10) {
                        radius *= (1.0 - RADIUS_CHANGE_WALKING);
                    } else if (locations.length < 3) {
                        radius *= (1.0 + RADIUS_CHANGE_WALKING);
                    }

                    if (radius < MIN_RADIUS_WALKING) {
                        radius = MIN_RADIUS_WALKING;
                    } else if (radius > MAX_RADIUS_WALKING) {
                        radius = MAX_RADIUS_WALKING;
                    }
                } else if (modeOFTravel == MODE_RIDING) {
                    if (locations.length > 10) {
                        radius *= (1.0 - RADIUS_CHANGE_RIDING);
                    } else if (locations.length < 3) {
                        radius *= (1.0 + RADIUS_CHANGE_RIDING);
                    }

                    if (radius < MIN_RADIUS_RIDING) {
                        radius = MIN_RADIUS_RIDING;
                    } else if (radius > MAX_RADIUS_RIDING) {
                        radius = MAX_RADIUS_RIDING;
                    }
                } else if (modeOFTravel == MODE_FLYING) {
                    if (locations.length > 10) {
                        radius *= (1.0 - RADIUS_CHANGE_FLYING);
                    } else if (locations.length < 3) {
                        radius *= (1.0 + RADIUS_CHANGE_FLYING);
                    }

                    if (radius < MIN_RADIUS_FLYING) {
                        radius = MIN_RADIUS_FLYING;
                    } else if (radius > MAX_RADIUS_FLYING) {
                        radius = MAX_RADIUS_FLYING;
                    }
                } else {
                    System.out.println("ERROR: Invalid modeOfTravel");
                }

                System.out.println("Radius: " + radius);

                if (!firstSelected) {
                    try {
                        synchronized (this) {
                            if (modeOFTravel == MODE_WALKING) {
                                wait(FAILED_FIND_WAIT_WALKING);
                            } else if (modeOFTravel == MODE_RIDING) {
                                wait(FAILED_FIND_WAIT_RIDING);
                            } else if (modeOFTravel == MODE_FLYING) {
                                wait(FAILED_FIND_WAIT_FLYING);
                            } else {
                                System.out.println("ERROR: Invalid modeOfTravel");
                            }
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Waiting didnt work!!");
                        e.printStackTrace();
                    }
                    if (!tripGoing) {
                        return;
                    }
                    APIHelper.getLocations(radius, modes, lPromise);
                }
            }
        };

        dPromise = new InformationPromise() {
            @Override
            public void foundInformation(Location location, String text) {
                if (!tripGoing) {
                    return;
                }
                if (!text.equals("")) {
                    locationsWithDecription.add(location);
                    if (tripChangeListener != null)
                        tripChangeListener.newMostRecentLocation(location);
                    TripService.say(text, tPromise);
                } else {
                    APIHelper.getLocations(radius, modes, lPromise);
                }
                System.out.println(locations);
            }
        };

        tPromise = new TalkPromise() {
            @Override
            public void talkingDone() {
                if (!tripGoing) {
                    return;
                }
                try {
                    synchronized (this) {
                        if (modeOFTravel == MODE_WALKING) {
                            wait(AFTER_SPEECH_WAIT_WALKING);
                        } else if (modeOFTravel == MODE_RIDING) {
                            wait(AFTER_SPEECH_WAIT_RIDING);
                        } else if (modeOFTravel == MODE_FLYING) {
                            wait(AFTER_SPEECH_WAIT_FLYING);
                        } else {
                            System.out.println("ERROR: Invalid modeOfTravel");
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Waiting didnt work!!");
                    e.printStackTrace();
                }
                if (!tripGoing) {
                    return;
                }
                APIHelper.getLocations(radius, modes, lPromise);
            }
        };

        final LocationsPromise<Location[]> localityPromise = new LocationsPromise<Location[]>() {
            @Override
            public void locationsFound(Location[] locations) {
                System.out.println("Checking Locality");

                if (locations[0].getTypes().contains("locality")) {
                    if (currentLocality == null || currentLocality.equals(locations[0])) {
                        currentLocality = locations[0];
                        if (tripChangeListener != null)
                            tripChangeListener.newLocality(currentLocality);
                    }
                }
            }
        };
        Timer t = new Timer();
        /*t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      try {
                                          APIHelper.getLocations(radius, new Mode[]{Mode.LOCALITY}, localityPromise);
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }
                                  }
                              },
                0, LOCALITY_REFRESH);*/
    }

    public void startTrip() {
        APIHelper.getLocations(radius, modes, lPromise);
    }

    public void stopTrip() {
        tripGoing = false;
        TripService.stop();
    }

    public static List<Location> getLocations() {
        return singleton.locationsWithDecription;
    }

    public static Location getMostRecentLocation() {
        if (singleton.locationsWithDecription.size() == 0) {
            return null;
        }
        return singleton.locationsWithDecription.get(singleton.locationsWithDecription.size() - 1);
    }

    public static Location getLocale() {
        return singleton.currentLocality;
    }

    public static Location locationAt(int index) {
        if (index >= singleton.locationsWithDecription.size()) {
            return null;
        }
        return singleton.locationsWithDecription.get(index);
    }

}
