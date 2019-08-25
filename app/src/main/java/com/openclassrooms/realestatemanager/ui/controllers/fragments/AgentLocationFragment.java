package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.openclassrooms.realestatemanager.service.FetchAddressIntentService;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.utils.Constants.RECEIVER_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.RESULT_EXTRA;

public class AgentLocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.fragment_agent_location_mapview)
    MapView mMapview;


    private String mParam1;
    private String mParam2;

    private Activity mActivity;
    private GoogleMap mGoogleMap;
    private Boolean mLocationPermissionsGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private AddressResultReceiver mResultReceiver;

    private RealEstateViewModel mRealEstateViewModel;

    private OnFragmentInteractionListener mListener;

    class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null)
                return;
            String addressOutput = resultData.getString(RESULT_EXTRA);
            if (addressOutput == null)
                addressOutput = "";
            displayLocation(addressOutput);

        }
    }

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
        mActivity = getActivity();
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
        mResultReceiver = new AddressResultReceiver(null);
        configureViewModel();
        return view;
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstateByZipcodeAndCountry(int zipcode, String countryCode) {
        mRealEstateViewModel.getRealEstateByZipcodeAndCountry(zipcode, countryCode).observe(getViewLifecycleOwner(), this::addRealEstatesMarker);
    }

    private void addRealEstatesMarker(List<RealEstate> realEstates) {
        if (mGoogleMap != null) {
            for (RealEstate realEstate : realEstates) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(realEstate.getLatitude(),
                        realEstate.getLongitude())).title(realEstate.getAddress()).snippet(String.valueOf(realEstate.getId())));
            }
        }
    }


    private void passRealEstateIdToFragmentDetails(long id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
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
                View view = LayoutInflater.from(getContext()).inflate(R.layout.infowindow_item,null);
                TextView title = view.findViewById(R.id.infowindow_item_txt);
                title.setText(marker.getTitle());
                return view;
            }
        });
        mGoogleMap.setOnInfoWindowClickListener(marker -> {
            long id = Long.parseLong(marker.getSnippet());
            passRealEstateIdToFragmentDetails(id);

        });
        mGoogleMap.setOnMarkerClickListener(marker -> {
            long id = Long.parseLong(marker.getSnippet());
            if (marker.getTitle().equals(marker.getTag())){
                marker.setTag(null);
                passRealEstateIdToFragmentDetails(id);
            }else{
                marker.showInfoWindow();
                marker.setTag(marker.getTitle());
            }
            return true;
        });
        getLastKnownLocation();

    }

    private void getLastKnownLocation() {
        try {
            if (mLocationPermissionsGranted) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        mGoogleMap.setMyLocationEnabled(true);
                        mLastKnownLocation = location;
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((
                                new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())), 14));
                        startIntentGeocoderService();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception : $%s", e.getMessage());
        }

    }

    private void startIntentGeocoderService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.LOCATION_EXTRA, mLastKnownLocation);
        intent.putExtra(RECEIVER_EXTRA, mResultReceiver);
        Objects.requireNonNull(getActivity()).startService(intent);
    }

    private void displayLocation(String address) {
        String countryCode = address.substring(0, address.indexOf("/")).trim();
        int zipcode = Integer.parseInt(address.substring(address.lastIndexOf("/") + 1).trim());
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> getRealEstateByZipcodeAndCountry(zipcode, countryCode));
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(long id);
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

}
