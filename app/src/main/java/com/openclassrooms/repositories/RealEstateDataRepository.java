package com.openclassrooms.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.database.dao.RealEstateDao;
import com.openclassrooms.models.RealEstate;

public class RealEstateDataRepository {

    private final RealEstateDao mRealEstateDao;

    public RealEstateDataRepository(RealEstateDao realEstateDao){
        mRealEstateDao = realEstateDao;
    }


//    Get
    public LiveData<RealEstate> getRealEstate(long realEstateId){
        return mRealEstateDao.getRealEstate(realEstateId);
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
