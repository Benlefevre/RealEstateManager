package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.DetailsPhotoAdapter;
import com.openclassrooms.realestatemanager.ui.viewholder.PicturesDetailsViewHolder;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA;


public class AddRealEstateFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String REAL_ESTATE_ID = "realEstateId";

    /*--------------------------------------------------------------------------------------------------
                                            VIEWS
     _________________________________________________________________________________________________*/
    @BindView(R.id.fragment_add_type_property_spinner)
    AppCompatAutoCompleteTextView mTypePropertySpinner;
    @BindView(R.id.fragment_add_price_txt)
    TextInputEditText mPriceTxt;
    @BindView(R.id.fragment_add_surface_txt)
    TextInputEditText mSurfaceTxt;
    @BindView(R.id.fragment_add_rooms_txt)
    TextInputEditText mRoomsTxt;
    @BindView(R.id.fragment_add_bedrooms_txt)
    TextInputEditText mBedroomsTxt;
    @BindView(R.id.fragment_add_bathrooms_txt)
    TextInputEditText mBathroomsTxt;
    @BindView(R.id.fragment_add_floors_txt)
    TextInputEditText mFloorsTxt;
    @BindView(R.id.fragment_add_description_txt)
    TextInputEditText mDescriptionTxt;
    @BindView(R.id.fragment_add_country_code_spinner)
    AppCompatAutoCompleteTextView mCountryCodeSpinner;
    @BindView(R.id.fragment_add_year_construction_txt)
    TextInputEditText mYearConstructionTxt;
    @BindView(R.id.fragment_add_address_txt)
    TextInputEditText mAddAddressTxt;
    @BindView(R.id.fragment_add_zipcode_txt)
    TextInputEditText mZipcodeTxt;
    @BindView(R.id.fragment_add_city_txt)
    TextInputEditText mCityTxt;
    @BindView(R.id.fragment_add_initial_sale_txt)
    TextInputEditText mInitialSaleTxt;
    @BindView(R.id.fragment_add_final_sale_txt)
    TextInputEditText mFinalSaleTxt;
    @BindView(R.id.fragment_add_co_ownership_spinner)
    AppCompatAutoCompleteTextView mCoOwnershipSpinner;
    @BindView(R.id.fragment_add_agent_spinner)
    AppCompatAutoCompleteTextView mAgentSpinner;
    @BindView(R.id.fragment_add_validation_btn)
    MaterialButton mValidationBtn;
    @BindView(R.id.fragment_add_pictures_txt)
    TextInputEditText mPicturesTxt;
    @BindView(R.id.fragment_add_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_price)
    TextInputLayout mLayoutPrice;
    @BindView(R.id.layout_surface)
    TextInputLayout mLayoutSurface;
    @BindView(R.id.chip_school)
    Chip mChipSchool;
    @BindView(R.id.chip_shop)
    Chip mChipShop;
    @BindView(R.id.chip_transport)
    Chip mChipTransport;
    @BindView(R.id.chip_garden)
    Chip mChipGarden;
    @BindView(R.id.layout_address)
    TextInputLayout mLayoutAddress;
    @BindView(R.id.layout_zipcode)
    TextInputLayout mLayoutZipcode;
    @BindView(R.id.layout_city)
    TextInputLayout mLayoutCity;

    private Unbinder mUnbinder;

    private RealEstate mRealEstate;
    private String mTypePropertyInput;
    private String mCountryCodeInput;
    private String mAgentInput;
    private String mCoOwnershipInput;
    private int price = 0;
    private int surface = 0;
    private int nbRooms = 0;
    private int nbBedrooms = 0;
    private int nbBathrooms = 0;
    private String description = null;
    private String address = null;
    private int zipCode = 0;
    private String city = null;
    private String amenities = null;
    private Date initialDate = null;
    private Date finalDate = null;
    private Date yearConstruction = null;
    private boolean isSold = false;
    private int floors = 0;
    private double mLatitude = 0;
    private double mLongitude = 0;
    private int mNbPictures;

    private String currentPhotoPath;
    private long mRealEstateId;
    private List<Pictures> mPicturesList;
    private DetailsPhotoAdapter mPhotoAdapter;

    private Activity mActivity;
    private RealEstateViewModel mRealEstateViewModel;

    public AddRealEstateFragment() {
        // Required empty public constructor
    }

    public static AddRealEstateFragment newInstance() {
        return new AddRealEstateFragment();
    }

    public static AddRealEstateFragment newInstance(long realEstateId) {
        AddRealEstateFragment fragment = new AddRealEstateFragment();
        Bundle args = new Bundle();
        args.putLong(REAL_ESTATE_ID, realEstateId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mRealEstateId = getArguments().getLong(REAL_ESTATE_ID);
        mPicturesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_real_estate, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        if (mRealEstateId == 0)
            toolbar.setTitle("Add a real estate");
        else
            toolbar.setTitle("Edit the real estate");
        configureViewModel();
        configureSpinner();
        configureRecyclerView();
        configureTextLayoutHintAccordingToPreferences();
        if (mRealEstateId != 0)
            fetchDetailsAndPicturesAccordingToRealEstateId(mRealEstateId);
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void fetchDetailsAndPicturesAccordingToRealEstateId(long realEstateId) {
        mRealEstateViewModel.getRealEstate(realEstateId).observe(getViewLifecycleOwner(), this::updateUiAfterFetchRealEstate);
        mRealEstateViewModel.getPictures(realEstateId).observe(getViewLifecycleOwner(), this::bindPicturesIntoRecyclerViewAfterFetch);
    }

    private void bindPicturesIntoRecyclerViewAfterFetch(List<Pictures> pictures) {
        mPicturesList.clear();
        for (Pictures pictures1 : pictures) {
            if (!mPicturesList.contains(pictures1))
                mPicturesList.add(pictures1);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mPhotoAdapter.notifyDataSetChanged();
    }

    private void updateUiAfterFetchRealEstate(RealEstate realEstate) {
        mRealEstate = realEstate;
        mTypePropertySpinner.setText(mRealEstate.getTypeProperty(), false);
        mTypePropertyInput = mTypePropertySpinner.getText().toString();
        mCoOwnershipSpinner.setText(String.valueOf(mRealEstate.isCoOwnership()), false);
        mCoOwnershipInput = mCoOwnershipSpinner.getText().toString();
        mAgentSpinner.setText(mRealEstate.getRealEstateAgent(), false);
        mAgentInput = mAgentSpinner.getText().toString();
        mDescriptionTxt.setText(mRealEstate.getDescription());
        mAddAddressTxt.setText(mRealEstate.getAddress());
        mCityTxt.setText(mRealEstate.getCity());
        mFloorsTxt.setText(String.valueOf(mRealEstate.getFloors()));
        if (mRealEstate.getCountryCode() != null)
            mCountryCodeSpinner.setText(getCountryAccordingToCountryCode(mRealEstate.getCountryCode()), false);
        if (mCountryCodeSpinner.getText() != null && mCountryCodeSpinner.getText().length() != 0)
            mCountryCodeInput = getCountryCodeAccordingToCountry(mCountryCodeSpinner.getText().toString());
        if (mRealEstate.getPrice() != 0)
            mPriceTxt.setText(String.valueOf(Utils.convertPriceAccordingToPreferenceToEdit(mActivity, mRealEstate.getPrice())));
        if (mRealEstate.getSurface() != 0)
            mSurfaceTxt.setText(String.valueOf(Utils.convertAreaAccordingToPreferencesToEdit(mActivity, mRealEstate.getSurface())));
        if (mRealEstate.getNbRooms() != 0)
            mRoomsTxt.setText(String.valueOf(mRealEstate.getNbRooms()));
        if (mRealEstate.getNbBedrooms() != 0)
            mBedroomsTxt.setText(String.valueOf(mRealEstate.getNbBedrooms()));
        if (mRealEstate.getNbBathrooms() != 0)
            mBathroomsTxt.setText(String.valueOf(mRealEstate.getNbBathrooms()));
        if (mRealEstate.getZipCode() != 0)
            mZipcodeTxt.setText(String.valueOf(mRealEstate.getZipCode()));
        if (mRealEstate.getInitialSale() != null)
            mInitialSaleTxt.setText(Utils.convertDateToString(mRealEstate.getInitialSale(), mActivity));
        if (mRealEstate.getFinalSale() != null)
            mFinalSaleTxt.setText(Utils.convertDateToString(mRealEstate.getFinalSale(), mActivity));
        if (mRealEstate.getYearConstruction() != null)
            mYearConstructionTxt.setText(Utils.convertDateToString(mRealEstate.getYearConstruction(), mActivity));
        if (mRealEstate.getAmenities() != null) {
            if (mRealEstate.getAmenities().contains("School"))
                mChipSchool.setChecked(true);
            if (mRealEstate.getAmenities().contains("Shops"))
                mChipShop.setChecked(true);
            if (mRealEstate.getAmenities().contains("Public transport"))
                mChipTransport.setChecked(true);
            if (mRealEstate.getAmenities().contains("Garden"))
                mChipGarden.setChecked(true);
        }
    }

    private void configureSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(mActivity, R.array.typeProperty,
                R.layout.dropdown_item_spinner);
        mTypePropertySpinner.setAdapter(typeAdapter);
        mTypePropertySpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(mActivity, R.array.countryCode,
                R.layout.dropdown_item_spinner);
        mCountryCodeSpinner.setAdapter(countryAdapter);
        mCountryCodeSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> agentAdapter = ArrayAdapter.createFromResource(mActivity, R.array.agent,
                R.layout.dropdown_item_spinner);
        mAgentSpinner.setAdapter(agentAdapter);
        mAgentSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> coOwnershipAdapter = ArrayAdapter.createFromResource(mActivity, R.array.co_ownership,
                R.layout.dropdown_item_spinner);
        mCoOwnershipSpinner.setAdapter(coOwnershipAdapter);
        mCoOwnershipSpinner.setOnItemClickListener(this);
    }

    private void configureRecyclerView() {
        mRecyclerView.setVisibility(View.GONE);
        mPhotoAdapter = new DetailsPhotoAdapter(mPicturesList, 2);
        mPhotoAdapter.setOnClickListener(view -> {
            PicturesDetailsViewHolder holder = (PicturesDetailsViewHolder) view.getTag();
            int position = holder.getAdapterPosition();
            new MaterialAlertDialogBuilder(mActivity)
                    .setTitle(getString(R.string.delete_picture))
                    .setMessage(getString(R.string.would_you_delete_picture))
                    .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton(getString(R.string.delete), (dialogInterface, i) -> {
                        if (mRealEstateId == 0) {
                            mPicturesList.remove(position);
                        } else {
                            mRealEstateViewModel.deletePicture(mPicturesList.get(position));
                            mPicturesList.remove(position);
                        }
                        mPhotoAdapter.notifyDataSetChanged();
                        if (mPicturesList.size() == 0)
                            mRecyclerView.setVisibility(View.GONE);
                    })
                    .show();
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    private void configureTextLayoutHintAccordingToPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String area = preferences.getString("area", "sq ft");
        String currency = preferences.getString("currency", "$");

        if (area.equals("sq ft"))
            mLayoutSurface.setHint("Enter the surface in sq ft");
        else
            mLayoutSurface.setHint("Enter the surface in m²");

        if (currency.equals("$"))
            mLayoutPrice.setHint("Enter the price in Dollars");
        else
            mLayoutPrice.setHint("Enter the price in Euros");
    }

    private void updateRealEstateWithNewValues() {
        getTheUserInput();
        mNbPictures = mPicturesList.size();
        updateCurrentRealEstate(mTypePropertyInput, price, surface, nbRooms, nbBedrooms, nbBathrooms,
                description, address, zipCode, mCountryCodeInput, mLatitude, mLongitude, city, amenities, isSold, initialDate,
                finalDate, mAgentInput, yearConstruction, floors, Boolean.valueOf(mCoOwnershipInput), mNbPictures);
        savePictureInDb(mRealEstateId);
        int nbRows = mRealEstateViewModel.updateRealEstate(mRealEstate);
        if (nbRows == 1) {
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container),
                    getString(R.string.update_success), Snackbar.LENGTH_LONG).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container),
                    getString(R.string.update_failed), Snackbar.LENGTH_LONG).show();
    }

    private void createNewRealEstateFromInputValues() {
        getTheUserInput();
        mNbPictures = mPicturesList.size();
        long rowId = mRealEstateViewModel.createRealEstate(new RealEstate(mTypePropertyInput, price, surface, nbRooms,
                nbBedrooms, nbBathrooms, description, address, zipCode, mCountryCodeInput, mLatitude,
                mLongitude, city, amenities, isSold, initialDate, finalDate, mAgentInput, yearConstruction,
                floors, Boolean.valueOf(mCoOwnershipInput), mNbPictures));
        if (rowId != -1L) {
            savePictureInDb(rowId);
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container), getString(R.string.save_success),
                    Snackbar.LENGTH_LONG).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container), getString(R.string.save_failed), Snackbar.LENGTH_LONG).show();
    }

    private void savePictureInDb(long realEstateId) {
        for (Pictures pictures : mPicturesList) {
            pictures.setRealEstateId(realEstateId);
            mRealEstateViewModel.createPictures(pictures);
        }
    }

    private void getTheUserInput() {
        if (!TextUtils.isEmpty(mPriceTxt.getText()))
            price = Utils.convertPriceAccordingToPreferences(mActivity, mPriceTxt.getText().toString());
        if (!TextUtils.isEmpty(mSurfaceTxt.getText()))
            surface = Utils.convertAreaAccordingToPreferences(mActivity, mSurfaceTxt.getText().toString());
        if (!TextUtils.isEmpty(mRoomsTxt.getText()))
            nbRooms = Integer.parseInt(mRoomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBedroomsTxt.getText()))
            nbBedrooms = Integer.parseInt(mBedroomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBathroomsTxt.getText()))
            nbBathrooms = Integer.parseInt(mBathroomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mDescriptionTxt.getText()))
            description = mDescriptionTxt.getText().toString();
        if (!TextUtils.isEmpty(mAddAddressTxt.getText())) {
            address = mAddAddressTxt.getText().toString();
//            getLocation(address);
        }
        if (!TextUtils.isEmpty(mZipcodeTxt.getText()))
            zipCode = Integer.parseInt(mZipcodeTxt.getText().toString());
        if (!TextUtils.isEmpty(mCityTxt.getText()))
            city = mCityTxt.getText().toString();
        if (!TextUtils.isEmpty(mFloorsTxt.getText()))
            floors = Integer.parseInt(mFloorsTxt.getText().toString());
        if (!TextUtils.isEmpty(mInitialSaleTxt.getText()))
            initialDate = Utils.convertStringToDate(Objects.requireNonNull(mInitialSaleTxt.getText()).toString());
        if (!TextUtils.isEmpty(mFinalSaleTxt.getText())) {
            finalDate = Utils.convertStringToDate(Objects.requireNonNull(mFinalSaleTxt.getText()).toString());
            isSold = true;
        } else
            isSold = false;
        if (!TextUtils.isEmpty(mYearConstructionTxt.getText()))
            yearConstruction = Utils.convertStringToDate(Objects.requireNonNull(mYearConstructionTxt.getText()).toString());
        amenities = Utils.getUserAmenitiesChoice(mChipSchool, mChipShop, mChipTransport, mChipGarden);

        if (address != null && zipCode != 0 && city != null) {
            if (Utils.isInternetAvailable(mActivity))
                getLocation(address + " " + zipCode + " " + city);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == mTypePropertySpinner.getAdapter()) {
            mTypePropertyInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mCountryCodeSpinner.getAdapter()) {
            mCountryCodeInput = getCountryCodeAccordingToCountry(adapterView.getItemAtPosition(i).toString());
        } else if (adapterView.getAdapter() == mAgentSpinner.getAdapter()) {
            mAgentInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mCoOwnershipSpinner.getAdapter()) {
            mCoOwnershipInput = adapterView.getItemAtPosition(i).toString();
        }
    }

    private void updateCurrentRealEstate(String typePropertyInput, int price, int surface, int nbRooms,
                                         int nbBedrooms, int nbBathrooms, String description, String address,
                                         int zipCode, String countryCodeInput, double latitude, double longitude, String city, String amenities,
                                         boolean isSold, Date initialDate, Date finalDate, String agentInput,
                                         Date yearConstruction, int floors, Boolean coownership, int nbPictures) {
        mRealEstate.setTypeProperty(typePropertyInput);
        mRealEstate.setPrice(price);
        mRealEstate.setSurface(surface);
        mRealEstate.setNbRooms(nbRooms);
        mRealEstate.setNbBedrooms(nbBedrooms);
        mRealEstate.setNbBathrooms(nbBathrooms);
        mRealEstate.setDescription(description);
        mRealEstate.setAddress(address);
        mRealEstate.setZipCode(zipCode);
        mRealEstate.setCountryCode(countryCodeInput);
        mRealEstate.setLatitude(latitude);
        mRealEstate.setLongitude(longitude);
        mRealEstate.setCity(city);
        mRealEstate.setAmenities(amenities);
        mRealEstate.setSold(isSold);
        mRealEstate.setInitialSale(initialDate);
        mRealEstate.setFinalSale(finalDate);
        mRealEstate.setRealEstateAgent(agentInput);
        mRealEstate.setYearConstruction(yearConstruction);
        mRealEstate.setFloors(floors);
        mRealEstate.setCoOwnership(coownership);
        mRealEstate.setNbPictures(nbPictures);
    }

    private String getCountryCodeAccordingToCountry(String country) {
        switch (country) {
            case "FRANCE":
                return "FR";
            case "UNITED STATE":
                return "US";
            case "BELGIUM":
                return "BE";
            case "CANADA":
                return "CA";
            case "LUXEMBOURG":
                return "LU";
            case "GREAT BRITAIN":
                return "GB";
            default:
                return null;
        }
    }

    private String getCountryAccordingToCountryCode(String countryCode) {
        switch (countryCode) {
            case "FR":
                return "FRANCE";
            case "US":
                return "UNITED STATE";
            case "BE":
                return "BELGIUM";
            case "CA":
                return "CANADA";
            case "LU":
                return "LUXEMBOURG";
            case "GB":
                return "GREAT BRITAIN";
            default:
                return null;
        }
    }

    private void getLocation(String address) {
        Geocoder geocoder = new Geocoder(mActivity);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            for (Address address1 : addresses) {
                mLatitude = address1.getLatitude();
                mLongitude = address1.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayDatePickerAndUpdateUi(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, (datePicker, year1, month1, day1) -> {
            switch (view.getId()) {
                case R.id.fragment_add_initial_sale_txt:
                    mInitialSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
                    break;
                case R.id.fragment_add_final_sale_txt:
                    mFinalSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
                    break;
                case R.id.fragment_add_year_construction_txt:
                    mYearConstructionTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
                    break;
            }
        }, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete Date", (dialogInterface, i) -> {
            switch (view.getId()) {
                case R.id.fragment_add_initial_sale_txt:
                    mInitialSaleTxt.setText(null);
                    break;
                case R.id.fragment_add_final_sale_txt:
                    mFinalSaleTxt.setText(null);
                    break;
                case R.id.fragment_add_year_construction_txt:
                    mYearConstructionTxt.setText(null);
                    break;
            }
        });
        datePickerDialog.show();
    }

    @OnClick({R.id.fragment_add_year_construction_txt, R.id.fragment_add_initial_sale_txt,
            R.id.fragment_add_final_sale_txt, R.id.fragment_add_validation_btn, R.id.fragment_add_pictures_txt})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_add_year_construction_txt:
                displayDatePickerAndUpdateUi(view);
                break;
            case R.id.fragment_add_initial_sale_txt:
                displayDatePickerAndUpdateUi(view);
                break;
            case R.id.fragment_add_final_sale_txt:
                displayDatePickerAndUpdateUi(view);
                break;
            case R.id.fragment_add_validation_btn:
                if (mRealEstateId == 0) {
                    if (verifyAddressInputForGeoCoding())
                        createNewRealEstateFromInputValues();
                } else {
                    if (verifyAddressInputForGeoCoding())
                        updateRealEstateWithNewValues();
                }
                break;
            case R.id.fragment_add_pictures_txt:
                getPermissionsExternalStorageAndCamera();
        }
    }

    private boolean verifyAddressInputForGeoCoding() {
        if (!TextUtils.isEmpty(mAddAddressTxt.getText()) && (TextUtils.isEmpty(mZipcodeTxt.getText()) || TextUtils.isEmpty(mCityTxt.getText()))) {
            if (TextUtils.isEmpty(mZipcodeTxt.getText()))
                mLayoutZipcode.setError("Please enter the zipcode");
            if (TextUtils.isEmpty(mCityTxt.getText()))
                mLayoutCity.setError("Please enter the city");
            return false;
        } else if (!TextUtils.isEmpty(mZipcodeTxt.getText()) && (TextUtils.isEmpty(mAddAddressTxt.getText()) || TextUtils.isEmpty(mCityTxt.getText()))) {
            if (TextUtils.isEmpty(mAddAddressTxt.getText()))
                mLayoutAddress.setError("Please enter the address");
            if (TextUtils.isEmpty(mCityTxt.getText()))
                mLayoutCity.setError("Please enter the city");
            return false;
        } else if (!TextUtils.isEmpty(mCityTxt.getText()) && (TextUtils.isEmpty(mAddAddressTxt.getText()) || TextUtils.isEmpty(mZipcodeTxt.getText()))) {
            if (TextUtils.isEmpty(mAddAddressTxt.getText()))
                mLayoutAddress.setError("Please enter the address");
            if (TextUtils.isEmpty(mZipcodeTxt.getText()))
                mLayoutZipcode.setError("Please enter the zipcode");
            return false;
        } else
            return true;
    }


    @AfterPermissionGranted(READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA)
    private void getPermissionsExternalStorageAndCamera() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(mActivity, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.storage_rationale),
                    READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA, perms);
        }else
            openDialogToFetchPictures();
    }

    private void openDialogToFetchPictures() {
        new MaterialAlertDialogBuilder(mActivity)
                .setTitle("By which means would you like to add a picture?")
                .setMessage("You can either take a picture from the camera of this camera or choose a picture stored on the camera's memory")
                .setNegativeButton(getString(R.string.camera), (dialogInterface, i) -> openCamera())
                .setNeutralButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.memory), (dialogInterface, i) -> pickImageFromGallery())
                .setCancelable(false)
                .show();
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Opens the device's camera to take a picture and fetch the file's path
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
            }
        }
    }

    /**
     * Opens an App to pick a picture and fetch the file's path
     */
    private void pickImageFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    private void savePictureCaption(String uri) {
        View customDialog = mActivity.getLayoutInflater().inflate(R.layout.dialog_pictures_validation, null);
        ImageView imageView = customDialog.findViewById(R.id.dialog_picture_preview_img);
        imageView.setImageURI(Uri.parse(uri));
        TextInputEditText editText = customDialog.findViewById(R.id.dialog_layout_title_txt);
        TextInputLayout textInputLayout = customDialog.findViewById(R.id.dialog_layout_title);
        MaterialButton negativeBtn = customDialog.findViewById(R.id.dialog_layout_negative_btn);
        MaterialButton positiveBtn = customDialog.findViewById(R.id.dialog_layout_positive_btn);

        AlertDialog builder = new MaterialAlertDialogBuilder(mActivity)
                .setView(customDialog)
                .setTitle(getString(R.string.save_picture_dialog))
                .setCancelable(false)
                .show();

        negativeBtn.setOnClickListener(view -> builder.cancel());
        positiveBtn.setOnClickListener(view -> {
            if (editText.getText() != null && editText.getText().length() == 0) {
                textInputLayout.setError("Please enter the picture's caption");
            } else {
                textInputLayout.setError(null);
                mPicturesList.add(new Pictures(Uri.parse(uri), editText.getText().toString()));
                builder.cancel();
                mRecyclerView.setVisibility(View.VISIBLE);
                mPhotoAdapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            savePictureCaption(currentPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            savePictureCaption(path);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mActivity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null);
        mPhotoAdapter.setOnClickListener(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
