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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.ui.viewholder.PropertyViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.openclassrooms.realestatemanager.utils.Constants.DETAILS_FRAGMENT;

public class PropertyListFragment extends Fragment {

    private static final String ORIGIN = "origin";

    @BindView(R.id.fragment_list_recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private PropertyAdapter mEstateAdapter;
    private RealEstateViewModel mRealEstateViewModel;
    private List<Property> mPropertyList;
    private List<Pictures> mPicturesList;
    private Activity mActivity;
    private String mOrigin;
    private long mRealEstateId;
    private boolean isTabletLand;


    private OnFragmentInteractionListener mListener;

    public PropertyListFragment() {
        // Required empty public constructor
    }

    public static PropertyListFragment newInstance(String origin) {
        PropertyListFragment fragment = new PropertyListFragment();
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
        mPicturesList = new ArrayList<>();
        mPropertyList = new ArrayList<>();
        configureRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        isTabletLand = getResources().getBoolean(R.bool.isTabletLand);
        configureViewModel();
//        Depending to the origin, fetches all the properties or only searched properties.
        if (mOrigin.equals(Constants.SEARCH_FRAGMENT))
            getSearchedRealEstate();
        else
            getAllRealEstate();
//        If the device is a tablet in landscape mode, fetches the selected property's id defines in
//        a ViewModel's MutableLiveData to open the right fragment when user clicks on a toolbar's item
        if (isTabletLand)
            getSelectedRealEstateId();
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
            case R.id.fragment_list_edit:
                mListener.openAddFragmentToEditRealEstate(mRealEstateId);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory)
                .get(RealEstateViewModel.class);
    }

    /**
     * Sets an observer to fetch the MutableLiveData's content.
     */
    private void getSelectedRealEstateId() {
        mRealEstateViewModel.getSelectedPropertyId().observe(getViewLifecycleOwner(),
                realEstateId -> mRealEstateId = realEstateId);
    }

    /**
     * Sets an observer to fetch all the properties in database.
     */
    private void getAllRealEstate() {
        mRealEstateViewModel.getAllRealEstate().observe(getViewLifecycleOwner(), this::initList);
    }

    /**
     * Sets an observer to fetch only the searched properties.
     */
    private void getSearchedRealEstate() {
        mRealEstateViewModel.getPropertyList().observe(getViewLifecycleOwner(), this::initList);
    }

    /**
     * Sets an observer to fetch only one picture that has the property's id in its attributes.
     */
    private void getOnePicture(long realEstateId) {
        mRealEstateViewModel.getOnePicture(realEstateId).observe(getViewLifecycleOwner(), this::initPictures);
    }

    /**
     * Gets the LiveData's content and add it in mPicturesList.
     */
    private void initPictures(Pictures pictures) {
        if (pictures != null)
            mPicturesList.add(pictures);
        mEstateAdapter.notifyDataSetChanged();
    }

    /**
     * Gets the LiveData's content and add it in mPropertiesList.
     */
    private void initList(List<Property> properties) {
        mPropertyList.clear();
        mPropertyList.addAll(properties);
        for (Property property : mPropertyList) {
            getOnePicture(property.getId());
        }
//        Sets the first property's id as MutableLiveData's value to display a default content in
//        DetailsFragment when the application runs on a tablet in landscape mode.
        if (!mPropertyList.isEmpty() && isTabletLand)
            mRealEstateViewModel.addSelectedPropertyId(mPropertyList.get(0).getId());
    }

    /**
     * Sets the RecyclerView's adapter and the expected behavior when user clicks on a RecyclerView's
     * item.
     * Depending to the device's type and orientation, defines the layout's type used as RecyclerView's
     * LayoutManager
     */
    private void configureRecyclerView() {
        mEstateAdapter = new PropertyAdapter(mPropertyList, mPicturesList);
        mEstateAdapter.setOnItemClickListener(view -> {
            PropertyViewHolder holder = (PropertyViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            long id = mPropertyList.get(position).getId();
            mRealEstateViewModel.addSelectedPropertyId(id);
            if (!isTabletLand)
                openDetailsFragment();
        });
        if (getResources().getBoolean(R.bool.isTablet))
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false));
        else
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mEstateAdapter);
    }

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

    @Override
    public void onDestroyView() {
//        Sets adapters and listeners to null to avoid memory leaks.
        mRecyclerView.setAdapter(null);
        mEstateAdapter.setOnItemClickListener(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onResume() {
        if (isTabletLand) {
            mListener.checkVisibility(DETAILS_FRAGMENT);
        }
        super.onResume();
    }

    public interface OnFragmentInteractionListener {
        void openDetailsFragment();
        void onFragmentInteraction(String destination);
        void checkVisibility(String destination);
        void openAddFragmentToEditRealEstate(long id);
    }

}
