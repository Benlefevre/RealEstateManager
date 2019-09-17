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
import android.widget.VideoView;

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
import com.openclassrooms.realestatemanager.data.entities.Property;
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
import static com.openclassrooms.realestatemanager.utils.Constants.ADD_REAL_ESTATE_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.MOVIE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.MOVIE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA;


public class AddPropertyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String REAL_ESTATE_ID = "realEstateId";

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

    private Property mProperty;
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
    private String currentVideoPath;
    private long mRealEstateId;
    private List<Pictures> mPicturesList;
    private DetailsPhotoAdapter mPhotoAdapter;

    private Activity mActivity;
    private RealEstateViewModel mRealEstateViewModel;
    private Unbinder mUnbinder;

    private AddPropertyFragment.OnFragmentInteractionListener mListener;

    public AddPropertyFragment() {
        // Required empty public constructor
    }

    public static AddPropertyFragment newInstance() {
        return new AddPropertyFragment();
    }

    public static AddPropertyFragment newInstance(long realEstateId) {
        AddPropertyFragment fragment = new AddPropertyFragment();
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
//        Restores the values of currentPhotoPath and currentVideoPath to prevent the loss of values
//        during the rotation of the device.
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString("photoFile", null);
            currentVideoPath = savedInstanceState.getString("movieFile", null);
        }
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
            toolbar.setTitle(getString(R.string.add_a_real_estate));
        else
            toolbar.setTitle(getString(R.string.edit_the_real_estate));
        configureViewModel();
        configureSpinner();
        configureRecyclerView();
        configureTextLayoutHintAccordingToPreferences();
