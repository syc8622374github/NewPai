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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.adapter.HomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.BannerDataBean;
import com.cyc.newpai.ui.main.entity.BannerResultBean;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.main.entity.HomePageBean;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.cyc.newpai.widget.MyGridView;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private Banner banner;
    private BaseFragment fragment;
    private View headView;
    private List<HomeBean> beanList = new ArrayList<>();
    private HomeRecyclerViewAdapter adapter;
    private int pageSize = 10;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeChanged(0, adapter.getList().size());
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private String[] shopCategorys = new String[]{"正在热拍", "我在拍", "我的收藏"};

    private HomeViewModel mViewModel;

    boolean isLoadMore = false;

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
        headView = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment_head_item, null);
        initBanner(headView);
        initWindow(headView);
        initTab(headView);
        initData();
    }

    private void initData() {
        Map<String, String> param = new HashMap<>();
        param.put("type", "1");
        param.put("pagesize", String.valueOf(pageSize));
        param.put("p", "1");
        OkHttpManager.getInstance(getActivity()).postNewPaiInterfaceAynsHttp(HttpUrl.HTTP_INDEX_URL, param, new OkHttpManager.HttpCallBack<HomePageBean>() {
            @Override
            public void onFailed(Call call, IOException e) {

            }

            @Override
            public void onSucessed(HomePageBean homePageBean) {
                updateShopData(homePageBean);
            }
        });

        OkHttpManager.getInstance(getActivity()).postNewPaiInterfaceAynsHttp(HttpUrl.HTTP_BANNER_URL, null, new OkHttpManager.HttpCallBack<BannerResultBean<BannerDataBean>>() {
            @Override
            public void onFailed(Call call, IOException e) {

            }

            @Override
            public void onSucessed(BannerResultBean<BannerDataBean> bannerDataBeanBannerResultBean) {
                updateBannerData(bannerDataBeanBannerResultBean.getList());
            }
        });
    }

    private void updateBannerData(List<BannerDataBean> result) {
        banner.setImages(result);
    }

    private void updateShopData(HomePageBean bean) {
        if(bean.getList()!=null){
            beanList.clear();
            beanList.addAll(bean.getList());
            handler.sendEmptyMessage(1);
        }
    }

    private void initRecyclerView(View view) {
        RecyclerView rvMain = view.findViewById(R.id.rv_main);
        adapter = new HomeRecyclerViewAdapter(rvMain);
        ((SimpleItemAnimator) rvMain.getItemAnimator()).setSupportsChangeAnimations(false);
        rvMain.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvMain.addItemDecoration(new GridDivider(getContext(), 2, getResources().getColor(R.color.divider)));
        adapter.setOnClickItemListener((view1, itemBean, position) -> startActivity(new Intent(getContext(), HomeShopDetailActivity.class)));
        rvMain.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.setAdapter(headerAndFooterRecyclerViewAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(rvMain, getFootView());
        }
        RecyclerViewUtil.addHearView(rvMain, headView);
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动停止
                    boolean isBottom = gridLayoutManager.findLastCompletelyVisibleItemPosition() >= adapter.getItemCount();
                    if (isBottom) {
                        if (!isLoadMore) {
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<HomeBean> beanList = new ArrayList<>();
                                    beanList.add(new HomeBean(10,"500"));
                                    beanList.add(new HomeBean(9,"500"));
                                    beanList.add(new HomeBean(8,"500"));
                                    adapter.addListNotify(beanList);
                                    isLoadMore = false;
                                }
                            }, 2000);
                            isLoadMore = true;
                        }
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
        /*beanList.add(new HomeBean(10,"500"));
        beanList.add(new HomeBean(9,"500"));
        beanList.add(new HomeBean(8,"500"));
        beanList.add(new HomeBean(7,"500"));
        beanList.add(new HomeBean(6,"500"));
        beanList.add(new HomeBean(5,"500"));
        beanList.add(new HomeBean(4,"500"));
        beanList.add(new HomeBean(3,"500"));
        beanList.add(new HomeBean(2,"500"));*/
        adapter.setListNotify(beanList);
        startRefreshData();
    }

    private void startRefreshData() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (HomeBean bean : beanList) {
                    bean.setLeft_second(bean.getLeft_second()-1);
                }
                handler.sendEmptyMessage(1);
            }
        }, 1000, 1000);
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
        for (String title : shopCategorys) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void initRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.srl_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //updateData();
            //            //swipeRefreshLayout.setRefreshing(false);
            //            //handler.sendEmptyMessageDelayed(1,1000);
            initData();
        });
    }

    private void initWindow(View view) {
        List<HomeWindowBean> beanList = new ArrayList<>();
        beanList.add(new HomeWindowBean("师徒分享", R.drawable.ic_home_window_share));
        beanList.add(new HomeWindowBean("大转盘", R.drawable.ic_home_window_turntable));
        beanList.add(new HomeWindowBean("每日签到", R.drawable.ic_home_window_sign));
        beanList.add(new HomeWindowBean("充值", R.drawable.ic_home_window_recharge));
        beanList.add(new HomeWindowBean("幸运晒单", R.drawable.ic_home_window_luckytime));

        int columns = 5;
        int rows = (int) Math.ceil(beanList.size() / columns);
        LinearLayout windowRoot = view.findViewById(R.id.ll_window_root);
        for (int i = 0; i < rows; i++) {
            LinearLayout child = (LinearLayout) windowRoot.getChildAt(i);
            if (child == null) {
                LinearLayout llChild = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llChild.setLayoutParams(layoutParams);
                llChild.setOrientation(LinearLayout.HORIZONTAL);
                child = llChild;
                windowRoot.addView(child);
            }
            for (int j = 0; j < columns; j++) {
                View childItem = child.getChildAt(j);
                if (childItem == null) {
                    childItem = View.inflate(getContext(), R.layout.home_window_item, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    childItem.setLayoutParams(layoutParams);
                    child.addView(childItem);
                }
                ((ImageView) childItem.findViewById(R.id.iv_home_window_icon)).setImageResource(beanList.get(j + i * j).getImageRes());
                ((TextView) childItem.findViewById(R.id.tv_home_window_title)).setText(beanList.get(j + i * j).getTitle());
            }
        }
    }

    private void initBanner(View view) {
        banner = view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    default:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
            }
        });
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                String pathStr = "";
                if(path instanceof BannerDataBean){
                    pathStr = ((BannerDataBean) path).getImg_url();
                }
                GlideApp.with(context).load(pathStr).placeholder(R.drawable.test111).into(imageView);
            }
        });
        List<String> images = new ArrayList<>();
        /*images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");*/
        banner.setImages(images);
        banner.start();
        banner.startAutoPlay();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
