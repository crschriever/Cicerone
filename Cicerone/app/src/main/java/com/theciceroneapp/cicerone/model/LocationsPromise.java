package com.theciceroneapp.cicerone.model;

/**
 * Created by crsch on 10/14/2017.
 */

public interface LocationsPromise<E> {
    void locationsFound(E locations);
}
