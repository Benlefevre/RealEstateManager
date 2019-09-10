package com.openclassrooms.realestatemanager.ui.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AddRealEstateFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AgentLocationFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.FullScreenFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.RealEstateListFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.SearchFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.SettingsFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

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
import static com.openclassrooms.realestatemanager.utils.Constants.SEARCH_FRAGMENT;
import static com.openclassrooms.realestatemanager.utils.Constants.SETTING_FRAGMENT;

public class MainActivity extends AppCompatActivity implements RealEstateListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        AgentLocationFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

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
    private boolean isNetworkEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureToolbar();
        configureDrawer();
        configureNavigationView();
        mFragmentManager = getSupportFragmentManager();
        displayMainFragment();
        verifyIfNetworkAccessEnabled();
    }

    private void displayMainFragment(){
        displayFragmentAccordingToTheDirection(ESTATE_LIST_FRAGMENT);
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
        setSupportActionBar(mToolbar);
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
            displayFragmentAccordingToTheDirection(AGENT_LOCATION_FRAGMENT);
        }
    }

    private void verifyIfNetworkAccessEnabled() {
        isNetworkEnabled = Utils.isNetworkAccessEnabled(this);
        if (!isNetworkEnabled) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("A network access is needed")
                    .setMessage("The application need a network access to locate your position and to display map")
                    .setPositiveButton("Go to setting", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .show();
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
                getPermissionsAccessLocation();
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
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container,
                            RealEstateListFragment.newInstance(ESTATE_LIST_FRAGMENT), ESTATE_LIST_FRAGMENT)
                            .commit();
                } else {
                    mFragmentManager.popBackStackImmediate("Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, realEstateListFragment).commit();
                    Log.i("test", "displayFragmentAccordingToTheDirection: 1");
                }
                break;
            case DETAILS_FRAGMENT:
                mDisplayedFragment = 2;
                DetailsFragment detailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag(DETAILS_FRAGMENT);
                if (detailsFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, DetailsFragment.newInstance(mId), DETAILS_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                } else
                    Log.i("test", "displayFragmentAccordingToTheDirection: 2");
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
                isNetworkEnabled = Utils.isNetworkAccessEnabled(this);
                if (isNetworkEnabled) {
                    mDisplayedFragment = 4;
                    mFragmentManager.popBackStack();
                    AgentLocationFragment agentLocationFragment = (AgentLocationFragment) mFragmentManager.findFragmentByTag(AGENT_LOCATION_FRAGMENT);
                    if (agentLocationFragment == null) {
                        mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AgentLocationFragment.newInstance(mLocationPermissionsGranted), AGENT_LOCATION_FRAGMENT)
                                .addToBackStack("Fragment")
                                .commit();
                    } else {
                        mFragmentManager.beginTransaction().replace(R.id.activity_main_container, agentLocationFragment)
                                .addToBackStack("Fragment")
                                .commit();
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(mDrawer, "Please enabled network access", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Go to settings", view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
                    snackbar.setActionTextColor(getResources().getColor(R.color.colorSecondary));
                    snackbar.show();
                }
                break;
            case SETTING_FRAGMENT:
                mDisplayedFragment = 5;
                mFragmentManager.popBackStack();
                SettingsFragment settingsFragment = (SettingsFragment) mFragmentManager.findFragmentByTag(SETTING_FRAGMENT);
                if (settingsFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, new SettingsFragment(), SETTING_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                } else {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, settingsFragment).commit();
                }
                break;
            case ADD_REAL_ESTATE_FRAGMENT:
                mDisplayedFragment = 6;
                mFragmentManager.popBackStack();
                AddRealEstateFragment addRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(ADD_REAL_ESTATE_FRAGMENT);
                if (addRealEstateFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddRealEstateFragment.newInstance(), ADD_REAL_ESTATE_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                } else {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, addRealEstateFragment).commit();
                }
                break;
            case EDIT_REAL_ESTATE_FRAGMENT:
                mDisplayedFragment = 7;
                AddRealEstateFragment editRealEstateFragment = (AddRealEstateFragment) mFragmentManager.findFragmentByTag(EDIT_REAL_ESTATE_FRAGMENT);
                if (editRealEstateFragment == null) {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddRealEstateFragment.newInstance(mId), EDIT_REAL_ESTATE_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                } else {
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, editRealEstateFragment).commit();
                }
                break;
            case SEARCH_FRAGMENT:
                mDisplayedFragment = 8;
                mFragmentManager.popBackStack();
                SearchFragment searchFragment = (SearchFragment) mFragmentManager.findFragmentByTag(SEARCH_FRAGMENT);
                if (searchFragment == null){
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, SearchFragment.newInstance(),SEARCH_FRAGMENT)
                            .addToBackStack("Fragment")
                            .commit();
                }else
                    mFragmentManager.beginTransaction().replace(R.id.activity_main_container, searchFragment, SEARCH_FRAGMENT).commit();
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

    @Override
    public void passSearchedRealEstate() {
        mFragmentManager.beginTransaction().replace(R.id.activity_main_container,RealEstateListFragment.newInstance(SEARCH_FRAGMENT))
                .addToBackStack("Fragment")
                .commit();
    }
}
