package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private Context mContext;
    private GoogleMap mGoogleMap;

    private long mRealEstateId;
    private RealEstateViewModel mRealEstateViewModel;

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
        mContext = getContext();
        if (getArguments() != null) {
            mRealEstateId = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        configureViewModel();
        getRealEstateDetails(mRealEstateId);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnMapClickListener(latLng -> {

        });
    }

    //    TODO voir pour mettre dans le service si possible et obtenir les coordonnées à l'ajout en base puis l'ajouter via un insert en base.
    private void getRealEstatePosition() {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocationName(mLocation.getText().toString(), 1);
            for (Address address : addresses) {
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstateDetails(long realEstateId) {
        mRealEstateViewModel.getRealEstate(realEstateId).observe(getViewLifecycleOwner(), this::initDetails);
        mRealEstateViewModel.getPictures(realEstateId).observe(getViewLifecycleOwner(), this::configureRecyclerViewWithPhotos);
    }

    private void configureRecyclerViewWithPhotos(List<Pictures> pictures) {
        DetailsPhotoAdapter detailsPhotoAdapter = new DetailsPhotoAdapter(pictures,1);
        detailsPhotoAdapter.setOnClickListener(view -> {
            PicturesDetailsViewHolder holder = (PicturesDetailsViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            Uri uri = pictures.get(position).getUri();
            passPicturesAndUriToFullScreenFragment(pictures, uri);
        });
        mPhotoRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mPhotoRecyclerview.setAdapter(detailsPhotoAdapter);
    }

    private void initDetails(RealEstate realEstate) {
        mDescription.setText(realEstate.getDescription());
        mSurface.setText(String.valueOf(realEstate.getSurface()));
        mRooms.setText(String.valueOf(realEstate.getNbPieces()));
        mBedrooms.setText(String.valueOf(realEstate.getNbBedrooms()));
        mBathroom.setText(String.valueOf(realEstate.getNbBathrooms()));
        mLocation.setText(String.valueOf(realEstate.getAddress()));
        mFloors.setText(String.valueOf(realEstate.getFloors()));
        mCoownershipTxt.setText(String.valueOf(realEstate.isCoOwnership()));
        mConstructionTxt.setText(Utils.convertDateToString(realEstate.getYearConstruction(),mContext));
        getRealEstatePosition();
    }


    private void passPicturesAndUriToFullScreenFragment(List<Pictures> pictures, Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pictures, uri);
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
    }
}
