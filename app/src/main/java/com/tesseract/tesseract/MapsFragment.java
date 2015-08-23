package com.tesseract.tesseract;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tesseract.tesseract.R;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends SupportMapFragment implements Observer{

    private GoogleMap mMap;

    public MapsFragment(){
        super();
        mMap=super.getMap();

    }


    public GoogleMap getCustomMap(){

        return mMap;
    }


    @Override
    public void update(Observable observable, Object data) {
        moveCamera((LatLng)data);
    }

    public void moveCamera(LatLng position){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }
}