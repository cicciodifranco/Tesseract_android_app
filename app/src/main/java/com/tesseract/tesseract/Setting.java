package com.tesseract.tesseract;

import android.app.Activity;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Setting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends PreferenceFragment{

    public Setting(){

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



}
