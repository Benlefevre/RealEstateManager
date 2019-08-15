package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.controllers.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FullScreenPhoto extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.fragment_fullscreen_img)
    ImageView mFullscreenImg;


    private Uri mUri;

//    private OnFragmentInteractionListener mListener;

    public FullScreenPhoto() {
        // Required empty public constructor
    }

    public static FullScreenPhoto newInstance(Uri uri) {
        FullScreenPhoto fragment = new FullScreenPhoto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uri.toString());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUri = Uri.parse((getArguments().getString(ARG_PARAM1)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);
        ButterKnife.bind(this, view);
        bindPhotoInImageView();
        return view;
    }


    private void bindPhotoInImageView() {
        mFullscreenImg.setImageURI(mUri);
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
