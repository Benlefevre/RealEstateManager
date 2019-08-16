package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.ui.adapters.FullScreenViewPagerAdapter;
import com.openclassrooms.realestatemanager.ui.controllers.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FullScreenPhoto extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.fragment_fullscreen_viewpager)
    ViewPager mViewPager;

    private List<Uri> mUriList;

//    private OnFragmentInteractionListener mListener;

    public FullScreenPhoto() {
        // Required empty public constructor
    }


    public static FullScreenPhoto newInstance(List<Pictures> picturesList) {
        ArrayList<String> uriList = new ArrayList<>();
        FullScreenPhoto fragment = new FullScreenPhoto();
        Bundle args = new Bundle();
        for (Pictures pictures : picturesList) {
            uriList.add(pictures.getUri().toString());
        }
        args.putStringArrayList(ARG_PARAM1, uriList);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUriList = new ArrayList<>();
            for (String string : getArguments().getStringArrayList(ARG_PARAM1)){
                mUriList.add(Uri.parse(string));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
