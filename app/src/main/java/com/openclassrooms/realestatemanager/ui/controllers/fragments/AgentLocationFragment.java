package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentAgentLocationBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AgentLocationFragment extends Fragment implements OnMapReadyCallback {

    private Activity mActivity;
    private GoogleMap mGoogleMap;
    private Location mLastKnownLocation;

    private RealEstateViewModel mRealEstateViewModel;

    FragmentAgentLocationBinding mBinding;
    private TakeOrNotFullScreen mCallback;

    public AgentLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAgentLocationBinding.inflate(inflater, container, false);
        mBinding.fragmentAgentLocationMapview.onCreate(savedInstanceState);
        mBinding.fragmentAgentLocationMapview.getMapAsync(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Around me");
        configureViewModel();
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    /**
     * Sets an observer to fetch the properties that have zipcode and country matched with parameters.
     */
    private void getRealEstateByCityAndCountry(String cityName, String countryCode) {
        mRealEstateViewModel.getRealEstateByCityAndCountry(cityName, countryCode).observe(getViewLifecycleOwner(), this::addRealEstatesMarker);
    }

    /**
     * Gets the LiveData's content and add for each property a marker on the map.
     */
    private void addRealEstatesMarker(List<Property> properties) {
        if (mGoogleMap != null) {
            for (Property property : properties) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(property.getLatitude(),
                        property.getLongitude())).title(property.getAddress()).snippet(String.valueOf(property.getId())));
            }
        }
    }

    private void passRealEstateIdToFragmentDetails(long id) {
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment2);
            Bundle args = new Bundle();
            args.putLong("propertyId",id);
            controller.navigate(R.id.detailsFragment2,args);
            mCallback.doNotTakeFullScreen();
        } else {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(AgentLocationFragmentDirections.actionAgentLocationFragmentToDetailsFragment(id));
        }
    }

    /**
     * Gets a GoogleMap object ready to use and defines the map's UI settings and the expected behavior
     * when user clicks on a marker or on a InfoWindow.
     */
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
//                Sets a custom layout inflated for InfoWindow view.
                View view = LayoutInflater.from(getContext()).inflate(R.layout.infowindow_item, null);
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
            if (marker.getTitle().equals(marker.getTag())) {
                marker.setTag(null);
                passRealEstateIdToFragmentDetails(id);
            } else {
                marker.showInfoWindow();
                marker.setTag(marker.getTitle());
            }
            return true;
        });
        getLastKnownLocation();
    }

    /**
     * Fetches the user's last known position (Latitude, Longitude) from the Google Play Services
     * and move the map's camera on this position.
     */
    private void getLastKnownLocation() {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    mGoogleMap.setMyLocationEnabled(true);
                    mLastKnownLocation = location;
                    getTheCountryCodeAndZipCode(mLastKnownLocation);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((
                            new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())), 12));
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the country code and the zipcode corresponding to the user's position.
     */
    private void getTheCountryCodeAndZipCode(Location location) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            for (Address address : addresses) {
                String countryCode = address.getCountryCode().trim();
                String cityName = address.getLocality();
                getRealEstateByCityAndCountry(cityName, countryCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onStart() {
        mBinding.fragmentAgentLocationMapview.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("test", "onresume");
        mBinding.fragmentAgentLocationMapview.onResume();
        if(mLastKnownLocation != null){
            getTheCountryCodeAndZipCode(mLastKnownLocation);
        }
        mCallback.takeFullScreenFragment();
        super.onResume();
    }

    @Override
    public void onPause() {
        mBinding.fragmentAgentLocationMapview.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mBinding.fragmentAgentLocationMapview.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mBinding.fragmentAgentLocationMapview.onLowMemory();
        super.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        if (mGoogleMap != null)
            mGoogleMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mBinding.fragmentAgentLocationMapview.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.fragmentAgentLocationMapview.onDestroy();
        mBinding = null;
    }
}
