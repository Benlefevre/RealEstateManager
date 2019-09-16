package com.openclassrooms.realestatemanager.data.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllRealEstate();

    @Query("SELECT * FROM Property WHERE mId = :id")
    LiveData<Property> getRealEstate(long id);

    @Query("SELECT * FROM Property WHERE mZipCode = :zipCode AND mCountryCode = :countryCode")
    LiveData<List<Property>> getRealEstateByZipcodeAndCountry(int zipCode, String countryCode);

    @RawQuery(observedEntities = {Property.class, Pictures.class})
    LiveData<List<Property>> getRealEstateAccordingUserSearch(SupportSQLiteQuery query);

    @Insert
    long insertRealEstate(Property property);

    @Insert
    void insertAll(Property... properties);

    @Update
    int updateRealEstate(Property property);

    @Query("SELECT * FROM Property WHERE mid = :id")
    Cursor getRealEstateWithCursor(long id);

    @Query("SELECT * FROM Property")
    Cursor getRealEstateWithCursor();
}
