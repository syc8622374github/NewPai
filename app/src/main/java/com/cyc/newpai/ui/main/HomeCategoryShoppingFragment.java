package com.cyc.newpai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.adapter.HomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.widget.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

public class HomeCategoryShoppingFragment extends BaseFragment {

    private RecyclerView rvMain;
    private HomeRecyclerViewAdapter adapter;
    private List<HomeBean> beanList;
    private LoadingFooter mFooterView;

    public static HomeCategoryShoppingFragment newInstance() {
        
        Bundle args = new Bundle();
        HomeCategoryShoppingFragment fragment = new HomeCategoryShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category_shopping,container,false);
        rvMain = view.findViewById(R.id.rv_main);
        init(rvMain);
        return view;
    }

    private void init(RecyclerView rvMain) {
        adapter = new HomeRecyclerViewAdapter(rvMain);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMain.addItemDecoration(new GridDivider(getContext(),2,getResources().getColor(R.color.divider)));
        adapter.setOnClickItemListener((view, itemBean, position) -> startActivity(new Intent(getContext(),RechargeActivity.class)));
        rvMain.setHasFixedSize(true);
        rvMain.setAdapter(headerAndFooterRecyclerViewAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(rvMain, getFootView());
        }
        rvMain.setLayoutManager(new GridLayoutManager(getContext(),2));
        beanList = new ArrayList<>();
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        adapter.setListNotify(beanList);
    }

    @Override
    public void updateFragmentData() {
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:9","暂未拍得"));
        adapter.notifyDataSetChanged();
    }

    protected View getFootView() {
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(getActivity());
            mFooterView.setState(LoadingFooter.State.Loading);
        }
        return mFooterView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
