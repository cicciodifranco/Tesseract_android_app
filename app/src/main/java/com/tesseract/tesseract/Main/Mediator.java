package com.tesseract.tesseract.Main;

import android.content.Context;
import android.location.Location;
import android.preference.Preference;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.tesseract.tesseract.R;

import java.util.Iterator;
import java.util.Map;

import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.Position;
import core.Entity.ConcreteEntity.Toolboth;
import core.PreferenceEditor;
import core.Services.PositionService;
import core.TesseractAsyncWorker;
import core.TesseractCreator;
import core.UserAsyncWorker;
import core.UserCreator;

/**
 * Created by kakashi on 10/4/15.
 */
public class Mediator implements TesseractCreator.TesseractCreatorListener, UserCreator.CreatorListener{

    private static Mediator istance;
    private MainActivity mainActivity;
    private PositionService positionService;
    private MapsFragment mapsFragment;
    private SettingFragment settingFragment;
    private ProfileFragment profileFragment;
    private Map <String, Toolboth> toolbothMap;
    private boolean inRoute= PreferenceEditor.getInstance().getRouteState();

    private UserCreator userCreator;
    private TesseractCreator tesseractCreator;

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
    }

    public void setPositionService(PositionService positionService){
        this.positionService=positionService;
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

    public void onMapReady() {

        if (toolbothMap != null && mapsFragment!= null) {
            Iterator iterator = toolbothMap.entrySet().iterator();
            while (iterator.hasNext()) {

                Toolboth t = (Toolboth) iterator.next();
                mapsFragment.setMarker(new LatLng(t.getLatitude(), t.getLongitude()), R.drawable.tollboth64x64);

            }
        }
        if(positionService!=null){
            Location currentLocation = positionService.getCurrentLocation();
            if(currentLocation!=null)
                mapsFragment.setMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), R.drawable.position);
        }

    }


    @Override
    public void OnActionFinished() {
        toolbothMap = tesseractCreator.getToolbothMap();
    }

    @Override
    public void OnActionsFinished(int action, boolean result) {
        if(action== UserAsyncWorker.START_ROUTE && result)
            return;
    }

    @Override
    public void update() {

    }

    public void nearATollboth(Toolboth toolboth){
        if(!inRoute)
            userCreator.startRoute(toolboth, (Car)(UserCreator.userFactory()).getSelectedCar());
        else
            userCreator.endRoute(toolboth);
    }

}
