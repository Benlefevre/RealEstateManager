package com.openclassrooms.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.data.dao.PicturesDao;
import com.openclassrooms.data.entities.Pictures;

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

//    Create
    public void createRealEstatePicture(Pictures pictures){
        mPicturesDao.insertPicture(pictures);
    }
}
