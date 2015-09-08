package com.tesseract.tesseract.Login;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tesseract.tesseract.R;

public class Login_Fragment extends Fragment implements View.OnClickListener{

    private MainLoginFragment.LoginCallback loginCallback;
    private Button cancel;
    private Button login;
    private static Login_Fragment instance;
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
                break;
        }
    }


}
