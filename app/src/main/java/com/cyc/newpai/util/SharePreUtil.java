package com.cyc.newpai.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreUtil {
    public static final String SHARED_NAME = "user_info";	   //数据共享名称

    public static final String SAVE_FRIEND_INFO = "save_friend_info";

    public static final String SAVE_USER_INFO = "save_user_info";

    public static void setPref(Context context,String key,String value){
        int mode = 2;
        mode = context.MODE_PRIVATE;
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_NAME, mode).edit();
        editor.putString(key, value).commit();
    }
    public static void setPref(Context context,String key,int value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value).commit();
    }

    public static void setPref(Context context,String key,float value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value).commit();
    }

    public static int getPref(Context context, String key,
                              int defaultValue) {
        int value = 0;
        try {
            value = context.getSharedPreferences(SHARED_NAME, 0).getInt(
                    key, defaultValue);
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    public static float getPref(Context context, String key,
                              float defaultValue) {
        float value = 0;
        try {
            value = context.getSharedPreferences(SHARED_NAME, 0).getFloat(
                    key, defaultValue);
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    public static String getPref(Context context ,String key ,String defaultValue){
        String value = "";
        try {
            value = context.getSharedPreferences(SHARED_NAME, 0).getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            value = defaultValue;
        }
        return value;
    }
    public static void setPref(Context context, String Key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SHARED_NAME,Context.MODE_WORLD_WRITEABLE).edit();
        editor.putBoolean(Key, value).commit();
    }

    public static boolean getPref(Context context, String key,
                                  boolean defaultValue) {
        boolean value = false;
        try {
            value = context.getSharedPreferences(SHARED_NAME, 0)
                    .getBoolean(key, defaultValue);
        } catch (Exception ex) {
            value = defaultValue;
        }
        return value;
    }

    public static void removePref(Context mContext, String key) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(
                SHARED_NAME, 0).edit();
        editor.remove(key).commit();
    }
}
