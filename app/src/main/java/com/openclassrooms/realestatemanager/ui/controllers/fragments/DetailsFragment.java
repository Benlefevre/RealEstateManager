package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.DetailsPhotoAdapter;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
import com.openclassrooms.realestatemanager.ui.viewholder.PicturesDetailsViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDetailsBinding mBinding;

    private Activity mActivity;
    private GoogleMap mGoogleMap;

    private long mRealEstateId;
    private RealEstateViewModel mRealEstateViewModel;
    private DetailsPhotoAdapter mPhotoAdapter;
    private List<Pictures> mPicturesList;
    private int mSelectedPosition;

    private double mLatitude;
    private double mLongitude;
    private TakeOrNotFullScreen mCallback;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRealEstateId = DetailsFragmentArgs.fromBundle(getArguments()).getPropertyId();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            toolbar.setTitle("Details");
        configureViewModel();
        configureRecyclerView();
        getPropertyDetails();
        mBinding.fragmentDetailsMapView.onCreate(savedInstanceState);
        mBinding.fragmentDetailsMapView.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fragment_details_edit) {
            openAddFragmentToEditRealEstate(mRealEstateId);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Gets a GoogleMap object ready to use and defines the UI parameters and the expected behavior
     * (nothing) when the user clicks on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnMapClickListener(latLng -> {
        });
    }

    /**
     * Adds a marker corresponding to the property's latitude and longitude on the map.
     */
    private void addPropertyPositionOnMap() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
        mRealEstateViewModel.getSelectedPropertyId().observe(getViewLifecycleOwner(), aLong -> {
            mRealEstateId = aLong;
            getPropertyDetails();
        });
    }

    /**
     * Sets observers to fetch the property's attributes and the pictures that have property's id in their attributes
     * into database.
     */
    private void getPropertyDetails() {
        mRealEstateViewModel.getRealEstate(mRealEstateId).observe(getViewLifecycleOwner(), this::initDetails);
        mRealEstateViewModel.getPictures(mRealEstateId).observe(getViewLifecycleOwner(), this::bindPhotoInRecyclerView);
    }

    /**
     * Gets the LiveData's content and add it in mPicturesList that is bound in RecyclerView.
     */
    private void bindPhotoInRecyclerView(List<Pictures> pictures) {
        mPicturesList.clear();
        mPicturesList.addAll(pictures);
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * Defines the RecyclerView's adapter and the expected behavior when user clicks on a RecyclerView's item.
     */
    private void configureRecyclerView() {
        mPicturesList = new ArrayList<>();
        mPhotoAdapter = new DetailsPhotoAdapter(mPicturesList, 1);
        mPhotoAdapter.setOnClickListener(view -> {
            PicturesDetailsViewHolder holder = (PicturesDetailsViewHolder) view.getTag();
            mSelectedPosition = holder.getAdapterPosition();
            setUriListInMutableLiveData();
            passPicturesAndUriToFullScreenFragment();
        });
        mBinding.fragmentDetailsRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mBinding.fragmentDetailsRecyclerview.setAdapter(mPhotoAdapter);
    }

    /**
     * Fetches the selected item's uri and set a MutableLiveData's value with this uri and mPicturesList.
     */
    private void setUriListInMutableLiveData() {
        List<Uri> uriList = new ArrayList<>();
        for (Pictures pictures : mPicturesList) {
            uriList.add(pictures.getUri());
        }
        mRealEstateViewModel.addUriList(uriList);
    }

    /**
     * Gets the LiveData's content and bind it into the corresponding view.
     */
    private void initDetails(Property property) {
        String address, zipcode, city;
        if (property.getAddress() == null)
            address = "";
        else
            address = property.getAddress();
        if (property.getZipCode() == 0)
            zipcode = "";
        else
            zipcode = String.valueOf(property.getZipCode());
        if (property.getCity() == null)
            city = "";
        else
            city = property.getCity();
        mBinding.fragmentDetailsDescTxt.setText(property.getDescription());
        mBinding.fragmentDetailsSurfaceTxt.setText(Utils.displayAreaUnitAccordingToPreferences(mActivity, property.getSurface()));
        mBinding.fragmentDetailsRoomsTxt.setText(String.valueOf(property.getNbRooms()));
        mBinding.fragmentDetailsBedroomsTxt.setText(String.valueOf(property.getNbBedrooms()));
        mBinding.fragmentDetailsBathroomTxt.setText(String.valueOf(property.getNbBathrooms()));
        mBinding.fragmentDetailsLocationTxt.setText(getString(R.string.adress_details, address, zipcode, city));
        mBinding.fragmentDetailsFloorsTxt.setText(String.valueOf(property.getFloors()));
        mBinding.fragmentDetailsCoownershipTxt.setText(String.valueOf(property.isCoOwnership()));
        mBinding.fragmentDetailsConstructionTxt.setText(Utils.convertDateToString(property.getYearConstruction(), mActivity));
        mLatitude = property.getLatitude();
        mLongitude = property.getLongitude();
        addPropertyPositionOnMap();
    }


    private void passPicturesAndUriToFullScreenFragment() {
        NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Bundle args = new Bundle();
        args.putInt("selectedPosition", mSelectedPosition);
        mController.navigate(R.id.fullScreenFragment, args);
        mCallback.takeFullScreenFragment();
    }

    private void openAddFragmentToEditRealEstate(long realEstateId) {
        if (!getResources().getBoolean(R.bool.isTabletLand)) {
            NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            mController.navigate(DetailsFragmentDirections.actionDetailsFragmentToAddPropertyFragment().setRealEstateId(realEstateId));
        } else {
            NavController mControllerLand = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment2);
            Bundle args = new Bundle();
            args.putLong("realEstateId", realEstateId);
            mControllerLand.navigate(R.id.addPropertyFragment, args);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleMap = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.fragmentDetailsMapView.getMapAsync(this);
        mCallback.doNotTakeFullScreen();
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
    public void onDestroyView() {
//        Sets adapters and listeners to null to avoid memory leaks.
        mBinding.fragmentDetailsRecyclerview.setAdapter(null);
        mPhotoAdapter.setOnClickListener(null);
        mPhotoAdapter = null;
        super.onDestroyView();
        mBinding = null;
    }
}
