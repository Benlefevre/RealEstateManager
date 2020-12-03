package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.entities.Property;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao mPropertyDao;

    public PropertyDataRepository(PropertyDao propertyDao){
        mPropertyDao = propertyDao;
    }

//    Get
    public LiveData<Property> getRealEstate(long realEstateId){
        return mPropertyDao.getRealEstate(realEstateId);
    }

//    Get
    public LiveData<List<Property>> getAllRealEstate(){
        return mPropertyDao.getAllRealEstate();
    }

//    Get
    public LiveData<List<Property>> getRealEstateByCityAndCountry(String cityName, String countryCode){
        return mPropertyDao.getPropertiesByCityAndCountry(cityName, countryCode);
    }

//    Get
    public LiveData<List<Property>> getRealEstateAccordingUserSearch(SupportSQLiteQuery query){
        return mPropertyDao.getPropertiesAccordingUserSearch(query);
    }

//    Create
    public long createRealEstate(Property property){
        return mPropertyDao.insertProperty(property);
    }

//    Update
    public int updateRealEstate(Property property){
        return mPropertyDao.updateProperty(property);
    }
}
