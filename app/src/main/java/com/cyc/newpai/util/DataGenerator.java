package com.cyc.newpai.util;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.CategoryFragment;
import com.cyc.newpai.ui.main.HomeFragment;
import com.cyc.newpai.ui.me.MeFragment;
import com.cyc.newpai.ui.transaction.CompleteTransactionFragment;
import com.cyc.newpai.ui.user.LoginActivity;
import com.cyc.newpai.ui.user.RegisterAndResetPasswdActivity;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.tab_home_no_select,R.drawable.tab_complete_no_selector,R.drawable.tab_category_no_selector,R.drawable.tab_me_no_selector};
    public static final int []mTabResPressed = new int[]{R.drawable.tab_home_selector,R.drawable.tab_complete_selector,R.drawable.tab_category_selector,R.drawable.tab_me_selector};
    public static final String []mTabTitle = new String[]{"首页","最新成交","分类","我的"};
    public static final String []mLoginTabTitle = new String[]{"快捷登录","账号登录"};

    public static BaseFragment[] getFragments(String from){
        BaseFragment fragments[] = new BaseFragment[4];
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
    public static View getMainBottomTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }

    public static View getLoginTabView(Context context,int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_login_custom_item,null);
        TextView tabText = view.findViewById(R.id.tv_login_tab_title);
        tabText.setText(mLoginTabTitle[position]);
        if(position == 0){
            view.setTag(LoginActivity.TYPE_QUICK_LOGIN);
        }else if(position == 1){
            view.setTag(LoginActivity.TYPE_USER_LOGIN);
        }
        return view;
    }
}