package com.openclassrooms.realestatemanager.ui.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AddPropertyFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.AgentLocationFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.FullScreenFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.PropertyListFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.SearchFragment;
import com.openclassrooms.realestatemanager.ui.controllers.fragments.SettingsFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Objects;

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

public class MainActivity extends AppCompatActivity implements PropertyListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        AgentLocationFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        AddPropertyFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_container)
    FrameLayout mContainer;
    @Nullable
    @BindView(R.id.activity_main_container2)
    FrameLayout mContainer2;
    @Nullable
    @BindView(R.id.guideline5)
    Guideline mGuideline;
    @BindView(R.id.activity_home_nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_home_drawer)
    DrawerLayout mDrawer;

    private FragmentManager mFragmentManager;
    private DetailsFragment mDetailsFragment;
    private long mId;
    private boolean isNetworkEnabled;
    private boolean isTabletLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureToolbar();
        configureDrawer();
        configureNavigationView();
        mFragmentManager = getSupportFragmentManager();
        verifyIfNetworkAccessEnabled();
//        Verifies if the device is a tablet and if it's in landscape
        isTabletLand = getResources().getBoolean(R.bool.isTabletLand);
        if (savedInstanceState == null)
            displayMainFragment();
        if (isTabletLand)
            setWeightForHideDetailsFragment();
    }

    /**
     * Displays the main fragment when opening the application.
     */
    private void displayMainFragment() {
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

    /**
     * Check if permission for location is granted with EasyPermission and request it if this is not the case.
     * If permissions are granted, the location fragment of the agent is displayed.
     */
    @AfterPermissionGranted(ACCESS_LOCATION)
    private void getPermissionsAccessLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    ACCESS_LOCATION, perms);
        } else {
            displayFragmentAccordingToTheDirection(AGENT_LOCATION_FRAGMENT);
        }
    }

    /**
     * Check with the isNetworkAccessEnabled method if network access is allowed or if network
     * coverage is present. If this is not the case, then the user is informed and he is offered
     * to go to the network settings.
     */
    private void verifyIfNetworkAccessEnabled() {
        isNetworkEnabled = Utils.isNetworkAccessEnabled(this);
        if (!isNetworkEnabled) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.network_needed))
                    .setMessage(getString(R.string.network_to_display_map))
                    .setPositiveButton(getString(R.string.go_to_setting), (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Needed code to use EasyPermission.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Displays the correct fragment based on the selected item
     */
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

    /**
     * Call the correct method to display the correct fragment according to the desired destination and
     * manages the FragmentManager BackStack.
     */
    private void displayFragmentAccordingToTheDirection(String direction) {
        switch (direction) {
            case ESTATE_LIST_FRAGMENT:
                displayPropertyListFragment();
                break;
            case DETAILS_FRAGMENT:
                displayDetailsFragment();
                break;
            case FULL_SCREEN_FRAGMENT:
                displayFullScreenFragment();
                break;
            case AGENT_LOCATION_FRAGMENT:
                isNetworkEnabled = Utils.isNetworkAccessEnabled(this);
                if (isNetworkEnabled) {
                    mFragmentManager.popBackStack();
                    displayAgentLocationFragment();
                } else {
                    Snackbar snackbar = Snackbar.make(mDrawer, "Please enabled network access", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Go to settings", view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
                    snackbar.setActionTextColor(getResources().getColor(R.color.colorSecondary));
                    snackbar.show();
                }
                break;
            case SETTING_FRAGMENT:
                mFragmentManager.popBackStack();
                displaySettingFragment();
                break;
            case ADD_REAL_ESTATE_FRAGMENT:
                mFragmentManager.popBackStack();
                displayAddPropertyFragment();
                break;
            case EDIT_REAL_ESTATE_FRAGMENT:
                displayAddPropertyFragmentToEdit();
                break;
            case SEARCH_FRAGMENT:
                mFragmentManager.popBackStack();
                displaySearchFragment();
                break;
        }
    }

    /**
     * Displays the Search fragment in the right FrameLayout.
     */
    private void displaySearchFragment() {
        SearchFragment searchFragment = (SearchFragment) mFragmentManager.findFragmentByTag(SEARCH_FRAGMENT);
        if (searchFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, SearchFragment.newInstance(), SEARCH_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    /**
     * Displays the AddProperty fragment (with a Property's id in the factory method) in the right FrameLayout
     */
    private void displayAddPropertyFragmentToEdit() {
        AddPropertyFragment editRealEstateFragment = (AddPropertyFragment) mFragmentManager.findFragmentByTag(EDIT_REAL_ESTATE_FRAGMENT);
        if (editRealEstateFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddPropertyFragment.newInstance(mId), EDIT_REAL_ESTATE_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    /**
     * Displays the AddProperty fragment (without a Property's id in the factory method) in the right FrameLayout
     */
    private void displayAddPropertyFragment() {
        AddPropertyFragment addPropertyFragment = (AddPropertyFragment) mFragmentManager.findFragmentByTag(ADD_REAL_ESTATE_FRAGMENT);
        if (addPropertyFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AddPropertyFragment.newInstance(), ADD_REAL_ESTATE_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    /**
     * Displays the SettingFragment in the right FrameLayout
     */
    private void displaySettingFragment() {
        SettingsFragment settingsFragment = (SettingsFragment) mFragmentManager.findFragmentByTag(SETTING_FRAGMENT);
        if (settingsFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, new SettingsFragment(), SETTING_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    /**
     * Displays the AgentLocationFragment in the right FrameLayout
     */
    private void displayAgentLocationFragment() {
        AgentLocationFragment agentLocationFragment = (AgentLocationFragment) mFragmentManager.findFragmentByTag(AGENT_LOCATION_FRAGMENT);
        if (agentLocationFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, AgentLocationFragment.newInstance(), AGENT_LOCATION_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
    }

    /**
     * Displays the FullScreenFragment in the right FrameLayout and if it's a tablet in landscape mode
     * then it calls the method to display the fragment in full screen.
     */
    private void displayFullScreenFragment() {
        FullScreenFragment fullScreenFragment = (FullScreenFragment) mFragmentManager.findFragmentByTag(FULL_SCREEN_FRAGMENT);
        if (fullScreenFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container, FullScreenFragment.newInstance(), FULL_SCREEN_FRAGMENT)
                    .addToBackStack("Fragment")
                    .commit();
        }
        if (isTabletLand)
            setWeightForHideDetailsFragment();
    }

    /**
     * Displays the DetailsFragment in the right FrameLayout
     */
    private void displayDetailsFragment() {
        mDetailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag(DETAILS_FRAGMENT);
        if (mDetailsFragment == null) {
            mDetailsFragment = DetailsFragment.newInstance();
            if (!isTabletLand) {
                mFragmentManager.beginTransaction().replace(R.id.activity_main_container, mDetailsFragment, DETAILS_FRAGMENT)
                        .addToBackStack("Fragment")
                        .commit();
            } else {
                setWeightToShowDetailsFragment();
                mFragmentManager.beginTransaction().replace(R.id.activity_main_container2, mDetailsFragment, DETAILS_FRAGMENT + 1)
                        .commit();
            }
        }
    }

    /**
     * Displays the PropertyListFragment in the right FrameLayout.
     */
    private void displayPropertyListFragment() {
        PropertyListFragment propertyListFragment = (PropertyListFragment) mFragmentManager.findFragmentByTag(ESTATE_LIST_FRAGMENT);
        if (propertyListFragment == null) {
            mFragmentManager.beginTransaction().replace(R.id.activity_main_container,
                    PropertyListFragment.newInstance(ESTATE_LIST_FRAGMENT), ESTATE_LIST_FRAGMENT)
                    .commit();
        } else {
            mFragmentManager.popBackStackImmediate("Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Checks if mDetailsFragment is in the second FrameLayout and remove it in portrait mode.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDetailsFragment = (DetailsFragment) mFragmentManager.findFragmentByTag(DETAILS_FRAGMENT + 1);
        if (!isTabletLand && mDetailsFragment != null)
            mFragmentManager.beginTransaction().remove(mDetailsFragment).commit();
    }

    /**
     * Changes the position of the guideline in tablet mode to display a fragment in full screen
     */
    private void setWeightForHideDetailsFragment() {
        if (isTabletLand) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(mGuideline).getLayoutParams();
            params.guidePercent = 1.0F;
            mGuideline.setLayoutParams(params);
        }
    }

    /**
     * Changes the position of the guideline in tablet mode to display the 2 fragments by sharing the screen
     */
    private void setWeightToShowDetailsFragment() {
        if (isTabletLand) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(mGuideline).getLayoutParams();
            params.guidePercent = 0.4F;
            mGuideline.setLayoutParams(params);
        }
    }

    /**
     * Checks if a fragment is full screen and calls the setWeightToShowDetailsFragment method if necessary.
     */
    private void checkWeight() {
        if (isTabletLand) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(mGuideline).getLayoutParams();
            if (params.guidePercent == 1F)
                setWeightToShowDetailsFragment();
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

//    ----------------------------------------------------------------------------------------------
//                                      Fragments callbacks
//    ----------------------------------------------------------------------------------------------

    /**
     * This Callback is needed to display fragments in full screen in landscape mode on tablets.
     */
    @Override
    public void checkVisibility(String destination) {
        if (DETAILS_FRAGMENT.equals(destination)) {
            if (isTabletLand) {
                checkWeight();
                displayFragmentAccordingToTheDirection(destination);
            }
        } else {
            setWeightForHideDetailsFragment();
        }
    }

    /**
     * Displays the list of the searched properties in a PropertyListFragment.
     */
    @Override
    public void passSearchedRealEstate() {
        mFragmentManager.beginTransaction().replace(R.id.activity_main_container,
                PropertyListFragment.newInstance(SEARCH_FRAGMENT))
                .addToBackStack("Fragment")
                .commit();
    }

    /**
     * Dispatches the user's click in PropertyListFragment's Toolbar according to the destination
     * to open the right Fragment.
     */
    @Override
    public void onFragmentInteraction(String destination) {
        if (destination.equals(ADD_REAL_ESTATE_FRAGMENT)) {
            displayFragmentAccordingToTheDirection(ADD_REAL_ESTATE_FRAGMENT);
        } else if (destination.equals(SEARCH_FRAGMENT)) {
            displayFragmentAccordingToTheDirection(SEARCH_FRAGMENT);
        }
    }

    /**
     * Opens the DetailsFragment in the right FrameLayout and verifies the guideline position when user
     * clicks on a marker in a AgentLocationFragment or on a item in a PropertyListFragment.
     */
    @Override
    public void openDetailsFragment() {
        if (isTabletLand)
            checkWeight();
        displayFragmentAccordingToTheDirection(DETAILS_FRAGMENT);
    }

    /**
     * Open a FullScreenFragment when user clicks on a picture in a DetailsFragment.
     */
    @Override
    public void openFullScreenFragment() {
        displayFragmentAccordingToTheDirection(FULL_SCREEN_FRAGMENT);
    }

    /**
     * Open a AddPropertyFragment with a property's Id when user click on an item in the DetailsFragment's
     * Toolbar or in a PropertyListFragment in the landscape mode.
     */
    @Override
    public void openAddFragmentToEditRealEstate(long id) {
        mId = id;
        displayFragmentAccordingToTheDirection(EDIT_REAL_ESTATE_FRAGMENT);
    }

}
