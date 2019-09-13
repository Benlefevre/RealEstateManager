package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentLocationFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.fragment_agent_location_mapview)
    MapView mMapview;

    private Activity mActivity;
    private GoogleMap mGoogleMap;
    private Location mLastKnownLocation;

    private RealEstateViewModel mRealEstateViewModel;

    private OnFragmentInteractionListener mListener;

    public AgentLocationFragment() {
        // Required empty public constructor
    }

    public static AgentLocationFragment newInstance() {
        AgentLocationFragment fragment = new AgentLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        assert mActivity != null;
        Toolbar toolbar = mActivity.findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Around me");
        configureViewModel();
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstateByZipCodeAndCountry(int zipCode, String countryCode) {
        mRealEstateViewModel.getRealEstateByZipcodeAndCountry(zipCode, countryCode).observe(getViewLifecycleOwner(), this::addRealEstatesMarker);
    }

    private void addRealEstatesMarker(List<RealEstate> realEstates) {
        if (mGoogleMap != null) {
            for (RealEstate realEstate : realEstates) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(realEstate.getLatitude(),
                        realEstate.getLongitude())).title(realEstate.getAddress()).snippet(String.valueOf(realEstate.getId())));
            }
        }
    }

    private void passRealEstateIdToFragmentDetails() {
        if (mListener != null) {
            mListener.openDetailsFragment();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.infowindow_item, null);
                TextView title = view.findViewById(R.id.infowindow_item_txt);
                title.setText(marker.getTitle());
                return view;
            }
        });
        mGoogleMap.setOnInfoWindowClickListener(marker -> {
            long id = Long.parseLong(marker.getSnippet());
            mRealEstateViewModel.addSelectedRealEstateId(id);
            passRealEstateIdToFragmentDetails();

        });
        mGoogleMap.setOnMarkerClickListener(marker -> {
            long id = Long.parseLong(marker.getSnippet());
            if (marker.getTitle().equals(marker.getTag())) {
                marker.setTag(null);
                mRealEstateViewModel.addSelectedRealEstateId(id);
                passRealEstateIdToFragmentDetails();
            } else {
                marker.showInfoWindow();
                marker.setTag(marker.getTitle());
            }
            return true;
        });
        getLastKnownLocation();
    }

    private void getLastKnownLocation() {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mLastKnownLocation = location;
                    getTheCountryCodeAndZipCode(mLastKnownLocation);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((
                            new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())), 14));
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getTheCountryCodeAndZipCode(Location location) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            for (Address address : addresses) {
                int zipcode = Integer.parseInt(address.getPostalCode().trim());
                String countryCode = address.getCountryCode().trim();
                getRealEstateByZipCodeAndCountry(zipcode, countryCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        void openDetailsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
