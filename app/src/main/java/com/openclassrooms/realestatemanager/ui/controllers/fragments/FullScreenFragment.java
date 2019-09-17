package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.FullScreenViewPagerAdapter;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FullScreenFragment extends Fragment {

    @BindView(R.id.fragment_fullscreen_viewpager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;
    private List<Uri> mUriList;
    private Activity mActivity;
    private RealEstateViewModel mRealEstateViewModel;


    public FullScreenFragment() {
        // Required empty public constructor
    }

    public static FullScreenFragment newInstance(){
        FullScreenFragment fragment = new FullScreenFragment();
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
        View view = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle(getString(R.string.details_pictures));
        configureViewModel();
        getUriList();
    }

//    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    /**
     * Sets an observer to fetch the MutableLiveData's content.
     */
    private void getUriList(){
        mUriList = new ArrayList<>();
        mRealEstateViewModel.getUriList().observe(getViewLifecycleOwner(),this::configureViewPager);
    }

    /**
     * Gets the MutableLiveData's content and add it in mUriList that is used to set the ViewPager's
     * adapter.
     */
    private void configureViewPager(List<Uri> uriList) {
        mUriList = new ArrayList<>();
        mUriList.addAll(uriList);
        FullScreenViewPagerAdapter adapter = new FullScreenViewPagerAdapter(mActivity,mUriList);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        mViewPager.setAdapter(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
