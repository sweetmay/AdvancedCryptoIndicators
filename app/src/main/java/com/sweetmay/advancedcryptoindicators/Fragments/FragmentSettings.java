package com.sweetmay.advancedcryptoindicators.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.sweetmay.advancedcryptoindicators.R;

public class FragmentSettings extends PreferenceFragmentCompat {

    public FragmentSettings(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
