package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;

public class OrderDetailActivity extends BaseActivity {

    private TextView title;
    private TextView nowPrice;
    private ImageView shopIcon;
    private TextView orderCount;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
        title = findViewById(R.id.tv_order_detail_title);
        nowPrice = findViewById(R.id.tv_order_detail_now_price);
        shopIcon = findViewById(R.id.iv_order_detail_icon);
        orderCount = findViewById(R.id.tv_order_detail_order_count);
        time = findViewById(R.id.tv_order_detail_time);
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
