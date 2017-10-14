package com.theciceroneapp.cicerone.model;

import android.app.Service;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crsch on 10/14/2017.
 */

public class Trip {

    private List<Location> locations = new ArrayList<>();
    private List<Mode> modes = new ArrayList<>();

    public Trip(Mode mode, Mode...otherModes) {
        modes.add(mode);
        for (Mode m: otherModes) {
            modes.add(m);
        }
    }

}
