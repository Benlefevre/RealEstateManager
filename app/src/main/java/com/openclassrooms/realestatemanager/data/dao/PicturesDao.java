package com.openclassrooms.realestatemanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.entities.Pictures;

import java.util.List;

@Dao
public interface PicturesDao {

    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId")
    LiveData<List<Pictures>> getPictures(long realEstateId);

    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId LIMIT 1")
    LiveData<Pictures> getOnePicture(long realEstateId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPicture(Pictures pictures);

    @Insert
    void insertAll(Pictures... pictures);

    @Delete
    void deletePicture (Pictures pictures);

}
