package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Converters;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SearchFragment extends Fragment {

    @BindView(R.id.search_price_txt)
    TextView mPriceTxt;
    @BindView(R.id.search_surface_txt)
    TextView mSurfaceTxt;
    @BindView(R.id.chip_apartment)
    Chip mChipApartment;
    @BindView(R.id.chip_loft)
    Chip mChipLoft;
    @BindView(R.id.chip_house)
    Chip mChipHouse;
    @BindView(R.id.chip_villa)
    Chip mChipVilla;
    @BindView(R.id.chip_manor)
    Chip mChipManor;
    @BindView(R.id.chipGroupPhoto)
    ChipGroup mChipGroupPhoto;
    @BindView(R.id.chip_school)
    Chip mChipSchool;
    @BindView(R.id.chip_shop)
    Chip mChipShop;
    @BindView(R.id.chip_transport)
    Chip mChipTransport;
    @BindView(R.id.chip_garden)
    Chip mChipGarden;
    @BindView(R.id.chipGroup_coowner)
    ChipGroup mChipGroupCoowner;
    @BindView(R.id.fragment_search_min_price_txt)
    TextInputEditText mMinPriceTxt;
    @BindView(R.id.fragment_search_max_price_txt)
    TextInputEditText mMaxPriceTxt;
    @BindView(R.id.fragment_search_min_surface_txt)
    TextInputEditText mMinSurfaceTxt;
    @BindView(R.id.fragment_search_max_surface_txt)
    TextInputEditText mMaxSurfaceTxt;
    @BindView(R.id.chipGroup_rooms)
    ChipGroup mChipGroupRooms;
    @BindView(R.id.chipGroup_bedrooms)
    ChipGroup mChipGroupBedrooms;
    @BindView(R.id.chipGroup_bathrooms)
    ChipGroup mChipGroupBathrooms;
    @BindView(R.id.fragment_search_min_floors_txt)
    TextInputEditText mMinFloorsTxt;
    @BindView(R.id.fragment_search_for_sale_txt)
    TextInputEditText mForSaleTxt;
    @BindView(R.id.fragment_search_sold_txt)
    TextInputEditText mSoldTxt;
    @BindView(R.id.fragment_search_zipcode_txt)
    TextInputEditText mZipcodeTxt;
    @BindView(R.id.fragment_search_city_txt)
    TextInputEditText mCityTxt;
    @BindView(R.id.fragment_search_search_btn)
    MaterialButton mSearchBtn;

    private RealEstateViewModel mRealEstateViewModel;

    private Activity mActivity;
    private Unbinder mUnbinder;

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
    private String mTypeInput;
    private String mAmenitiesInput;
    private long mForSaleDate;
    private long mSoldDate;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Search a real estate");
        configureTextViewAccordingToPreference();
        configureViewModel();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getUserInput() {
        if (!TextUtils.isEmpty(mZipcodeTxt.getText()))
            mZipcodeInput = Integer.parseInt(mZipcodeTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mCityTxt.getText()))
            mCityInput = mCityTxt.getText().toString().trim();
        if (!TextUtils.isEmpty(mMinSurfaceTxt.getText()))
            mMinSurfaceInput = Utils.convertAreaAccordingToPreferences(mActivity, mMinSurfaceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mMaxSurfaceTxt.getText()))
            mMaxSurfaceInput = Utils.convertAreaAccordingToPreferences(mActivity, mMaxSurfaceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mMinPriceTxt.getText()))
            mMinPriceInput = Utils.convertPriceAccordingToPreferences(mActivity, mMinPriceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mMaxPriceTxt.getText()))
            mMaxPriceInput = Utils.convertPriceAccordingToPreferences(mActivity, mMaxPriceTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mMinFloorsTxt.getText()))
            mFloorsInput = Integer.parseInt(mMinFloorsTxt.getText().toString().trim());
        if (!TextUtils.isEmpty(mForSaleTxt.getText()))
            mForSaleDate = Converters.dateToTimestamp(Utils.convertStringToDate(mForSaleTxt.getText().toString().trim()));
        if (!TextUtils.isEmpty(mSoldTxt.getText()))
            mSoldDate = Converters.dateToTimestamp(Utils.convertStringToDate(mSoldTxt.getText().toString().trim()));

        getUserPhotoChoice();
        getUserRoomChoice();
        getUserBedroomsChoice();
        getUserBathroomsChoice();
        getUserCoownerChoice();
        getUserTypeChoice();
        mAmenitiesInput = Utils.getUserAmenitiesChoice(mChipSchool, mChipShop, mChipTransport, mChipGarden);


    }

    private void getUserTypeChoice() {
        mTypeInput = "(";
        if (mChipApartment.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput = mTypeInput + "'Apartment'";
        }
        if (mChipLoft.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Loft'";
        }
        if (mChipHouse.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'House'";
        }
        if (mChipVilla.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Villa'";
        }
        if (mChipManor.isChecked()) {
            if (!mTypeInput.equals("("))
                mTypeInput += ", ";
            mTypeInput += "'Manor'";
        }
        mTypeInput += ")";
    }

    private void getUserCoownerChoice() {
        switch (mChipGroupCoowner.getCheckedChipId()) {
            case R.id.chip_yes:
                mChipCoownerInput = 1;
                break;
            case R.id.chip_no:
                mChipCoownerInput = 0;
                break;
            default:
                mChipCoownerInput = 10;
                break;
        }
    }

    private void getUserBathroomsChoice() {
        switch (mChipGroupBathrooms.getCheckedChipId()) {
            case R.id.chip_1_bathroom:
                mChipBathroomsInput = 1;
                break;
            case R.id.chip_2_bathrooms:
                mChipBathroomsInput = 2;
                break;
            case R.id.chip_3_bathrooms:
                mChipBathroomsInput = 3;
                break;
            case R.id.chip_4_bathrooms:
                mChipBathroomsInput = 4;
                break;
            case R.id.chip_5_bathrooms:
                mChipBathroomsInput = 5;
                break;
            default:
                mChipBathroomsInput = 0;
                break;
        }
    }

    private void getUserBedroomsChoice() {
        switch (mChipGroupBedrooms.getCheckedChipId()) {
            case R.id.chip_1_bedroom:
                mChipBedroomsInput = 1;
                break;
            case R.id.chip_2_bedrooms:
                mChipBedroomsInput = 2;
                break;
            case R.id.chip_3_bedrooms:
                mChipBedroomsInput = 3;
                break;
            case R.id.chip_4_bedrooms:
                mChipBedroomsInput = 4;
                break;
            case R.id.chip_5_bedrooms:
                mChipBedroomsInput = 5;
                break;
            default:
                mChipBedroomsInput = 0;
                break;
        }
    }

    private void getUserRoomChoice() {
        switch (mChipGroupRooms.getCheckedChipId()) {
            case R.id.chip_1_room:
                mChipRoomsInput = 1;
                break;
            case R.id.chip_2_rooms:
                mChipRoomsInput = 2;
                break;
            case R.id.chip_3_rooms:
                mChipRoomsInput = 3;
                break;
            case R.id.chip_4_rooms:
                mChipRoomsInput = 4;
                break;
            case R.id.chip_5_rooms:
                mChipRoomsInput = 5;
                break;
            default:
                mChipRoomsInput = 0;
                break;
        }
    }

    private void getUserPhotoChoice() {
        switch (mChipGroupPhoto.getCheckedChipId()) {
            case R.id.chip_1_photo:
                mChipPhotoInput = 1;
                break;
            case R.id.chip_3_photo:
                mChipPhotoInput = 3;
                break;
            case R.id.chip_5_photo:
                mChipPhotoInput = 5;
                break;
            default:
                mChipPhotoInput = 0;
                break;
        }
    }

    private void configureTextViewAccordingToPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String area = preferences.getString("area", "sq ft");
        String currency = preferences.getString("currency", "$");

        if (area.equals("sq ft"))
            mSurfaceTxt.setText(R.string.surface_sqft);
        else
            mSurfaceTxt.setText(R.string.surface_m2);

        if (currency.equals("$"))
            mPriceTxt.setText(R.string.price_dollars);
        else
            mPriceTxt.setText(R.string.price_euros);
    }

    private void displayDatePickerAndUpdateUi(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, (datePicker, year1, month1, day1) -> {
            switch (view.getId()) {
                case R.id.fragment_search_for_sale_txt:
                    mForSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
                    break;
                case R.id.fragment_search_sold_txt:
                    mSoldTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
                    break;
            }
        }, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete Date", (dialogInterface, i) -> {
            switch (view.getId()) {
                case R.id.fragment_search_for_sale_txt:
                    mForSaleTxt.setText(null);
                    break;
                case R.id.fragment_search_sold_txt:
                    mSoldTxt.setText(null);
                    break;
            }
        });
        datePickerDialog.show();
    }

    private void fetchRealEstateAccordingToUserInput() {
        String query = "SELECT * FROM RealEstate WHERE mId > 0";
        if (!TextUtils.isEmpty(mZipcodeTxt.getText()))
            query += " AND mZipCode = " + mZipcodeInput;
        if (!TextUtils.isEmpty(mCityTxt.getText()))
            query += " AND RealEstate.mCity LIKE " + "'%" + mCityInput + "%'";
        if (!TextUtils.isEmpty(mMinSurfaceTxt.getText()))
            query += " AND RealEstate.mSurface >= " + mMinSurfaceInput;
        if (!TextUtils.isEmpty(mMaxSurfaceTxt.getText()))
            query += " AND RealEstate.mSurface <= " + mMaxSurfaceInput;
        if (!TextUtils.isEmpty(mMinPriceTxt.getText()))
            query += " AND RealEstate.mPrice >= " + mMinPriceInput;
        if (!TextUtils.isEmpty(mMaxPriceTxt.getText()))
            query += " AND RealEstate.mPrice <= " + mMaxPriceInput;
        if (!TextUtils.isEmpty(mMinFloorsTxt.getText()))
            query += " AND RealEstate.mFloors >= " + mFloorsInput;
        if (!mTypeInput.equals("()"))
            query += " AND RealEstate.mTypeProperty IN " + mTypeInput;
        if (mAmenitiesInput.contains("School"))
            query += " AND RealEstate.mAmenities LIKE '%School%'";
        if (mAmenitiesInput.contains("Shops"))
            query += " AND RealEstate.mAmenities LIKE '%Shops%'";
        if (mAmenitiesInput.contains("Public transport"))
            query += " AND RealEstate.mAmenities LIKE '%Public transport%'";
        if (mAmenitiesInput.contains("Garden"))
            query += " AND RealEstate.mAmenities LIKE '%Garden%'";
        if (mChipRoomsInput != 0)
            query += " AND RealEstate.mNbRooms >= " + mChipRoomsInput;
        if (mChipBedroomsInput != 0)
            query += " AND RealEstate.mNbBedrooms >= " + mChipBedroomsInput;
        if (mChipBathroomsInput != 0)
            query += " AND RealEstate.mNbBathrooms >= " + mChipBathroomsInput;
        if (mChipCoownerInput != 10)
            query += " AND RealEstate.mCoOwnership = " + mChipCoownerInput;
        if (mForSaleDate != 0)
            query += " AND RealEstate.mInitialSale >= " + mForSaleDate;
        if (mSoldDate != 0)
            query += " AND RealEstate.mFinalSale <= " + mSoldDate;
        if (mChipPhotoInput != 0)
            query += " AND RealEstate.mNbPictures >= " + mChipPhotoInput;

        query += " ;";

        Log.i("test", "fetchRealEstateAccordingToUserInput: " + query);

        mRealEstateViewModel.getRealEstateAccordingUserSearch(new SimpleSQLiteQuery(query)).observe(getViewLifecycleOwner(), realEstates -> {
            if (realEstates.isEmpty())
                Snackbar.make(mActivity.findViewById(R.id.activity_main_container), "Sorry, there is no results", Snackbar.LENGTH_SHORT).show();
            else {
                mRealEstateViewModel.addRealEstateList(realEstates);
                if (mListener != null) {
                    mListener.passSearchedRealEstate();
                }
            }
        });
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
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.fragment_search_for_sale_txt, R.id.fragment_search_sold_txt, R.id.fragment_search_search_btn})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_for_sale_txt:
                displayDatePickerAndUpdateUi(view);
                break;
            case R.id.fragment_search_sold_txt:
                displayDatePickerAndUpdateUi(view);
                break;
            case R.id.fragment_search_search_btn:
                getUserInput();
                fetchRealEstateAccordingToUserInput();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void passSearchedRealEstate();
    }
}
