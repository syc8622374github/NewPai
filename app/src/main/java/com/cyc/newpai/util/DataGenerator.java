package com.cyc.newpai.util;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.ui.category.CategoryFragment;
import com.cyc.newpai.ui.main.HomeFragment;
import com.cyc.newpai.ui.me.MeFragment;
import com.cyc.newpai.ui.transaction.CompleteTransactionFragment;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.tab_home_selector,R.drawable.tab_complete_no_selector,R.drawable.tab_category_no_selector,R.drawable.tab_me_no_selector};
    public static final int []mTabResPressed = new int[]{R.drawable.tab_home_selector,R.drawable.tab_complete_selector,R.drawable.tab_category_selector,R.drawable.tab_me_selector};
    public static final String []mTabTitle = new String[]{"首页","最新成交","分类","我的"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[4];
        fragments[0] = HomeFragment.newInstance();
        fragments[1] = CompleteTransactionFragment.newInstance();
        fragments[2] = CategoryFragment.newInstance();
        fragments[3] = MeFragment.newInstance();
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}