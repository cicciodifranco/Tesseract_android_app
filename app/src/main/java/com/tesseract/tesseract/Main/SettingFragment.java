package com.tesseract.tesseract.Main;

import android.os.Bundle;
import android.app.Fragment;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.tesseract.tesseract.R;

import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.User;
import core.PreferenceEditor;
import core.UserCreator;



public class SettingFragment extends PreferenceFragment{
    private static String TAG = "settings fragment";

    private static UserCreator userCreator;
    private User mUser;
    public boolean changed=false;
    public SettingFragment(){

            userCreator = UserCreator.getInstance();
            //mUser= userCreator.userFactory();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

        Preference name = findPreference("name");
        name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CharSequence text = ((EditTextPreference) preference).getText();
                if (text != null) {
                    User user = UserCreator.userFactory();

                    user.setName(text.toString());
                    UserCreator.storeUser();
                }
                else Log.i(TAG, "text name null");


                return true;
            }
        });
        name.setDefaultValue(UserCreator.userFactory().getName());


        Preference surname = findPreference("surname");

        surname.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CharSequence text = ((EditTextPreference) preference).getText();
                User user= UserCreator.userFactory();
                if(text != null) {
                    user.setSurname(text.toString());
                    UserCreator.storeUser();
                }
                else Log.i(TAG, "text surname null, obj: "+newValue.toString());
                return true;
            }
        });
        surname.setDefaultValue(UserCreator.userFactory().getSurname());

        Preference car = findPreference("car");
        car.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CharSequence text = ((EditTextPreference) preference).getText();
                if(text!=null)
                    UserCreator.carFactory(text.toString());
                return true;
            }
        });
        Car carEntity =(Car) UserCreator.userFactory().getSelectedCar();
        if(carEntity!=null)
            car.setDefaultValue(carEntity.getRegistration_number());



        
    }

    @Override
    public void onDetach(){
        super.onDetach();

    //        userCreator.storeUser(mUser);
    }



}
