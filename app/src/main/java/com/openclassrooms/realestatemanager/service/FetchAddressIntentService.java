package com.openclassrooms.realestatemanager.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constants.LOCATION_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.RECEIVER_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.RESULT_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.RESULT_FAILURE;
import static com.openclassrooms.realestatemanager.utils.Constants.RESULT_SUCCESS;

public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("name");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this);
        if (intent == null)
            return;
        Location location = intent.getParcelableExtra(LOCATION_EXTRA);
        mReceiver = intent.getParcelableExtra(RECEIVER_EXTRA);
        List<Address> addresses = null;
        try{
            if (location != null && location.getLatitude() != 0 && location.getLongitude()!= 0){
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);}
        }catch (IOException | IllegalArgumentException exception){
            exception.getStackTrace();
        }
        if (addresses == null || addresses.size() == 0){
            Log.i("info", "onHandleIntent: no address found");
            deliverResultToReceiver(RESULT_FAILURE,"no address found");
        }else{
            Address address = addresses.get(0);
            deliverResultToReceiver(RESULT_SUCCESS, address.getCountryCode() + " / " + address.getPostalCode());
        }

    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_EXTRA, message);
        mReceiver.send(resultCode,bundle);
    }
}
