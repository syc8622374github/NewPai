package com.cyc.newpai;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.CategoryFragment;
import com.cyc.newpai.ui.main.HomeFragment;
import com.cyc.newpai.util.DataGenerator;

public class MainActivity extends BaseActivity {

    private BaseFragment []mFragmensts;
    private TabLayout mTabLayout;
    private Fragment showPositionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmensts = DataGenerator.getFragments("");
        mTabLayout = findViewById(R.id.tl_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BaseFragment fragment = mFragmensts[tab.getPosition()];
                onTabItemSelected(fragment);
                // Tab 选中之后，改变各个Tab的状态
                for (int i=0;i<mTabLayout.getTabCount();i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = view.findViewById(R.id.tab_content_image);
                    TextView text = view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
                if(fragment.getClass().getName() == CategoryFragment.getFlag()){
                    ctb_toolbar.divider.setVisibility(View.GONE);
                }else{
                    ctb_toolbar.divider.setVisibility(View.VISIBLE);
                }
                ctb_toolbar.setTitle(((TextView)tab.getCustomView().findViewById(R.id.tab_content_text)).getText().toString());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 提供自定义的布局添加Tab
        for(int i=0;i<4;i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this,i)));
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setLeftAction1(R.drawable.ic_search,null);
        ctb_toolbar.setRightAction1(R.drawable.ic_notification,null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void onTabItemSelected(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment!=null&&!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container,fragment,fragment.getClass().getName());
        }
        if(showPositionFragment!=null){
            fragmentTransaction.hide(showPositionFragment);
        }
        fragmentTransaction.show(fragment).commit();
        showPositionFragment = fragment;
    }

}
