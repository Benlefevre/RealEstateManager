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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentAddRealEstateBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.adapters.DetailsPhotoAdapter;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
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

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.MOVIE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.MOVIE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA;


public class AddPropertyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String REAL_ESTATE_ID = "realEstateId";

    FragmentAddRealEstateBinding mBinding;

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
    private TakeOrNotFullScreen mCallback;

    public AddPropertyFragment() {
        // Required empty public constructor
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddRealEstateBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        setClickListener();
        Toolbar toolbar = Objects.requireNonNull(mActivity).findViewById(R.id.activity_main_toolbar);
        if (mRealEstateId == 0)
            toolbar.setTitle(getString(R.string.add_a_real_estate));
        else
            toolbar.setTitle(getString(R.string.edit_the_real_estate));
        if (!Utils.isNetworkAccessEnabled(requireContext())) {
            Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment),
                    getString(R.string.verify_for_geocoder), Snackbar.LENGTH_LONG).show();
        }
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
        mRealEstateViewModel = new ViewModelProvider((FragmentActivity) mActivity, viewModelFactory).get(RealEstateViewModel.class);
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
        mBinding.fragmentAddRecyclerView.setVisibility(View.VISIBLE);
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the LiveData values in mProperty and binds mProperty's attributes into the dedicated views.
     */
    private void updateUiAfterFetchRealEstate(Property property) {
        mProperty = property;
        mBinding.fragmentAddTypePropertySpinner.setText(mProperty.getTypeProperty(), false);
        mTypePropertyInput = mBinding.fragmentAddTypePropertySpinner.getText().toString();
        mBinding.fragmentAddCoOwnershipSpinner.setText(String.valueOf(mProperty.isCoOwnership()), false);
        mCoOwnershipInput = mBinding.fragmentAddCoOwnershipSpinner.getText().toString();
        mBinding.fragmentAddAgentSpinner.setText(mProperty.getRealEstateAgent(), false);
        mAgentInput = mBinding.fragmentAddAgentSpinner.getText().toString();
        mBinding.fragmentAddDescriptionTxt.setText(mProperty.getDescription());
        mBinding.fragmentAddAddressTxt.setText(mProperty.getAddress());
        mBinding.fragmentAddCityTxt.setText(mProperty.getCity());
        mBinding.fragmentAddFloorsTxt.setText(String.valueOf(mProperty.getFloors()));
//        We use displayed values more user friendly so we use methods to find the existing value in the database
        if (mProperty.getCountryCode() != null)
            mBinding.fragmentAddCountryCodeSpinner.setText(getCountryAccordingToCountryCode(mProperty.getCountryCode()), false);
        if (mBinding.fragmentAddCountryCodeSpinner.getText() != null && mBinding.fragmentAddCountryCodeSpinner.getText().length() != 0)
            mCountryCodeInput = getCountryCodeAccordingToCountry(mBinding.fragmentAddCountryCodeSpinner.getText().toString());
//        We use methods to convert the values in database into values corresponding to the user's preferences
        if (mProperty.getPrice() != 0)
            mBinding.fragmentAddPriceTxt.setText(String.valueOf(Utils.convertPriceAccordingToPreferenceToEdit(mActivity, mProperty.getPrice())));
        if (mProperty.getSurface() != 0)
            mBinding.fragmentAddSurfaceTxt.setText(String.valueOf(Utils.convertAreaAccordingToPreferencesToEdit(mActivity, mProperty.getSurface())));
        if (mProperty.getNbRooms() != 0)
            mBinding.fragmentAddRoomsTxt.setText(String.valueOf(mProperty.getNbRooms()));
        if (mProperty.getNbBedrooms() != 0)
            mBinding.fragmentAddBedroomsTxt.setText(String.valueOf(mProperty.getNbBedrooms()));
        if (mProperty.getNbBathrooms() != 0)
            mBinding.fragmentAddBathroomsTxt.setText(String.valueOf(mProperty.getNbBathrooms()));
        if (mProperty.getZipCode() != 0)
            mBinding.fragmentAddZipcodeTxt.setText(String.valueOf(mProperty.getZipCode()));
//        We convert the dates in database into String values
        if (mProperty.getInitialSale() != null)
            mBinding.fragmentAddInitialSaleTxt.setText(Utils.convertDateToString(mProperty.getInitialSale(), mActivity));
        if (mProperty.getFinalSale() != null)
            mBinding.fragmentAddFinalSaleTxt.setText(Utils.convertDateToString(mProperty.getFinalSale(), mActivity));
        if (mProperty.getYearConstruction() != null)
            mBinding.fragmentAddYearConstructionTxt.setText(Utils.convertDateToString(mProperty.getYearConstruction(), mActivity));
        if (mProperty.getAmenities() != null) {
            if (mProperty.getAmenities().contains("School"))
                mBinding.chipGroupAmenities.chipSchool.setChecked(true);
            if (mProperty.getAmenities().contains("Shops"))
                mBinding.chipGroupAmenities.chipShop.setChecked(true);
            if (mProperty.getAmenities().contains("Public transport"))
                mBinding.chipGroupAmenities.chipTransport.setChecked(true);
            if (mProperty.getAmenities().contains("Garden"))
                mBinding.chipGroupAmenities.chipGarden.setChecked(true);
        }
    }

    /**
     * Configures different spinner with the right entries and values. Sets OnItemClickListener for
     * all spinner with "this" to override only once the corresponding method.
     */
    private void configureSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(mActivity, R.array.typeProperty,
                R.layout.dropdown_item_spinner);
        mBinding.fragmentAddTypePropertySpinner.setAdapter(typeAdapter);
        mBinding.fragmentAddTypePropertySpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(mActivity, R.array.countryCode,
                R.layout.dropdown_item_spinner);
        mBinding.fragmentAddCountryCodeSpinner.setAdapter(countryAdapter);
        mBinding.fragmentAddCountryCodeSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> agentAdapter = ArrayAdapter.createFromResource(mActivity, R.array.agent,
                R.layout.dropdown_item_spinner);
        mBinding.fragmentAddAgentSpinner.setAdapter(agentAdapter);
        mBinding.fragmentAddAgentSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> coOwnershipAdapter = ArrayAdapter.createFromResource(mActivity, R.array.co_ownership,
                R.layout.dropdown_item_spinner);
        mBinding.fragmentAddCoOwnershipSpinner.setAdapter(coOwnershipAdapter);
        mBinding.fragmentAddCoOwnershipSpinner.setOnItemClickListener(this);
    }

    /**
     * Sets the selected item's value in a String.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == mBinding.fragmentAddTypePropertySpinner.getAdapter()) {
            mTypePropertyInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mBinding.fragmentAddCountryCodeSpinner.getAdapter()) {
            mCountryCodeInput = getCountryCodeAccordingToCountry(adapterView.getItemAtPosition(i).toString());
        } else if (adapterView.getAdapter() == mBinding.fragmentAddAgentSpinner.getAdapter()) {
            mAgentInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mBinding.fragmentAddCoOwnershipSpinner.getAdapter()) {
            mCoOwnershipInput = adapterView.getItemAtPosition(i).toString();
        }
    }

    /**
     * Configures the RecyclerView's adapter and sets the expected behavior when user click on an
     * RecyclerView's item
     */
    private void configureRecyclerView() {
        mBinding.fragmentAddRecyclerView.setVisibility(View.GONE);
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
                        if (mRealEstateId != 0) {
//                            If the fragment is used to edit a property, it deletes the picture in
//                            database too.
                            mRealEstateViewModel.deletePicture(mPicturesList.get(position));
                        }
                        mPicturesList.remove(position);
                        mPhotoAdapter.notifyDataSetChanged();
                        if (mPicturesList.size() == 0)
                            mBinding.fragmentAddRecyclerView.setVisibility(View.GONE);
                    })
                    .show();
        });
        mBinding.fragmentAddRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mBinding.fragmentAddRecyclerView.setAdapter(mPhotoAdapter);
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
            mBinding.layoutSurface.setHint(getString(R.string.enter_surface_sqft));
        else
            mBinding.layoutSurface.setHint(getString(R.string.enter_surface_m));

        if (currency.equals("$"))
            mBinding.layoutPrice.setHint(getString(R.string.enter_price_dollars));
        else
            mBinding.layoutPrice.setHint(getString(R.string.enter_price_euros));
    }

    /**
     * Recovers the user's input, verifies if the input is not empty and sets the values into the
     * corresponding fields.
     */
    private void getTheUserInput() {
        if (!TextUtils.isEmpty(mBinding.fragmentAddPriceTxt.getText()))
            price = Utils.convertPriceAccordingToPreferences(mActivity, mBinding.fragmentAddPriceTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddSurfaceTxt.getText()))
            surface = Utils.convertAreaAccordingToPreferences(mActivity, mBinding.fragmentAddSurfaceTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddRoomsTxt.getText()))
            nbRooms = Integer.parseInt(mBinding.fragmentAddRoomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddBedroomsTxt.getText()))
            nbBedrooms = Integer.parseInt(mBinding.fragmentAddBedroomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddBathroomsTxt.getText()))
            nbBathrooms = Integer.parseInt(mBinding.fragmentAddBathroomsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddDescriptionTxt.getText()))
            description = mBinding.fragmentAddDescriptionTxt.getText().toString();
        if (!TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText())) {
            address = mBinding.fragmentAddAddressTxt.getText().toString();
        }
        if (!TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText()))
            zipCode = Integer.parseInt(mBinding.fragmentAddZipcodeTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText()))
            city = mBinding.fragmentAddCityTxt.getText().toString();
        if (!TextUtils.isEmpty(mBinding.fragmentAddFloorsTxt.getText()))
            floors = Integer.parseInt(mBinding.fragmentAddFloorsTxt.getText().toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddInitialSaleTxt.getText()))
            initialDate = Utils.convertStringToDate(Objects.requireNonNull(mBinding.fragmentAddInitialSaleTxt.getText()).toString());
        if (!TextUtils.isEmpty(mBinding.fragmentAddFinalSaleTxt.getText())) {
            finalDate = Utils.convertStringToDate(Objects.requireNonNull(mBinding.fragmentAddFinalSaleTxt.getText()).toString());
//            If a sale date is filled then the property is considered sold
            isSold = true;
        } else
            isSold = false;
        if (!TextUtils.isEmpty(mBinding.fragmentAddYearConstructionTxt.getText()))
            yearConstruction = Utils.convertStringToDate(Objects.requireNonNull(
                    mBinding.fragmentAddYearConstructionTxt.getText()).toString()
            );
        amenities = Utils.getUserAmenitiesChoice(mBinding.chipGroupAmenities.chipSchool,
                mBinding.chipGroupAmenities.chipShop, mBinding.chipGroupAmenities.chipTransport,
                mBinding.chipGroupAmenities.chipGarden);
