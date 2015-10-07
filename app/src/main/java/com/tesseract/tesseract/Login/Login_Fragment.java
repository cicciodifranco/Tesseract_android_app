package com.tesseract.tesseract.Login;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tesseract.tesseract.R;

import core.UserAsyncWorker;
import core.PreferenceEditor;
import core.UserCreator;

public class Login_Fragment extends Fragment implements View.OnClickListener, UserCreator.CreatorListener{

    private MainLoginFragment.LoginCallback loginCallback;
    private Button cancel;
    private Button login;
    private EditText userName;
    private EditText password;
    private static  Login_Fragment instance;
    private boolean login_running=false;

    public Login_Fragment() {

    }

    public static Login_Fragment getInstance(){
        if(instance==null) {
            instance = new Login_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        login = (Button) view.findViewById(R.id.confirm_login_button);
        cancel = (Button) view.findViewById(R.id.cancel_login_button);
        userName = (EditText) view.findViewById(R.id.email_field);
        password = (EditText) view.findViewById(R.id.password_field);
        cancel.setOnClickListener(this);
        login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            loginCallback = (MainLoginFragment.LoginCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cancel_login_button :
                loginCallback.cancelPressed();
                break;
            case R.id.confirm_login_button :
                login();
                break;
        }
    }

    private void login(){


        if(userName != null && password != null && !login_running){

            String email = userName.getText().toString();
            String pass = password.getText().toString();
            if(!email.equals("") || !pass.equals("")){
                UserCreator.getInstance().setCreatorListener(this);
                UserCreator.getInstance().login(email, pass);
                login_running=true;
                ((Splash_Screen)getActivity()).loginInProgress();
            }
        }
        else Toast.makeText(getActivity().getApplicationContext(), "Login in progress ...", Toast.LENGTH_LONG).show();

    }


    @Override
    public void OnActionsFinished(int action, boolean result) {

        if(action == UserAsyncWorker.GET_ALL_INFO && result){
            ((Splash_Screen)getActivity()).loginCompleted(true, Splash_Screen.TESSERACT);

        }
        else{
            Toast.makeText(getContext(), "Login error", Toast.LENGTH_SHORT).show();
            login_running=false;
            ((Splash_Screen)getActivity()).loginCompleted(false, Splash_Screen.TESSERACT);
        }
    }

    @Override
    public void update() {

    }



}
