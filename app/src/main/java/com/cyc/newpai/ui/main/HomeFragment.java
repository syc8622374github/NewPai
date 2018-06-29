package com.cyc.newpai.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.adapter.HomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.adapter.HomeWindowRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private Banner banner;
    private RecyclerView window;
    private BaseFragment fragment;
    private View headView;
    private List<HomeBean> beanList;
    private HomeRecyclerViewAdapter adapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public class MyHandler extends Handler{

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    }

    public MyHandler handler = new MyHandler(Looper.getMainLooper());

    private String[] shopCategorys = new String[]{"正在热拍","我在拍","我的收藏"};

    private HomeViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initHeader();
        initRefresh(view);
        initRecyclerView(view);
    }

    private void initHeader() {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment_head_item,null);
        initBanner(headView);
        initWindow(headView);
        initTab(headView);
    }

    private void initRecyclerView(View view) {
        RecyclerView rvMain = view.findViewById(R.id.rv_main);
        adapter = new HomeRecyclerViewAdapter(rvMain);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMain.addItemDecoration(new GridDivider(getContext(),2,getResources().getColor(R.color.divider)));
        adapter.setOnClickItemListener((view1, itemBean, position) -> startActivity(new Intent(getContext(),RechargeActivity.class)));
        rvMain.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.setAdapter(headerAndFooterRecyclerViewAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(rvMain, getFootView());
        }
        RecyclerViewUtil.addHearView(rvMain,headView);
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
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动停止
                    boolean isBottom = gridLayoutManager.findLastCompletelyVisibleItemPosition()>= adapter.getItemCount();
                    if (isBottom) {
                        getView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addListNotify(beanList);
                            }
                        },2000);
                    }
                } else if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    //用户正在滑动
//                    Logger.d("用户正在滑动 position=" + mAdapter.getAdapterPosition());
                } else {
                    //惯性滑动
//                    Logger.d("惯性滑动 position=" + mAdapter.getAdapterPosition());
                }
            }
        });
        Timer timer = new Timer();
        Random random = new Random();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(HomeBean bean : beanList){
                    bean.setCountdown(bean.getCountdown()-1);
                    bean.setPrice(bean.getPrice()+random.nextInt(10));
                }
                handler.sendEmptyMessage(1);
            }
        },1000,1000);
    }

    protected View getFootView() {
        LoadingFooter mFooterView = null;
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(getActivity());
            mFooterView.setState(LoadingFooter.State.Loading);
        }
        return mFooterView;
    }

    private void initTab(View view) {
        tabLayout = view.findViewById(R.id.tl_home_shop_category);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //onTabItemSelected(tab.getPosition());
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

    private void initRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.srl_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //updateData();
            //swipeRefreshLayout.setRefreshing(false);
            //handler.sendEmptyMessageDelayed(1,1000);
        });
    }

    private void updateData() {
        fragment.refreshFragmentData();
    }

    private void onTabItemSelected(int position){
        fragment = null;
        switch (position){
            case 0:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
            case 1:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
            case 2:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
        }
        if(fragment !=null) {
            //getChildFragmentManager().beginTransaction().replace(R.id.fl_home_shop_content, fragment).commit();
        }
    }

    private void initWindow(View view) {
        window = view.findViewById(R.id.rv_window);
        window.setLayoutManager(new GridLayoutManager(getContext(),5));
        HomeWindowRecyclerViewAdapter adapter  = new HomeWindowRecyclerViewAdapter(window);
        List<HomeWindowBean> beanList = new ArrayList<>();
        beanList.add(new HomeWindowBean("师徒分享", R.drawable.ic_home_window_share));
        beanList.add(new HomeWindowBean("大转盘",R.drawable.ic_home_window_turntable));
        beanList.add(new HomeWindowBean("每日签到",R.drawable.ic_home_window_sign));
        beanList.add(new HomeWindowBean("充值",R.drawable.ic_home_window_recharge));
        beanList.add(new HomeWindowBean("幸运晒单",R.drawable.ic_home_window_luckytime));
        adapter.setListNotify(beanList);
        window.setAdapter(adapter);
    }

    private void initBanner(View view) {
        banner = view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //Glide.with(context).load(path).into(imageView);
                imageView.setBackgroundResource(R.drawable.test111);
            }
        });
        List<String> images = new ArrayList<>();
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        banner.setImages(images);
        banner.start();
        banner.startAutoPlay();
    }

    private void updateBanner(List<String> images) {
        banner.update(images);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
