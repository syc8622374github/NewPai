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
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.adapter.CategoryDetailRecyclerAdapter;
import com.cyc.newpai.ui.category.adapter.CategoryTitleRecyclerAdapter;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;
import com.cyc.newpai.ui.category.entity.CategoryTitleBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends BaseFragment {

    private RecyclerView rvCategoryTitle;
    private RecyclerView rvCategoryDetail;
    private Map<Integer,List<CategoryDetailBean>> categoryDetailBeanMap = new HashMap<>();
    public static final String FLAG = CategoryFragment.class.getName();

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
        rvCategoryDetail.addItemDecoration(new CommItemDecoration(getContext(),1,getResources().getColor(R.color.divider),2));
        CategoryDetailRecyclerAdapter categoryDetailRecyclerAdapter = new CategoryDetailRecyclerAdapter(rvCategoryDetail);
        List<CategoryDetailBean> categoryDetailBeansType1 = new ArrayList<>();
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,1));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,2));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,3));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,3));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,4));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,5));
        categoryDetailBeansType1.add(new CategoryDetailBean(R.drawable.shop_iphonex,"1111","￥357.5","00:00:10",1,6));
        categoryDetailBeanMap.put(1,categoryDetailBeansType1);
        categoryDetailRecyclerAdapter.setListNotify(categoryDetailBeansType1);
        rvCategoryDetail.setAdapter(categoryDetailRecyclerAdapter);
    }

    private void initCategoryTitle(View view) {
        rvCategoryTitle = view.findViewById(R.id.rv_category_left_category_title);
        rvCategoryTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategoryTitle.addItemDecoration(new CommItemDecoration(getContext(),1,getResources().getColor(R.color.divider),2));
        CategoryTitleRecyclerAdapter categoryTitleRecyclerAdapter = new CategoryTitleRecyclerAdapter(rvCategoryTitle);
        List<CategoryTitleBean> categoryTitleBeans = new ArrayList<>();
        categoryTitleBeans.add(new CategoryTitleBean(1,"手机专区",true));
        categoryTitleBeans.add(new CategoryTitleBean(2,"电脑平板",false));
        categoryTitleBeans.add(new CategoryTitleBean(3,"数码影音",false));
        categoryTitleBeans.add(new CategoryTitleBean(4,"生活家电",false));
        categoryTitleBeans.add(new CategoryTitleBean(5,"运动户外",false));
        categoryTitleBeans.add(new CategoryTitleBean(6,"汽车用品",false));
        categoryTitleBeans.add(new CategoryTitleBean(7,"母婴玩具",false));
        categoryTitleRecyclerAdapter.setListNotify(categoryTitleBeans);
        categoryTitleRecyclerAdapter.setOnClickItemListener((view1, itemBean, position) -> {
            for(int i=0;i<categoryTitleBeans.size();i++){
                if(i==position){
                    categoryTitleBeans.get(i).setSelect(true);
                }else{
                    categoryTitleBeans.get(i).setSelect(false);
                }
            }
            categoryTitleRecyclerAdapter.notifyDataSetChanged();
        });
        rvCategoryTitle.setAdapter(categoryTitleRecyclerAdapter);
    }

    public static String getFlag() {
        return FLAG;
    }
}
