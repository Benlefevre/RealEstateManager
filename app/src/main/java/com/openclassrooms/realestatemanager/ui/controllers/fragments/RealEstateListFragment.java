package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RealEstateListFragment extends Fragment {

    private static final String ORIGIN = "origin";

    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private RealEstateAdapter mEstateAdapter;
    private RealEstateViewModel mRealEstateViewModel;
    private List<RealEstate> mRealEstates;
    private List<Pictures> mPictures;
    private Activity mActivity;
    private String mOrigin;


    private OnFragmentInteractionListener mListener;

    public RealEstateListFragment() {
        // Required empty public constructor
    }

    public static RealEstateListFragment newInstance(String origin) {
        RealEstateListFragment fragment = new RealEstateListFragment();
        Bundle args = new Bundle();
        args.putString(ORIGIN, origin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrigin = getArguments().getString(ORIGIN);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        mUnbinder = ButterKnife.bind(this, view);
        mPictures = new ArrayList<>();
        mRealEstates = new ArrayList<>();
        configureRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        configureViewModel();
        if (mOrigin.equals(Constants.SEARCH_FRAGMENT))
            getSearchedRealEstate();
        else
            getAllRealEstate();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Real Estate Manager");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_list_add_btn:
                mListener.onFragmentInteraction(Constants.ADD_REAL_ESTATE_FRAGMENT);
                break;
            case R.id.fragment_list_search_btn:
                mListener.onFragmentInteraction(Constants.SEARCH_FRAGMENT);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    //    Récupération de l'ensemble des biens
    private void getAllRealEstate() {
        mRealEstateViewModel.getAllRealEstate().observe(getViewLifecycleOwner(), this::initList);
    }

    private void getSearchedRealEstate() {
        mRealEstateViewModel.getRealEstateList().observe(getViewLifecycleOwner(), this::initList);
    }

    //    Récupération d'une photo pour la liste
    private void getOnePicture(long realEstateId) {
        mRealEstateViewModel.getOnePicture(realEstateId).observe(getViewLifecycleOwner(), this::initPictures);
    }

    private void initPictures(Pictures pictures) {
        if (pictures != null)
            mPictures.add(pictures);
        mEstateAdapter.notifyDataSetChanged();
    }


    private void initList(List<RealEstate> realEstates) {
        mRealEstates.addAll(realEstates);
        for (RealEstate realEstate : mRealEstates) {
            getOnePicture(realEstate.getId());
        }
    }

    private void configureRecyclerView() {
        mEstateAdapter = new RealEstateAdapter(mRealEstates, mPictures);
        mEstateAdapter.setOnItemClickListener(view -> {
            RealEstateViewHolder holder = (RealEstateViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            long id = mRealEstates.get(position).getId();
            mRealEstateViewModel.addSelectedRealEstateId(id);
            openDetailsFragment();
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mEstateAdapter);
    }

    //    Passe l'id du bien selectionné via le callback à l'activité
    private void openDetailsFragment() {
        if (mListener != null) {
            mListener.openDetailsFragment();
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
        void openDetailsFragment();

        void onFragmentInteraction(String destination);
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null);
        mEstateAdapter.setOnItemClickListener(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
