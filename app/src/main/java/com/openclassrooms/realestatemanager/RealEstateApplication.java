package com.openclassrooms.realestatemanager;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class RealEstateApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
