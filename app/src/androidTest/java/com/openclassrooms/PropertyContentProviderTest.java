package com.openclassrooms;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.provider.RealEstateManagerContentProvider;
import com.openclassrooms.realestatemanager.utils.Converters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PropertyContentProviderTest {

    private ContentResolver mContentResolver;
    private RealEstateManagerDatabase mRealEstateManagerDatabase;

//    To execute this tests, the application needs a first install to initialize the database and the
//    Property with Id 1 and the corresponding Pictures don't be modified.

    @Before
    public void setUp() {
        mRealEstateManagerDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
    }

    @After
    public void closeDb(){
        mRealEstateManagerDatabase.close();
    }

    /**
     * Checks that the ContentProvider's cursor contains no values if the Property's id is wrong.
     */
    @Test
    public void getPropertiesWhenWrongIdInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateManagerContentProvider.URI_REALESTATE, 250000), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    /**
     * Checks that the ContentProvider's cursor contains all the properties saved in database.
     */
    @Test
    public void getAllPropertiesWhenIdEquals0(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateManagerContentProvider.URI_REALESTATE,0),null,null,null,null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(greaterThan(5)));

    }

    /**
     * Checks that the ContentProvider's cursor contains the right property according to the property's id.
     */
    @Test
    public void getRealEstateWithId() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateManagerContentProvider.URI_REALESTATE, 1), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());

        assertEquals(1, cursor.getLong(cursor.getColumnIndexOrThrow("mId")));
        assertEquals("Apartment", cursor.getString(cursor.getColumnIndexOrThrow("mTypeProperty")));
        assertEquals(160000, cursor.getInt(cursor.getColumnIndexOrThrow("mPrice")));
        assertEquals(1399, cursor.getInt(cursor.getColumnIndexOrThrow("mSurface")));
        assertEquals(4, cursor.getInt(cursor.getColumnIndexOrThrow("mNbRooms")));
        assertEquals(3, cursor.getInt(cursor.getColumnIndexOrThrow("mNbBedrooms")));
        assertEquals(2, cursor.getInt(cursor.getColumnIndexOrThrow("mNbBathrooms")));
        assertEquals("600 E 8th 6F St ", cursor.getString(cursor.getColumnIndexOrThrow("mAddress")));
        assertEquals(64106, cursor.getInt(cursor.getColumnIndexOrThrow("mZipCode")));
        assertEquals("US", cursor.getString(cursor.getColumnIndexOrThrow("mCountryCode")));
        assertEquals(39.104264, cursor.getDouble(cursor.getColumnIndexOrThrow("mLatitude")), 0);
        assertEquals(-94.576036, cursor.getDouble(cursor.getColumnIndexOrThrow("mLongitude")), 0);
        assertEquals("Kansas City", cursor.getString(cursor.getColumnIndexOrThrow("mCity")));
        assertEquals("(School, Shops)", cursor.getString(cursor.getColumnIndexOrThrow("mAmenities")));
        assertEquals(false, Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("mSold"))));
        assertEquals("Thu Aug 01 00:00:00 GMT 2019", String.valueOf(Converters.fromTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("mInitialSale")))));
        assertEquals("Lefèvre Benoit", cursor.getString(cursor.getColumnIndexOrThrow("mRealEstateAgent")));
        assertEquals("Sun Aug 20 22:00:00 GMT 1989", String.valueOf(Converters.fromTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("mYearConstruction")))));
        assertEquals(6, cursor.getInt(cursor.getColumnIndexOrThrow("mFloors")));
        assertEquals(4, cursor.getInt(cursor.getColumnIndexOrThrow("mNbPictures")));

        cursor.close();
    }

    /**
     * Checks that the ContentProvider's cursor contains all the pictures that have the property's id
     * in their attributes.
     */
    @Test
    public void getPicturesWithId() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateManagerContentProvider.URI_PICTURE, 1), null, null, null, null);

        assertThat(cursor, notNullValue());
        assertEquals(4, cursor.getCount());
        assertTrue(cursor.moveToFirst());

        assertEquals(1,cursor.getInt(cursor.getColumnIndexOrThrow("mId")));
        assertEquals("android.resource://com.openclassrooms.realestatemanager/drawable/p600_1",cursor.getString(cursor.getColumnIndexOrThrow("mUri")));
        assertEquals("Facade",cursor.getString(cursor.getColumnIndexOrThrow("mDescription")));
        assertEquals(1,cursor.getLong(cursor.getColumnIndexOrThrow("mRealEstateId")));

        cursor.moveToNext();

        assertEquals(2,cursor.getInt(cursor.getColumnIndexOrThrow("mId")));
        assertEquals("android.resource://com.openclassrooms.realestatemanager/drawable/p600_2",cursor.getString(cursor.getColumnIndexOrThrow("mUri")));
        assertEquals("Lounge",cursor.getString(cursor.getColumnIndexOrThrow("mDescription")));
        assertEquals(1,cursor.getLong(cursor.getColumnIndexOrThrow("mRealEstateId")));

        cursor.close();
    }

}
