package com.cyc.newpai.framework.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.util.GsonManager;
import com.google.gson.Gson;


public abstract class BaseFragment extends Fragment {

    private GsonManager gsonManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonManager = GsonManager.getInstance();
    }

    protected Gson getGson(){
        return  GsonManager.getGson();
    }

    public void updateFragmentData(){

    }

    public void refreshFragmentData(){

    }
}
