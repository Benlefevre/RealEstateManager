package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.openclassrooms.realestatemanager.controllers.R;
import com.openclassrooms.realestatemanager.controllers.fragments.DetailsFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.RealEstateListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements RealEstateListFragment.OnFragmentInteractionListener {

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_container)
    FrameLayout mContainer;

    private FragmentManager mFragmentManager;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermissionsExternalStorage();
        configureToolbar();
        mFragmentManager = getSupportFragmentManager();
        displayMainFragment();
    }

    private void displayMainFragment() {
        RealEstateListFragment realEstateListFragment = (RealEstateListFragment) mFragmentManager.findFragmentByTag("EstateListFragment");
        if (realEstateListFragment == null) {
            mFragmentManager.beginTransaction().add(R.id.activity_main_container,
                    RealEstateListFragment.newInstance(), "EstateListFragment").commit();
        }
    }

    private void configureToolbar() {
        mToolbar.setTitle("Real Estate Manager");
        setSupportActionBar(mToolbar);
    }

    /**
     * Opens the device's camera to take a picture and fetch the file's path
     */
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    /**
     * Opens an App to pick a picture and fetch the file's path
     */
    private void pickImageFromGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }

    @AfterPermissionGranted(READ_AND_WRITE_EXTERNAL_STORAGE)
    private void getPermissionsExternalStorage() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "We need this permissions to access pictures saved in your device.",
                    READ_AND_WRITE_EXTERNAL_STORAGE, perms);
        }
    }

//    @OnClick(R.id.photo_btn)
//    @AfterPermissionGranted(READ_AND_WRITE_EXTERNAL_STORAGE)
//    public void onViewClicked() {
//        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (EasyPermissions.hasPermissions(this, perms)) {
////                openCamera();
////            pickImageFromGallery();
//            getPictures(1);
//        } else {
//            EasyPermissions.requestPermissions(this, "We need this permissions to access to the camera and save your pictures.",
//                    READ_AND_WRITE_EXTERNAL_STORAGE, perms);
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onFragmentInteraction(long id) {
        DetailsFragment detailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag("DetailsFragment");
        if (detailsFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, DetailsFragment.newInstance(id), "DetailsFragment")
                    .addToBackStack("Fragment")
                    .commit();
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
//            Log.i("info", image_uri.toString());
//            mImageView.setImageURI(image_uri);
//        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            Log.i("info", String.valueOf(data.getData()));
//            mImageView.setImageURI(data.getData());
//        }
//    }
}
