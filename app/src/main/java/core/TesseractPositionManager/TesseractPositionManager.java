package core.TesseractPositionManager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by francesco on 06/06/15.
 */
public class TesseractPositionManager{

    private Context mContext;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location current_position = null;
    private int current_status;
    private String locationProvider;



    public TesseractPositionManager (Context mContext, LocationListener locationListener ) {

        this.mContext = mContext;
        this.locationListener=locationListener;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
    }

    public void stop(){
        locationManager.removeUpdates(locationListener);
    }

    public void start(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void setLatency(long millisec){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, millisec, 0, locationListener);
    }

    public Location getLastKnowLocation(){
        Location location;
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null)
            return location;

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null)
            return location;

        location= locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(location!=null)
            return location;

        return null;
    }



}