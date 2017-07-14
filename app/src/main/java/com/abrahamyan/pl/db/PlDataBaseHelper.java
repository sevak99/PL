package com.abrahamyan.pl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SEVAK on 05.07.2017.
 */

public class PlDataBaseHelper extends SQLiteOpenHelper {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = PlDataBaseHelper.class.getName();
    private static final String DATABASE_NAME = "PL.DB";
    private static final int DATABASE_VERSION = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(PlDataBase.CREATE_PRODUCT_TABLE);
        Log.d(LOG_TAG, "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + PlDataBase.PRODUCT_TABLE);
        onCreate(db);
        Log.d(LOG_TAG, "Table upgraded");
    }

    public void onChange(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS" + PlDataBase.PRODUCT_TABLE);
        onCreate(db);
        Log.d(LOG_TAG, "Table upgraded");
    }


    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
