package com.openclassrooms.realestatemanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.entities.RealEstate;

import java.util.List;

@Dao
public interface RealEstateDao {

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstate>> getAllRealEstate();

    @Query("SELECT * FROM RealEstate WHERE mId = :id")
    LiveData<RealEstate> getRealEstate(long id);

    @Query("SELECT * FROM RealEstate WHERE mZipCode = :zipCode AND mCountryCode = :countryCode")
    LiveData<List<RealEstate>> getRealEstateByZipcodeAndCountry(int zipCode, String countryCode);

    @Insert
    long insertRealEstate(RealEstate realEstate);

    @Insert
    void insertAll(RealEstate... RealEstates);

    @Update
    int updateRealEstate(RealEstate realEstate);
}
