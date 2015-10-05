package com.tesseract.tesseract.Main;

import core.TesseractPositionManager.TesseractPositionManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.tesseract.tesseract.R;


public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks {

    private static String MAPS_FRAGMENT_TAG = "maps_fragment";
    private static String SETTINGS_FRAGMENT_TAG = "settings_fragment";
    private static final int MAPS_FRAGMENT=0;
    private static final int SETTINGS_FRAGMENT=1;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GoogleMap mMap;
    private MapsFragment mapFragment;
    private TesseractPositionManager positionManager;
    private SettingFragment settingFragment;

    private int itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        settingFragment = new SettingFragment();

        mapFragment= new MapsFragment();
        mMap=mapFragment.getCustomMap();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.container, mapFragment, MAPS_FRAGMENT_TAG).commit();
        itemSelected=0;
        ImageView imgView = (ImageView)findViewById(R.id.imgAvatar);
        //Bitmap img = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable);
        //imgView.setImageDrawable(new RoundImage(img));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position != itemSelected) {

            switch (itemSelected) {
                case MAPS_FRAGMENT :
                    removeMapsFragment();
                    break;
                case SETTINGS_FRAGMENT :
                    removeSettingsFragment();
                    break;
            }

            switch (position) {
                case 0:
                    addMapsFragment();
                    break;
                case 1:
                    addSettingsFragment();
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    public void addMapsFragment(){

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, 0, 0, 0);
        transaction.add(R.id.container, mapFragment, MAPS_FRAGMENT_TAG).commit();
        itemSelected = MAPS_FRAGMENT;
    }

    public void removeMapsFragment() {
        mapFragment =(MapsFragment) getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
        if(mapFragment!=null)
            getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    public void addSettingsFragment(){
        getFragmentManager().beginTransaction().add(R.id.container, settingFragment).commit();
        itemSelected=SETTINGS_FRAGMENT;
    }

    public void removeSettingsFragment(){
        settingFragment = (SettingFragment) getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        if(settingFragment!=null)
            getFragmentManager().beginTransaction().remove(settingFragment).commit();
    }






}
