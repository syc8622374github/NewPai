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

    private String[] tabTitle = new String[]{"全部","正在拍","未拍中","我在拍","待付款","待晒单"};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        fragments.add(MyAutionAllFragment.newInstance());
        initView();
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
        viewPager.setAdapter(mAdapter);

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
