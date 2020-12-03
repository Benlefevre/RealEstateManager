package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.PropertyAdapter;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
import com.openclassrooms.realestatemanager.ui.viewholder.PropertyViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropertyListFragment extends Fragment {

    private PropertyAdapter mEstateAdapter;
    private RealEstateViewModel mRealEstateViewModel;
    private List<Property> mPropertyList;
    private List<Pictures> mPicturesList;
    private Activity mActivity;
    private String mOrigin;
    private long mRealEstateId;

    FragmentListBinding mBinding;
    private TakeOrNotFullScreen mCallback;

    public PropertyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrigin = PropertyListFragmentArgs.fromBundle(getArguments()).getOrigin();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentListBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        mPicturesList = new ArrayList<>();
        mPropertyList = new ArrayList<>();
        configureRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        configureViewModel();
//        Depending to the origin, fetches all the properties or only searched properties.
        if (mOrigin.equals(Constants.SEARCH_FRAGMENT))
            getSearchedRealEstate();
        else
            getAllRealEstate();
////        If the device is a tablet in landscape mode, fetches the selected property's id defines in
////        a ViewModel's MutableLiveData to open the right fragment when user clicks on a toolbar's item
//        if (getResources().getBoolean(R.bool.isTabletLand))
//            getSelectedRealEstateId();
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
        NavController controller = Navigation.findNavController(mActivity, R.id.nav_host_fragment);
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_letf)
                .build();
        if (item.getItemId() == R.id.fragment_list_edit) {
            Bundle args = new Bundle();
            args.putLong("realEstateId", mRealEstateId);
            controller.navigate(R.id.addPropertyFragment, args,navOptions);
            return true;
        } else if (item.getItemId() == R.id.fragment_list_search_btn) {
            controller.navigate(R.id.searchFragment,null,navOptions);
            return true;
        } else if (item.getItemId() == R.id.fragment_list_add_btn) {
            controller.navigate(R.id.addPropertyFragment,null,navOptions);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory)
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
//        if (!mPropertyList.isEmpty() && getResources().getBoolean(R.bool.isTabletLand))
//            mRealEstateViewModel.addSelectedPropertyId(mPropertyList.get(0).getId());
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
            mRealEstateId = id;
            mRealEstateViewModel.addSelectedPropertyId(mRealEstateId);
            if (!getResources().getBoolean(R.bool.isTabletLand)) {
                openDetailsFragment(id);
            } else {
                openDetailsFragmentLand(id);
            }
        });
        if (getResources().getBoolean(R.bool.isTablet) && !getResources().getBoolean(R.bool.isTabletLand)) {
            mBinding.fragmentListRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false));
        } else {
            mBinding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        }
        mBinding.fragmentListRecyclerView.setAdapter(mEstateAdapter);
    }

    private void openDetailsFragment(long realEstateId) {
        NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        PropertyListFragmentDirections.ActionPropertyListFragmentToDetailsFragment action = PropertyListFragmentDirections.actionPropertyListFragmentToDetailsFragment(realEstateId);
        mController.navigate(action);
    }

    private void openDetailsFragmentLand(long realEstateId) {
        NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment2);
        mController.popBackStack();
        Bundle args = new Bundle();
        args.putLong("propertyId", realEstateId);
        mController.navigate(R.id.detailsFragment2, args);
    }

    @Override
    public void onDestroyView() {
//        Sets adapters and listeners to null to avoid memory leaks.
        mBinding.fragmentListRecyclerView.setAdapter(null);
        mEstateAdapter.setOnItemClickListener(null);
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            mCallback.doNotTakeFullScreen();
        }
//        If the device is a tablet in landscape mode, fetches the selected property's id defines in
//        a ViewModel's MutableLiveData to open the right fragment when user clicks on a toolbar's item
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            getSelectedRealEstateId();
            Log.i("player", "selectedId : " + mRealEstateId );
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


}
