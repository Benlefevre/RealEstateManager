package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;

public class RealEstateManagerContentProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".dbprovider";
    public static final String TABLE_REALESTATE = Property.class.getSimpleName();
    public static final String TABLE_REALESTATE_ITEM = Property.class.getSimpleName() + "/#";
    public static final String TABLE_PICTURE = Pictures.class.getSimpleName();
    public static final String TABLE_PICTURES_ITEM = Pictures.class.getSimpleName() + "/#";
    public static final Uri URI_REALESTATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_REALESTATE);
    public static final Uri URI_PICTURE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_PICTURE);

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, TABLE_REALESTATE, 1);
        mUriMatcher.addURI(AUTHORITY, TABLE_PICTURE, 2);
        mUriMatcher.addURI(AUTHORITY, TABLE_REALESTATE_ITEM, 3);
        mUriMatcher.addURI(AUTHORITY, TABLE_PICTURES_ITEM, 4);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if (getContext() != null) {
            final Cursor cursor;
            switch (mUriMatcher.match(uri)) {
                case 3:
                    long id = ContentUris.parseId(uri);
                    if (id == 0)
                        cursor = RealEstateManagerDatabase.getInstance(getContext()).mPropertyDao().getRealEstateWithCursor();
                    else
                        cursor = RealEstateManagerDatabase.getInstance(getContext()).mPropertyDao().getRealEstateWithCursor(id);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    cursor.moveToFirst();
                    return cursor;
                case 4:
                    long estateId = ContentUris.parseId(uri);
                    cursor = RealEstateManagerDatabase.getInstance(getContext()).mPicturesDao().getPicturesWithCursor(estateId);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    cursor.moveToFirst();
                    return cursor;
                default:
                    return null;
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri" + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.realestate/" + AUTHORITY + "." + TABLE_REALESTATE;
            case 2:
                return "vnd.android.cursor.pictures/" + AUTHORITY + "." + TABLE_PICTURE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
