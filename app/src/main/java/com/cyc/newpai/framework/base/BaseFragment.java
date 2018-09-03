package com.cyc.newpai.framework.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.util.varyview.VaryViewHelper;
import com.cyc.newpai.util.GsonManager;
import com.google.gson.Gson;


public abstract class BaseFragment extends Fragment {

    protected VaryViewHelper varyViewHelper;
    private Activity mActivity;

    public Handler handler = new Handler(Looper.getMainLooper());

    private GsonManager gsonManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gsonManager = GsonManager.getInstance();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public Activity getMyActivity(){

        return mActivity;
    }

    protected Gson getGson(){
        return  GsonManager.getGson();
    }

    public void updateFragmentData(){

    }

    public void review(){

    }

    public void refreshFragmentData(){

    }

    public void initVaryView(){
        if (getLoadingTargetView() != null) {
            varyViewHelper = new VaryViewHelper.Builder()
                    .setDataView(getLoadingTargetView())
                    .setLoadingView(View.inflate(getContext(), R.layout.layout_loadingview, null))
                    .setEmptyView(View.inflate(getContext(), R.layout.layout_emptyview, null))
                    .setErrorView(View.inflate(getContext(), R.layout.layout_errorview, null))
                    .setRefreshListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRetryLoad();
                        }
                    })
                    .build();
        }
    }

    protected View getLoadingTargetView(){
        return null;
    }

    protected void onRetryLoad(){}
}
