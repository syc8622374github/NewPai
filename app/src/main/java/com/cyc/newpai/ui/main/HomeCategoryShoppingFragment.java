package com.cyc.newpai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeCategoryShoppingFragment extends BaseFragment {

    private RecyclerView rvMain;
    private HomeRecyclerViewAdapter adapter;
    private List<HomeBean> beanList;
    private LoadingFooter mFooterView;

    public class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public MyHandler handler = new MyHandler(Looper.getMainLooper());

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
        adapter.setOnClickItemListener((view, itemBean, position) -> startActivity(new Intent(getContext(),HomeShopDetailActivity.class)));
        rvMain.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.setAdapter(headerAndFooterRecyclerViewAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(rvMain, getFootView());
        }
        beanList = new ArrayList<>();
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,10,"暂未拍得",100));
        adapter.setListNotify(beanList);
        Timer timer = new Timer();
        Random random = new Random();
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(HomeBean bean : beanList){
                    bean.setCountdown(bean.getCountdown()-1);
                    bean.setPrice(bean.getPrice()+random.nextInt(10));
                }
                handler.sendEmptyMessage(1);
            }
        },1000,1000);*/
    }

    @Override
    public void updateFragmentData() {
        adapter.addListNotify(beanList);
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
