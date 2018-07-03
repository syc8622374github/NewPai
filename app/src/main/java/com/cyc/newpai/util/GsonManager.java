package com.cyc.newpai.util;

import com.google.gson.Gson;

public class GsonManager {

    private static GsonManager mInstance;
    private static Gson gson;

    public GsonManager() {
        gson = new Gson();
    }

    public static GsonManager getInstance() {
        if(mInstance==null){
            synchronized(GsonManager.class){
                if(mInstance==null){
                    mInstance = new GsonManager();
                }
            }
        }
        return mInstance;
    }

    public static Gson getGson(){
        return gson;
    }
}
