package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.os.Bundle;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public class PaySuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.rv_pay_success_list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_success;
    }
}
