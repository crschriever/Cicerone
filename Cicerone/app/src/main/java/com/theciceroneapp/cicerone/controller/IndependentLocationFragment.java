package com.theciceroneapp.cicerone.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.model.Location;
import com.theciceroneapp.cicerone.model.Trip;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndependentLocationFragment extends Fragment {
    private static final String ARG_LOCATION = "ARG_LOCATION";

    private int mLocationIndex;
    private Location location;

    public IndependentLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param locationIndex index needed from trip
     * @return A new instance of fragment LocationFragment.
     */
    public static LocationFragment newInstance(int locationIndex) {
        Bundle args = new Bundle();

        args.putInt(ARG_LOCATION, locationIndex);

        LocationFragment fragment = new LocationFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("But Why");

        if (getArguments() != null) {
            mLocationIndex = getArguments().getInt(ARG_LOCATION);


            if (mLocationIndex == -2) {
                location = Trip.getLocale();
            } else if (mLocationIndex == -1) {
                location = Trip.getMostRecentLocation();
            } else {
                // Other indexes
                location = Trip.locationAt(mLocationIndex);
            }

        }
        System.out.println(location);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //savedInstanceState.get
        System.out.println("What is happening here");

        TextView locationName = (TextView) view.findViewById(R.id.tvLocationName);
        ImageView locationImage = (ImageView) view.findViewById(R.id.imvLocationPhoto);
        TextView locationDescription = (TextView) view.findViewById(R.id.tvDescription);
        TextView locationDescriptionTitle = (TextView) view.findViewById(R.id.tvDescriptionTitle);
        TextView locationAddress = (TextView) view.findViewById(R.id.tvAddress);
        TextView locationRating = (TextView) view.findViewById(R.id.tvRating);
        TextView locationRatingTitle = (TextView) view.findViewById(R.id.tvRatingTitle);
        TextView locationWebsite = (TextView) view.findViewById(R.id.tvWebsite);

        Log.d("Location here:" + location.toString(), "LOCATION");

        // Avoid using an empty location
        if (location != null) {
            locationName.setText(location.getName());
            locationDescription.setText(location.getDescription());
            locationAddress.setText(location.getAddress());
            locationRating.setText(location.getRating());
            locationWebsite.setText(location.getWebsiteURL());
            if (location.getName() == null || location.getName().equals("")) {
                locationName.setVisibility(View.GONE);
            }
            if (location.getDescription() == null || location.getDescription().equals("")) {
                locationDescription.setVisibility(View.GONE);
                locationDescriptionTitle.setVisibility(View.GONE);
            }
            if (location.getAddress() == null || location.getAddress().equals("")) {
                locationAddress.setVisibility(View.GONE);
            }
            if (location.getRating() == null || location.getRating().equals("-1.0")|| location.getRating().equals("")) {
                locationRating.setVisibility(View.GONE);
                locationRatingTitle.setVisibility(View.GONE);
            }
            if (location.getWebsiteURL() == null || location.getWebsiteURL().equals("")) {
                locationWebsite.setVisibility(View.GONE);
            }
        } else {
            // I want these invisible
            locationName.setVisibility(View.GONE);
            locationDescription.setVisibility(View.GONE);
            locationDescriptionTitle.setVisibility(View.GONE);
            locationAddress.setVisibility(View.GONE);
            locationRating.setVisibility(View.GONE);
            locationRatingTitle.setVisibility(View.GONE);
            locationWebsite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
