package com.openclassrooms.realestatemanager.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.data.dao.PicturesDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.utils.Converters;

import java.util.concurrent.Executors;

@Database(entities = {Property.class, Pictures.class}, version =1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    private static volatile RealEstateManagerDatabase INSTANCE;

    public abstract PropertyDao mPropertyDao();
    public abstract PicturesDao mPicturesDao();

    public static RealEstateManagerDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RealEstateManagerDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RealEstateManagerDatabase.class,
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
                        getInstance(context).mPropertyDao().insertAll(Property.populateData()));
            }
        };
    }

    private static Callback prepopulateDatabaseWithPictures(Context context){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(() ->
                        getInstance(context).mPicturesDao().insertAll(Pictures.populateData()));
            }
        };
    }

}
