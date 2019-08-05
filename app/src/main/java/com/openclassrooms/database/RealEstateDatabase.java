package com.openclassrooms.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.database.dao.RealEstateDao;
import com.openclassrooms.models.RealEstate;
import com.openclassrooms.utils.Converters;

import java.util.Date;

@Database(entities = {RealEstate.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RealEstateDatabase extends RoomDatabase {

    private static volatile RealEstateDatabase INSTANCE;

    public abstract RealEstateDao mPropertyDao();

    public static RealEstateDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (RealEstateDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),RealEstateDatabase.class,
                            "RealEstateManager.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Date date = new Date();
                ContentValues contentValues =  new ContentValues();
                contentValues.put("mId", 1);
                contentValues.put("mTypeProperty", "apartment");
                contentValues.put("mPrice", 325000);
                contentValues.put("mSurface", 130);
                contentValues.put("mNbPieces", 4);
                contentValues.put("mNbBedrooms", 3);
                contentValues.put("mNbBathrooms", 2);
                contentValues.put("mDescription", "Very nice apartment located on the 3rd and last floor of 130m2." +
                        " Located in the heart of town and close to all amenities. It has 3 large bedrooms," +
                        " 2 bathrooms and a large living room with a terrace overlooking a private park.");
                contentValues.put("mAddress", "324 East 11th Street Kansas City, MO 64106");
                contentValues.put("mZipCode", 64106);
                contentValues.put("mCity", "Kansas City");
                contentValues.put("mStatus", "For sale");
                contentValues.put("mInitialSale", String.valueOf(date));

                db.insert("Property", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}
