package com.tesseract.tesseract.Main;

import core.CommunicationManager.WebSocket.PubNub;
import core.PreferenceEditor;
import core.Services.Communication_service;
import core.Services.PositionService;
import core.TesseractPositionManager.TesseractPositionManager;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.tesseract.tesseract.R;


public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks {
    private int mId;
    private static String MAPS_FRAGMENT_TAG = "maps_fragment";
    private static String SETTINGS_FRAGMENT_TAG = "settings_fragment";
    private static final int MAPS_FRAGMENT=0;
    private static final int SETTINGS_FRAGMENT=1;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GoogleMap mMap;
    private Mediator mediator;
    private MapsFragment mapFragment;
    private TesseractPositionManager positionManager;
    private SettingFragment settingFragment;
    private PositionService positionService;
    private Communication_service communication_service;
    private int itemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediator = Mediator.getInstance();
        if(!mediator.carSetted())
            showCarDialog();
        mediator.setMainActivity(this);
        //layout
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        settingFragment = new SettingFragment();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        if(savedInstanceState!=null) {
            int activeFrg= mediator.getActivefragment();
            if(activeFrg==MAPS_FRAGMENT) {
                itemSelected=SETTINGS_FRAGMENT;
                onNavigationDrawerItemSelected(MAPS_FRAGMENT);
            }
            else{
                itemSelected=MAPS_FRAGMENT;
                onNavigationDrawerItemSelected(SETTINGS_FRAGMENT);
            }
        }
        else {
            itemSelected=SETTINGS_FRAGMENT;
            onNavigationDrawerItemSelected(MAPS_FRAGMENT);
        }
        //ImageView imgView = (ImageView)findViewById(R.id.imgAvatar);
        //Bitmap img = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable);
        //imgView.setImageDrawable(new RoundImage(img));

        //Binding services
        if(!mediator.positionServiceBound()){
            Intent posService = new Intent(this, PositionService.class);
            bindService(posService, positionServiceConnection, Context.BIND_AUTO_CREATE);
        }
        if(!mediator.communicationServiceBound()){
            Intent comService = new Intent(this, Communication_service.class);
            bindService(comService, communicationServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }


    //-------begin of layout method--------//
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
                case MAPS_FRAGMENT:
                    addMapsFragment();
                    break;
                case SETTINGS_FRAGMENT:
                    addSettingsFragment();
                    break;
            }
            mediator.setActivefragment(itemSelected);
        }

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }
        if(itemSelected==1) {
            onNavigationDrawerItemSelected(MAPS_FRAGMENT);
            return;
        }

        super.onBackPressed();
    }

    public void addMapsFragment(){
        mapFragment = new MapsFragment();
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
        settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction().add(R.id.container, settingFragment).commit();
        itemSelected=SETTINGS_FRAGMENT;
    }

    public void removeSettingsFragment(){
        settingFragment = (SettingFragment) getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        if(settingFragment!=null)
            getFragmentManager().beginTransaction().remove(settingFragment).commit();
    }

    //------ end of layout methods ------//



    //-------begin of service connections ------//
    private ServiceConnection positionServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            PositionService.LocalBinder binder = (PositionService.LocalBinder) service;
            mediator.setPositionService(binder.getService());

            Log.i("Main Activity", "Position service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mediator.removePositionService();
        }
    };
    private ServiceConnection communicationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Communication_service.LocalBinder binder = (Communication_service.LocalBinder) service;
            mediator.setCommunicationService(binder.getService());

            Log.i("Main Activity", "Communication service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mediator.removePositionService();
        }
    };

    //--------- end of service connections -------//


    //------ beginning of routine method ----- //

    private void showCarDialog(){
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        inputText.setPadding(75, 20, 20, 20);
        new AlertDialog.Builder(this)
                .setTitle("Insert your car's registration number")
                .setMessage("Insert your car's registration number or exit.")
                .setView(inputText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String text = inputText.getText().toString();
                        if (text.length() > 0) {
                            mediator.setCar(text);
                        } else {
                            Toast.makeText(getApplicationContext(), "No car submitted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .show();
    }

    public void showNotification(String title, String text){
        mId = PreferenceEditor.getInstance().getNotificationCount();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_setting_light)
                        .setContentTitle("Tesseract - " + title)
                        .setContentText(text);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
