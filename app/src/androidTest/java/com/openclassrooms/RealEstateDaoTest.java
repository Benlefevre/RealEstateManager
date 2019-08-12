package com.openclassrooms;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.data.database.RealEstateDatabase;
import com.openclassrooms.data.entities.RealEstate;
import com.openclassrooms.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RealEstateDaoTest {

    private RealEstateDatabase mRealEstateDatabase;

    private static RealEstate realEstateDemo = new RealEstate(1,"apartment",345000,130,4,3,2,"Test description",
            "testAddress", 75000, "Paris", "School, Supermarket ","For sale",new Date(), null,
            "Benoit Lefèvre", null, 3, true);

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception{
        mRealEstateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception{
        mRealEstateDatabase.close();
    }


    @Test
    public void insertAndGetRealEstate() throws InterruptedException {
        mRealEstateDatabase.mRealEstateDao().insertRealEstate(realEstateDemo);

        RealEstate realEstate = LiveDataTestUtil.getValue(mRealEstateDatabase.mRealEstateDao().getRealEstate(1));
        assertEquals(realEstateDemo.getId(),realEstate.getId());
        assertEquals(realEstateDemo.getTypeProperty(),realEstate.getTypeProperty());
        assertEquals(realEstateDemo.getPrice(), realEstate.getPrice());
        assertEquals(realEstateDemo.getSurface(), realEstate.getSurface());
        assertEquals(realEstateDemo.getNbPieces(), realEstate.getNbPieces());
        assertEquals(realEstateDemo.getNbBedrooms(),realEstate.getNbBedrooms());
        assertEquals(realEstateDemo.getNbBathrooms(),realEstate.getNbBathrooms());
        assertEquals(realEstateDemo.getDescription(),realEstate.getDescription());
        assertEquals(realEstateDemo.getAddress(),realEstate.getAddress());
        assertEquals(realEstateDemo.getZipCode(),realEstate.getZipCode());
        assertEquals(realEstateDemo.getCity(),realEstate.getCity());
        assertEquals(realEstateDemo.getPointOfInterests(),realEstate.getPointOfInterests());
        assertEquals(realEstateDemo.getInitialSale(), realEstate.getInitialSale());
        assertEquals(realEstateDemo.getStatus(), realEstate.getStatus());
        assertEquals(realEstateDemo.getRealEstateAgent(),realEstate.getRealEstateAgent());
        assertEquals(realEstateDemo.getFloors(),realEstate.getFloors());
        assertEquals(realEstateDemo.isCoOwnership(),realEstate.isCoOwnership());
    }
}
