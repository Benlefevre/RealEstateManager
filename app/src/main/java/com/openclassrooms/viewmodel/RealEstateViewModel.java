package com.openclassrooms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.data.entities.RealEstate;
import com.openclassrooms.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

//    Repositories
    private final RealEstateDataRepository mRealEstateDataRepository;
    private final Executor mExecutor;

//    Data
    private LiveData<RealEstate> currentRealEstate;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataRepository, Executor executor){
        mRealEstateDataRepository = realEstateDataRepository;
        mExecutor = executor;
    }

    public LiveData<RealEstate> getRealEstate(long realEstateId){
        return mRealEstateDataRepository.getRealEstate(realEstateId);
    }
}
