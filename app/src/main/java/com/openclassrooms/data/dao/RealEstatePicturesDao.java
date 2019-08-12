package com.openclassrooms.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.data.entities.RealEstatePictures;

import java.util.List;

@Dao
public interface RealEstatePicturesDao {

    @Query("SELECT * FROM RealEstatePictures WHERE mRealEstateId = :realEstateId")
    LiveData<List<RealEstatePictures>> getPictures(long realEstateId);

    @Insert
    long insertPicture(RealEstatePictures realEstatePictures);

}