//        If this fragment is used to edit a property, it calls the following method to fetch the property's attributes.
        if (mRealEstateId != 0)
            fetchDetailsAndPicturesAccordingToRealEstateId(mRealEstateId);
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(mActivity);
        mRealEstateViewModel = ViewModelProviders.of((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
    }

    /**
     * Configures observers to fetch the property's attributes and the property's pictures.
     */
    private void fetchDetailsAndPicturesAccordingToRealEstateId(long realEstateId) {
        mRealEstateViewModel.getRealEstate(realEstateId).observe(getViewLifecycleOwner(), this::updateUiAfterFetchRealEstate);
        mRealEstateViewModel.getPictures(realEstateId).observe(getViewLifecycleOwner(), this::bindPicturesIntoRecyclerViewAfterFetch);
    }

    /**
     * Sets the LiveData values in mPicturesList and notifies the RecyclerView adapter that the data changed
     */
    private void bindPicturesIntoRecyclerViewAfterFetch(List<Pictures> pictures) {
        mPicturesList.clear();
        for (Pictures pictures1 : pictures) {
            if (!mPicturesList.contains(pictures1))
                mPicturesList.add(pictures1);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the LiveData values in mProperty and binds mProperty's attributes into the dedicated views.
     */
    private void updateUiAfterFetchRealEstate(Property property) {
        mProperty = property;
        mTypePropertySpinner.setText(mProperty.getTypeProperty(), false);
        mTypePropertyInput = mTypePropertySpinner.getText().toString();
        mCoOwnershipSpinner.setText(String.valueOf(mProperty.isCoOwnership()), false);
        mCoOwnershipInput = mCoOwnershipSpinner.getText().toString();
        mAgentSpinner.setText(mProperty.getRealEstateAgent(), false);
        mAgentInput = mAgentSpinner.getText().toString();
        mDescriptionTxt.setText(mProperty.getDescription());
        mAddAddressTxt.setText(mProperty.getAddress());
        mCityTxt.setText(mProperty.getCity());
        mFloorsTxt.setText(String.valueOf(mProperty.getFloors()));
//        We use displayed values more user friendly so we use methods to find the existing value in the database
        if (mProperty.getCountryCode() != null)
            mCountryCodeSpinner.setText(getCountryAccordingToCountryCode(mProperty.getCountryCode()), false);
        if (mCountryCodeSpinner.getText() != null && mCountryCodeSpinner.getText().length() != 0)
            mCountryCodeInput = getCountryCodeAccordingToCountry(mCountryCodeSpinner.getText().toString());
//        We use methods to convert the values in database into values corresponding to the user's preferences
        if (mProperty.getPrice() != 0)
            mPriceTxt.setText(String.valueOf(Utils.convertPriceAccordingToPreferenceToEdit(mActivity, mProperty.getPrice())));
        if (mProperty.getSurface() != 0)
            mSurfaceTxt.setText(String.valueOf(Utils.convertAreaAccordingToPreferencesToEdit(mActivity, mProperty.getSurface())));
        if (mProperty.getNbRooms() != 0)
            mRoomsTxt.setText(String.valueOf(mProperty.getNbRooms()));
        if (mProperty.getNbBedrooms() != 0)
            mBedroomsTxt.setText(String.valueOf(mProperty.getNbBedrooms()));
        if (mProperty.getNbBathrooms() != 0)
            mBathroomsTxt.setText(String.valueOf(mProperty.getNbBathrooms()));
        if (mProperty.getZipCode() != 0)
            mZipcodeTxt.setText(String.valueOf(mProperty.getZipCode()));
//        We convert the dates in database into String values
        if (mProperty.getInitialSale() != null)
            mInitialSaleTxt.setText(Utils.convertDateToString(mProperty.getInitialSale(), mActivity));
        if (mProperty.getFinalSale() != null)
            mFinalSaleTxt.setText(Utils.convertDateToString(mProperty.getFinalSale(), mActivity));
        if (mProperty.getYearConstruction() != null)
            mYearConstructionTxt.setText(Utils.convertDateToString(mProperty.getYearConstruction(), mActivity));
        if (mProperty.getAmenities() != null) {
            if (mProperty.getAmenities().contains("School"))
                mChipSchool.setChecked(true);
            if (mProperty.getAmenities().contains("Shops"))
                mChipShop.setChecked(true);
            if (mProperty.getAmenities().contains("Public transport"))
                mChipTransport.setChecked(true);
            if (mProperty.getAmenities().contains("Garden"))
                mChipGarden.setChecked(true);
        }
    }

    /**
     * Configures different spinner with the right entries and values. Sets OnItemClickListener for
     * all spinner with "this" to override only once the corresponding method.
     */
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

    /**
     * Sets the selected item's value in a String.
     */
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

    /**
     * Configures the RecyclerView's adapter and sets the expected behavior when user click on an
     * RecyclerView's item
     */
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
//                        If this fragment is used to add a property in database, it removes le selected
//                        picture of the list
                        if (mRealEstateId == 0) {
                            mPicturesList.remove(position);
                        } else {
//                            If the fragment is used to edit a property, it deletes the picture in
//                            database too.
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

    /**
     * Defines the mLayoutSurface and mLayoutPrice hints according to the user's preferences for
     * currency and units of measure.
     */
    private void configureTextLayoutHintAccordingToPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String area = preferences.getString("area", "sq ft");
        String currency = preferences.getString("currency", "$");

        if (area.equals("sq ft"))
            mLayoutSurface.setHint(getString(R.string.enter_surface_sqft));
        else
            mLayoutSurface.setHint(getString(R.string.enter_surface_m));

        if (currency.equals("$"))
            mLayoutPrice.setHint(getString(R.string.enter_price_dollars));
        else
            mLayoutPrice.setHint(getString(R.string.enter_price_euros));
    }

    /**
     * Recovers the user's input, verifies if the input is not empty and sets the values into the
     * corresponding fields.
     */
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
//            If a sale date is filled then the property is considered sold
            isSold = true;
        } else
            isSold = false;
        if (!TextUtils.isEmpty(mYearConstructionTxt.getText()))
            yearConstruction = Utils.convertStringToDate(Objects.requireNonNull(mYearConstructionTxt.getText()).toString());
        amenities = Utils.getUserAmenitiesChoice(mChipSchool, mChipShop, mChipTransport, mChipGarden);
//        If the set of fields relative to the address are filled then it calls the method getLocation.
        if (address != null && zipCode != 0 && city != null) {
            if (Utils.isInternetAvailable(mActivity))
                getLocation(address + " " + zipCode + " " + city);
        }
    }

    /**
     * Updates mProperty that is the current Property with the fetched user's input.
     */
    private void updateCurrentRealEstate(String typePropertyInput, int price, int surface, int nbRooms,
                                         int nbBedrooms, int nbBathrooms, String description, String address,
                                         int zipCode, String countryCodeInput, double latitude, double longitude, String city, String amenities,
                                         boolean isSold, Date initialDate, Date finalDate, String agentInput,
                                         Date yearConstruction, int floors, Boolean coownership, int nbPictures) {
        mProperty.setTypeProperty(typePropertyInput);
        mProperty.setPrice(price);
        mProperty.setSurface(surface);
        mProperty.setNbRooms(nbRooms);
        mProperty.setNbBedrooms(nbBedrooms);
        mProperty.setNbBathrooms(nbBathrooms);
        mProperty.setDescription(description);
        mProperty.setAddress(address);
        mProperty.setZipCode(zipCode);
        mProperty.setCountryCode(countryCodeInput);
        mProperty.setLatitude(latitude);
        mProperty.setLongitude(longitude);
        mProperty.setCity(city);
        mProperty.setAmenities(amenities);
        mProperty.setSold(isSold);
        mProperty.setInitialSale(initialDate);
        mProperty.setFinalSale(finalDate);
        mProperty.setRealEstateAgent(agentInput);
        mProperty.setYearConstruction(yearConstruction);
        mProperty.setFloors(floors);
        mProperty.setCoOwnership(coownership);
        mProperty.setNbPictures(nbPictures);
    }

    /**
     * Creates the selected pictures in database with the current Property's Id for attribute.
     */
    private void savePictureInDb(long realEstateId) {
        for (Pictures pictures : mPicturesList) {
            pictures.setRealEstateId(realEstateId);
            mRealEstateViewModel.createPictures(pictures);
        }
    }

    /**
     * Uses Geocoder object to fetch the latitude and the longitude from an address.
     */
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

    /**
     * Updates a property in database with the user's input. Displays a notification message with a
     * SnackBar to inform user of the update's successful.
     */
    private void updateRealEstateWithNewValues() {
        getTheUserInput();
        mNbPictures = mPicturesList.size();
        updateCurrentRealEstate(mTypePropertyInput, price, surface, nbRooms, nbBedrooms, nbBathrooms,
                description, address, zipCode, mCountryCodeInput, mLatitude, mLongitude, city, amenities, isSold, initialDate,
                finalDate, mAgentInput, yearConstruction, floors, Boolean.valueOf(mCoOwnershipInput), mNbPictures);
        savePictureInDb(mRealEstateId);
        int nbRows = mRealEstateViewModel.updateRealEstate(mProperty);
        if (nbRows == 1) {
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container),
                    getString(R.string.update_success), Snackbar.LENGTH_LONG).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container),
                    getString(R.string.update_failed), Snackbar.LENGTH_LONG).show();
    }

    /**
     * Creates a property in database with the user's input. Displays a notification message with a
     * SnackBar to inform user of the create's successful.
     */
    private void createNewRealEstateFromInputValues() {
        getTheUserInput();
        mNbPictures = mPicturesList.size();
        long rowId = mRealEstateViewModel.createRealEstate(new Property(mTypePropertyInput, price, surface, nbRooms,
                nbBedrooms, nbBathrooms, description, address, zipCode, mCountryCodeInput, mLatitude,
                mLongitude, city, amenities, isSold, initialDate, finalDate, mAgentInput, yearConstruction,
                floors, Boolean.valueOf(mCoOwnershipInput), mNbPictures));
//        Fetch the row's Id to save chosen pictures with the Property's Id in attributes.
        if (rowId != -1L) {
            savePictureInDb(rowId);
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container), getString(R.string.save_success),
                    Snackbar.LENGTH_LONG).show();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.activity_main_container), getString(R.string.save_failed), Snackbar.LENGTH_LONG).show();
    }


    /**
     * Returns the right country code according the user's friendly value set in a spinner.
     */
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

    /**
     * Returns the user's frinedly value according to a country code.
     */
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

    /**
     * Displays a DatePicker to select a date and sets the selected value in the corresponding view.
     */
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
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.delete_date), (dialogInterface, i) -> {
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

    /**
     * Checks if all the text fields relating to the address are filled, and if necessary show an
     * error message on the missing fields
     */
    private boolean verifyAddressInputForGeoCoding() {
        if (!TextUtils.isEmpty(mAddAddressTxt.getText()) && (TextUtils.isEmpty(mZipcodeTxt.getText()) || TextUtils.isEmpty(mCityTxt.getText()))) {
            if (TextUtils.isEmpty(mZipcodeTxt.getText()))
                mLayoutZipcode.setError(getString(R.string.please_zipcode));
            if (TextUtils.isEmpty(mCityTxt.getText()))
                mLayoutCity.setError(getString(R.string.please_city));
            return false;
        } else if (!TextUtils.isEmpty(mZipcodeTxt.getText()) && (TextUtils.isEmpty(mAddAddressTxt.getText()) || TextUtils.isEmpty(mCityTxt.getText()))) {
            if (TextUtils.isEmpty(mAddAddressTxt.getText()))
                mLayoutAddress.setError(getString(R.string.please_address));
            if (TextUtils.isEmpty(mCityTxt.getText()))
                mLayoutCity.setError(getString(R.string.please_city));
            return false;
        } else if (!TextUtils.isEmpty(mCityTxt.getText()) && (TextUtils.isEmpty(mAddAddressTxt.getText()) || TextUtils.isEmpty(mZipcodeTxt.getText()))) {
            if (TextUtils.isEmpty(mAddAddressTxt.getText()))
                mLayoutAddress.setError(getString(R.string.please_address));
            if (TextUtils.isEmpty(mZipcodeTxt.getText()))
                mLayoutZipcode.setError(getString(R.string.please_zipcode));
            return false;
        } else
            return true;
    }

    /**
     * Checks if read, write on external storage and camera permissions are granted with EasyPermission
     * and request permissions if it's not the case.
     * If permissions are granted, calls the right method.
     */
    @AfterPermissionGranted(READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA)
    private void getPermissionsExternalStorageAndCamera() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(mActivity, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.storage_rationale),
                    READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA, perms);
        } else
            openDialogToFetchPictures();
    }

    /**
     * Displays a MaterialAlertDialog to propose to the user to add to the choice either a photo or
     * a video. Depending on the user's choice, call the corresponding method.
     */
    private void openDialogToFetchPictures() {
        new MaterialAlertDialogBuilder(mActivity)
                .setTitle(getString(R.string.what_you_add))
                .setMessage(getString(R.string.pictures_or_movie))
                .setNegativeButton(getString(R.string.add_pictures), (dialogInterface, i) -> new MaterialAlertDialogBuilder(mActivity)
                        .setTitle(getString(R.string.which_mean_pictures))
                        .setMessage(getString(R.string.gallery_or_camera))
                        .setNegativeButton(getString(R.string.camera), (dialogInterface1, i1) -> openCameraForPhoto())
                        .setNeutralButton(getString(R.string.cancel), (dialogInterface12, i12) -> dialogInterface12.cancel())
                        .setPositiveButton(getString(R.string.memory), (dialogInterface13, i13) -> pickPhotoFromGallery())
                        .setCancelable(false)
                        .show())
                .setNeutralButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.add_movies), (dialogInterface, i) -> new MaterialAlertDialogBuilder(mActivity)
                        .setTitle(getString(R.string.which_mean_movies))
                        .setMessage(getString(R.string.gallery_or_camera_movie))
                        .setNegativeButton(getString(R.string.camera), (dialogInterface14, i14) -> openCameraForVideo())
                        .setNeutralButton(getString(R.string.cancel), (dialogInterface15, i15) -> dialogInterface15.cancel())
                        .setPositiveButton(getString(R.string.memory), (dialogInterface16, i16) -> pickMovieFromGallery())
                        .setCancelable(false)
                        .show())
                .setCancelable(false)
                .show();
    }

    /**
     * Creates the file to save the taken photo by user.
     */
    private File createPhotoFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Creates the file to save the taken movie by user.+
     */
    private File createMovieFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timestamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(videoFileName, ".mp4", storageDir);
        currentVideoPath = video.getAbsolutePath();
        return video;
    }

    /**
     * Creates an intent to allow the user to take a movie with his device's camera and with the
     * right camera application. Passes the created file by "createMovieFile" in intent's extra.
     */
    private void openCameraForVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createMovieFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (videoFile != null) {
                Uri videoUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileprovider", videoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(cameraIntent, MOVIE_CAPTURE_CODE);
            }
        }
    }

    /**
     * Creates an intent to allow the user to take a photo with his device's camera and with the
     * right camera application. Passes the created file by "createPhotoFile" in intent's extra.
     */
    private void openCameraForPhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
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
     * Creates an intent to allow the user to pick a photo in memory with the right application.
     */
    private void pickPhotoFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    /**
     * Creates an intent to allow the user to pick a video in memory with the right application.
     */
    private void pickMovieFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, MOVIE_PICK_CODE);
    }

    /**
     * Displays a MaterialAlertDialog to ask the user to enter a caption for the chosen photo.
     */
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
                textInputLayout.setError(getString(R.string.please_caption));
            } else {
                textInputLayout.setError(null);
                mPicturesList.add(new Pictures(Uri.parse(uri), editText.getText().toString()));
                builder.cancel();
                mRecyclerView.setVisibility(View.VISIBLE);
                mPhotoAdapter.notifyDataSetChanged();
            }

        });
    }

    /**
     * Displays a MaterialAlertDialog to ask the user to enter a caption for the chosen movie.
     */
    private void saveMovieCaption(String uri) {
        View customDialog = mActivity.getLayoutInflater().inflate(R.layout.dialog_movies_validation, null);
        VideoView videoView = customDialog.findViewById(R.id.dialog_picture_preview_movie);
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
        videoView.start();
        TextInputEditText editText = customDialog.findViewById(R.id.dialog_layout_title_txt);
        TextInputLayout textInputLayout = customDialog.findViewById(R.id.dialog_layout_title);
        MaterialButton negativeBtn = customDialog.findViewById(R.id.dialog_layout_negative_btn);
        MaterialButton positiveBtn = customDialog.findViewById(R.id.dialog_layout_positive_btn);

        AlertDialog builder = new MaterialAlertDialogBuilder(mActivity)
                .setView(customDialog)
                .setTitle(getString(R.string.save_movie_dialog))
                .setCancelable(false)
                .show();

        negativeBtn.setOnClickListener(view -> builder.cancel());
        positiveBtn.setOnClickListener(view -> {
            if (editText.getText() != null && editText.getText().length() == 0) {
                textInputLayout.setError(getString(R.string.please_caption));
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Calls the right method according to the requestCode defined in startActivityForResult() if
     * the resultCode equals RESULT_OK
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            savePictureCaption(currentPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            savePictureCaption(path);
        } else if (resultCode == RESULT_OK && requestCode == MOVIE_CAPTURE_CODE) {
            saveMovieCaption(currentVideoPath);
        } else if (resultCode == RESULT_OK && requestCode == MOVIE_PICK_CODE && data != null) {
            Uri uri = data.getData();
            String path = getRealPathMovieFromURI(uri);
            saveMovieCaption(path);
        }
    }

    /**
     * Gets the real path from a picture pick in memory. The path is needed to save the chosen path
     * in database.
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mActivity, contentUri, proj, null, null,
                null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    /**
     * Gets the real path from a movie pick in memory. The path is needed to save the chosen path
     * in database.
     */
    private String getRealPathMovieFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null,
                null);
        if (cursor != null) {
            int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        } else
            return null;
    }

    @OnClick({R.id.fragment_add_year_construction_txt, R.id.fragment_add_initial_sale_txt,
            R.id.fragment_add_final_sale_txt, R.id.fragment_add_validation_btn, R.id.fragment_add_pictures_txt})
    void onViewClicked(View view) {
        switch (view.getId()) {
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
                break;
            default:
                displayDatePickerAndUpdateUi(view);
                break;
        }
    }

    @Override
    public void onDestroyView() {
//        Sets adapters and listeners to null to avoid memory leaks.
        mRecyclerView.setAdapter(null);
        mPhotoAdapter.setOnClickListener(null);
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("photoFile", currentPhotoPath);
        outState.putString("movieFile", currentVideoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddPropertyFragment.OnFragmentInteractionListener) {
            mListener = (AddPropertyFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        if (getResources().getBoolean(R.bool.isTabletLand) && mListener != null)
            mListener.checkVisibility(ADD_REAL_ESTATE_FRAGMENT);
        super.onResume();
    }

    public interface OnFragmentInteractionListener {
        void checkVisibility(String destination);
    }

}
