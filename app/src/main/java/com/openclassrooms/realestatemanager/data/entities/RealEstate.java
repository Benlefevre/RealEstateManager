package com.openclassrooms.realestatemanager.data.entities;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.utils.Converters;

import java.util.Date;

@Entity
public class RealEstate {

    @PrimaryKey(autoGenerate = true)
    private long mId;
    private String mTypeProperty;
    private int mPrice;
    private int mSurface;
    private int mNbRooms;
    private int mNbBedrooms;
    private int mNbBathrooms;
    private String mDescription;
    private String mAddress;
    private int mZipCode;
    private String mCountryCode;
    private double mLatitude;
    private double mLongitude;
    private String mCity;
    private String mAmenities;
    private boolean mSold;
    private Date mInitialSale;
    private Date mFinalSale;
    private String mRealEstateAgent;
    private Date mYearConstruction;
    private int mFloors;
    private boolean mCoOwnership;
    private int mNbPictures;


    public RealEstate() {
    }

    public RealEstate(String typeProperty, int price, int surface, int nbRooms,
                      int nbBedrooms, int nbBathrooms, String description, String address,
                      int zipCode, String countryCode, double latitude, double longitude, String city, String amenities, boolean status,
                      Date initialSale, Date finalSale, String realEstateAgent, Date yearConstruction,
                      int floors, boolean coOwnership, int nbPictures) {
        mTypeProperty = typeProperty;
        mPrice = price;
        mSurface = surface;
        mNbRooms = nbRooms;
        mNbBedrooms = nbBedrooms;
        mNbBathrooms = nbBathrooms;
        mDescription = description;
        mAddress = address;
        mZipCode = zipCode;
        mCountryCode = countryCode;
        mLatitude = latitude;
        mLongitude = longitude;
        mCity = city;
        mAmenities = amenities;
        mSold = status;
        mInitialSale = initialSale;
        mFinalSale = finalSale;
        mRealEstateAgent = realEstateAgent;
        mYearConstruction = yearConstruction;
        mFloors = floors;
        mCoOwnership = coOwnership;
        mNbPictures = nbPictures;
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

    public int getNbRooms() {
        return mNbRooms;
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

    public String getCountryCode() {
        return mCountryCode;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCity() {
        return mCity;
    }

    public String getAmenities() {
        return mAmenities;
    }

    public boolean isSold() {
        return mSold;
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

    public int getNbPictures() {
        return mNbPictures;
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

    public void setNbRooms(int nbRooms) {
        mNbRooms = nbRooms;
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

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public void setAmenities(String pointOfInterests) {
        mAmenities = pointOfInterests;
    }

    public void setSold(boolean sold) {
        mSold = sold;
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

    public void setNbPictures(int nbPictures) {
        mNbPictures = nbPictures;
    }

    @NonNull
    @Override
    public String toString() {
        return "RealEstate{" +
                "mId=" + mId +
                ", mTypeProperty='" + mTypeProperty + '\'' +
                ", mPrice=" + mPrice +
                ", mSurface=" + mSurface +
                ", mNbRooms=" + mNbRooms +
                ", mNbBedrooms=" + mNbBedrooms +
                ", mNbBathrooms=" + mNbBathrooms +
                ", mDescription='" + mDescription + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mZipCode=" + mZipCode +
                ", mCountryCode='" + mCountryCode + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mCity='" + mCity + '\'' +
                ", mAmenities='" + mAmenities + '\'' +
                ", mSold=" + mSold +
                ", mInitialSale=" + mInitialSale +
                ", mFinalSale=" + mFinalSale +
                ", mRealEstateAgent='" + mRealEstateAgent + '\'' +
                ", mYearConstruction=" + mYearConstruction +
                ", mFloors=" + mFloors +
                ", mCoOwnership=" + mCoOwnership +
                ", mNbPictures=" + mNbPictures +
                '}';
    }

    public static RealEstate[] populateData(){
        Date date = new Date();
        return new RealEstate[]{
                new RealEstate("Apartment",160000, 1399, 4, 3, 2, "Very nice apartment located on the 6th and last floor of 130m2." +
                        " Located in the heart of town and close to all amenities. It has 3 large bedrooms," +
                        " 2 bathrooms and a large living room with a terrace overlooking a private park.",
                        "600 E 8th 6F St Kansas City, MO 64106", 64106, "US", 39.104264, -94.576036, "Kansas City","(School, Shops)", false, new Date(1564617600000L),
                        null, "Lefèvre Benoit", new Date(619653600000L), 6, true,4),
                new RealEstate("House", 515000, 3390, 6, 5, 4, "Very nice house of 315m2." +
                        " Located in the heart of town and close to all amenities. It has 5 large bedrooms," +
                        " 4 bathrooms and a large living room with a large garden of 700m2.",
                        "6124 Ward Pkwy, Kansas City, MO 64106", 64106, "US", 39.0167956, -94.6016936, "Kansas City","(School, Garden)", false, new Date(1564617600000L),
                        null, "Lefèvre Benoit", new Date(734652000000L), 2, false,4),
                new RealEstate("Apartment", 999950, 2799, 3, 2, 2, "Very nice apartment located on the 31th and last floor of 260m2." +
                        " Located in the heart of town and close to all amenities. It has 2 large bedrooms," +
                        " 2 bathrooms and a large living room with a terrace where you have an amazing view .",
                        "909 Walnut St Unit 31, Kansas City, MO 64106", 64106, "US", 39.1032208, -94.5815734, "Kansas City","(Shops, Public transport )", false, new Date(1564617600000L),
                        null, "Lefèvre Benoit", new Date(213318000000L), 31, true,5),
        };
    }
}
