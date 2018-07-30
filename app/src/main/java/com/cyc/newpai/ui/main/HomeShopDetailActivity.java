package com.cyc.newpai.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.interfaces.OnItemClickListener;
import com.cyc.newpai.framework.adapter.interfaces.OnLoadMoreListener;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.main.adapter.BidRecordRecyclerViewAdapter;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.ui.main.entity.BidLuckyBean;
import com.cyc.newpai.ui.main.entity.BidRecordBean;
import com.cyc.newpai.ui.main.entity.BidRecordItemBean;
import com.cyc.newpai.ui.main.entity.BidResultBean;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;
import com.cyc.newpai.ui.main.entity.ShopDetailBean;
import com.cyc.newpai.util.GlideCircleTransform;
import com.cyc.newpai.util.GsonManager;
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

public class HomeShopDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = HomeShopDetailActivity.class.getSimpleName();
    private String[] shopCategory = new String[]{"往期成交","幸运晒单","竞拍规则"};
    private BidRecordRecyclerViewAdapter bidRecordRecyclerViewAdapter;
    private RecyclerView bidRecord;
    private TabLayout tabLayout;
    private Banner banner;
    private View head;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    break;
            }
        }
    };
    private HistoryCompleteTransactionAdapter historyCompleteTransactionAdapter;
    boolean isLoadMore = false;
    private TextView bidNume;
    private int type;
    private TextView nowPrice;
    private TextView marketPrice;
    private TextView shopName;
    private TextView countDown;
    private ImageView avator;
    private TextView prompt;
    private TextView name;
    private int countDownNum = 10;
    private String gid;
    private Timer timer;
    private ShopDetailBean shopDetailBean;
    private TextView useBi;
    private BidResultBean bidResultBean;
    private List<BidAgeRecordBean> ageLists = new ArrayList<>();
    private List<BidAgeRecordBean> luckLists = new ArrayList<>();

    private void updateMainBidInfo(BidRecordItemBean item) {
        handler.post(()->{
            nowPrice.setText("￥"+item.getMoney());
            marketPrice.setText("￥"+shopDetailBean.getMarket_price());
            name.setText(item.getNickname());
            shopName.setText(shopDetailBean.getGoods_name());
            countDown.setText("00:00:0"+shopDetailBean.getLimit_second());
            try {
                if(!isDestroyed()){
                    GlideApp.with(this)
                            .load(item.getImg())
                            .placeholder(R.drawable.ic_avator_default)
                            .transform(new GlideCircleTransform(this))
                            .into(avator);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            prompt.setText("若无人出价，将以￥"+item.getMoney()+"拍的本商品");
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gid = getIntent().getStringExtra("gid");
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }

    private void initView() {
        initHeader();
        initList();
        initBidMenu();
    }

    private void initBidMenu() {
        findViewById(R.id.tv_bid_less).setOnClickListener(this);
        bidNume = findViewById(R.id.tv_bid_num);
        findViewById(R.id.tv_bid_add).setOnClickListener(this);
        findViewById(R.id.btn_bid).setOnClickListener(this);
    }

    private void initList() {
        RecyclerView rvList = findViewById(R.id.rv_shop_detail_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        historyCompleteTransactionAdapter = new HistoryCompleteTransactionAdapter(this,null,true,HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        historyCompleteTransactionAdapter.addHeaderView(head);
        //初始化 开始加载更多的loading View
        historyCompleteTransactionAdapter.setLoadingView(ViewUtil.getFootView(this,LoadingFooter.State.Loading));
        //newHomeRecyclerViewAdapter.setReloadView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_emptyview, (ViewGroup) rvMain.getParent(), false));
        //newHomeRecyclerViewAdapter.setEmptyView(R.layout.layout_emptyview);
        //加载失败，更新footer view提示
        historyCompleteTransactionAdapter.setLoadFailedView(ViewUtil.getFootView(this,LoadingFooter.State.NetWorkError));
        //加载完成，更新footer view提示
        historyCompleteTransactionAdapter.setLoadEndView(ViewUtil.getFootView(this,LoadingFooter.State.TheEnd));
        historyCompleteTransactionAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                updateBidAgeData();
            }
        });
        rvList.setAdapter(historyCompleteTransactionAdapter);
        /*rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                try{
                    super.onScrollStateChanged(recyclerView, newState);
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                        //滑动停止
                        boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition() >= historyCompleteTransactionAdapter.getItemCount();
                        if (isBottom && historyCompleteTransactionAdapter.getItemCount() > 0) {
                            if (!isLoadMore) {
                                isLoadMore = true;
                                updateBidAgeData();
                            }
                        }
                    } else if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                        //用户正在滑动
//                    Logger.d("用户正在滑动 position=" + mAdapter.getAdapterPosition());
                    } else {
                        //惯性滑动
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/
        initTab(tabLayout);
    }

    private void updateBidAgeData() {
        int type = historyCompleteTransactionAdapter.getViewType();
        if(type == historyCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE){
            Map<String,String> params = new HashMap<>();
            params.put("gid",gid);
            params.put("p","1");
            OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_RECORD_AGO_URL, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    isLoadMore = false;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            ResponseBean<ResponseResultBean<BidAgeRecordBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>() {
                            }.getType());
                            if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                updateBidAgeRecordView(responseBean.getResult().getList());
                                return;
                            }
                        }
                        ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    isLoadMore = false;
                }
            });
        }else{
            Map<String,String> params = new HashMap<>();
            params.clear();
            params.put("gid",gid);
            params.put("p","1");
            OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_LUCKY_SHOW_URL, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    isLoadMore = false;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            ResponseBean<ResponseResultBean<BidAgeRecordBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>() {
                            }.getType());
                            if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                updateBidLuckyData(responseBean.getResult().getList());
                                return;
                            }
                        }
                        handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        isLoadMore = false;
                    }
                }
            });
        }
    }

    private void initHeader() {
        head = LayoutInflater.from(this).inflate(R.layout.activity_shop_detail_head_item,null);
        banner = head.findViewById(R.id.ber_shop_detail_banner);
        initBanner(banner);
        tabLayout = head.findViewById(R.id.tl_shop_detail_tab);
        bidRecord = head.findViewById(R.id.rv_shop_detail_record);
        initBidRecord();
        name = head.findViewById(R.id.tv_shop_detail_nickname);
        nowPrice = head.findViewById(R.id.tv_shop_detail_now_price);
        marketPrice = head.findViewById(R.id.tv_shop_detail_market_price);
        shopName = head.findViewById(R.id.tv_shop_detail_name);
        countDown = head.findViewById(R.id.tv_shop_detail_count_down);
        countDown.setText("00:00:"+countDownNum);
        avator = head.findViewById(R.id.iv_shop_detail_avator);
        prompt = head.findViewById(R.id.tv_shop_detail_win_prompt);
        useBi = head.findViewById(R.id.tv_shop_detail_use_bi);
        startTime();
    }

    private void startTime() {
        if(timer==null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        updateBidView(null);
                        //getShopDetailHttp(new HashMap<>());
                    });
                }
            },1000,1000);
        }
    }

    private void initTab(TabLayout mTabLayout) {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        for(String title : shopCategory){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
    }

    private void onTabItemSelected(int position) {
        switch (position){
            case 0:
                historyCompleteTransactionAdapter.updateType(HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
                historyCompleteTransactionAdapter.setListNotifyCustom(ageLists);
                break;
            case 1:
                historyCompleteTransactionAdapter.updateType(HistoryCompleteTransactionAdapter.LUCKY_TIME_TYPE);
                historyCompleteTransactionAdapter.setListNotifyCustom(luckLists);
                break;
            case 2:
                break;
        }
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        getShopDetailHttp(params);
        params.put("shopid",gid);
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_RECORD_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<BidRecordBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<BidRecordBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            updateBidRecordView(responseBean.getResult());
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        params.put("gid",gid);
        params.put("p","1");
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_RECORD_AGO_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<BidAgeRecordBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            //updateBidAgeRecordView(responseBean.getResult().getList());
                            ageLists.clear();
                            ageLists.addAll(responseBean.getResult().getList());
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        params.clear();
        params.put("gid",gid);
        params.put("p","1");
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_LUCKY_SHOW_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<BidAgeRecordBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            //updateBidLuckyData(responseBean.getResult().getList());
                            luckLists.clear();
                            luckLists.addAll(responseBean.getResult().getList());
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void getShopDetailHttp(Map<String, String> params) {
        params.put("id",gid);
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_SHOP_DETAIL_URL, params, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<ShopDetailBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<ShopDetailBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            shopDetailBean = responseBean.getResult().getItem();
                            handler.post(()->banner.update(shopDetailBean.getImages()));
                            return;
                        }
                    }
                    handler.post(()-> ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void updateBidLuckyData(List<BidAgeRecordBean> list) {
        /*luckLists.clear();
        luckLists.addAll(list);*/
        handler.post(() -> {
            historyCompleteTransactionAdapter.setNewData(list);
        });
    }

    private void updateBidAgeRecordView(List<BidAgeRecordBean> list) {
        handler.post(() -> {
            historyCompleteTransactionAdapter.setNewData(list);
            //historyCompleteTransactionAdapter.updateListNotifyItemRangeChanged(list);
            isLoadMore = false;
        });
    }

    private void initBidRecord() {
        bidRecordRecyclerViewAdapter = new BidRecordRecyclerViewAdapter(bidRecord);
        bidRecord.setLayoutManager(new LinearLayoutManager(this));
        bidRecord.setAdapter(bidRecordRecyclerViewAdapter);
        bidRecord.setFocusableInTouchMode(false);
        bidRecord.requestFocus();
    }

    private void updateBidRecordView(BidRecordBean data) {
        handler.post(() -> {
            List<BidRecordItemBean> list = bidRecordRecyclerViewAdapter.getList();
            if(list.size()==0||!list.get(0).getMoney().equals(data.getItemBeans().get(0).getMoney())){
                bidRecordRecyclerViewAdapter.setListNotify(data.getItemBeans());
            }
        });
    }

    private void initBanner(Banner banner) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //Glide.with(context).load(path).into(imageView);
                //imageView.setBackgroundResource(R.drawable.test111);
                GlideApp.with(context).load(String.valueOf(path)).placeholder(R.drawable.test111).into(imageView);
            }
        });
        banner.start();
        banner.startAutoPlay();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setRightAction1(0, view -> {

        });
        ctb_toolbar.tv_title.setTextColor(Color.BLACK);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_shop_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bid_add:
                bidNume.setText(Integer.valueOf(bidNume.getText().toString())+1+"");
                break;
            case R.id.tv_bid_less:
                int n = Integer.valueOf(bidNume.getText().toString());
                if(n>1)
                bidNume.setText(n-1+"");
                break;
            case R.id.btn_bid:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("交易提示")
                        .setCancelable(false)
                        .setMessage("确认支付"+bidNume.getText()+"个拍币竞拍？")
                        .setNegativeButton("确定", (dialog, which) -> {
                            bid();
                        }).setPositiveButton("取消", (dialog, which) -> dialog.dismiss()).show();
                break;
        }
    }

    private void bid() {
        Map<String,String> params = new HashMap<>();
        params.put("shopid",gid);
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<BidResultBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<BidResultBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 1 && responseBean.getResult() != null) {
                            bidResultBean = responseBean.getResult().getItem();
                            handler.post(()->useBi.setText("我已消耗"+bidResultBean.getMoney()+"拍币/赠币"));
                            updateBidView(responseBean.getResult().getItem());
                            getShopDetailHttp(new HashMap<>());
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void updateBidView(BidResultBean item) {
        Map<String,String> params = new HashMap<>();
        params.put("shopid",gid);
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_RECORD_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<BidRecordBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<BidRecordBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            updateBidRecordView(responseBean.getResult());
                            updateMainBidInfo(responseBean.getResult().getItemBeans().get(0));
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(HomeShopDetailActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}
