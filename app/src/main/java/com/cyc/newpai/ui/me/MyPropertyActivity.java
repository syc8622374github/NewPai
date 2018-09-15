package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.framework.base.BaseFragment;

public class MyPropertyActivity extends BaseActivity {

    private TabLayout tabLayout;

    private String[] shopCategorys = new String[]{"全部记录","收入","支出"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(R.color.color_toolbar_title_black));
    }

    private void initView() {
        tabLayout = findViewById(R.id.tbl_my_property_nav);
        initTab();
    }

    private void initTab() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        // 提供自定义的布局添加Tab
        for(String title : shopCategorys){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void onTabItemSelected(int position){
        BaseFragment fragment = null;
        switch (position){
            case 0:
                fragment = MyPropertyAllRecordFragment.newInstance(MyPropertyAllRecordFragment.TYPE_ALL_RECORD);
                break;
            case 1:
                fragment = MyPropertyAllRecordFragment.newInstance(MyPropertyAllRecordFragment.TYPE_INCOME_RECORD);
                break;
            case 2:
                fragment = MyPropertyAllRecordFragment.newInstance(MyPropertyAllRecordFragment.TYPE_PAY_RECORD);
                break;
        }
        if(fragment !=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_property_container, fragment).commit();
        }
    }
}
