package com.tesseract.tesseract.Login;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tesseract.tesseract.R;

import core.Entity.ConcreteEntity.User;
import core.UserCreator;


public class SignUpFragment extends Fragment implements View.OnClickListener, UserCreator.CreatorListener{
    private EditText name_editText, surname_editText, registartion_number_edittext, email_editText, confirm_email_editText, password_editText;
    private Button cancel, register;

    private String name, surname, email, email2, password;
    private boolean working=false;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name_editText = (EditText) view.findViewById(R.id.name_editText);
        surname_editText = (EditText) view.findViewById(R.id.surname_editText);
        email_editText = (EditText) view.findViewById(R.id.email_editText);
        confirm_email_editText = (EditText) view.findViewById(R.id.confrim_email_edittext);
        password_editText = (EditText) view.findViewById(R.id.password_editText);

        register = (Button) view.findViewById(R.id.confirm_registration);
        register.setOnClickListener(this);

        cancel = (Button) view.findViewById(R.id.cancel_registration);
        cancel.setOnClickListener(this);

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {

        if(!working)
            switch (v.getId()){

                case R.id.confirm_registration :
                    working=true;
                    confirmPressed();
                    break;
                case R.id.cancel_registration :
                    cancelPressed();
                    break;
            }
        else
            Toast.makeText(getActivity().getApplicationContext(), "Registration in progress ...", Toast.LENGTH_SHORT).show();
    }

    private void confirmPressed(){

        name = name_editText.getText().toString();
        surname = surname_editText.getText().toString();
        email = email_editText.getText().toString();
        email2 = confirm_email_editText.getText().toString();
        password = password_editText.getText().toString();

        if(email2.equals(email)){
            UserCreator.getInstance().setCreatorListener(this);
            UserCreator.register(email, name, surname, "", "", "", password);


        }
        else
            Toast.makeText(getActivity().getApplicationContext(), "Check email address!", Toast.LENGTH_SHORT).show();
    }

    private void cancelPressed(){

    }

    @Override
    public void OnActionsFinished(int action, boolean result) {
        working=false;


        ((Splash_Screen)getActivity()).loginCompleted(result, Splash_Screen.TESSERACT);
    }

    @Override
    public void update() {

    }
}



