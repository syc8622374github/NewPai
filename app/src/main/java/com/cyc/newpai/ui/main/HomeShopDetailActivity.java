package com.cyc.newpai.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.util.DataGenerator;
import com.cyc.newpai.widget.CustomToolbar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeShopDetailActivity extends BaseActivity {


    private TabLayout tabLayout;
    private String[] shopCategory = new String[]{"往期成交","幸运晒单","竞拍规则"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Banner banner = findViewById(R.id.ber_shop_detail_banner);
        initBanner(banner);
        tabLayout = findViewById(R.id.tl_shop_detail_tab);
        initTab(tabLayout);
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

    private void initBanner(Banner banner) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //Glide.with(context).load(path).into(imageView);
                imageView.setBackgroundResource(R.drawable.test111);
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
