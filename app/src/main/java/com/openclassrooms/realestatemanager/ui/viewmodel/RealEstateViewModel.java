package com.openclassrooms.realestatemanager.ui.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.PropertyDataRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RealEstateViewModel extends ViewModel {

//    Repositories
    private final PropertyDataRepository mPropertyDataRepository;
    private final PicturesDataRepository mPicturesDataRepository;
    private final Executor mExecutor;
    private ExecutorService mExecutorService;

    private MutableLiveData<List<Property>> mRealEstateList = new MutableLiveData<>();
    private MutableLiveData<Long> mSelectedRealEstateId = new MutableLiveData<>();
    private MutableLiveData<List<Uri>> mUriList = new MutableLiveData<>();

    public RealEstateViewModel(PropertyDataRepository propertyDataRepository, Executor executor,
                               PicturesDataRepository picturesDataRepository){
        mPropertyDataRepository = propertyDataRepository;
        mExecutor = executor;
        mPicturesDataRepository = picturesDataRepository;
    }

    public LiveData<List<Property>> getAllRealEstate(){
        return mPropertyDataRepository.getAllRealEstate();
    }

    public LiveData<Property> getRealEstate(long realEstateId){
        return mPropertyDataRepository.getRealEstate(realEstateId);
    }

    public LiveData<List<Property>> getRealEstateByZipcodeAndCountry(int zipcode, String countryCode){
        return mPropertyDataRepository.getRealEstateByZipcodeAndCountry(zipcode, countryCode);
    }

    public LiveData<List<Property>> getRealEstateAccordingUserSearch(SupportSQLiteQuery query){
        return mPropertyDataRepository.getRealEstateAccordingUserSearch(query);
    }

    public LiveData<List<Pictures>> getPictures(long realEstateId){
        return mPicturesDataRepository.getPictures(realEstateId);
    }

    public LiveData<Pictures> getOnePicture(long realEstateId){
        return mPicturesDataRepository.getOnePicture(realEstateId);
    }

    public long createRealEstate(Property property){
        Callable<Long> insertCallable = () -> mPropertyDataRepository.createRealEstate(property);
        long rowId = 0;

        mExecutorService = Executors.newFixedThreadPool(1);
        Future<Long> future = mExecutorService.submit(insertCallable);
        try {
            rowId = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return rowId;
    }

    public long createPictures(Pictures pictures){
        Callable<Long> insertCallable = () -> mPicturesDataRepository.createRealEstatePicture(pictures);
        long rowId = 0;

        mExecutorService = Executors.newFixedThreadPool(1);
        Future<Long> future = mExecutorService.submit(insertCallable);
        try {
            rowId = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return rowId;
    }

    public int updateRealEstate(Property property){
        Callable<Integer> insertCallable = () -> mPropertyDataRepository.updateRealEstate(property);
        int nbRows = 0;

        mExecutorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = mExecutorService.submit(insertCallable);
        try {
            nbRows = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return nbRows;
    }

    public void deletePicture(Pictures pictures){
        mExecutor.execute(() -> mPicturesDataRepository.deletePicture(pictures));
    }

    public void addRealEstateList(List<Property> properties){
        mRealEstateList.setValue(properties);
    }

    public LiveData<List<Property>> getRealEstateList(){
        return mRealEstateList;
    }

    public void addSelectedRealEstateId(long realEstateId){
        mSelectedRealEstateId.setValue(realEstateId);
    }

    public LiveData<Long> getSelectedRealEstateId(){
        return mSelectedRealEstateId;
    }

    public void addUriList(List<Uri> uriList){
        mUriList.setValue(uriList);
    }

    public LiveData<List<Uri>> getUriList(){
        return mUriList;
    }

}
