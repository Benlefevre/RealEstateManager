package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentLocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.fragment_agent_location_mapview)
    MapView mMapview;


    private String mParam1;
    private String mParam2;

    private GoogleMap mGoogleMap;
    private Boolean mLocationPermissionsGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mLastKnownLocation;

//    private OnFragmentInteractionListener mListener;

    public AgentLocationFragment() {
        // Required empty public constructor
    }

//    public static AgentLocationFragment newInstance(String param1, String param2) {
//        AgentLocationFragment fragment = new AgentLocationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static AgentLocationFragment newInstance(Boolean locationPermissionsGranted) {
        AgentLocationFragment fragment = new AgentLocationFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, locationPermissionsGranted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocationPermissionsGranted = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_location, container, false);
        ButterKnife.bind(this, view);
        mMapview.onCreate(savedInstanceState);
        mMapview.getMapAsync(this);
        return view;
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(@NonNull Context context) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getLastKnownLocation();
    }
//TODO créer un intent service pour le geocoding afin de recup le code postal et le pays où se situe l'agent.
    private void getPositionDetails() {
        if (mLastKnownLocation != null) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                List<Address> address = geocoder.getFromLocation(mLastKnownLocation.latitude, mLastKnownLocation.longitude, 1);
                for (Address address1 : address) {
                    Log.i("info", "getLastKnownLocation: " + address1.getPostalCode() + "/" + address1.getCountryName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void getLastKnownLocation() {
        try {
            if (mLocationPermissionsGranted) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        mGoogleMap.setMyLocationEnabled(true);
                        mLastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLastKnownLocation, 19));
                        getPositionDetails();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception : $%s", e.getMessage());
        }

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapview.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapview.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapview.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapview.onDestroy();
    }
}
