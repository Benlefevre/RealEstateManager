package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import androidx.preference.PreferenceManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Converters;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.utils.Constants.SEARCH_FRAGMENT;

public class SearchFragment extends Fragment {

    private RealEstateViewModel mRealEstateViewModel;

    private Activity mActivity;

    private int mZipcodeInput;
    private String mCityInput;
    private int mMinPriceInput;
    private int mMaxPriceInput;
    private int mMinSurfaceInput;
    private int mMaxSurfaceInput;
    private int mFloorsInput;
    private int mChipPhotoInput;
    private int mChipRoomsInput;
    private int mChipBedroomsInput;
    private int mChipBathroomsInput;
    private int mChipCoownerInput;
    private int mChipIsSoldInput;
    private String mTypeInput;
    private String mAmenitiesInput;
    private long mForSaleDate;
    private long mSoldDate;

    FragmentSearchBinding mBinding;
    private TakeOrNotFullScreen mCallback;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        setOnClickListeners();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Search a real estate");
        configureTextViewAccordingToPreference();
        configureViewModel();
    }

    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    /**
     * Calls all the needed methods to get all the user's input
     */
    private void getUserInput() {
        getUserTextInput();
        getUserPhotoChoice();
        getUserRoomChoice();
        getUserBedroomsChoice();
        getUserBathroomsChoice();
        getUserCoownerChoice();
        getUserIsSoldChoice();
        getUserTypeChoice();
        mAmenitiesInput = Utils.getUserAmenitiesChoice(mBinding.chipGroupAmenities.chipSchool,
                mBinding.chipGroupAmenities.chipShop, mBinding.chipGroupAmenities.chipTransport,
                mBinding.chipGroupAmenities.chipGarden);
    }

    /**
     * Checks if the text fields are empty and if it's not the case, sets the user's input in the
     * corresponding  fields.
     */
    private void getUserTextInput() {
        if (!TextUtils.isEmpty(mBinding.fragmentSearchZipcodeTxt.getText()))
            mZipcodeInput = Integer.parseInt(mBinding.fragmentSearchZipcodeTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchCityTxt.getText()))
            mCityInput = mBinding.fragmentSearchCityTxt.getText().toString().trim();
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinSurfaceTxt.getText()))
            mMinSurfaceInput = Utils.convertAreaAccordingToPreferences(mActivity, mBinding.fragmentSearchMinSurfaceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMaxSurfaceTxt.getText()))
            mMaxSurfaceInput = Utils.convertAreaAccordingToPreferences(mActivity, mBinding.fragmentSearchMaxSurfaceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinPriceTxt.getText()))
            mMinPriceInput = Utils.convertPriceAccordingToPreferences(mActivity, mBinding.fragmentSearchMinPriceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMaxPriceTxt.getText()))
            mMaxPriceInput = Utils.convertPriceAccordingToPreferences(mActivity, mBinding.fragmentSearchMaxPriceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinFloorsTxt.getText()))
            mFloorsInput = Integer.parseInt(mBinding.fragmentSearchMinFloorsTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mBinding.fragmentSearchForSaleTxt.getText()))
            mForSaleDate = Converters.dateToTimestamp(Utils.convertStringToDate(mBinding.fragmentSearchForSaleTxt.getText().toString().trim()));
        if (!TextUtils.isEmpty(mBinding.fragmentSearchSoldTxt.getText()))
            mSoldDate = Converters.dateToTimestamp(Utils.convertStringToDate(mBinding.fragmentSearchSoldTxt.getText().toString().trim()));
    }

    /**
     * Verifies if each chip of the ChipGroup is checked and add values in a String field.
     */
    private void getUserTypeChoice() {
        mTypeInput = "(";
        if (mBinding.chipApartment.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput = mTypeInput + "'Apartment'";
        }
        if (mBinding.chipLoft.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Loft'";
        }
        if (mBinding.chipHouse.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'House'";
        }
        if (mBinding.chipVilla.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Villa'";
        }
        if (mBinding.chipManor.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Manor'";
        }
        mTypeInput += ")";
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipCoownerInput's value.
     */
    private void getUserCoownerChoice() {
        int checkedChipId = mBinding.chipGroupCoowner.getCheckedChipId();
        if (checkedChipId == R.id.chip_yes) {
            mChipCoownerInput = 1;
        } else if (checkedChipId == R.id.chip_no) {
            mChipCoownerInput = 0;
        } else {
            mChipCoownerInput = 10;
        }
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipIsSoldInput's value.
     */
    private void getUserIsSoldChoice() {
        int checkedChipId = mBinding.chipGroupIsSold.getCheckedChipId();
        if (checkedChipId == R.id.chip_yes_sold) {
            mChipIsSoldInput = 1;
        } else if (checkedChipId == R.id.chip_no_sold) {
            mChipIsSoldInput = 0;
        } else {
            mChipIsSoldInput = 10;
        }
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipBathroomsInput's value.
     */
    private void getUserBathroomsChoice() {
        int checkedChipId = mBinding.chipGroupBathrooms.getCheckedChipId();
        if (checkedChipId == R.id.chip_1_bathroom) {
            mChipBathroomsInput = 1;
        } else if (checkedChipId == R.id.chip_2_bathrooms) {
            mChipBathroomsInput = 2;
        } else if (checkedChipId == R.id.chip_3_bathrooms) {
            mChipBathroomsInput = 3;
        } else if (checkedChipId == R.id.chip_4_bathrooms) {
            mChipBathroomsInput = 4;
        } else if (checkedChipId == R.id.chip_5_bathrooms) {
            mChipBathroomsInput = 5;
        } else {
            mChipBathroomsInput = 0;
        }
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipBedroomsInput's value.
     */
    private void getUserBedroomsChoice() {
        int checkedChipId = mBinding.chipGroupBedrooms.getCheckedChipId();
        if (checkedChipId == R.id.chip_1_bedroom) {
            mChipBedroomsInput = 1;
        } else if (checkedChipId == R.id.chip_2_bedrooms) {
            mChipBedroomsInput = 2;
        } else if (checkedChipId == R.id.chip_3_bedrooms) {
            mChipBedroomsInput = 3;
        } else if (checkedChipId == R.id.chip_4_bedrooms) {
            mChipBedroomsInput = 4;
        } else if (checkedChipId == R.id.chip_5_bedrooms) {
            mChipBedroomsInput = 5;
        } else {
            mChipBedroomsInput = 0;
        }
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipRoomsInput's value.
     */
    private void getUserRoomChoice() {
        int checkedChipId = mBinding.chipGroupRooms.getCheckedChipId();
        if (checkedChipId == R.id.chip_1_room) {
            mChipRoomsInput = 1;
        } else if (checkedChipId == R.id.chip_2_rooms) {
            mChipRoomsInput = 2;
        } else if (checkedChipId == R.id.chip_3_rooms) {
            mChipRoomsInput = 3;
        } else if (checkedChipId == R.id.chip_4_rooms) {
            mChipRoomsInput = 4;
        } else if (checkedChipId == R.id.chip_5_rooms) {
            mChipRoomsInput = 5;
        } else {
            mChipRoomsInput = 0;
        }
    }

    /**
     * Verifies which chip of the ChipGroup is checked and defines the mChipPhotoInput's value.
     */
    private void getUserPhotoChoice() {
        int checkedChipId = mBinding.chipGroupPhoto.getCheckedChipId();
        if (checkedChipId == R.id.chip_1_photo) {
            mChipPhotoInput = 1;
        } else if (checkedChipId == R.id.chip_3_photo) {
            mChipPhotoInput = 3;
        } else if (checkedChipId == R.id.chip_5_photo) {
            mChipPhotoInput = 5;
        } else {
            mChipPhotoInput = 0;
        }
    }

    /**
     * Checks the user's preferences and set the TextView's values with the right String.
     */
    private void configureTextViewAccordingToPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String area = preferences.getString("area", "sq ft");
        String currency = preferences.getString("currency", "$");

        if (area.equals("sq ft"))
            mBinding.searchSurfaceTxt.setText(R.string.surface_sqft);
        else
            mBinding.searchSurfaceTxt.setText(R.string.surface_m2);

        if (currency.equals("$"))
            mBinding.searchPriceTxt.setText(R.string.price_dollars);
        else
            mBinding.searchPriceTxt.setText(R.string.price_euros);
    }

    /**
     * Displays a DatePicker to select a date and sets the selected value in the corresponding view.
     */
    private void displayDatePickerAndUpdateUi(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, (datePicker, year1, month1, day1) -> {
            int id = view.getId();
            if (id == R.id.fragment_search_for_sale_txt) {
                mBinding.fragmentSearchForSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
            } else if (id == R.id.fragment_search_sold_txt) {
                mBinding.fragmentSearchSoldTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
            }
        }, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete Date", (dialogInterface, i) -> {
            int id = view.getId();
            if (id == R.id.fragment_search_for_sale_txt) {
                mBinding.fragmentSearchForSaleTxt.setText(null);
            } else if (id == R.id.fragment_search_sold_txt) {
                mBinding.fragmentSearchSoldTxt.setText(null);
            }
        });
        datePickerDialog.show();
    }

    /**
     * Sets the query's content depending to the user's input.
     */
    private void fetchPropertiesAccordingToUserInput() {
        String query = "SELECT * FROM Property WHERE mId > 0";
        if (!TextUtils.isEmpty(mBinding.fragmentSearchZipcodeTxt.getText()))
            query += " AND mZipCode = " + mZipcodeInput;
        if (!TextUtils.isEmpty(mBinding.fragmentSearchCityTxt.getText()))
            query += " AND Property.mCity LIKE " + "'%" + mCityInput + "%'";
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinSurfaceTxt.getText()))
            query += " AND Property.mSurface >= " + mMinSurfaceInput;
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMaxSurfaceTxt.getText()))
            query += " AND Property.mSurface <= " + mMaxSurfaceInput;
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinPriceTxt.getText()))
            query += " AND Property.mPrice >= " + mMinPriceInput;
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMaxPriceTxt.getText()))
            query += " AND Property.mPrice <= " + mMaxPriceInput;
        if (!TextUtils.isEmpty(mBinding.fragmentSearchMinFloorsTxt.getText()))
            query += " AND Property.mFloors >= " + mFloorsInput;
        if (!mTypeInput.equals("()"))
            query += " AND Property.mTypeProperty IN " + mTypeInput;
        if (mAmenitiesInput.contains("School"))
            query += " AND Property.mAmenities LIKE '%School%'";
        if (mAmenitiesInput.contains("Shops"))
            query += " AND Property.mAmenities LIKE '%Shops%'";
        if (mAmenitiesInput.contains("Public transport"))
            query += " AND Property.mAmenities LIKE '%Public transport%'";
        if (mAmenitiesInput.contains("Garden"))
            query += " AND Property.mAmenities LIKE '%Garden%'";
        if (mChipRoomsInput != 0)
            query += " AND Property.mNbRooms >= " + mChipRoomsInput;
        if (mChipBedroomsInput != 0)
            query += " AND Property.mNbBedrooms >= " + mChipBedroomsInput;
        if (mChipBathroomsInput != 0)
            query += " AND Property.mNbBathrooms >= " + mChipBathroomsInput;
        if (mChipCoownerInput != 10)
            query += " AND Property.mCoOwnership = " + mChipCoownerInput;
        if (mChipIsSoldInput != 10)
            query += " AND Property.mSold = " + mChipIsSoldInput;
        if (mForSaleDate != 0)
            query += " AND Property.mInitialSale >= " + mForSaleDate;
        if (mSoldDate != 0)
            query += " AND Property.mFinalSale <= " + mSoldDate;
        if (mChipPhotoInput != 0)
            query += " AND Property.mNbPictures >= " + mChipPhotoInput;
        query += " ;";

        fetchPropertiesAccordingToCriteria(query);
    }

    /**
     * Sets an observer to fetch the properties that matched the user's criteria and displays an error
     * message in a snackbar if there is no result or passes the LiveData's content in a MutableLiveData
     * to the PropertyListFragment.
     */
    private void fetchPropertiesAccordingToCriteria(String query) {
        mRealEstateViewModel.getRealEstateAccordingUserSearch(new SimpleSQLiteQuery(query)).observe(getViewLifecycleOwner(), realEstates -> {
            if (realEstates.isEmpty())
                Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment), getString(R.string.sorry_no_result), Snackbar.LENGTH_SHORT).show();
            else {
                mRealEstateViewModel.addPropertyList(realEstates);
                NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                SearchFragmentDirections.ActionSearchFragmentToPropertyListFragment action =
                        SearchFragmentDirections.actionSearchFragmentToPropertyListFragment();
                action.setOrigin(SEARCH_FRAGMENT);
                mController.navigate(action);
            }
        });
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
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.takeFullScreenFragment();
    }

    void setOnClickListeners() {
        mBinding.fragmentSearchForSaleTxt.setOnClickListener(this::displayDatePickerAndUpdateUi);
        mBinding.fragmentSearchSoldTxt.setOnClickListener(this::displayDatePickerAndUpdateUi);
        mBinding.fragmentSearchSearchBtn.setOnClickListener(v -> {
            getUserInput();
            fetchPropertiesAccordingToUserInput();
        });
    }
}
