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

    /**
     * Queries the database to get all the properties. Returns a LiveData<List<Property>>
     */
    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllRealEstate();

    /**
     * Queries the database to get the property whose primary key
     * is the id passed as parameter. Returns a LiveData<Property>.
     */
    @Query("SELECT * FROM Property WHERE mId = :id")
    LiveData<Property> getRealEstate(long id);

    /**
     * Queries the database to get the properties having as attributes the postal code
     * and the country code passed in parameters. Returns a LiveData<Property>
     */
    @Query("SELECT * FROM Property WHERE mCity = :cityName AND mCountryCode = :countryCode")
    LiveData<List<Property>> getPropertiesByCityAndCountry(String cityName, String countryCode);

    /**
     * Queries the database to get the properties having as attributes the query contents passed
     * in parameters. Used to search properties according to the user criteria. Returns a LiveData<List<Property>>.
     */
    @RawQuery(observedEntities = {Property.class, Pictures.class})
    LiveData<List<Property>> getPropertiesAccordingUserSearch(SupportSQLiteQuery query);

    /**
     * Inserts in database the property passed in parameter. Returns the id (primary key) of the inserted
     * property.
     */
    @Insert
    long insertProperty(Property property);

    /**
     * Inserts in database all the properties passes in parameters. Used to populated the database when
     * tha application launches.
     */
    @Insert
    void insertAll(Property... properties);

    /**
     * Updates an existing property with the attributes of the property passed as parameters.
     */
    @Update
    int updateProperty(Property property);

    /**
     * Queries the database to get the property whose primary key
     * is the id passed as parameter. Returns a Cursor used with ContentProvider.
     */
    @Query("SELECT * FROM Property WHERE mid = :id")
    Cursor getRealEstateWithCursor(long id);

    /**
     * Queries the database to get all the properties. Returns a Cursor used with ContentProvider.
     */
    @Query("SELECT * FROM Property")
    Cursor getRealEstateWithCursor();
}
