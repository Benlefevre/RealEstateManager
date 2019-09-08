package com.openclassrooms;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.data.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
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
public class RealEstateDaoTest {

    private RealEstateDatabase mRealEstateDatabase;

    private static RealEstate realEstateDemo = new RealEstate("apartment",3450000,
            130,4,3,2,"Test description","Test Address",
            75000, "FR", 0, 0 ,"Paris","School, Supermarket ",false,new Date(), null,
            "Benoit Lefèvre", null, 3, true);

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        mRealEstateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        mRealEstateDatabase.close();
    }


    @Test
    public void insertAndGetRealEstate() throws InterruptedException {
        mRealEstateDatabase.mRealEstateDao().insertRealEstate(realEstateDemo);

        List<RealEstate> realEstates = LiveDataTestUtil.getValue(mRealEstateDatabase.mRealEstateDao().getAllRealEstate());
        RealEstate realEstate = realEstates.get(0);
        assertEquals(realEstateDemo.getTypeProperty(),realEstate.getTypeProperty());
        assertEquals(realEstateDemo.getPrice(), realEstate.getPrice());
        assertEquals(realEstateDemo.getSurface(), realEstate.getSurface());
        assertEquals(realEstateDemo.getNbRooms(), realEstate.getNbRooms());
        assertEquals(realEstateDemo.getNbBedrooms(),realEstate.getNbBedrooms());
        assertEquals(realEstateDemo.getNbBathrooms(),realEstate.getNbBathrooms());
        assertEquals(realEstateDemo.getDescription(),realEstate.getDescription());
        assertEquals(realEstateDemo.getAddress(),realEstate.getAddress());
        assertEquals(realEstateDemo.getZipCode(),realEstate.getZipCode());
        assertEquals(realEstateDemo.getCity(),realEstate.getCity());
        assertEquals(realEstateDemo.getAmenities(),realEstate.getAmenities());
        assertEquals(realEstateDemo.getInitialSale(), realEstate.getInitialSale());
        assertEquals(realEstateDemo.isSold(), realEstate.isSold());
        assertEquals(realEstateDemo.getRealEstateAgent(),realEstate.getRealEstateAgent());
        assertEquals(realEstateDemo.getFloors(),realEstate.getFloors());
        assertEquals(realEstateDemo.isCoOwnership(),realEstate.isCoOwnership());
    }
}
