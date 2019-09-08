package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

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

//    Get
    public LiveData<List<RealEstate>> getRealEstateAccordingUserSearch(SupportSQLiteQuery query){
        return mRealEstateDao.getRealEstateAccordingUserSearch(query);
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
