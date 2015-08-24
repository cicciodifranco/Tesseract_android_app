package com.tesseract.tesseract;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class LoginFragment extends android.support.v4.app.Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String TESSERACT = "tesseract";

    private Fragment fragment;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private String TAG = "Login Activity";
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private LoginCallback loginCallback;
    private final static int LOGIN_REQUEST_CODE=1;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        LoginButton button = (LoginButton)view.findViewById(R.id.facebook_login_button);
        //button.setOnClickListener(this);
        




        callbackManager = CallbackManager.Factory.create();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                fetchFbInfo(loginResult.getAccessToken());
                Toast.makeText(getActivity().getApplicationContext(), "Login effetuato", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Login cancellato", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginCallback = (LoginCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        FacebookSdk.sdkInitialize(activity.getApplicationContext());




    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginCallback = null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
       /* if (v.getId() == R.id.plus_sign_in_button) {
            onSignInClicked();
            loginCallback.loginCompleted(true, Splash_Screen.GOOGLE);

        }
        if(v.getId()==R.id.plus_sign_in_button){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_location"));
            //Intent loginActivity= new Intent(getActivity(), Login_Activity.class);
            //startActivityForResult(loginActivity,LOGIN_REQUEST_CODE);
            //loginCallback.loginCompleted(true);

        }*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                Log.i(TAG, connectionResult.toString());
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "On Activity Result");
        if(requestCode==LOGIN_REQUEST_CODE){
            if(resultCode==0)
                loginCallback.loginCompleted(true, Splash_Screen.TESSERACT);

        }


        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        //mStatusTextView.setText(R.string.signing_in);
        Log.i(TAG, "login ok");
    }

    public void fetchFbInfo(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Iterator iterator = object.keys();
                        while(iterator.hasNext()){
                            try {
                                String key = (String) iterator.next();
                                String value = object.get(key).toString();
                                Log.i(TAG,"\""+key+"\":"+"\""+value+"\"");
                            }
                            catch (Exception e){

                            }
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public interface LoginCallback{

        public void loginCompleted(boolean result, String provider);
    }

}
