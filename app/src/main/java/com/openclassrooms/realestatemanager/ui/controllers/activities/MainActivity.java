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
import com.openclassrooms.realestatemanager.ui.controllers.fragments.SettingsFragment;

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
import static com.openclassrooms.realestatemanager.utils.Constants.SEARCH_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.SETTING_FRAGMENT;

public class MainActivity extends AppCompatActivity implements RealEstateListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        AgentLocationFragment.OnFragmentInteractionListener {

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
    private long mId;
    private List<Pictures> mPicturesList;
    private Uri mUri;

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
        displayFragmentAccordingToTheDirection(ESTATE_LIST_FRAGMENT);
//        displayMainFragment();
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

    private void configureNavigationView() {
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
    private void getPermissionsAccessLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            mLocationPermissionsGranted = false;
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    ACCESS_LOCATION, perms);
        } else {
            mLocationPermissionsGranted = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onFragmentInteraction(String destination) {
        if (destination.equals(ADD_REAL_ESTATE_FRAGMENT)) {
            displayFragmentAccordingToTheDirection(ADD_REAL_ESTATE_FRAGMENT);
        } else if (destination.equals(SEARCH_FRAGMENT)) {
            displayFragmentAccordingToTheDirection(SEARCH_FRAGMENT);
        }
    }

    @Override
    public void onFragmentInteraction(long id) {
        mId = id;
        displayFragmentAccordingToTheDirection(DETAILS_FRAGMENT);
    }

    @Override
    public void onFragmentInteraction(List<Pictures> pictures, Uri uri) {
        mPicturesList = pictures;
        mUri = uri;
        displayFragmentAccordingToTheDirection(FULL_SCREEN_FRAGMENT);
    }

    @Override
    public void onFragmentInteractionEdit(long id) {
        mId = id;
        displayFragmentAccordingToTheDirection(EDIT_REAL_ESTATE_FRAGMENT);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_add:
                displayFragmentAccordingToTheDirection(ADD_REAL_ESTATE_FRAGMENT);
                break;
            case R.id.drawer_list:
                displayFragmentAccordingToTheDirection(ESTATE_LIST_FRAGMENT);
                break;
            case R.id.drawer_localisation:
                displayFragmentAccordingToTheDirection(AGENT_LOCATION_FRAGMENT);
                break;
            case R.id.drawer_search:
                displayFragmentAccordingToTheDirection(SEARCH_FRAGMENT);
                break;
            case R.id.drawer_setting:
                displayFragmentAccordingToTheDirection(SETTING_FRAGMENT);
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayFragmentAccordingToTheDirection(String direction) {
        switch (direction) {
            case ESTATE_LIST_FRAGMENT:
                mDisplayedFragment = 1;
                RealEstateListFragment realEstateListFragment = (RealEstateListFragment) mFragmentManager.findFragmentByTag(ESTATE_LIST_FRAGMENT);
                if (realEstateListFragment == null) {
                    mFragmentManager.beginTransaction().add(R.id.activity_main_container,
                            RealEstateListFragment.newInstance(), ESTATE_LIST_FRAGMENT)
                            .commit();
                }
                break;
            case DETAILS_FRAGMENT:
                mDisplayedFragment = 2;
                DetailsFragment detailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag(DETAILS_FRAGMENT);
                if (detailsFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, DetailsFragment.newInstance(mId), DETAILS_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case FULL_SCREEN_FRAGMENT:
                mDisplayedFragment = 3;
                FullScreenFragment fullScreenFragment = (FullScreenFragment) mFragmentManager.findFragmentByTag(FULL_SCREEN_FRAGMENT);
                if (fullScreenFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, FullScreenFragment.newInstance(mPicturesList, mUri), FULL_SCREEN_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case AGENT_LOCATION_FRAGMENT:
                mDisplayedFragment = 4;
                getPermissionsAccessLocation();
                AgentLocationFragment agentLocationFragment = (AgentLocationFragment) mFragmentManager.findFragmentByTag(AGENT_LOCATION_FRAGMENT);
                if (agentLocationFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AgentLocationFragment.newInstance(mLocationPermissionsGranted), AGENT_LOCATION_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case SETTING_FRAGMENT:
                mDisplayedFragment = 5;
                SettingsFragment settingsFragment = (SettingsFragment) mFragmentManager.findFragmentByTag(SETTING_FRAGMENT);
                if (settingsFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, new SettingsFragment(), SETTING_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case ADD_REAL_ESTATE_FRAGMENT:
                mDisplayedFragment = 6;
                AddRealEstateFragment addRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(ADD_REAL_ESTATE_FRAGMENT);
                if (addRealEstateFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddRealEstateFragment.newInstance(), ADD_REAL_ESTATE_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case EDIT_REAL_ESTATE_FRAGMENT:
                mDisplayedFragment = 7;
                AddRealEstateFragment editRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(EDIT_REAL_ESTATE_FRAGMENT);
                if (editRealEstateFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddRealEstateFragment.newInstance(mId), EDIT_REAL_ESTATE_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }
                break;
            case SEARCH_FRAGMENT:
                mDisplayedFragment = 8;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
