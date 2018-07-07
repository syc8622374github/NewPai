package com.cyc.newpai.framework.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.widget.CustomToolbar;
import com.google.gson.Gson;

public abstract class BaseActivity extends AppCompatActivity {

    protected CustomToolbar ctb_toolbar;
    private GsonManager gsonManager;

    protected Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ctb_toolbar = findViewById(R.id.ctb_toolbar);
        initToolbar();
        gsonManager = GsonManager.getInstance();
    }

    protected Gson getGson(){
        return  GsonManager.getGson();
    }

    protected void initToolbar() {
        if(ctb_toolbar!=null){
            ctb_toolbar.setLeftAction1(R.drawable.ic_toolbar_back, view -> finish());
            ctb_toolbar.setTitle(getTitle().toString());
        }
    }

    public abstract int getLayoutId();

}
