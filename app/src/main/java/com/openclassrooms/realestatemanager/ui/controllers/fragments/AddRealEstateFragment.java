package com.openclassrooms.realestatemanager.ui.controllers.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.utils.Constants.ACCESS_CAMERA;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;


public class AddRealEstateFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    @BindView(R.id.fragment_add_amenities_txt)
    TextInputEditText mAmenitiesTxt;
    @BindView(R.id.fragment_add_initial_sale_txt)
    TextInputEditText mInitialSaleTxt;
    @BindView(R.id.fragment_add_final_sale_txt)
    TextInputEditText mFinalSaleTxt;
    @BindView(R.id.fragment_add_co_ownership_spinner)
    AppCompatAutoCompleteTextView mCoOwnershipSpinner;
    @BindView(R.id.fragment_add_status_spinner)
    AppCompatAutoCompleteTextView mStatusSpinner;
    @BindView(R.id.fragment_add_agent_spinner)
    AppCompatAutoCompleteTextView mAgentSpinner;
    @BindView(R.id.fragment_add_validation_btn)
    MaterialButton mValidationBtn;
    @BindView(R.id.fragment_add_pictures_txt)
    TextInputEditText mPicturesTxt;


    private String mParam1;
    private String mParam2;
    private String mTypePropertyInput;
    private String mCountryCodeInput;
    private String mStatusInput;
    private String mAgentInput;
    private String mCoOwnershipInput;
    private Uri image_uri;
    private String currentPhotoPath;

    private long mRowId;
    private List<Pictures> mPicturesList;

    private Activity mActivity;

    private RealEstateViewModel mRealEstateViewModel;

//    private OnFragmentInteractionListener mListener;

    public AddRealEstateFragment() {
        // Required empty public constructor
    }


