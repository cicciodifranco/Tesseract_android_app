package com.tesseract.tesseract;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tesseract.tesseract.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends SupportMapFragment {

    private GoogleMap mMap;

    public MapsFragment(){
        super();
        mMap=super.getMap();

    }


    public GoogleMap getCustomMap(){

        return mMap;
    }


}
