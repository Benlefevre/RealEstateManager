package com.openclassrooms.realestatemanager.data.dao;

import android.database.Cursor;

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

    /**
     * Queries the database to get all the pictures having as attribute the property id passed
     * as parameter. Returns a LiveDat<List<Pictures>>
     */
    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId")
    LiveData<List<Pictures>> getPictures(long realEstateId);

    /**
     * Queries the database to obtain a single picture having as attribute the id of the property
     * passed in parameters and not being a video. Returns a LivaData<Pictures>
     */
    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId AND mUri NOT LIKE '%VID%' " +
            "AND mUri NOT LIKE '%Movies%' LIMIT 1")
    LiveData<Pictures> getOnePicture(long realEstateId);

    /**
     * Insert a pictures in database and return the id (primary key) of this insertion
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPicture(Pictures pictures);

    /**
     * Insert all the pictures passed in parameter in database. Used to populate the database
     * when the application launches
     */
    @Insert
    void insertAll(Pictures... pictures);

    /**
     * Deletes the pictures passed in parameter in the database.
     */
    @Delete
    void deletePicture (Pictures pictures);

    /**
     * Queries the database to get all the pictures having as attribute the property id passed
     * as parameter. Returns a Cursor used with the ContentProvider.
     */
    @Query("SELECT * FROM Pictures WHERE mRealEstateId = :realEstateId")
    Cursor getPicturesWithCursor(long realEstateId);
}
