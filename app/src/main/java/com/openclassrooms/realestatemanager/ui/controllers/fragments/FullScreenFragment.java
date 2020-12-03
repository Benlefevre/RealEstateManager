package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentFullScreenPhotoBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.FullScreenAdapter;
import com.openclassrooms.realestatemanager.ui.adapters.OnSnapPositionChangeListener;
import com.openclassrooms.realestatemanager.ui.adapters.SnapOnScrollListener;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullScreenFragment extends Fragment implements OnSnapPositionChangeListener {

    private List<Uri> mUriList;
    private Activity mActivity;
    private RealEstateViewModel mRealEstateViewModel;
    FragmentFullScreenPhotoBinding mBinding;
    FullScreenAdapter mAdapter;
    int mSelectedPosition;

    public FullScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mSelectedPosition = FullScreenFragmentArgs.fromBundle(getArguments()).getSelectedPosition();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentFullScreenPhotoBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
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
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    /**
     * Sets an observer to fetch the MutableLiveData's content.
     */
    private void getUriList(){
        mUriList = new ArrayList<>();
        mRealEstateViewModel.getUriList().observe(getViewLifecycleOwner(),this::configureRecyclerView);
    }

    private void configureRecyclerView(List<Uri> uriList){
        mUriList = new ArrayList<>();
        mUriList.addAll(uriList);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBinding.fragmentFullscreenRecycler);
        mAdapter = new FullScreenAdapter(mUriList);
        mBinding.fragmentFullscreenRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false));
        mBinding.fragmentFullscreenRecycler.setAdapter(mAdapter);
        SnapOnScrollListener snapOnScrollListener = new SnapOnScrollListener(snapHelper,SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,this);
        mBinding.fragmentFullscreenRecycler.addOnScrollListener(snapOnScrollListener);
        mBinding.fragmentFullscreenRecycler.getLayoutManager().scrollToPosition(mSelectedPosition);
    }

    @Override
    public void onDestroyView() {
        mBinding.fragmentFullscreenRecycler.setAdapter(null);
        mAdapter = null;
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onSnapPositionChange(int lastPosition, int newPosition) {
        Log.i("player", "last position : " + lastPosition + " / new position : " + newPosition);
        mAdapter.pausePlayer(lastPosition);
        mAdapter.reloadPlayer(newPosition);
    }
}
