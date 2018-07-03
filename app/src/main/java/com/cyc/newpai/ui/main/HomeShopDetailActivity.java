package com.cyc.newpai.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.main.adapter.BidRecordRecyclerViewAdapter;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.BidRecordBean;
import com.cyc.newpai.ui.main.entity.ShopDetailBean;
import com.cyc.newpai.ui.main.entity.ShopDetailResultBean;
import com.cyc.newpai.util.DataGenerator;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.widget.CustomToolbar;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeShopDetailActivity extends BaseActivity {


    private static final String TAG = HomeShopDetailActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private String[] shopCategory = new String[]{"往期成交","幸运晒单","竞拍规则"};
    private Banner banner;
    private RecyclerView bidRecord;
    private BidRecordRecyclerViewAdapter bidRecordRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        banner = findViewById(R.id.ber_shop_detail_banner);
        initBanner(banner);
        tabLayout = findViewById(R.id.tl_shop_detail_tab);
        initTab(tabLayout);
        bidRecord = findViewById(R.id.rv_shop_detail_record);
        initBidRecord();
        initData();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_shop_detail_container,HistoryCompleteTransactionFragment.newInstance(HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE)).commit();
    }

    private void initTab(TabLayout mTabLayout) {
        // 提供自定义的布局添加Tab
        for(String title : shopCategory){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = HistoryCompleteTransactionFragment.newInstance(HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
                break;
            case 1:
                fragment = HistoryCompleteTransactionFragment.newInstance(HistoryCompleteTransactionAdapter.LUCKY_TIME_TYPE);
                break;
            case 2:
                fragment = HistoryCompleteTransactionFragment.newInstance(HistoryCompleteTransactionAdapter.RULE_TYPE);
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_shop_detail_container,fragment).commit();
        }
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("id","1");
        OkHttpManager.getInstance(this).postAynsHttp(HttpUrl.HTTP_SHOP_DETAIL_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String str = response.body().string();
                    ResponseBean<ShopDetailResultBean> responseBean = getGson().fromJson(str,new TypeToken<ResponseBean<ShopDetailResultBean>>() {
                    }.getType());
                    if(responseBean.getCode()==200&&responseBean.getResult()!=null){
                        ShopDetailBean data = responseBean.getResult().getItem();
                        updateDetailView(data);
                    }
                    //Log.i(TAG,data.toString());
                }
            }
        });
        params.put("shopid","1");
        OkHttpManager.getInstance(this).postAynsHttp(HttpUrl.HTTP_BID_RECORD_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String str = response.body().string();
                    ResponseBean<BidRecordBean> data = getGson().fromJson(str,new TypeToken<ResponseBean<BidRecordBean>>() {
                    }.getType());
                    if(data.getCode()==200&&data.getResult()!=null){
                        updateBidRecordView(data);
                    }
                }
            }
        });
    }

    private void initBidRecord() {
        bidRecordRecyclerViewAdapter = new BidRecordRecyclerViewAdapter(bidRecord);
        bidRecord.setLayoutManager(new LinearLayoutManager(this));
        bidRecord.setAdapter(bidRecordRecyclerViewAdapter);
    }

    private void updateBidRecordView(ResponseBean<BidRecordBean> data) {
        BidRecordBean bidRecordBean = data.getResult();
        bidRecordRecyclerViewAdapter.setListNotify(bidRecordBean.getItemBeans());
    }

    private void updateDetailView(ShopDetailBean data) {
        banner.setImages(data.getImages());

    }

    private void initBanner(Banner banner) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //Glide.with(context).load(path).into(imageView);
                //imageView.setBackgroundResource(R.drawable.test111);
                GlideApp.with(context).load(path).placeholder(R.drawable.test111).into(imageView);
            }
        });
        List<String> images = new ArrayList<>();
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        banner.setImages(images);
        banner.start();
        banner.startAutoPlay();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setRightAction1(0, view -> {

        });
        ctb_toolbar.tv_title.setTextColor(Color.BLACK);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_shop_detail;
    }
}
