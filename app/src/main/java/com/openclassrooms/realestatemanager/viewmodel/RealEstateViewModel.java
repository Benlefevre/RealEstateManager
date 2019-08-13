package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repositories.PicturesDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

//    Repositories
    private final RealEstateDataRepository mRealEstateDataRepository;
    private final PicturesDataRepository mPicturesDataRepository;
    private final Executor mExecutor;


    public RealEstateViewModel(RealEstateDataRepository realEstateDataRepository, Executor executor,
                               PicturesDataRepository picturesDataRepository){
        mRealEstateDataRepository = realEstateDataRepository;
        mExecutor = executor;
        mPicturesDataRepository = picturesDataRepository;
    }

    public LiveData<List<RealEstate>> getAllRealEstate(){
        return mRealEstateDataRepository.getAllRealEstate();
    }

    public LiveData<RealEstate> getRealEstate(long realEstateId){
        return mRealEstateDataRepository.getRealEstate(realEstateId);
    }

    public LiveData<List<Pictures>> getPictures(long realEstateId){
        return mPicturesDataRepository.getPictures(realEstateId);
    }
}
