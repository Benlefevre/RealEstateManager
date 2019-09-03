package com.openclassrooms.realestatemanager.ui.controllers.activities;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
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
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AddRealEstateFragment;
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
import static com.openclassrooms.realestatemanager.utils.Constants.ADD_REAL_ESTATE_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.AGENT_LOCATION_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.DETAILS_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.EDIT_REAL_ESTATE_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.ESTATE_LIST_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.FULL_SCREEN_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.READ_AND_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements RealEstateListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        AgentLocationFragment.OnFragmentInteractionListener{

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_container)
    FrameLayout mContainer;
    @BindView(R.id.activity_home_nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_home_drawer)
    DrawerLayout mDrawer;

    private FragmentManager mFragmentManager;
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
    public void onFragmentInteractionEdit(long id) {
        AddRealEstateFragment editRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(EDIT_REAL_ESTATE_FRAGMENT);
        if (editRealEstateFragment == null){
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddRealEstateFragment.newInstance(id),EDIT_REAL_ESTATE_FRAGMENT)
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
            case R.id.drawer_add:
                mDisplayedFragment = 6;
                AddRealEstateFragment addRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(ADD_REAL_ESTATE_FRAGMENT);
                if (addRealEstateFragment == null){
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container,AddRealEstateFragment.newInstance(),ADD_REAL_ESTATE_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
