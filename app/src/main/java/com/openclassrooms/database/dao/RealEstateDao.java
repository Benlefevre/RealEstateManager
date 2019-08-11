package com.openclassrooms.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.models.RealEstate;

@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate WHERE mId = :mId")
    LiveData<RealEstate> getRealEstate(long mId);

    @Insert
    long insertRealEstate(RealEstate realEstate);

    @Update
    int updateRealEstate(RealEstate realEstate);
}
