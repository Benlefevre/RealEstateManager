package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.dao.PicturesDao;
import com.openclassrooms.realestatemanager.data.entities.Pictures;

import java.util.List;

public class PicturesDataRepository {

    private PicturesDao mPicturesDao;

    public PicturesDataRepository(PicturesDao picturesDao){
        mPicturesDao = picturesDao;
    }

//    Get
    public LiveData<List<Pictures>> getPictures(long realEstateId){
        return mPicturesDao.getPictures(realEstateId);
    }

//    Get
    public LiveData<Pictures> getOnePicture(long realEstateId){
        return mPicturesDao.getOnePicture(realEstateId);
    }

//    Create
    public long createRealEstatePicture(Pictures pictures){
        return mPicturesDao.insertPicture(pictures);
    }

//    Delete
    public void deletePicture(Pictures pictures){
        mPicturesDao.deletePicture(pictures);
    }
}
