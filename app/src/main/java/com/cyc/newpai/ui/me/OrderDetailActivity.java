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
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.ui.me.entity.OrderDetailResultBean;
import com.cyc.newpai.util.DateUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderDetailActivity extends BaseActivity {

    public static final String ORDER_DATA_BEAN = "order_data_bean";
    public static final String ORDER_DETAIL_BEAN = "order_detail_bean";
    private MyAuctionBean myAuctionBean;
    private OrderDetailResultBean orderDetailResultBean;
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
        /*if(myAuctionBean!=null){
            title.setText(myAuctionBean.getGoods_name());
            dealPrice.setText(myAuctionBean.getDeal_price());
            GlideApp.with(this).load(myAuctionBean.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
            long time = (Long.valueOf(myAuctionBean.getLast_bid_time())*1000+7200000-System.currentTimeMillis());
            Date countDownDate = new Date(time>0?time:0);
            countDownTime.setText("剩余:"+(time>0?countDownDate.getDay():0)+"天"+(time>0?countDownDate.getHours():0)+"小时"+(time>0?countDownDate.getMinutes():0)+"分钟"+(time>0?countDownDate.getSeconds():0)+"秒");
            dealTime.setText(DateUtil.formatDate(Long.valueOf(myAuctionBean.getLast_bid_time()),"yyyy:MM:dd HH:mm:ss"));
        }else{
            finish();
        }*/
        Map<String,String> params = new HashMap<>();
        params.put("shopid",myAuctionBean.getId());
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_PAY_SUCCESS_SHOP_DETAIL_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<OrderDetailResultBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<OrderDetailResultBean>>>(){}.getType());
                        if(data.getCode()==200){
                            handler.post(()->{
                                orderDetailResultBean = data.getResult().getItem();
                                title.setText(data.getResult().getItem().getGoods_name());
                                dealPrice.setText(data.getResult().getItem().getDeal_price());
                                if(!OrderDetailActivity.this.isDestroyed()){
                                    GlideApp.with(OrderDetailActivity.this).load(data.getResult().getItem().getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
                                }
                                long time = (Long.valueOf(data.getResult().getItem().getLast_bid_time())*1000+7200000-System.currentTimeMillis());
                                Date countDownDate = new Date(time>0?time:0);
                                countDownTime.setText("剩余:"+(time>0?countDownDate.getDay():0)+"天"+(time>0?countDownDate.getHours():0)+"小时"+(time>0?countDownDate.getMinutes():0)+"分钟"+(time>0?countDownDate.getSeconds():0)+"秒");
                                dealTime.setText(DateUtil.formatDate(Long.valueOf(data.getResult().getItem().getLast_bid_time()),"yyyy:MM:dd HH:mm:ss"));
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                Intent intent = new Intent(this,SubmitOrderActivity.class);
                intent.putExtra(ORDER_DETAIL_BEAN,orderDetailResultBean);
                intent.putExtra(ORDER_DATA_BEAN,myAuctionBean);
                startActivity(intent);
                break;
        }
    }
}
