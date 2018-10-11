package com.wkz.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.wkz.framework.FRApplication;

import java.util.List;
import java.util.Map;

/**
 * SharedPreferences操作
 */
public class SharedPreferencesUtils {

    private SharedPreferencesUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get SharedPreferences
     */
    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(FRApplication.getContext());
    }

    /**
     * Get SharedPreferences by name
     */
    private static SharedPreferences getSharedPreferences(String name) {
        SharedPreferences sharedPreferences;
        if (TextUtils.isEmpty(name)) {
            sharedPreferences = getSharedPreferences();
        } else {
            sharedPreferences = FRApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * Put SharedPreferences, the method may set a string/boolean/int/float/long value in the preferences editor
     */
    public static boolean put(String key, Object value) {
        return put(null, key, value);
    }

    /**
     * Put SharedPreferences, the method may set a string/boolean/int/float/long value in the preferences editor
     */
    public static boolean put(String name, String key, Object value) {
        if (TextUtils.isEmpty(key) || null == value) {
            throw new RuntimeException("key or value cannot be null.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        return editor.commit();
    }

    /**
     * Put all SharedPreferences, all the data will be maked a list.
     */
    public static boolean putAll(String key, List<?> list) {
        return putAll(null, key, list);
    }

    /**
     * Put all SharedPreferences, all the data will be maked a list.
     */
    public static boolean putAll(String name, String key, List<?> list) {
        if (TextUtils.isEmpty(key) || list.isEmpty()) {
            throw new RuntimeException("key or list cannot be null.");
        }
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        int size = list.size();
        if (list.get(0) instanceof String) {
            for (int i = 0; i < size; i++) {
                editor.putString(key + i, (String) list.get(i));
            }
        } else if (list.get(0) instanceof Long) {
            for (int i = 0; i < size; i++) {
                editor.putLong(key + i, (Long) list.get(i));
            }
        } else if (list.get(0) instanceof Float) {
            for (int i = 0; i < size; i++) {
                editor.putFloat(key + i, (Float) list.get(i));
            }
        } else if (list.get(0) instanceof Integer) {
            for (int i = 0; i < size; i++) {
                editor.putLong(key + i, (Integer) list.get(i));
            }
        } else if (list.get(0) instanceof Boolean) {
            for (int i = 0; i < size; i++) {
                editor.putBoolean(key + i, (Boolean) list.get(i));
            }
        }
        return editor.commit();
    }

    /**
     * Retrieve all values from the preferences.
     *
     * @return Returns a map containing a list of pairs key/value representing the preferences.
     */
    public static Map<String, ?> getAll() {
        return getAll(null);
    }

    /**
     * Retrieve all values from the preferences.
     *
     * @return Returns a map containing a list of pairs key/value representing the preferences.
     */
    public static Map<String, ?> getAll(String name) {
        return getSharedPreferences(name).getAll();
    }

    /**
     * Retrieve a boolean value from the preferences.
     */
    public static boolean getBoolean(String key) {
        return getBoolean(null, key);
    }

    /**
     * Retrieve a boolean value from the preferences.
     */
    public static boolean getBoolean(String name, String key) {
        return getSharedPreferences(name).getBoolean(key, false);
    }

    /**
     * Retrieve a long value from the preferences.
     */
    public static long getLong(String key) {
        return getLong(null, key);
    }

    /**
     * Retrieve a long value from the preferences.
     */
    public static long getLong(String name, String key) {
        return getSharedPreferences(name).getLong(key, 0L);
    }

    /**
     * Retrieve a float value from the preferences.
     */
    public static float getFloat(String key) {
        return getFloat(null, key);
    }

    /**
     * Retrieve a float value from the preferences.
     */
    public static float getFloat(String name, String key) {
        return getSharedPreferences(name).getFloat(key, 0F);
    }

    /**
     * Retrieve a int value from the preferences.
     */
    public static int getInt(String key) {
        return getInt(null, key);
    }

    /**
     * Retrieve a int value from the preferences.
     */
    public static int getInt(String name, String key) {
        return getSharedPreferences(name).getInt(key, 0);
    }

    /**
     * Retrieve a String value from the preferences.
     */
    public static String getString(String key) {
        return getString(null, key);
    }

    /**
     * Retrieve a String value from the preferences.
     */
    public static String getString(String name, String key) {
        return getSharedPreferences(name).getString(key, null);
    }

    /**
     * Mark in the editor that a preference value should be removed.
     */
    public static boolean remove(String key) {
        return remove(null, key);
    }

    /**
     * Mark in the editor that a preference value should be removed.
     */
    public static boolean remove(String name, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * Mark in the editor to remove all values from the preferences.
     */
    public static boolean clear() {
        return clear(null);
    }

    /**
     * Mark in the editor to remove all values from the preferences.
     */
    public static boolean clear(String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        editor.clear();
        return editor.commit();
    }

}
