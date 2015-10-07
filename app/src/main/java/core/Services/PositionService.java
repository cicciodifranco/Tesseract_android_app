package core.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.tesseract.tesseract.Main.Mediator;

import java.util.Map;

import core.Entity.ConcreteEntity.Toolboth;
import core.TesseractCreator;
import core.TesseractPositionManager.TesseractPositionManager;

public class PositionService extends Service implements LocationListener {
    private static final String TAG = "Position service";

    private TesseractPositionManager tesseractPositionManager;
    private Mediator mediator;
    private Location location;
    private Map<String, Toolboth> toolbothMap;
    private Toolboth nearest;
    private boolean tollbothLoaded = false;

    private final IBinder mBinder = new LocalBinder();


    public PositionService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        this.mediator=Mediator.getInstance();
        mediator.setPositionService(this);
        tesseractPositionManager = new TesseractPositionManager(getApplicationContext(), this);
        location = tesseractPositionManager.getLastKnowLocation();
        tesseractPositionManager.setLatency(1000);
        return mBinder;
    }



    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        float dist [] = new float[1];
        mediator.positionChanged(location);
        if(nearest!=null) {
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), nearest.getLatitude(), nearest.getLongitude(), dist);
            //Log.i(TAG, "Distance from nearest("+ nearest.getName() + ") " +dist[0]+" meters");
            if(dist[0]>1000f)
                searchNearestToolboths();

            if(dist[0]<50f && location.getSpeed()<50f)
                mediator.nearATollboth(nearest);
        }
        else {
            if(tollbothLoaded) {
                searchNearestToolboths();
                Log.i(TAG, "tollboth map = null");
            }
            Log.i(TAG, "nearest null");
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocalBinder extends Binder {

        public PositionService getService() {

            return PositionService.this;
        }
    }

    public Location getCurrentLocation(){
        return this.location;
    }

    public void setTollbothLoaded(boolean loaded){
        this.tollbothLoaded = loaded;
    }

    private void searchNearestToolboths(){

        toolbothMap = TesseractCreator.getInstance().getToolbothMap();

           if(toolbothMap!= null){
                Toolboth tmp;
                float minDist=1000*1000;
                for(String key : toolbothMap.keySet()){
                    float dist [] = new float[1];

                    tmp= toolbothMap.get(key);
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), tmp.getLatitude(), tmp.getLongitude(), dist);

                    if(dist[0]<minDist){
                        nearest = toolbothMap.get(key);
                        minDist=dist[0];
                    }
                }

                if(nearest!=null)
                    Log.i(TAG, "Distance form nearest (" + nearest.getName() + ") " + minDist + " m");

            }

    }
}
