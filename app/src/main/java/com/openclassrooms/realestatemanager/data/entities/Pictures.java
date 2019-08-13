package com.openclassrooms.realestatemanager.data.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.controllers.R;

@Entity(foreignKeys = @ForeignKey(entity = RealEstate.class,
        parentColumns = "mId",
        childColumns = "mRealEstateId"))

public class Pictures {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private Uri mUri;
    private long mRealEstateId;

    public Pictures(){}

    public Pictures(Uri uri, long realEstateId){
        mUri = uri;
        mRealEstateId = realEstateId;
    }

//    -------------------------------------- Getters -----------------------------------------------

    public long getId() {
        return mId;
    }

    public Uri getUri() {
        return mUri;
    }

    public long getRealEstateId() {
        return mRealEstateId;
    }


//    -------------------------------------- Setters -----------------------------------------------


    public void setId(long id) {
        mId = id;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public void setRealEstateId(long realEstateId) {
        mRealEstateId = realEstateId;
    }


    @Override
    public String toString() {
        return "Pictures{" +
                "mId=" + mId +
                ", mUri=" + mUri +
                ", mRealEstateId=" + mRealEstateId +
                '}';
    }


    public static Pictures[] populateData(){
        return new Pictures[]{
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_1"),1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_2"),1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_3"),1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_4"),1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_1"),2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_2"),2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_3"),2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_4"),2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_1"),3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_2"),3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_3"),3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_4"),3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_5"),3),
        };
    }
}


