package com.openclassrooms.realestatemanager.data.entities;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RealEstate.class,
        parentColumns = "mId",
        childColumns = "mRealEstateId"))

public class Pictures {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private Uri mUri;
    private String mDescription;
    private long mRealEstateId;

    public Pictures(){}

    public Pictures(Uri uri, String description){
        mUri  = uri;
        mDescription = description;
    }

    public Pictures(Uri uri,String description , long realEstateId){
        mUri = uri;
        mDescription = description;
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

    public String getDescription() {
        return mDescription;
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

    public void setDescription(String description) {
        mDescription = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pictures{" +
                "mId=" + mId +
                ", mUri=" + mUri +
                ", mDescription='" + mDescription + '\'' +
                ", mRealEstateId=" + mRealEstateId +
                '}';
    }


    public static Pictures[] populateData(){
        return new Pictures[]{
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_1"),"Facade", 1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_2"), "Lounge", 1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_3"), "Kitchen", 1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p600_4"), "Bedroom", 1),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_1"), "Facade", 2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_2"), "Facade 2", 2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_3"), "Lounge", 2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p6124_4"), "Kitchen", 2),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_1"), "Facade", 3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_2"), "Lounge", 3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_3"), "Kitchen", 3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_4"), "Bedroom", 3),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p909_5"), "Terrace", 3),
        };
    }
}


