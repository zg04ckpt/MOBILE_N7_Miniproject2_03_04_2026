package com.n7.miniproject2.utils;

import android.content.Context;

public class SharedPreferencesUtil {
    private static final String PREFS_NAME = "N7";
    public static void saveString(Context context, String key, String value) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(key, defaultValue);
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
}