package com.md.basedpc.persistence;

import android.content.Context;
import android.content.SharedPreferences;

public class PHSPUtil {

    public static final String KEY_DEVICE_ID = "DeviceId";//设备id
    private static PHSPUtil instance;
    private final static String PHOENIX = "phoenix";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String nowLan;

    public static PHSPUtil getInstance() {
        if (instance == null) {
            synchronized (PHSPUtil.class) {
                if (instance == null) {
                    instance = new PHSPUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context mContext){
        mSharedPreferences = mContext.getSharedPreferences(PHOENIX,
                Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void removeByKey(String key) {
        mEditor.remove(key).commit();
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public String getString(String key) {
        return getInstance().getString(key, "");
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public Boolean getBoolean(String key) {
        return getInstance().getBoolean(key, false);
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value).commit();
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value).commit();
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value).commit();
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public float getFloat(String key) {
        return getInstance().getFloat(key, 0);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }

//    public String getDeviceId() {
//        String dv = getString(KEY_DEVICE_ID, "");
//        if (Utils.isEmpty(dv)) {
//            dv = Utils.getDeviceId();
//            putString(KEY_DEVICE_ID, dv);
//        }
//        return dv;
//    }

    public String getLanguage() {
        if (nowLan == null) {
            nowLan = getString("language", "");
        }
        return nowLan;
    }

    public void setLanguage(String language) {
        nowLan = language;
        putString("language", language);
    }
}
