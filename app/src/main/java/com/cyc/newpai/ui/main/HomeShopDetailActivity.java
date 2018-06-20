package com.cyc.newpai.ui.main;

import android.os.Bundle;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public class HomeShopDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(toolbar!=null){
            toolbar.setTitle("sssssssss");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_shop_detail;
    }

    @Override
    protected boolean getToolbarAvailable() {
        return true;
    }

    @Override
    protected int getOptionsMenuId() {
        return R.menu.navigation;
    }
}
