package com.tesseract.tesseract.Main;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String TAG = "Map fragment";
    private GoogleMap mMap;
    private Mediator mediator = Mediator.getInstance();

    public MapsFragment(){
        super();
        this.mMap=super.getMap();
        if(mMap==null)
            getMapAsync(this);
        mediator.setMapsFragment(this);
    }


    public GoogleMap getCustomMap(){

        return mMap;
    }


    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap=googleMap;
        mediator.onMapReady();
    }

    public void moveCamera(LatLng position){
        if(mMap!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f));
    }


    public Marker setMarker(LatLng location, int resource, String title){
        Marker marker=null;

        if(mMap!=null){
            marker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .draggable(false)
                    .title(title)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(resource)));

        }
        return marker;
    }
}
