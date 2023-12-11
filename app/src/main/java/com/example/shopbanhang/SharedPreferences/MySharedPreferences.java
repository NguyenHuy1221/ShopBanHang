package com.example.shopbanhang.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_PREFERENCES = "MY_PREFERENCES";
    private static final String MY_PREFERENCES_BOOLEAN = "MY_PREFERENCES_BOOLEAN";
    private final Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putValue(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void putBooleanValue(String key, boolean b) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES_BOOLEAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES_BOOLEAN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }


}
