package com.theciceroneapp.cicerone.controller;

import com.theciceroneapp.cicerone.model.Location;

/**
 * Created by crsch on 10/14/2017.
 */

public interface TripChangeListener {

    public void newLocality(Location loc);
    public void newMostRecentLocation(Location loc);

}
