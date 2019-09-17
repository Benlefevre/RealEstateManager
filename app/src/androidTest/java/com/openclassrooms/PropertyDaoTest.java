package com.openclassrooms;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PropertyDaoTest {

    private RealEstateManagerDatabase mRealEstateManagerDatabase;

    private static Property sPropertyDemo = new Property("apartment",3450000,
            130,4,3,2,"Test description","Test Address",
            75000, "FR", 0, 0 ,"Paris","School, Supermarket ",false,new Date(), null,
            "Benoit Lefèvre", null, 3, true,3);

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        mRealEstateManagerDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        mRealEstateManagerDatabase.close();
    }


    @Test
    public void insertAndGetRealEstate() throws InterruptedException {
        mRealEstateManagerDatabase.mPropertyDao().insertProperty(sPropertyDemo);

        List<Property> properties = LiveDataTestUtil.getValue(mRealEstateManagerDatabase.mPropertyDao().getAllRealEstate());
        Property property = properties.get(0);
        assertEquals(sPropertyDemo.getTypeProperty(), property.getTypeProperty());
        assertEquals(sPropertyDemo.getPrice(), property.getPrice());
        assertEquals(sPropertyDemo.getSurface(), property.getSurface());
        assertEquals(sPropertyDemo.getNbRooms(), property.getNbRooms());
        assertEquals(sPropertyDemo.getNbBedrooms(), property.getNbBedrooms());
        assertEquals(sPropertyDemo.getNbBathrooms(), property.getNbBathrooms());
        assertEquals(sPropertyDemo.getDescription(), property.getDescription());
        assertEquals(sPropertyDemo.getAddress(), property.getAddress());
        assertEquals(sPropertyDemo.getZipCode(), property.getZipCode());
        assertEquals(sPropertyDemo.getCity(), property.getCity());
        assertEquals(sPropertyDemo.getAmenities(), property.getAmenities());
        assertEquals(sPropertyDemo.getInitialSale(), property.getInitialSale());
        assertEquals(sPropertyDemo.isSold(), property.isSold());
        assertEquals(sPropertyDemo.getRealEstateAgent(), property.getRealEstateAgent());
        assertEquals(sPropertyDemo.getFloors(), property.getFloors());
        assertEquals(sPropertyDemo.isCoOwnership(), property.isCoOwnership());
    }
}
