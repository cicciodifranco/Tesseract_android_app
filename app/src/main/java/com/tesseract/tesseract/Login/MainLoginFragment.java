package com.tesseract.tesseract.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.tesseract.tesseract.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import core.PreferenceEditor;
import core.UserCreator;
import core.UserCreator.CreatorListener;


public class MainLoginFragment extends android.support.v4.app.Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        CreatorListener{

    public static final int LOGIN_REQUEST_CODE=1;
    public static final int REGISTRATION_REQUEST_CODE=2;
    private static final int RC_SIGN_IN = 0;
    private CallbackManager callbackManager;
    private String TAG = "Login Activity";
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private LoginCallback loginCallback;
    private static List fbPermissions = Arrays.asList("public_profile", "email", "user_birthday");
    private static MainLoginFragment instance;
    private UserCreator userCreator = UserCreator.getInstance();
    public MainLoginFragment() {

    }

    public static MainLoginFragment getInstance(){
        if(instance==null){
            instance= new MainLoginFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_login, container, false);

        Button facebookButton = (Button)view.findViewById(R.id.facebook_login_button);
        Button googleButton = (Button) view.findViewById(R.id.google_login_button);
        Button loginButton = (Button) view.findViewById(R.id.tesseract_login_button);
        Button registerButton = (Button) view.findViewById(R.id.tesseract_register_button);
        userCreator.setCreatorListener(this);

        //setting button listener
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //facebook callback manager init


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


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
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fetchFbInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Login cancellato", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.facebook_login_cancelled), Toast.LENGTH_SHORT).show();
            }
        });



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

        switch(v.getId()){

            case R.id.facebook_login_button : {
                //facebook login activity
                LoginManager.getInstance().logInWithReadPermissions(this, fbPermissions);
                break;
            }
            case R.id.google_login_button : {
                //google login dialog
                onSignInClicked();
                break;

            }
            case R.id.tesseract_register_button : {
                loginCallback.sigUpPressed();
                break;
            }

            case R.id.tesseract_login_button : {
                loginCallback.logInPressed();
                break;
            }

            default : break;

        }

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
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        //mStatusTextView.setText(R.string.signing_in);
        Log.i(TAG, "login ok");
    }

    /*
"id":"10205769986521041"
"first_name":"Ciccio"
"middle_name":"Di"
"last_name":"Franco"
gender":"male"
"birthday":"01/03/1990"
"locale":"it_IT"
"email":"cicciodifranco@hotmail.it"
*/

    public void fetchFbInfo(final AccessToken accessToken){
        UserCreator creator = UserCreator.getInstance();
        String fbAccessToken=""+accessToken.getToken();
        creator.storeAccessToken(fbAccessToken);
        creator.setCreatorListener(this);
        Log.i(TAG, fbAccessToken);
        GraphRequest request = GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(response.getError()==null)
                            if(object!=null) {
                                try {

                                    int id= object.getInt("id");
                                    String name = object.getString("first_name"),
                                           surname = object.getString("last_name"),
                                           gender= object.getString("gender"),
                                           birthday= object.getString("birthday"),
                                           locale= object.getString("locale");
                                    UserCreator.userFactory(id, "null", name, surname, birthday, gender, "null");



                                    return;
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                    }
                            }
                        Toast.makeText(getActivity().getApplicationContext(), "Facebook response error", Toast.LENGTH_LONG).show();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, middle_name, last_name, gender, birthday, locale, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void OnActionsFinished(int action, boolean result) {
        if(result) {

            ((Splash_Screen) getActivity()).loginCompleted(true, Splash_Screen.FACEBOOK);
            PreferenceEditor.getInstance().setLogged(true);
        }
    }

    @Override
    public void update() {

    }

    public interface LoginCallback{

        void loginCompleted(boolean result, String provider);
        void loginInProgress();
        void sigUpPressed();
        void logInPressed();
        void cancelPressed();
    }

}
