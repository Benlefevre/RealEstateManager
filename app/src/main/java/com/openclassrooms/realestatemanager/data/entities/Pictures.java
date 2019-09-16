package com.openclassrooms.realestatemanager.data.entities;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Property.class,
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
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_1"), "Facade", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_2"), "Facade 2", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_3"), "Living room", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_4"), "Living room 2", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_5"), "Bedroom", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_6"), "Bathroom", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_7"), "Bedroom 2", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_8"), "Living room 2", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7905_9"), "Terrace", 4),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_1"), "Facade", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_2"), "Living room", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_3"), "Kitchen", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_4"), "Kitchen 2", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_5"), "Bedroom", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_6"), "Bathroom", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p7105_7"), "Terrace", 5),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_2"), "Facade", 6),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_1"), "Lounge", 6),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_3"), "Lounge 2", 6),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_4"), "Lounge 3", 6),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_5"), "Lounge 4", 6),
                new Pictures(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/p700_6"), "Bedroom", 6),
        };
    }
}


