package com.abrahamyan.pl.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.abrahamyan.pl.BuildConfig;
import com.abrahamyan.pl.db.PlDataBase;
import com.abrahamyan.pl.db.PlDataBaseHelper;

public class PlProvider extends ContentProvider {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = PlProvider.class.getSimpleName();

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    public class Path {
        static final String PRODUCT_LOCATION = PlDataBase.PRODUCT_TABLE;
    }

    private class Code {
        private static final int ALL_PRODUCTS = 1;
        private static final int SINGLE_PRODUCT = 2;
    }

    private static class ContentType {

        private static final String ALL_PRODUCTS = "vnd.android.cursor.dir/vnd."
                + AUTHORITY + "." + Path.PRODUCT_LOCATION;

        private static final String SINGLE_PRODUCT = "vnd.android.cursor.item/vnd."
                + AUTHORITY + "." + Path.PRODUCT_LOCATION;
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // ===========================================================
    // Fields
    // ===========================================================

    private PlDataBaseHelper mDataBaseHelper;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public boolean onCreate() {
        mDataBaseHelper = new PlDataBaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Code.ALL_PRODUCTS:
                return ContentType.ALL_PRODUCTS;

            case Code.SINGLE_PRODUCT:
                return ContentType.SINGLE_PRODUCT;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri.toString());
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri contentUri;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long id;

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_PRODUCTS:
                id = db.insertWithOnConflict(PlDataBase.PRODUCT_TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
                contentUri = ContentUris.withAppendedId(UriBuilder.buildProductUri(), id);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri.toString());
        }

        return contentUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_PRODUCTS:
                cursor = db.query(PlDataBase.PRODUCT_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case Code.SINGLE_PRODUCT:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PlDataBase.PRODUCT_ID + "=" + id;
                } else {
                    selection = selection + " AND " + PlDataBase.PRODUCT_ID + "=" + id;
                }
                cursor = db.query(PlDataBase.PRODUCT_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri.toString());
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteCount;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_PRODUCTS:
                deleteCount = db.delete(PlDataBase.PRODUCT_TABLE, selection, selectionArgs);
                break;

            case Code.SINGLE_PRODUCT:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PlDataBase.PRODUCT_ID + "=" + id;
                } else {
                    selection = selection + " AND " + PlDataBase.PRODUCT_ID + "=" + id;
                }
                deleteCount = db.delete(PlDataBase.PRODUCT_TABLE, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri.toString());
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount;
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case Code.ALL_PRODUCTS:
                updateCount = db.update(PlDataBase.PRODUCT_TABLE, values, selection, selectionArgs);
                break;

            case Code.SINGLE_PRODUCT:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PlDataBase.PRODUCT_ID + "=" + id;
                } else {
                    selection = selection + " AND " + PlDataBase.PRODUCT_ID + "=" + id;
                }
                updateCount = db.update(PlDataBase.PRODUCT_TABLE, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri.toString());
        }
        return updateCount;
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, Path.PRODUCT_LOCATION, Code.ALL_PRODUCTS);
        uriMatcher.addURI(AUTHORITY, Path.PRODUCT_LOCATION + "/#", Code.SINGLE_PRODUCT);

        return uriMatcher;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}