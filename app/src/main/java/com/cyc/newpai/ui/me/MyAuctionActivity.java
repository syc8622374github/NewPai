package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyAuctionActivity extends BaseActivity {

    private String[] tabTitle = new String[]{"正在拍","未拍中","我在拍","待付款","待晒单"};
    private String[] auctionTypes = new String[]{"1","2","3","4","5",};
    private List<Fragment> fragments = new ArrayList<>();
    private int selectTabNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        initView();
        initData();
    }

    private void initData() {
    }

    public String getAuctionType(){
        return auctionTypes[selectTabNum];
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void initView() {
        TabLayout tabLayout = findViewById(R.id.tl_my_auction_tab);
        for(String title : tabTitle){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        ViewPager viewPager = findViewById(R.id.vp_my_auction_view_pager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTabNum = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_auction;
    }
}
