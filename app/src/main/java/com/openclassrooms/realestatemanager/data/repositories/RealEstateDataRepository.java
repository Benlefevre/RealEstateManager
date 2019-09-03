package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;

import java.util.List;

public class RealEstateDataRepository {

    private final RealEstateDao mRealEstateDao;

    public RealEstateDataRepository(RealEstateDao realEstateDao){
        mRealEstateDao = realEstateDao;
    }


//    Get
    public LiveData<RealEstate> getRealEstate(long realEstateId){
        return mRealEstateDao.getRealEstate(realEstateId);
    }

//    Get
    public LiveData<List<RealEstate>> getAllRealEstate(){
        return mRealEstateDao.getAllRealEstate();
    }

//    Get
    public LiveData<List<RealEstate>> getRealEstateByZipcodeAndCountry(int zipcode, String countryCode){
        return mRealEstateDao.getRealEstateByZipcodeAndCountry(zipcode, countryCode);
    }

//    Create
    public long createRealEstate(RealEstate realEstate){
        return mRealEstateDao.insertRealEstate(realEstate);
    }

//    Update
    public int updateRealEstate(RealEstate realEstate){
        return mRealEstateDao.updateRealEstate(realEstate);
    }
}
