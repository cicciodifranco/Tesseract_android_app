package com.tesseract.tesseract.Main;

import core.TesseractPositionManager.TesseractPositionManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.tesseract.tesseract.R;


public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GoogleMap mMap;
    private MapsFragment mapFragment;
    private TesseractPositionManager positionManager;
    private Setting setting;

    private int itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        setting = new Setting();
        positionManager = new TesseractPositionManager(this);
        mapFragment= new MapsFragment();
        mMap=mapFragment.getCustomMap();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.container, mapFragment).commit();
        itemSelected=0;
        ImageView imgView = (ImageView)findViewById(R.id.imgAvatar);
        Bitmap img = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.position);
        imgView.setImageDrawable(new RoundImage(img));

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
                case 0 :
                    getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
                    break;
                case 1 :
                    getFragmentManager().beginTransaction().remove(setting).commit();
                    break;
            }


            switch (position) {
                case 0:
                    if (mapFragment != null) {
                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter, 0, 0, 0);
                        transaction.add(R.id.container, mapFragment).commit();

                        itemSelected = 0;
                    }
                    break;
                case 1:
                    getFragmentManager().beginTransaction().add(R.id.container, setting).commit();
                    itemSelected=1;
                    break;
                case 2 :
                    getFragmentManager().beginTransaction().add(R.id.container, setting).commit();
                    itemSelected=1;
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







}
