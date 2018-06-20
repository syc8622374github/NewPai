package com.cyc.newpai.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.cyc.newpai.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        if(toolbar !=null&&getToolbarAvailable()){
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() != 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected int getOptionsMenuId(){
        return 0;
    }

    public abstract int getLayoutId();

    protected boolean getToolbarAvailable(){
        return false;
    }
}
