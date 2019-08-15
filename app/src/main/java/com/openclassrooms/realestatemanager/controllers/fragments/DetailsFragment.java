package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.adapters.DetailsPhotoAdapter;
import com.openclassrooms.realestatemanager.controllers.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.viewholder.PicturesDetailsViewHolder;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.fragment_details_desc_txt)
    TextView mDescription;
    @BindView(R.id.fragment_details_surface_txt)
    TextView mSurface;
    @BindView(R.id.fragment_details_rooms_txt)
    TextView mRooms;
    @BindView(R.id.guideline)
    Guideline mGuideline;
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


    private long mRealEstateId;
    private RealEstateViewModel mRealEstateViewModel;

//    private OnFragmentInteractionListener mListener;

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
            Log.i("info", "onCreate: " + mRealEstateId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        configureViewModel();
        getRealEstateDetails(mRealEstateId);
        return view;
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
        DetailsPhotoAdapter detailsPhotoAdapter = new DetailsPhotoAdapter(pictures);
        detailsPhotoAdapter.setOnClickListener(view -> {
            PicturesDetailsViewHolder holder = (PicturesDetailsViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            Uri uri = pictures.get(position).getUri();
            getFragmentManager().beginTransaction().replace(R.id.activity_main_container, FullScreenPhoto.newInstance(uri))
                    .addToBackStack("Fragment")
                    .commit();
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
