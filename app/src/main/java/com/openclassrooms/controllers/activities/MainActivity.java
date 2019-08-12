package com.openclassrooms.controllers.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.controllers.R;
import com.openclassrooms.injections.Injection;
import com.openclassrooms.injections.ViewModelFactory;
import com.openclassrooms.data.entities.RealEstate;
import com.openclassrooms.utils.Utils;
import com.openclassrooms.viewmodel.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.utils.Constants.RC_CAMERA_AND_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.activity_main_activity_text_view_main)
    TextView textViewMain;
    @BindView(R.id.activity_main_activity_text_view_quantity)
    TextView textViewQuantity;
    @BindView(R.id.ID)
    TextView mId;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.surface)
    TextView mSurface;
    @BindView(R.id.nbPiece)
    TextView mNbPiece;
    @BindView(R.id.nbBedrooms)
    TextView mNbBedrooms;
    @BindView(R.id.nbBathroom)
    TextView mNbBathroom;
    @BindView(R.id.photo_btn)
    Button mPhotoBtn;
    @BindView(R.id.imageView)
    ImageView mImageView;


    private RealEstateViewModel mRealEstateViewModel;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        configureViewModel();
        getRealEstate(1);
    }

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText("" + quantity);
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(this);
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstate(long id) {
        mRealEstateViewModel.getRealEstate(id).observe(this, this::configureTest);
    }

    private void configureTest(RealEstate realEstate) {
        mId.setText(realEstate.getId() + "");
        mAddress.setText(realEstate.getAddress());
        mSurface.setText(realEstate.getSurface() + "");
        mNbPiece.setText(realEstate.getNbPieces() + "");
        mNbBedrooms.setText(realEstate.getNbBedrooms() + "");
        mNbBathroom.setText(realEstate.getNbBathrooms() + "");
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void pickImageFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    @OnClick(R.id.photo_btn)
    @AfterPermissionGranted(RC_CAMERA_AND_WRITE_EXTERNAL_STORAGE)
    public void onViewClicked() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
//                openCamera();
            pickImageFromGallery();
        } else {
            EasyPermissions.requestPermissions(this, "We need this permissions to access to the camera and save your pictures.",
                    RC_CAMERA_AND_WRITE_EXTERNAL_STORAGE, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            Log.i("info", image_uri.toString());
            mImageView.setImageURI(image_uri);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Log.i("info", String.valueOf(data.getData()));
            mImageView.setImageURI(data.getData());
        }
    }
}
