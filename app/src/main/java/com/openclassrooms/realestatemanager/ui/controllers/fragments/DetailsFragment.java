package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.DetailsPhotoAdapter;
import com.openclassrooms.realestatemanager.ui.viewholder.PicturesDetailsViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.fragment_details_desc_txt)
    TextView mDescription;
    @BindView(R.id.fragment_details_surface_txt)
    TextView mSurface;
    @BindView(R.id.fragment_details_rooms_txt)
    TextView mRooms;
    @BindView(R.id.fragment_details_bedrooms_txt)
    TextView mBedrooms;
    @BindView(R.id.fragment_details_bathroom_txt)
    TextView mBathroom;
    @BindView(R.id.fragment_details_location_txt)
    TextView mLocation;
    @BindView(R.id.fragment_details_floors_txt)
    TextView mFloors;
    @BindView(R.id.fragment_details_recyclerview)
    RecyclerView mPhotoRecyclerview;
    @BindView(R.id.fragment_details_mapView)
    MapView mMapView;
    @BindView(R.id.fragment_details_coownership_txt)
    TextView mCoownershipTxt;
    @BindView(R.id.fragment_details_construction_txt)
    TextView mConstructionTxt;

    private Unbinder mUnbinder;
    private SharedPreferences mPreferences;
    private Activity mActivity;
    private GoogleMap mGoogleMap;

    private long mRealEstateId;
    private RealEstateViewModel mRealEstateViewModel;

    private DetailsPhotoAdapter mPhotoAdapter;
    private List<Pictures> mPicturesList;

    private double mLatitude;
    private double mLongitude;

    private OnFragmentInteractionListener mListener;


    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(long param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRealEstateId = getArguments().getLong(ARG_PARAM1);
        }
        configureViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        setHasOptionsMenu(true);
        mUnbinder = ButterKnife.bind(this, view);
        configureRecyclerView();
        getRealEstateDetails(mRealEstateId);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Details");
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fragment_details_edit) {
            passRealEstateIdToAddFragment(mRealEstateId);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnMapClickListener(latLng -> {
        });
    }

    private void addRealEstatePositionOnMap() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstateDetails(long realEstateId) {
        mRealEstateViewModel.getRealEstate(realEstateId).observe(getViewLifecycleOwner(), this::initDetails);
        mRealEstateViewModel.getPictures(realEstateId).observe(getViewLifecycleOwner(), this::bindPhotoInRecyclerView);
    }

    private void bindPhotoInRecyclerView(List<Pictures> pictures) {
        mPicturesList.addAll(pictures);
        mPhotoAdapter.notifyDataSetChanged();
    }

    private void configureRecyclerView(){
        mPicturesList = new ArrayList<>();
        mPhotoAdapter = new DetailsPhotoAdapter(mPicturesList, 1);
        mPhotoAdapter.setOnClickListener(view -> {
            PicturesDetailsViewHolder holder = (PicturesDetailsViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            Uri uri = mPicturesList.get(position).getUri();
            passPicturesAndUriToFullScreenFragment(mPicturesList, uri);
        });
        mPhotoRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mPhotoRecyclerview.setAdapter(mPhotoAdapter);
    }

    private void initDetails(RealEstate realEstate) {
        mDescription.setText(realEstate.getDescription());
        mSurface.setText(Utils.displayAreaUnitAccordingToPreferences(mActivity,realEstate.getSurface()));
        mRooms.setText(String.valueOf(realEstate.getNbRooms()));
        mBedrooms.setText(String.valueOf(realEstate.getNbBedrooms()));
        mBathroom.setText(String.valueOf(realEstate.getNbBathrooms()));
        mLocation.setText(String.valueOf(realEstate.getAddress()));
        mFloors.setText(String.valueOf(realEstate.getFloors()));
        mCoownershipTxt.setText(String.valueOf(realEstate.isCoOwnership()));
        mConstructionTxt.setText(Utils.convertDateToString(realEstate.getYearConstruction(), mActivity));
        mLatitude = realEstate.getLatitude();
        mLongitude = realEstate.getLongitude();
        addRealEstatePositionOnMap();
    }


    private void passPicturesAndUriToFullScreenFragment(List<Pictures> pictures, Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pictures, uri);
        }
    }

    private void passRealEstateIdToAddFragment(long realEstateId){
        if (mListener != null){
            mListener.onFragmentInteractionEdit(realEstateId);
        }
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(List<Pictures> pictures, Uri uri);
        void onFragmentInteractionEdit(long id);
    }

    @Override
    public void onDestroyView() {
        mPhotoRecyclerview.setAdapter(null);
        mPhotoAdapter.setOnClickListener(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
