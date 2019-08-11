package com.openclassrooms.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class RealEstate {

    @PrimaryKey
    private long mId;
    private String mTypeProperty;
    private int mPrice;
    private int mSurface;
    private int mNbPieces;
    private int mNbBedrooms;
    private int mNbBathrooms;
    private String mDescription;
    private String mAddress;
    private int mZipCode;
    private String mCity;
    private String mPointOfInterests;
    private String mStatus;
    private Date mInitialSale;
    private Date mFinalSale;
    private String mRealEstateAgent;
    private Date mYearConstruction;
    private int mFloors;
    private boolean mCoOwnership;


    public RealEstate() {
    }

    public RealEstate(long id, String typeProperty, int price, int surface, int nbPieces,
                      int nbBedrooms, int nbBathrooms, String description, String address,
                      int zipCode, String city, String pointOfInterests, String status,
                      Date initialSale, Date finalSale, String realEstateAgent, Date yearConstruction,
                      int floors, boolean coOwnership) {
        mId = id;
        mTypeProperty = typeProperty;
        mPrice = price;
        mSurface = surface;
        mNbPieces = nbPieces;
        mNbBedrooms = nbBedrooms;
        mNbBathrooms = nbBathrooms;
        mDescription = description;
        mAddress = address;
        mZipCode = zipCode;
        mCity = city;
        mPointOfInterests = pointOfInterests;
        mStatus = status;
        mInitialSale = initialSale;
        mFinalSale = finalSale;
        mRealEstateAgent = realEstateAgent;
        mYearConstruction = yearConstruction;
        mFloors = floors;
        mCoOwnership = coOwnership;
    }

//    --------------------------------------Getters-------------------------------------------------

    public long getId() {
        return mId;
    }

    public String getTypeProperty() {
        return mTypeProperty;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getSurface() {
        return mSurface;
    }

    public int getNbPieces() {
        return mNbPieces;
    }

    public int getNbBedrooms() {
        return mNbBedrooms;
    }

    public int getNbBathrooms() {
        return mNbBathrooms;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getZipCode() {
        return mZipCode;
    }

    public String getCity() {
        return mCity;
    }

    public String getPointOfInterests() {
        return mPointOfInterests;
    }

    public String getStatus() {
        return mStatus;
    }

    public Date getInitialSale() {
        return mInitialSale;
    }

    public Date getFinalSale() {
        return mFinalSale;
    }

    public String getRealEstateAgent() {
        return mRealEstateAgent;
    }

    public Date getYearConstruction() {
        return mYearConstruction;
    }

    public int getFloors() {
        return mFloors;
    }

    public boolean isCoOwnership() {
        return mCoOwnership;
    }


//    --------------------------------------Setters-------------------------------------------------


    public void setId(long id) {
        mId = id;
    }

    public void setTypeProperty(String typeProperty) {
        mTypeProperty = typeProperty;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public void setSurface(int surface) {
        mSurface = surface;
    }

    public void setNbPieces(int nbPieces) {
        mNbPieces = nbPieces;
    }

    public void setNbBedrooms(int nbBedrooms) {
        mNbBedrooms = nbBedrooms;
    }

    public void setNbBathrooms(int nbBathrooms) {
        mNbBathrooms = nbBathrooms;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setZipCode(int zipCode) {
        mZipCode = zipCode;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public void setPointOfInterests(String pointOfInterests) {
        mPointOfInterests = pointOfInterests;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setInitialSale(Date initialSale) {
        mInitialSale = initialSale;
    }

    public void setFinalSale(Date finalSale) {
        mFinalSale = finalSale;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        mRealEstateAgent = realEstateAgent;
    }

    public void setYearConstruction(Date yearConstruction) {
        mYearConstruction = yearConstruction;
    }

    public void setFloors(int floors) {
        mFloors = floors;
    }

    public void setCoOwnership(boolean coOwnership) {
        mCoOwnership = coOwnership;
    }




    @Override
    public String toString() {
        return "RealEstate{" +
                "mId=" + mId +
                ", mTypeProperty='" + mTypeProperty + '\'' +
                ", mPrice=" + mPrice +
                ", mSurface=" + mSurface +
                ", mNbPieces=" + mNbPieces +
                ", mNbBedrooms=" + mNbBedrooms +
                ", mNbBathrooms=" + mNbBathrooms +
                ", mDescription='" + mDescription + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mZipCode=" + mZipCode +
                ", mCity='" + mCity + '\'' +
                ", mPointOfInterests=" + mPointOfInterests +
                ", mStatus='" + mStatus + '\'' +
                ", mInitialSale=" + mInitialSale +
                ", mFinalSale=" + mFinalSale +
                ", mRealEstateAgent='" + mRealEstateAgent + '\'' +
                ", mYearConstruction=" + mYearConstruction +
                ", mFloors=" + mFloors +
                ", mCoOwnership=" + mCoOwnership +
                '}';
    }
}
