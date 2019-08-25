package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.ui.viewholder.RealEstateViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RealEstateListFragment extends Fragment {

    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView mRecyclerView;

    private RealEstateViewModel mRealEstateViewModel;
    private List<RealEstate> mRealEstates;
    private List<Pictures> mPictures;


    private OnFragmentInteractionListener mListener;

    public RealEstateListFragment() {
        // Required empty public constructor
    }

    public static RealEstateListFragment newInstance() {
        RealEstateListFragment fragment = new RealEstateListFragment();
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mPictures = new ArrayList<>();
        configureViewModel();
        getAllRealEstate();
        return view;
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    //    Récupération de l'ensemble des biens
    private void getAllRealEstate() {
        mRealEstateViewModel.getAllRealEstate().observe(getViewLifecycleOwner(), this::initList);
    }

    //    Récupération d'une photo pour la liste
    private void getOnePicture(long realEstateId) {
        mRealEstateViewModel.getOnePicture(realEstateId).observe(getViewLifecycleOwner(), this::initPictures);
    }

    private void initPictures(Pictures pictures) {
        if (pictures != null)
            mPictures.add(pictures);
        configureRecyclerView();
    }


    private void initList(List<RealEstate> realEstates) {
        mRealEstates = realEstates;
        for (RealEstate realEstate : mRealEstates) {
            getOnePicture(realEstate.getId());
        }
    }


    private void configureRecyclerView() {
        RealEstateAdapter realEstateAdapter = new RealEstateAdapter(mRealEstates, mPictures);
        realEstateAdapter.setOnItemClickListener(view -> {
            RealEstateViewHolder holder = (RealEstateViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            long id = mRealEstates.get(position).getId();
            passIdToDetailsFragment(id);
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mRecyclerView.setAdapter(realEstateAdapter);
        realEstateAdapter.notifyDataSetChanged();
    }


    //    Passe l'id du bien selectionné via le callback à l'activité
    private void passIdToDetailsFragment(long id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
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
        void onFragmentInteraction(long id);
    }
}