//        If the set of fields relative to the address are filled then it calls the method getLocation.
        if (address != null && zipCode != 0 && city != null) {
            if (Utils.isNetworkAccessEnabled(mActivity)) {
                getLocation(address + " " + zipCode + " " + city);
            }
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
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            Log.i("Location", "Location : Lat :" + addresses.get(0).getLatitude() + " / Long" + addresses.get(0).getLongitude());
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
            Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment),
                    getString(R.string.update_success), Snackbar.LENGTH_LONG).show();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment),
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
                floors, Boolean.parseBoolean(mCoOwnershipInput), mNbPictures));
//        Fetch the row's Id to save chosen pictures with the Property's Id in attributes.
        if (rowId != -1L) {
            savePictureInDb(rowId);
            Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment), getString(R.string.save_success),
                    Snackbar.LENGTH_LONG).show();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack();
        } else
            Snackbar.make(mActivity.findViewById(R.id.nav_host_fragment), getString(R.string.save_failed), Snackbar.LENGTH_LONG).show();
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
            int id = view.getId();
            if (id == R.id.fragment_add_initial_sale_txt) {
                mBinding.fragmentAddInitialSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
            } else if (id == R.id.fragment_add_final_sale_txt) {
                mBinding.fragmentAddFinalSaleTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
            } else if (id == R.id.fragment_add_year_construction_txt) {
                mBinding.fragmentAddYearConstructionTxt.setText(getString(R.string.hour_format, day1, month1 + 1, year1));
            }
        }, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.delete_date), (dialogInterface, i) -> {
            int id = view.getId();
            if (id == R.id.fragment_add_initial_sale_txt) {
                mBinding.fragmentAddInitialSaleTxt.setText(null);
            } else if (id == R.id.fragment_add_final_sale_txt) {
                mBinding.fragmentAddFinalSaleTxt.setText(null);
            } else if (id == R.id.fragment_add_year_construction_txt) {
                mBinding.fragmentAddYearConstructionTxt.setText(null);
            }
        });
        datePickerDialog.show();
    }

    /**
     * Checks if all the text fields relating to the address are filled, and if necessary show an
     * error message on the missing fields
     */
    private boolean verifyAddressInputForGeoCoding() {
        if (!TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText())
                && (TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText())
                || TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText()))) {
            if (TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText()))
                mBinding.layoutZipcode.setError(getString(R.string.please_zipcode));
            if (TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText()))
                mBinding.layoutCity.setError(getString(R.string.please_city));
            return false;
        } else if (!TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText())
                && (TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText())
                | TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText()))) {
            if (TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText()))
                mBinding.layoutAddress.setError(getString(R.string.please_address));
            if (TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText()))
                mBinding.layoutCity.setError(getString(R.string.please_city));
            return false;
        } else if (!TextUtils.isEmpty(mBinding.fragmentAddCityTxt.getText())
                && (TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText())
                || TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText()))) {
            if (TextUtils.isEmpty(mBinding.fragmentAddAddressTxt.getText()))
                mBinding.layoutAddress.setError(getString(R.string.please_address));
            if (TextUtils.isEmpty(mBinding.fragmentAddZipcodeTxt.getText()))
                mBinding.layoutZipcode.setError(getString(R.string.please_zipcode));
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
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
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
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setDataAndType(collection, "image/*");
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    /**
     * Creates an intent to allow the user to pick a video in memory with the right application.
     */
    private void pickMovieFromGallery() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setDataAndType(collection, "video/mp4");
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, MOVIE_PICK_CODE);
    }

    /**
     * Displays a MaterialAlertDialog to ask the user to enter a caption for the chosen photo.
     */
    private void savePictureCaption(String uri) {
        View customDialog = mActivity.getLayoutInflater().inflate(R.layout.dialog_pictures_validation, null);
        ImageView imageView = customDialog.findViewById(R.id.dialog_picture_preview_img);
        Glide.with(requireContext()).load(uri).into(imageView);
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
                mBinding.fragmentAddRecyclerView.setVisibility(View.VISIBLE);
                mPhotoAdapter.notifyDataSetChanged();
                builder.cancel();
            }
        });
    }

    /**
     * Displays a MaterialAlertDialog to ask the user to enter a caption for the chosen movie.
     */
    private void saveMovieCaption(String uri) {
        View customDialog = mActivity.getLayoutInflater().inflate(R.layout.dialog_movies_validation, null);
        StyledPlayerView videoView = customDialog.findViewById(R.id.dialog_picture_preview_movie);
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(mActivity).build();
        videoView.setPlayer(player);
        player.setMediaItem(MediaItem.fromUri(Uri.parse(uri)));
        player.prepare();
        player.play();
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
                mBinding.fragmentAddRecyclerView.setVisibility(View.VISIBLE);
                mPhotoAdapter.notifyDataSetChanged();
                builder.cancel();
            }
            player.release();
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
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    /**
     * Gets the real path from a movie pick in memory. The path is needed to save the chosen path
     * in database.
     */
    private String getRealPathMovieFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, projection, null, null,
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

    void setClickListener() {
        mBinding.fragmentAddInitialSaleTxt.setOnClickListener(this::displayDatePickerAndUpdateUi);
        mBinding.fragmentAddYearConstructionTxt.setOnClickListener(this::displayDatePickerAndUpdateUi);
        mBinding.fragmentAddFinalSaleTxt.setOnClickListener(this::displayDatePickerAndUpdateUi);
        mBinding.fragmentAddValidationBtn.setOnClickListener(v -> {
            if (mRealEstateId == 0) {
                if (verifyAddressInputForGeoCoding()) {
                    createNewRealEstateFromInputValues();
                }
            } else {
                if (verifyAddressInputForGeoCoding()) {
                    updateRealEstateWithNewValues();
                }
            }
        });

        mBinding.fragmentAddPicturesTxt.setOnClickListener(v -> getPermissionsExternalStorageAndCamera());
    }

    @Override
    public void onDestroyView() {
//        Sets adapters and listeners to null to avoid memory leaks.
        mBinding.fragmentAddRecyclerView.setAdapter(null);
        mPhotoAdapter.setOnClickListener(null);
        super.onDestroyView();
        mBinding = null;
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
    public void onResume() {
        super.onResume();
        mCallback.takeFullScreenFragment();
    }
}
