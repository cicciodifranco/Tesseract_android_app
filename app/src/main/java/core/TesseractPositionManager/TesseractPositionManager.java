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
public class TesseractPositionManager extends Observable{
    private Context mContext;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location current_position = null;
    private int current_status;
    private String locationProvider;


    public TesseractPositionManager (Context mContext) {

        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                current_position=location;
                setChanged();
                notifyObservers(location);
                Log.i("position manager:", "position changed");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                    current_status=status;

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        current_position = locationManager.getLastKnownLocation(locationProvider);

        if(current_position!=null){
            current_position = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(current_position!=null)
            Log.i("Location Manager: ", "Current position " + current_position.toString());
        else
            Log.i("Location Manager: ", "Current position null");
    }

    public Location getLastLocation(){
        return this.current_position;

    }
    public LatLng getLatLong(){
        return new LatLng(current_position.getLatitude(),current_position.getLongitude());
    }
    public boolean isAvaible(){
        return (current_position != null);
    }

    public int isActive(){
        return current_status;
    }


    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    @Override
    public void deleteObserver(Observer observer){super.deleteObserver(observer);}

    @Override
    public void notifyObservers(Object data) {
        super.notifyObservers(data);
    }
}
