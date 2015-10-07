package com.tesseract.tesseract.Main;

import android.content.Context;
import android.location.Location;
import android.preference.Preference;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.tesseract.tesseract.R;

import java.util.Iterator;
import java.util.Map;

import core.CommunicationManager.WebSocket.PubNub;
import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.Position;
import core.Entity.ConcreteEntity.Toolboth;
import core.PreferenceEditor;
import core.Services.Communication_service;
import core.Services.PositionService;
import core.TesseractAsyncWorker;
import core.TesseractCreator;
import core.UserAsyncWorker;
import core.UserCreator;

/**
 * Created by kakashi on 10/4/15.
 */
public class Mediator implements TesseractCreator.TesseractCreatorListener, UserCreator.CreatorListener{
    private static final String TAG = "Mediator";
    private static Mediator istance;
    private MainActivity mainActivity;
    private PositionService positionService;
    private Communication_service communication_service;
    private MapsFragment mapsFragment;
    private SettingFragment settingFragment;
    private ProfileFragment profileFragment;
    private Map <String, Toolboth> toolbothMap;
    private boolean inRoute= false;
    private int activefragment=0;
    private UserCreator userCreator;
    private TesseractCreator tesseractCreator;
    private Marker positionMarker;
    private boolean waitingForResponse = false;

    private PubNub pubNub;

    private Mediator(){
        this.userCreator=UserCreator.getInstance();
        this.tesseractCreator = TesseractCreator.getInstance();
        this.tesseractCreator.setTesseractCreatorListener(this);
        this.userCreator.setCreatorListener(this);

    }

    public static Mediator getInstance(){
        if(istance == null)
            istance = new Mediator();
        return istance;
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity=mainActivity;
        if(PreferenceEditor.getInstance().isLogged())
            pubNub = new PubNub(""+UserCreator.userFactory().getId());

    }

    public void setPositionService(PositionService positionService){
        this.positionService=positionService;
    }
    public boolean positionServiceBound(){
        return this.positionService!=null;
    }
    public void removePositionService(){
        this.positionService=null;
    }

    public void setProfileFragment(ProfileFragment profileFragment){
        this.profileFragment=profileFragment;
    }


    public void setMapsFragment(MapsFragment mapsFragment){
        this.mapsFragment=mapsFragment;
    }

    public Context getApplicationContext(){
        if(mainActivity!=null)
            return mainActivity.getApplicationContext();
        return null;
    }

    public void setActivefragment(int activefragment){
        this.activefragment=activefragment;
    }

    public int getActivefragment(){
        return this.activefragment;
    }

    public void setCommunicationService(Communication_service communicationService){
        this.communication_service=communicationService;
    }

    public boolean communicationServiceBound(){
        return this.communication_service!=null;
    }

    public void onMapReady() {

        //setting current position marker
        if(positionService!=null) {
            Location currentLocation = positionService.getCurrentLocation();
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                this.positionMarker = mapsFragment.setMarker(latLng, R.drawable.position, "");
                mapsFragment.moveCamera(latLng);
            }
        }

        //setting tollboths marker
        if(toolbothMap==null)
            TesseractCreator.fetchToolboth();
        else{
            if (toolbothMap != null && mapsFragment!= null) {

                for(String key : toolbothMap.keySet()){
                    Toolboth t = toolbothMap.get(key);
                    Log.i(TAG, t.getName());
                    mapsFragment.setMarker(new LatLng(t.getLatitude(), t.getLongitude()), R.drawable.tollboth64x64, t.getCity()+" - "+t.getName());
                }
            }
        }


    }


    @Override
    public void OnActionFinished() {

        //called only once

        toolbothMap = tesseractCreator.getToolbothMap();
        if (toolbothMap != null && mapsFragment!= null) {

            for(String key : toolbothMap.keySet()){
                Toolboth t = toolbothMap.get(key);
                //Log.i(TAG, t.getName());
                mapsFragment.setMarker(new LatLng(t.getLatitude(), t.getLongitude()), R.drawable.tollboth64x64, t.getCity()+" - "+t.getName());
            }
            if(positionService!=null)
                positionService.setTollbothLoaded(true);
        }

    }

    @Override
    public void OnActionsFinished(int action, boolean result) {
        if(action== UserAsyncWorker.START_ROUTE && result) {
            inRoute = true;

        }

        if(action == UserAsyncWorker.END_ROUTE) {
            inRoute = false;

        }


        waitingForResponse = false;

    }

    @Override
    public void update() {

    }

    public void nearATollboth(Toolboth toolboth){
        Log.i(TAG, "near a tollboth");
        if(!waitingForResponse) {
            if (!inRoute) {
                userCreator.startRoute(toolboth, (Car) (UserCreator.userFactory()).getSelectedCar());

            }
            else {
                userCreator.endRoute(toolboth);
                Log.i(TAG,"end route called");
            }
            waitingForResponse = true;
        }
    }

    public void positionChanged(Location location){
        if(positionMarker!=null && mapsFragment!=null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            positionMarker.setPosition(latLng);
            mapsFragment.moveCamera(latLng);
        }
    }

    public boolean carSetted(){
        if(PreferenceEditor.getInstance()!=null)
            return PreferenceEditor.getInstance().getSelectedCar()!=null;
        else
            return false;
    }

    public void setCar(String registration_number){
        UserCreator.carFactory(registration_number);
    }

    public void messageArrived(String title, String message){
        if(mainActivity!=null)
            mainActivity.showNotification(title, message);
    }

}
