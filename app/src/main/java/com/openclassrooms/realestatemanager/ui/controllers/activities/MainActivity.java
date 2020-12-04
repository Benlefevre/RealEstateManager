package com.openclassrooms.realestatemanager.ui.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.controllers.TakeOrNotFullScreen;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.utils.Constants.ACCESS_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TakeOrNotFullScreen {

    private ActivityMainBinding binding;
    private boolean isNetworkEnabled;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureNavigation();
        verifyIfNetworkAccessEnabled();
    }

    private void configureNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            mNavController = navHostFragment.getNavController();
        }
        setSupportActionBar(binding.activityMainToolbar.getRoot());
        NavigationUI.setupActionBarWithNavController(this, mNavController, binding.activityHomeDrawer);
        NavigationUI.setupWithNavController(binding.activityMainToolbar.getRoot(), mNavController, binding.activityHomeDrawer);
        NavigationUI.setupWithNavController(binding.activityHomeNavView, mNavController);

        binding.activityHomeNavView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, mNavController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.mapFragment) {
            verifyIfNetworkAccessEnabled();
            if (isNetworkEnabled) {
                getPermissionsAccessLocation();
            }
        } else if (itemId == R.id.addPropertyFragment) {
            mNavController.navigate(R.id.addPropertyFragment,null,getNavOptions());
            binding.activityHomeDrawer.close();
        } else if (itemId == R.id.searchFragment) {
            mNavController.navigate(R.id.searchFragment,null,getNavOptions());
            binding.activityHomeDrawer.close();
        } else if (itemId == R.id.settingsFragment) {
            mNavController.navigate(R.id.settingsFragment,null,getNavOptions());
            binding.activityHomeDrawer.close();
        }
        return true;
    }

    private NavOptions getNavOptions(){
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .build();
    }

    /**
     * Check if permission for location is granted with EasyPermission and request it if this is not the case.
     * If permissions are granted, the location fragment of the agent is displayed.
     */
    @AfterPermissionGranted(ACCESS_LOCATION)
    private void getPermissionsAccessLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mNavController.navigate(R.id.agentLocationFragment,null,getNavOptions());
            binding.activityHomeDrawer.closeDrawer(GravityCompat.START);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    ACCESS_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Needed code to use EasyPermission.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    /**
     * Changes the position of the guideline in tablet mode to display a fragment in full screen
     */
    private void setWeightForHideListFragment() {
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(binding.guideline5).getLayoutParams();
            params.guidePercent = 0.0F;
            binding.guideline5.setLayoutParams(params);
        }
    }

    /**
     * Changes the position of the guideline in tablet mode to display the 2 fragments by sharing the screen
     */
    private void setWeightToShowListFragment() {
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(binding.guideline5).getLayoutParams();
            params.guidePercent = 0.4F;
            binding.guideline5.setLayoutParams(params);
        }
    }

    /**
     * Checks if a fragment is full screen and calls the setWeightToShowDetailsFragment method if necessary.
     */
    private void setWeightToHideDetailsFragment() {
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) Objects.requireNonNull(binding.guideline5).getLayoutParams();
            params.guidePercent = 1.0f;
            binding.guideline5.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.activityHomeDrawer.isDrawerOpen(GravityCompat.START))
            binding.activityHomeDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mNavController.navigateUp();
        return super.onSupportNavigateUp();
    }


    @Override
    public void takeFullScreenPicture() {
        setWeightForHideListFragment();
    }

    @Override
    public void takeFullScreenFragment() {
        setWeightToHideDetailsFragment();
    }

    @Override
    public void doNotTakeFullScreen() {
        setWeightToShowListFragment();
    }
}
