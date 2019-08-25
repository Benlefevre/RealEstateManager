package com.openclassrooms.realestatemanager.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public LiveData<List<RealEstate>> getRealEstateByZipcodeAndCountry(int zipcode, String countryCode){
        return mRealEstateDataRepository.getRealEstateByZipcodeAndCountry(zipcode, countryCode);
    }

    public LiveData<List<Pictures>> getPictures(long realEstateId){
        return mPicturesDataRepository.getPictures(realEstateId);
    }

    public LiveData<Pictures> getOnePicture(long realEstateId){
        return mPicturesDataRepository.getOnePicture(realEstateId);
    }

    public long createRealEstate(RealEstate realEstate){
        Callable<Long> insertCallable = () -> mRealEstateDataRepository.createRealEstate(realEstate);
        long rowId = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return rowId;
    }

    public void createPictures(Pictures pictures){
        mExecutor.execute(() -> mPicturesDataRepository.createRealEstatePicture(pictures));
    }
}
