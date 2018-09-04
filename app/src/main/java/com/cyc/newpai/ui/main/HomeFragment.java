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
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.base.WrapContentGridLayoutManager;
import com.cyc.newpai.framework.adapter.interfaces.OnItemClickListener;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.common.entity.TopLineBean;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.adapter.NewHomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.BannerDataBean;
import com.cyc.newpai.ui.main.entity.BannerResultBean;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.main.entity.HomePageBean;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.util.ScreenUtil;
import com.cyc.newpai.util.ViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private View headView;
    private List<HomeBean> beanList = new ArrayList<>();
    private int pageSize = 10;
    private TextSwitcher topLine;
    private List<String> images;
    private int recyclerCount = 0;
    private List<TopLineBean> topLineBeanList = new ArrayList<>();
    private NewHomeRecyclerViewAdapter newHomeRecyclerViewAdapter;
    private Timer timer;
    private String selectType = "1";
    private View loading;
    private RecyclerView rvMain;
    private String[] shopCategorys = new String[]{"正在热拍", "我在拍", "我的收藏"};
    private HomeViewModel mViewModel;
    boolean isLoadMore = false;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 2:
                        if (recyclerCount < topLineBeanList.size()) {
                            // 设置切入动画
                            topLine.setInAnimation(AnimationUtils.loadAnimation(getMyActivity(), R.anim.slide_in_bottom));
                            // 设置切出动画
                            topLine.setOutAnimation(AnimationUtils.loadAnimation(getMyActivity(), R.anim.slide_out_up));
                            //items是一个字符串列表，index就是动态的要显示的items中的索引
                            topLine.setText(Html.fromHtml("恭喜"
                                    + topLineBeanList.get(recyclerCount).getNickname()
                                    + "以" + "<font color=#FF6A6A>￥" + topLineBeanList.get(recyclerCount).getDeal_price() + "</font>" + "拍到" + topLineBeanList.get(recyclerCount).getGoods_name()));
                            handler.sendEmptyMessageDelayed(2, 2000);
                            recyclerCount++;
                            if (recyclerCount == topLineBeanList.size()) {
                                recyclerCount = 0;
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loading = view.findViewById(R.id.v_loading);
        initVaryView();
        initHeader();
        initRefresh(view);
        initRecyclerView(view);
        initData();
        initBannerData();
    }

    private void initHeader() {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.home_fragment_head_item, null);
        initTopLine(headView);
        initBanner(headView);
        initWindow(headView);
        initTab(headView);
    }

    private void initTopLine(View headView) {
        topLine = headView.findViewById(R.id.ts_home_fragment_top_line);
        topLine.setFactory(() -> {
            TextView tv = new TextView(getContext());
            //设置文字大小
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            //设置文字 颜色
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(true);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setLayoutParams(layoutParams);
            return tv;
        });
        handler.sendEmptyMessageDelayed(2, 1000);
    }

    @Override
    public void onStart() {
        super.onStart();
        startRefreshData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initBannerData() {
        OkHttpManager.getInstance(getMyActivity()).postAsyncHttp(HttpUrl.HTTP_BANNER_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<BannerResultBean<BannerDataBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<BannerResultBean<BannerDataBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            updateBannerData(responseBean.getResult().getList());
                            return;
                        }
                    }
                    ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void initData() {
        updateIndexData(selectType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> swipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<HomePageBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            updateShopData(responseBean.getResult());
                            return;
                        }
                    }
                    ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    handler.post(() -> swipeRefreshLayout.setRefreshing(false));
                }
            }
        });

        OkHttpManager.getInstance(getMyActivity()).postAsyncHttp(HttpUrl.HTTP_HEADLINE_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<TopLineBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<TopLineBean>>>() {
                        }.getType());
                        if (data.getCode() == 200 && data.getResult() != null) {
                            List<TopLineBean> topLineBeans = data.getResult().getList();
                            updateTopLine(topLineBeans);
                        }
                        return;
                    }
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载异常", Toast.LENGTH_LONG));
                }
            }
        });
    }

    private void updateIndexData(String type, Callback callback) {
        Map<String, String> param = new HashMap<>();
        param.put("type", type);
        param.put("pagesize", String.valueOf(pageSize));
        param.put("p", selectType);
        OkHttpManager.getInstance(getMyActivity()).postAsyncHttp(HttpUrl.HTTP_INDEX_URL, param, callback);
    }

    private void updateTopLine(List<TopLineBean> topLineBean) {
        if (topLineBean != null) {
            topLineBeanList.clear();
            topLineBeanList.addAll(topLineBean);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return loading;
    }

    private void updateBannerData(List<BannerDataBean> result) {
        handler.post(() -> banner.update(result));
    }

    private void updateShopData(HomePageBean bean) {
        if (bean.getList() != null) {
            beanList.clear();
            beanList.addAll(bean.getList());
            pageSize = beanList.size();
            handler.post(() -> newHomeRecyclerViewAdapter.setListNotifyCustom(beanList));
        }
    }

    private void initRecyclerView(View view) {
        rvMain = view.findViewById(R.id.rv_main);
        ((SimpleItemAnimator) rvMain.getItemAnimator()).setSupportsChangeAnimations(false);
        rvMain.getItemAnimator().setChangeDuration(0);
        GridLayoutManager gridLayoutManager = new WrapContentGridLayoutManager(getContext(), 2);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.addItemDecoration(new GridDivider(getContext(), 2, getResources().getColor(R.color.divider)));
        newHomeRecyclerViewAdapter = new NewHomeRecyclerViewAdapter(getMyActivity(), null, true, NewHomeRecyclerViewAdapter.HOME_DATA_TYPE);
        //初始化 开始加载更多的loading View
        newHomeRecyclerViewAdapter.setLoadingView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.Loading));
        //newHomeRecyclerViewAdapter.setReloadView(LayoutInflater.from(getMyActivity()).inflate(R.layout.layout_emptyview, (ViewGroup) rvMain.getParent(), false));
        //加载失败，更新footer view提示
        newHomeRecyclerViewAdapter.setLoadFailedView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.NetWorkError));
        //加载完成，更新footer view提示
        newHomeRecyclerViewAdapter.setLoadEndView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.TheEnd));
        //RecyclerViewUtil.addHearView(rvMain,headView);
        newHomeRecyclerViewAdapter.addHeaderView(headView);
        //newHomeRecyclerViewAdapter.setEmptyView(Util.inflate(getMyActivity(),R.layout.layout_emptyview,(ViewGroup) rvMain,false));
        //设置加载更多触发的事件监听
        newHomeRecyclerViewAdapter.setOnLoadMoreListener(isReload -> getView().postDelayed(() -> {
            pageSize += 10;
            updateIndexData(selectType, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    isLoadMore = false;
                    pageSize -= 10;
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            ResponseBean<HomePageBean> responseBean = getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                            }.getType());
                            if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                if (responseBean.getResult().getList().size() > newHomeRecyclerViewAdapter.getDataCount()) {
                                    updateShopData(responseBean.getResult());
                                } else {
                                    handler.post(() -> newHomeRecyclerViewAdapter.loadEnd());
                                }
                                return;
                            }
                        }
                        ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        isLoadMore = false;
                    }
                    pageSize -= 10;
                }
            });
        }, 500));
        rvMain.setAdapter(newHomeRecyclerViewAdapter);
        newHomeRecyclerViewAdapter.setOnItemClickListener((OnItemClickListener<HomeBean>) (viewHolder, data, position) -> {
            Intent intent = new Intent(getContext(), HomeShopDetailActivity.class);
            intent.putExtra("id", data.getId());
            startActivity(intent);
        });
        startRefreshData();
    }

    private void startRefreshData() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateIndexData(selectType, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();
                                    ResponseBean<HomePageBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                                    }.getType());
                                    if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                        updateShopData(responseBean.getResult());
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    private void initTab(View view) {
        tabLayout = view.findViewById(R.id.tl_home_shop_category);
        // 提供自定义的布局添加Tab
        for (String title : shopCategorys) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
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
    }

    private void onTabItemSelected(int position) {
        selectType = (position + 1) + "";
        varyViewHelper.showLoadingView();
        newHomeRecyclerViewAdapter.setData(new ArrayList());
        updateIndexData(selectType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<HomePageBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            if (responseBean.getResult().getList().size() > 0) {
                                updateShopData(responseBean.getResult());
                            }
                            rvMain.post(() -> varyViewHelper.showDataView());
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void initRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.srl_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initBannerData();
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
        //windowRoot.setPadding(ScreenUtil.dp2px(getMyActivity(),10),0,ScreenUtil.dp2px(getMyActivity(),10),0);
        for (int i = 0; i < rows; i++) {
            LinearLayout child = (LinearLayout) windowRoot.getChildAt(i);
            if (child == null) {
                LinearLayout llChild = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llChild.setGravity(Gravity.CENTER);
                llChild.setLayoutParams(layoutParams);
                llChild.setOrientation(LinearLayout.HORIZONTAL);
                child = llChild;
                windowRoot.addView(child);
            }
            for (int j = 0; j < columns; j++) {
                View childItem = child.getChildAt(j);
                if (childItem == null) {
                    int itemWidth = ScreenUtil.getScreenWidth(getMyActivity()) / columns;
                    childItem = View.inflate(getContext(), R.layout.home_window_item, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(itemWidth - ScreenUtil.dp2px(getMyActivity(),20), itemWidth - ScreenUtil.dp2px(getMyActivity(),20));
                    childItem.findViewById(R.id.iv_home_window_icon).setLayoutParams(layoutParams);
                    child.addView(childItem);
                    int finalI = i;
                    int finalJ = j;
                    childItem.setOnClickListener(v -> {
                        if (finalI == 0) {
                            if (finalJ == 3) {
                                startActivity(new Intent(getContext(), RechargeActivity.class));
                            } else if (finalJ == 4) {
                                startActivity(new Intent(getContext(), MainLuckyTimeActivity.class));
                            }
                        }
                    });
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
        banner.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:// 经测试，ViewPager的DOWN事件不会被分发下来
                    break;
                case MotionEvent.ACTION_MOVE:
                    swipeRefreshLayout.setEnabled(false);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    swipeRefreshLayout.setEnabled(true);
                    break;
            }
            return false;
        });
        banner.setImageLoader(new ImageLoader() {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                String pathStr = "";
                if (path instanceof BannerDataBean) {
                    pathStr = ((BannerDataBean) path).getImg_url();
                }
                try {
                    GlideApp.with(context).load(pathStr).placeholder(R.drawable.test111).into(imageView);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        images = new ArrayList<>();
        banner.isAutoPlay(true);
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
