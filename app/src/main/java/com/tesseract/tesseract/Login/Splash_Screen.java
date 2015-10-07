package com.tesseract.tesseract.Login;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tesseract.tesseract.Main.MainActivity;
import com.tesseract.tesseract.R;

import core.CommunicationManager.WebSocket.PubNub;
import core.PreferenceEditor;


public class Splash_Screen extends ActionBarActivity implements MainLoginFragment.LoginCallback{
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String TESSERACT = "tesseract";
    private static final String TAG ="Splash screen";

    private static final String ACTIVE_FRAGMENT="active_fragment";
    private static final int MAIN_LOGIN = 0;
    private static final int LOGIN = 1;
    private static final int REGISTER = 2;




    private static MainLoginFragment mainLoginFragment;
    private static Login_Fragment loginFragment;
    private static PreferenceEditor editor;
    private int activeFragment;
    private android.app.Fragment selectedFragment;
    private ProgressBar progressBar;
    private boolean paused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        PreferenceEditor.init(getApplicationContext());
        editor= PreferenceEditor.getInstance();

        if(editor.isLogged())
            loginCompleted(true, editor.getIdentityProvider());
        else {

            progressBar.setVisibility(View.INVISIBLE);
            if(savedInstanceState==null) {
                mainLoginFragment = MainLoginFragment.getInstance();
                loginFragment = Login_Fragment.getInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.splash_screen_container,mainLoginFragment, "fragment_main_login").commit();
                activeFragment=MAIN_LOGIN;
            }
            else {
                RelativeLayout container = (RelativeLayout) findViewById(R.id.splash_screen_container);
                container.removeAllViews();
                activeFragment=savedInstanceState.getInt(ACTIVE_FRAGMENT);
                savedInstanceState.remove(ACTIVE_FRAGMENT);
                switch (activeFragment){
                    case MAIN_LOGIN :
                        mainLoginFragment = (MainLoginFragment)getSupportFragmentManager().findFragmentByTag("fragment_main_login");
                        break;
                    case LOGIN :
                        loginFragment = (Login_Fragment) getFragmentManager().findFragmentByTag("login_fragment");
                        selectedFragment=loginFragment;
                        break;
                    case REGISTER :
                        break;
                }
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(ACTIVE_FRAGMENT, activeFragment);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    public void onStop(){
        super.onStop();

    }
    @Override
    public void onResume(){

        super.onResume();

    }
    @Override
    public void onStart(){
        super.onStart();
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
        progressBar.setVisibility(View.INVISIBLE);
        if(result) {
            //getSupportFragmentManager().beginTransaction().remove(mainLoginFragment).commit();
            if(provider.equals(FACEBOOK)) {
                Log.i(TAG, "login completed");
                PreferenceEditor.getInstance().setIdentityProvider(FACEBOOK);
            }
            if(provider.equals(TESSERACT)){
                PreferenceEditor.getInstance().setIdentityProvider(TESSERACT);
            }
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            PreferenceEditor.getInstance().setLogged(true);
            finish();
        }
    }
    @Override
    public void onBackPressed(){
        if(activeFragment!=MAIN_LOGIN)
            cancelPressed();
        else super.onBackPressed();

    }

    @Override
    public void loginInProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void sigUpPressed() {
        if(mainLoginFragment==null)
            mainLoginFragment = (MainLoginFragment)getSupportFragmentManager().findFragmentByTag("fragment_main_login");

        getSupportFragmentManager().beginTransaction().remove(mainLoginFragment).commit();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter, 0, 0, 0);

        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.splash_screen_container, signUpFragment).commit();
        selectedFragment=signUpFragment;
        activeFragment=REGISTER;
    }

    @Override
    public void logInPressed() {
        if(mainLoginFragment==null)
            mainLoginFragment = (MainLoginFragment)getSupportFragmentManager().findFragmentByTag("fragment_main_login");

        getSupportFragmentManager().beginTransaction().remove(mainLoginFragment).commit();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter, 0, 0, 0);

        if(loginFragment==null) {
            loginFragment = new Login_Fragment();
        }
        getFragmentManager().beginTransaction().add(R.id.splash_screen_container, loginFragment, "login_fragment").commit();
        selectedFragment=loginFragment;
        activeFragment=LOGIN;
    }

    @Override
    public void cancelPressed() {
        if(mainLoginFragment==null)
            mainLoginFragment = new MainLoginFragment();
        getFragmentManager().beginTransaction().remove(selectedFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.splash_screen_container, mainLoginFragment, "fragment_main_login").commit();
        activeFragment=MAIN_LOGIN;
        selectedFragment=null;
    }

}
