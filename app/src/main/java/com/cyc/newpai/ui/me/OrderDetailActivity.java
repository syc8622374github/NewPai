package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public class OrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.btn_order_detail_ok:
                startActivity(new Intent(this,SubmitOrderActivity.class));
                break;
        }
    }
}