//    public static AddRealEstateFragment newInstance(String param1, String param2) {
//        AddRealEstateFragment fragment = new AddRealEstateFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static AddRealEstateFragment newInstance() {
        AddRealEstateFragment fragment = new AddRealEstateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_real_estate, container, false);
        ButterKnife.bind(this, view);
        mPicturesList = new ArrayList<>();
        configureSpinner();
        configureViewModel();
        return view;
    }

    private void configureSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(mActivity, R.array.typeProperty, R.layout.dropdown_item_spinner);
        mTypePropertySpinner.setAdapter(typeAdapter);
        mTypePropertySpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(mActivity, R.array.countryCode, R.layout.dropdown_item_spinner);
        mCountryCodeSpinner.setAdapter(countryAdapter);
        mCountryCodeSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(mActivity, R.array.status, R.layout.dropdown_item_spinner);
        mStatusSpinner.setAdapter(statusAdapter);
        mStatusSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> agentAdapter = ArrayAdapter.createFromResource(mActivity, R.array.agent, R.layout.dropdown_item_spinner);
        mAgentSpinner.setAdapter(agentAdapter);
        mAgentSpinner.setOnItemClickListener(this);

        ArrayAdapter<CharSequence> coOwnershipAdapter = ArrayAdapter.createFromResource(mActivity, R.array.co_ownership, R.layout.dropdown_item_spinner);
        mCoOwnershipSpinner.setAdapter(coOwnershipAdapter);
        mCoOwnershipSpinner.setOnItemClickListener(this);
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(getActivity());
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void createNewRealEstateFromInformations() {
        int price = 0;
        int surface = 0;
        int nbRooms = 0;
        int nbBedrooms = 0;
        int nbBathrooms = 0;
        String description = null;
        String address = null;
        int zipCode = 0;
        String city = null;
        String amenities = null;
        Date initialDate;
        Date finalDate;
        Date yearConstruction;
        int floors = 0;
        if (mPriceTxt.getText() != null && mPriceTxt.getText().length() != 0)
            price = Integer.parseInt(mPriceTxt.getText().toString());
        if (mSurfaceTxt.getText() != null && mSurfaceTxt.getText().length() != 0)
            surface = Integer.parseInt(mSurfaceTxt.getText().toString());
        if (mRoomsTxt.getText() != null && mRoomsTxt.getText().length() != 0)
            nbRooms = Integer.parseInt(mRoomsTxt.getText().toString());
        if (mBedroomsTxt.getText() != null && mBedroomsTxt.getText().length() != 0)
            nbBedrooms = Integer.parseInt(mBedroomsTxt.getText().toString());
        if (mBathroomsTxt.getText() != null && mBathroomsTxt.getText().length() != 0)
            nbBathrooms = Integer.parseInt(mBathroomsTxt.getText().toString());
        if (mDescriptionTxt.getText() != null && mDescriptionTxt.getText().length() != 0)
            description = mDescriptionTxt.getText().toString();
        if (mAddAddressTxt.getText() != null && mAddAddressTxt.getText().length() != 0)
            address = mAddAddressTxt.getText().toString();
        if (mZipcodeTxt.getText() != null && mZipcodeTxt.getText().length() != 0)
            zipCode = Integer.parseInt(mZipcodeTxt.getText().toString());
        if (mCityTxt.getText() != null && mCityTxt.getText().length() != 0)
            city = mCityTxt.getText().toString();
        if (mAmenitiesTxt.getText() != null && mAmenitiesTxt.getText().length() != 0)
            amenities = mAmenitiesTxt.getText().toString();
        if (mFloorsTxt.getText() != null && mFloorsTxt.getText().length() != 0)
            floors = Integer.parseInt(mFloorsTxt.getText().toString());

        initialDate = Utils.convertStringToDate(mInitialSaleTxt.getText().toString());
        finalDate = Utils.convertStringToDate(mFinalSaleTxt.getText().toString());
        yearConstruction = Utils.convertStringToDate(mYearConstructionTxt.getText().toString());

        mRowId = mRealEstateViewModel.createRealEstate(new RealEstate(mTypePropertyInput, price, surface, nbRooms,
                nbBedrooms, nbBathrooms, description, address, zipCode, mCountryCodeInput, 0, 0, city, amenities,
                mStatusInput, initialDate, finalDate, mAgentInput, yearConstruction, floors, Boolean.valueOf(mCoOwnershipInput)));

        for (Pictures pictures : mPicturesList) {
            pictures.setRealEstateId(mRowId);
            mRealEstateViewModel.createPictures(pictures);
        }
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == mTypePropertySpinner.getAdapter()) {
            mTypePropertyInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mCountryCodeSpinner.getAdapter()) {
            mCountryCodeInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mStatusSpinner.getAdapter()) {
            mStatusInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mAgentSpinner.getAdapter()) {
            mAgentInput = adapterView.getItemAtPosition(i).toString();
        } else if (adapterView.getAdapter() == mCoOwnershipSpinner.getAdapter()) {
            mCoOwnershipInput = adapterView.getItemAtPosition(i).toString();
        }
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
                createNewRealEstateFromInformations();
                break;
            case R.id.fragment_add_pictures_txt:
                getCameraPermissions();

        }
    }

    @AfterPermissionGranted(ACCESS_CAMERA)
    private void getCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(mActivity, perms))
            openDialogToFetchPictures();
        else
            EasyPermissions.requestPermissions(mActivity, "We need this permission to allow you to take pictures",
                    ACCESS_CAMERA, perms);
    }

    private void openDialogToFetchPictures() {
        AlertDialog builder = new MaterialAlertDialogBuilder(mActivity)
                .setTitle("By which means would you like to add a picture?")
                .setMessage("You can either take a picture from the camera of this camera or choose a picture stored on the camera's memory")
                .setNegativeButton(getString(R.string.camera), (dialogInterface, i) -> openCamera())
                .setNeutralButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.memory), (dialogInterface, i) -> pickImageFromGallery())
                .setCancelable(false)
                .show();
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp;
//        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Opens the device's camera to take a picture and fetch the file's path
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile  = null;
            try {
                photoFile = createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider",photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
            }
        }
    }

    /**
     * Opens an App to pick a picture and fetch the file's path
     */
    private void pickImageFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    private void displayDatePickerAndUpdateUi(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                switch (view.getId()) {
                    case R.id.fragment_add_initial_sale_txt:
                        mInitialSaleTxt.setText(year + "-" + month + "-" + day);
                        break;
                    case R.id.fragment_add_final_sale_txt:
                        mFinalSaleTxt.setText(year + "-" + month + "-" + day);
                        break;
                    case R.id.fragment_add_year_construction_txt:
                        mYearConstructionTxt.setText(year + "-" + month + "-" + day);
                        break;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void savePicture(String uri) {
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
            if (editText.getText().length() == 0) {
                textInputLayout.setError("Please enter the picture's caption");
            } else {
                textInputLayout.setError(null);
                mPicturesList.add(new Pictures(Uri.parse(uri), editText.getText().toString()));
                builder.cancel();
            }

        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            Log.i("info", currentPhotoPath);
            savePicture(currentPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            Log.i("info", String.valueOf(data.getData()));
            Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            savePicture(path);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(mActivity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
