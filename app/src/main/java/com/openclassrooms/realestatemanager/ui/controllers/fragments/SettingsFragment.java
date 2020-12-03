package com.openclassrooms.realestatemanager.ui.controllers.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private TakeOrNotFullScreen mCallback;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        Objects.requireNonNull(activity).setTheme(R.style.customPreferencesStyle);
        Toolbar toolbar = Objects.requireNonNull(activity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Settings");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TakeOrNotFullScreen) {
            mCallback = (TakeOrNotFullScreen) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TakeFullScreen");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.takeFullScreenFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

    }
}
