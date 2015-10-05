package core.Services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.tesseract.tesseract.Main.Mediator;

import java.util.Iterator;
import java.util.Map;

import core.Entity.ConcreteEntity.Toolboth;
import core.TesseractPositionManager.TesseractPositionManager;
import core.UserCreator;

public class PositionService extends Service implements LocationListener {


    private TesseractPositionManager tesseractPositionManager;
    private Mediator mediator;
    private Location location;
    private Map<String, Toolboth> toolbothMap;
    private Toolboth nearest;

    private final IBinder mBinder = new LocalBinder();


    public PositionService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        this.mediator=Mediator.getInstance();
        tesseractPositionManager = new TesseractPositionManager(getApplicationContext(), this);
        location = tesseractPositionManager.getLastKnowLocation();
        tesseractPositionManager.setLatency(1000);
        return mBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        float dist [] = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), nearest.getLatitude(), nearest.getLongitude(), dist);
        if(dist[0]>1000f)
            searchNearestToolboths();

        if(dist[0]<50f && location.getSpeed()<50f)
            mediator.nearATollboth(nearest);

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


    private void searchNearestToolboths(){
        if(toolbothMap!= null){
            Toolboth tmp;
            float minDist=1000*1000;

            Iterator iterator = toolbothMap.entrySet().iterator();
            while (iterator.hasNext()){
                tmp = (Toolboth)iterator.next();
                float dist [] = new float[1];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), tmp.getLatitude(), tmp.getLongitude(), dist);
                if(dist[0]<minDist){
                    nearest = tmp;
                }
            }

        }
    }
}
