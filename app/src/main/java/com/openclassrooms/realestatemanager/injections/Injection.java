package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.PropertyDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static PropertyDataRepository provideRealEstateDataSource(Context context) {
        RealEstateManagerDatabase realEstateManagerDatabase = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(realEstateManagerDatabase.mPropertyDao());
    }

    private static PicturesDataRepository providePicturesDataSource(Context context) {
        RealEstateManagerDatabase realEstateManagerDatabase = RealEstateManagerDatabase.getInstance(context);
        return new PicturesDataRepository(realEstateManagerDatabase.mPicturesDao());
    }

    private static Executor providerExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory providerViewModelFactory(Context context) {
        PropertyDataRepository propertyDataRepository = provideRealEstateDataSource(context);
        PicturesDataRepository picturesDataRepository = providePicturesDataSource(context);
        Executor executor = providerExecutor();
        return new ViewModelFactory(propertyDataRepository, executor, picturesDataRepository);
    }
}
