package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.ui.adapters.FullScreenViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FullScreenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.fragment_fullscreen_viewpager)
    ViewPager mViewPager;

    private List<Uri> mUriList;


    public FullScreenFragment() {
        // Required empty public constructor
    }


    public static FullScreenFragment newInstance(List<Pictures> picturesList, Uri uri) {
        ArrayList<String> uriList = new ArrayList<>();
        FullScreenFragment fragment = new FullScreenFragment();
        Bundle args = new Bundle();
        for (Pictures pictures : picturesList) {
            uriList.add(pictures.getUri().toString());
        }
        args.putStringArrayList(ARG_PARAM1, uriList);
        args.putString(ARG_PARAM2, uri.toString());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUriList = new ArrayList<>();
            Uri uri = Uri.parse(getArguments().getString(ARG_PARAM2));
            mUriList.add(uri);
            for (String string : Objects.requireNonNull(getArguments().getStringArrayList(ARG_PARAM1))){
                if(!string.equals(uri.toString())){
                    mUriList.add(Uri.parse(string));
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);
        ButterKnife.bind(this, view);
        configureViewPager();
        return view;
    }

    private void configureViewPager() {
        FullScreenViewPagerAdapter adapter = new FullScreenViewPagerAdapter(getActivity(),mUriList);
        mViewPager.setAdapter(adapter);
    }
}
