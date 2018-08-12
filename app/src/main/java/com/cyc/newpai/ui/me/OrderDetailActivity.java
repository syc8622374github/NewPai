package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.util.DateUtil;

import java.util.Date;

public class OrderDetailActivity extends BaseActivity {

    public static final String ORDER_DATA_BEAN = "order_data_bean";
    private MyAuctionBean myAuctionBean;

    private TextView title;
    private TextView dealPrice;
    private ImageView shopIcon;
    private TextView dealTime;
    private TextView countDownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAuctionBean = (MyAuctionBean) getIntent().getSerializableExtra(ORDER_DATA_BEAN);
        initView();
        initData();
    }

    private void initData() {
        if(myAuctionBean!=null){
            title.setText(myAuctionBean.getGoods_name());
            dealPrice.setText(myAuctionBean.getDeal_price());
            GlideApp.with(this).load(myAuctionBean.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
            long time = (Long.valueOf(myAuctionBean.getLast_bid_time())*1000+7200000-System.currentTimeMillis());
            Date countDownDate = new Date(time>0?time:0);
            countDownTime.setText("剩余:"+(time>0?countDownDate.getDay():0)+"天"+(time>0?countDownDate.getHours():0)+"小时"+(time>0?countDownDate.getMinutes():0)+"分钟"+(time>0?countDownDate.getSeconds():0)+"秒");
            dealTime.setText(DateUtil.formatDate(Long.valueOf(myAuctionBean.getLast_bid_time()),"yyyy:MM:dd HH:mm:ss"));
        }else{
            finish();
        }
    }

    private void initView() {
        title = findViewById(R.id.tv_order_detail_title);
        dealPrice = findViewById(R.id.tv_order_detail_deal_price);
        shopIcon = findViewById(R.id.iv_order_detail_icon);
        dealTime = findViewById(R.id.tv_order_detail_time);
        countDownTime = findViewById(R.id.tv_order_detail_count_down_time);
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
