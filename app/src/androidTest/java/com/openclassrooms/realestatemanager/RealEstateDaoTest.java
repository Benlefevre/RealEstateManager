package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.database.RealEstateDatabase;
import com.openclassrooms.models.RealEstate;
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
    private static RealEstate realEstateDemo = new RealEstate(1,"apartment",345000,130,4,3,2,"",
            "testAddress", 75000, "Paris", null,"For sale",new Date(), null,
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
        mRealEstateDatabase.mPropertyDao().insertProperty(realEstateDemo);

        RealEstate realEstate = LiveDataTestUtil.getValue(mRealEstateDatabase.mPropertyDao().getProperties(1));
        assertEquals(realEstateDemo.getId(),realEstate.getId());
    }
}
