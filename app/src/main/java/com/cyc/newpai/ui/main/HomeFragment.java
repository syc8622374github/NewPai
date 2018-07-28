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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.common.entity.TopLineBean;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.adapter.HomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.BannerDataBean;
import com.cyc.newpai.ui.main.entity.BannerResultBean;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.main.entity.HomePageBean;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.RecyclerViewUtil;
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
    private BaseFragment fragment;
    private View headView;
    private List<HomeBean> beanList = new ArrayList<>();
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    private int pageSize = 10;
    private TextSwitcher topLine;
    private List<String> images;
    private int recyclerCount = 0;
    private List<TopLineBean> topLineBeanList = new ArrayList<>();
    private HeaderAndFooterRecyclerViewAdapter homeHeaderAndFooterRecyclerViewAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //adapter.notifyDataSetChanged();
                    homeRecyclerViewAdapter.notifyItemRangeChanged(0, homeRecyclerViewAdapter.getList().size());
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 2:
                    if(recyclerCount<topLineBeanList.size()){
                        // 设置切入动画
                        topLine.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom));
                        // 设置切出动画
                        topLine.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_up));
                        //items是一个字符串列表，index就是动态的要显示的items中的索引
                        topLine.setText(Html.fromHtml("恭喜"
                                +topLineBeanList.get(recyclerCount).getNickname()
                                +"以"+ "<font color=#FF6A6A>￥"+topLineBeanList.get(recyclerCount).getDeal_price()+"</font>"+"拍到"+topLineBeanList.get(recyclerCount).getGoods_name()));
                        handler.sendEmptyMessageDelayed(2,2000);
                        recyclerCount++;
                        if(recyclerCount==topLineBeanList.size()){
                            recyclerCount=0;
                        }
                    }
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
        initData();
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
            TextView tv =new TextView(getContext());
            //设置文字大小
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            //设置文字 颜色
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(true);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setLayoutParams(layoutParams);
            return tv;
        });
        handler.sendEmptyMessageDelayed(2,1000);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private void initData() {
        Map<String, String> param = new HashMap<>();
        updateIndexData(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(()->swipeRefreshLayout.setRefreshing(false));
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
                    handler.post(()->swipeRefreshLayout.setRefreshing(false));
                }
            }
        });

        OkHttpManager.getInstance(getActivity()).postAsyncHttp(HttpUrl.HTTP_BANNER_URL, null, new Callback() {
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

        OkHttpManager.getInstance(getActivity()).postAsyncHttp(HttpUrl.HTTP_HEADLINE_URL, null, new Callback() {
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
                        //handler.post(() -> ToastManager.showToast(getContext(), data.getMsg(), Toast.LENGTH_LONG));
                        return;
                    }
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载异常", Toast.LENGTH_LONG));
                }
            }
        });
    }

    private void updateIndexData(Map<String, String> param,Callback callback) {
        param.put("type", "1");
        param.put("pagesize", String.valueOf(pageSize));
        param.put("p", "1");
        OkHttpManager.getInstance(getActivity()).postAsyncHttp(HttpUrl.HTTP_INDEX_URL, param,callback);
    }

    private void updateTopLine(List<TopLineBean> topLineBean) {
        if(topLineBean!=null){
            topLineBeanList.clear();
            topLineBeanList.addAll(topLineBean);
        }
    }

    private void updateBannerData(List<BannerDataBean> result) {
        handler.post(()-> banner.update(result));
    }

    private void updateShopData(HomePageBean bean) {
        if(bean.getList().size()==homeRecyclerViewAdapter.getList().size()){

        }
        if (bean.getList() != null) {
            beanList.clear();
            beanList.addAll(bean.getList());
            pageSize = beanList.size();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    homeRecyclerViewAdapter.setListNotify(bean.getList());
                }
            });
            //handler.sendEmptyMessage(1);
        }
    }

    private void initRecyclerView(View view) {
        RecyclerView rvMain = view.findViewById(R.id.rv_main);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(rvMain);
        ((SimpleItemAnimator) rvMain.getItemAnimator()).setSupportsChangeAnimations(false);
        rvMain.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        homeHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(homeRecyclerViewAdapter);
        rvMain.addItemDecoration(new GridDivider(getContext(), 2, getResources().getColor(R.color.divider)));
        homeRecyclerViewAdapter.setOnClickItemListener((view1, itemBean, position) -> startActivity(new Intent(getContext(), HomeShopDetailActivity.class)));
        rvMain.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMain.setLayoutManager(gridLayoutManager);
        rvMain.setAdapter(homeHeaderAndFooterRecyclerViewAdapter);
        if (getFootView(LoadingFooter.State.Loading) != null) {
            RecyclerViewUtil.addFootView(rvMain, getFootView(LoadingFooter.State.Loading));
        }
        RecyclerViewUtil.addHearView(rvMain, headView);
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动停止
                    boolean isBottom = gridLayoutManager.findLastCompletelyVisibleItemPosition() >= homeRecyclerViewAdapter.getItemCount();
                    if (((LoadingFooter)homeHeaderAndFooterRecyclerViewAdapter.getFooterView()).getState()==LoadingFooter.State.Loading&&isBottom && homeRecyclerViewAdapter.getItemCount() > 0) {
                        if (!isLoadMore) {
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pageSize++;
                                    updateIndexData(new HashMap<>(), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            isLoadMore = false;
                                            pageSize--;
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
                                                        //RecyclerViewUtil.removeFooterView(rvMain);
                                                        //RecyclerViewUtil.addFootView(rvMain,getFootView(LoadingFooter.State.TheEnd));
                                                        return;
                                                    }
                                                }
                                                ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.getMessage());
                                            } finally {
                                                isLoadMore = false;
                                            }
                                            pageSize--;
                                        }
                                    });
                                    /*List<HomeBean> beanList = new ArrayList<>();
                                    beanList.add(new HomeBean(10, "500"));
                                    beanList.add(new HomeBean(9, "500"));
                                    beanList.add(new HomeBean(8, "500"));
                                    homeRecyclerViewAdapter.addListNotify(beanList);*/
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
        startRefreshData();
    }

    private void startRefreshData() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*for (HomeBean bean : beanList) {
                    //bean.setLeft_second(bean.getLeft_second() - 1);
                    //bean.setNow_price((Double.valueOf(bean.getNow_price()) + Math.round(10))+"");
                }*/
                updateIndexData(new HashMap<>(), new Callback() {
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

                                    Log.i("updateIndex",str);
                                    return;
                                }
                            }
                            //ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

                //handler.sendEmptyMessage(1);
            }
        }, 1000, 1000);
    }

    protected View getFootView(LoadingFooter.State state) {
        LoadingFooter mFooterView = null;
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(getActivity());
            mFooterView.setState(state);
        }
        return mFooterView;
    }

    private void initTab(View view) {
        tabLayout = view.findViewById(R.id.tl_home_shop_category);
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
        // 提供自定义的布局添加Tab
        for (String title : shopCategorys) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void onTabItemSelected(int position) {

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
                if (path instanceof BannerDataBean) {
                    pathStr = ((BannerDataBean) path).getImg_url();
                }
                try {
                    GlideApp.with(context).load(pathStr).placeholder(R.drawable.test111).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        images = new ArrayList<>();
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
