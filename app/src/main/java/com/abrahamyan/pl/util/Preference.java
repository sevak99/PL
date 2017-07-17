package com.abrahamyan.pl.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SEVAK on 16.07.2017.
 */

public class Preference {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String PREF_USER_MAIL = "PREF_USER_MAIL";
    private static final String PREF_USER_PASS = "PREF_USER_PASS";

    // ===========================================================
    // Fields
    // ===========================================================

    private static Preference sInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    // ===========================================================
    // Constructors
    // ===========================================================

    private Preference(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public static Preference getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new Preference(context);
        }
        return sInstance;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setUserMail(String userMail) {
        mEditor.putString(PREF_USER_MAIL, userMail);
        mEditor.apply();
    }

    public String getUserMail() {
        return mSharedPreferences.getString(PREF_USER_MAIL, null);
    }

    public void setUserPass(String userPass) {
        mEditor.putString(PREF_USER_PASS, userPass);
        mEditor.apply();
    }

    public String getUserPass() {
        return mSharedPreferences.getString(PREF_USER_PASS, null);
    }
}
