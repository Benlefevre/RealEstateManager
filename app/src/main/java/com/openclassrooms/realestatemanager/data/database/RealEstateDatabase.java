package com.openclassrooms.realestatemanager.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.data.dao.PicturesDao;
import com.openclassrooms.realestatemanager.data.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.utils.Converters;

import java.util.concurrent.Executors;

@Database(entities = {RealEstate.class, Pictures.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RealEstateDatabase extends RoomDatabase {

    private static volatile RealEstateDatabase INSTANCE;

    public abstract RealEstateDao mRealEstateDao();
    public abstract PicturesDao mRealEstatePicturesDao();

    public static RealEstateDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RealEstateDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),RealEstateDatabase.class,
                            "RealEstateManager.db")
                            .addCallback(prepopulateDataBaseWithRealEstate(context.getApplicationContext()))
                            .addCallback(prepopulateDatabaseWithPictures(context.getApplicationContext()))
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDataBaseWithRealEstate(Context context){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(() ->
                        getInstance(context).mRealEstateDao().insertAll(RealEstate.populateData()));
            }
        };
    }

    private static Callback prepopulateDatabaseWithPictures(Context context){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(() ->
                        getInstance(context).mRealEstatePicturesDao().insertAll(Pictures.populateData()));
            }
        };
    }

}
