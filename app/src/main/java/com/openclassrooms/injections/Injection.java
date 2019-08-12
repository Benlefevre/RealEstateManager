package com.openclassrooms.injections;

import android.content.Context;

import com.openclassrooms.data.database.RealEstateDatabase;
import com.openclassrooms.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static RealEstateDataRepository provideRealEstateDataSource(Context context){
        RealEstateDatabase realEstateDatabase = RealEstateDatabase.getInstance(context);
        return new RealEstateDataRepository(realEstateDatabase.mRealEstateDao());
    }

    public static Executor providerExcutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory providerViewModelFactory(Context context){
        RealEstateDataRepository realEstateDataRepository = provideRealEstateDataSource(context);
        Executor executor = providerExcutor();
        return new ViewModelFactory(realEstateDataRepository,executor);
    }
}
