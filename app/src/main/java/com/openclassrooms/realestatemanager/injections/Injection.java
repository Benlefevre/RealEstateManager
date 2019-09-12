package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.data.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static RealEstateDataRepository provideRealEstateDataSource(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RealEstateDataRepository(realEstateDatabase.mRealEstateDao());
    }

    private static PicturesDataRepository providePicturesDataSource(Context context) {
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new PicturesDataRepository(realEstateDatabase.mRealEstatePicturesDao());
    }

    private static Executor providerExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory providerViewModelFactory(Context context) {
        RealEstateDataRepository realEstateDataRepository = provideRealEstateDataSource(context);
        PicturesDataRepository picturesDataRepository = providePicturesDataSource(context);
        Executor executor = providerExecutor();
        return new ViewModelFactory(realEstateDataRepository, executor, picturesDataRepository);
    }
}
