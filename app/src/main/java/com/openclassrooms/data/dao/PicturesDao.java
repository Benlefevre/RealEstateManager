package com.openclassrooms.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.data.entities.Pictures;

import java.util.List;

@Dao
public interface PicturesDao {

    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId")
    LiveData<List<Pictures>> getPictures(long realEstateId);

    @Insert
    long insertPicture(Pictures pictures);

}
