package com.cyc.newpai.ui.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.adapter.CategoryDetailRecyclerAdapter;
import com.cyc.newpai.ui.category.adapter.CategoryTitleRecyclerAdapter;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;
import com.cyc.newpai.ui.category.entity.CategoryTitleBean;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment {

    private RecyclerView rvCategoryTitle;
    private RecyclerView rvCategoryDetail;

    public static CategoryFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);
        initCategoryTitle(view);
        initCategoryDetail(view);
        return view;
    }

    private void initCategoryDetail(View view) {
        rvCategoryDetail = view.findViewById(R.id.rv_category_right_category_detail);
        rvCategoryDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategoryDetail.addItemDecoration(new DividerItemDecoration(getContext(),1));
        CategoryDetailRecyclerAdapter categoryDetailRecyclerAdapter = new CategoryDetailRecyclerAdapter(rvCategoryDetail);
        List<CategoryDetailBean> categoryDetailBeans = new ArrayList<>();
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailBeans.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10"));
        categoryDetailRecyclerAdapter.setListNotify(categoryDetailBeans);
        rvCategoryDetail.setAdapter(categoryDetailRecyclerAdapter);
    }

    private void initCategoryTitle(View view) {
        rvCategoryTitle = view.findViewById(R.id.rv_category_left_category_title);
        rvCategoryTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategoryTitle.addItemDecoration(new DividerItemDecoration(getContext(),1));
        CategoryTitleRecyclerAdapter categoryTitleRecyclerAdapter = new CategoryTitleRecyclerAdapter(rvCategoryTitle);
        List<CategoryTitleBean> categoryTitleBeans = new ArrayList<>();
        categoryTitleBeans.add(new CategoryTitleBean("手机专区",true));
        categoryTitleBeans.add(new CategoryTitleBean("电脑平板",false));
        categoryTitleBeans.add(new CategoryTitleBean("数码影音",false));
        categoryTitleBeans.add(new CategoryTitleBean("生活家电",false));
        categoryTitleBeans.add(new CategoryTitleBean("运动户外",false));
        categoryTitleBeans.add(new CategoryTitleBean("汽车用品",false));
        categoryTitleBeans.add(new CategoryTitleBean("母婴玩具",false));
        categoryTitleRecyclerAdapter.setListNotify(categoryTitleBeans);
        rvCategoryTitle.setAdapter(categoryTitleRecyclerAdapter);
    }
}
