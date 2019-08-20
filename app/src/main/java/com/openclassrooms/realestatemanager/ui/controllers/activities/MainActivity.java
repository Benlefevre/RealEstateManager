package com.openclassrooms.realestatemanager.ui.controllers.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AgentLocationFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.FullScreenFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.RealEstateListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.utils.Constants.ACCESS_LOCATION;
import static com.openclassrooms.realestatemanager.utils.Constants.AGENT_LOCATION_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.DETAILS_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.ESTATE_LIST_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.FULL_SCREEN_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements RealEstateListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_container)
    FrameLayout mContainer;
    @BindView(R.id.activity_home_nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_home_drawer)
    DrawerLayout mDrawer;

    private FragmentManager mFragmentManager;
    private Uri image_uri;
    private int mDisplayedFragment;
    private Boolean mLocationPermissionsGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getPermissionsExternalStorage();
        configureToolbar();
        configureDrawer();
        configureNavigationView();
        mFragmentManager = getSupportFragmentManager();
        displayMainFragment();
    }

    private void displayMainFragment() {
        mDisplayedFragment = 1;
        RealEstateListFragment realEstateListFragment = (RealEstateListFragment) mFragmentManager.findFragmentByTag(ESTATE_LIST_FRAGMENT);
        if (realEstateListFragment == null) {
            mFragmentManager.beginTransaction().add(R.id.activity_main_container,
                    RealEstateListFragment.newInstance(), ESTATE_LIST_FRAGMENT)
                    .commit();
        }
    }

    private void configureNavigationView(){
        mNavView.setNavigationItemSelectedListener(this);
    }

    private void configureDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.open_drawer, R.string.close_drawer);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
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
            EasyPermissions.requestPermissions(this, getString(R.string.storage_rationale),
                    READ_AND_WRITE_EXTERNAL_STORAGE, perms);
        }
    }

    @AfterPermissionGranted(ACCESS_LOCATION)
    private void getPermissionsAccessLocation(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(!EasyPermissions.hasPermissions(this,perms)){
            mLocationPermissionsGranted = false;
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    ACCESS_LOCATION, perms);
        }else{
            mLocationPermissionsGranted = true;
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
        mDisplayedFragment = 2;
        DetailsFragment detailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag(DETAILS_FRAGMENT);
        if (detailsFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, DetailsFragment.newInstance(id), DETAILS_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(List<Pictures> pictures, Uri uri) {
        mDisplayedFragment = 3;
        FullScreenFragment fullScreenFragment = (FullScreenFragment) mFragmentManager.findFragmentByTag(FULL_SCREEN_FRAGMENT);
        if (fullScreenFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, FullScreenFragment.newInstance(pictures, uri), FULL_SCREEN_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.drawer_localisation:
                mDisplayedFragment = 4;
                getPermissionsAccessLocation();
                AgentLocationFragment agentLocationFragment = (AgentLocationFragment) mFragmentManager.findFragmentByTag(AGENT_LOCATION_FRAGMENT);
                if (agentLocationFragment == null){
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AgentLocationFragment.newInstance(mLocationPermissionsGranted),AGENT_LOCATION_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case R.id.drawer_setting:
                mDisplayedFragment = 5;
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
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


    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
