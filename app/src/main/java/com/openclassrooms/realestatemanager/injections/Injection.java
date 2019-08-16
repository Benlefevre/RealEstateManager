package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.data.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.data.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static RealEstateDataRepository provideRealEstateDataSource(Context context){
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RealEstateDataRepository(realEstateDatabase.mRealEstateDao());
    }

    public static PicturesDataRepository providePicturesDataSource(Context context){
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new PicturesDataRepository(realEstateDatabase.mRealEstatePicturesDao());
    }

    public static Executor providerExcutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory providerViewModelFactory(Context context){
        RealEstateDataRepository realEstateDataRepository = provideRealEstateDataSource(context);
        PicturesDataRepository picturesDataRepository = providePicturesDataSource(context);
        Executor executor = providerExcutor();
        return new ViewModelFactory(realEstateDataRepository,executor, picturesDataRepository);
    }
}
