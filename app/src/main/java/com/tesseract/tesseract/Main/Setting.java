package com.tesseract.tesseract.Main;

import android.os.Bundle;
import android.app.Fragment;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.tesseract.tesseract.R;

import core.Entity.User;
import core.UserCreator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Setting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends PreferenceFragment{
    private static UserCreator userCreator;
    private User mUser;
    public boolean changed=false;
    public Setting(){

            userCreator = UserCreator.getInstance();
            //mUser= userCreator.userFactory();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        findPreference("example_text").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CharSequence text = ((EditTextPreference) preference).getText();
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                return true;
            }
        });



        
    }

    @Override
    public void onDetach(){
        super.onDetach();

    //        userCreator.storeUser(mUser);
    }



}
