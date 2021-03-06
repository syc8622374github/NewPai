package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class MyAuctionActivity extends BaseActivity {

    public String[] tabTitle = new String[]{"正在拍","未拍中","我拍中","待付款","待晒单"};
    public String[] auctionTypes = new String[]{"1","2","3","4","5"};
    private List<MyAutionAllFragment> fragments = new ArrayList<>();
    private int selectTabNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments.add(MyAutionAllFragment.newInstance(auctionTypes[0]));
        fragments.add(MyAutionAllFragment.newInstance(auctionTypes[1]));
        fragments.add(MyAutionAllFragment.newInstance(auctionTypes[2]));
        fragments.add(MyAutionAllFragment.newInstance(auctionTypes[3]));
        fragments.add(MyAutionAllFragment.newInstance(auctionTypes[4]));
        initView();
        initData();
    }

    private void initData() {
    }

    /*public String getAuctionType(){
        return auctionTypes[selectTabNum];
    }*/

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
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectTabNum = position;
                fragments.get(selectTabNum).refreshLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
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
