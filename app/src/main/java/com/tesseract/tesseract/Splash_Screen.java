package com.tesseract.tesseract;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import core.ComunicationManager.WebSocket.PubNub;
import core.PreferenceEditor;


public class Splash_Screen extends ActionBarActivity implements com.tesseract.tesseract.LoginFragment.LoginCallback{
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String TESSERACT = "tesseract";

    private PreferenceEditor preferenceEditor = PreferenceEditor.getInstance();
    private PubNub pubnub = new PubNub("francescodifranco90@gmail.com");

    private static com.tesseract.tesseract.LoginFragment loginFragment;
    private static PreferenceEditor editor;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        //bg.setImageResource(R.drawable.autostrada_ponte);
        PreferenceEditor.init(getApplicationContext());
        editor= PreferenceEditor.getInstance();

        if(editor.isLogged())
            loginCompleted(true, editor.getIdentityProvider());
        else {
            loginFragment = new LoginFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
            transaction.add(R.id.container, loginFragment).commit();
            //getFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loginCompleted(boolean result, String provider) {
        if(result) {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
            if(provider.equals(FACEBOOK)) {

            }
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }
}
