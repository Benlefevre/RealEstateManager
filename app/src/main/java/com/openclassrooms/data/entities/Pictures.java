package com.openclassrooms.data.entities;

import android.net.Uri;

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
}
