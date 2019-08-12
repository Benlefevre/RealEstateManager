package com.openclassrooms.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.data.dao.RealEstateDao;
import com.openclassrooms.data.entities.RealEstate;

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

//    Create
    public void createRealEstate(RealEstate realEstate){
        mRealEstateDao.insertRealEstate(realEstate);
    }

//    Update
    public void updateRealEstate(RealEstate realEstate){
        mRealEstateDao.updateRealEstate(realEstate);
    }
}
