package com.cyc.newpai.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.util.DateUtil;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.ScreenUtil;
import com.cyc.newpai.util.ViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainLuckyTimeActivity extends BaseActivity {

    private static final String TAG = MainLuckyTimeActivity.class.getSimpleName();
    private RecyclerView rvList;
    private HistoryCompleteTransactionAdapter historyCompleteTransactionAdapter;
    private int pageSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_lucky_time;
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("p",String.valueOf(pageSize++));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_MAIN_LUCKY_SHOW_URL, params, new Callback() {
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
                        if (responseBean.getCode() == 200 && responseBean.getResult().getList() != null) {
                            //updateBidLuckyData(responseBean.getResult().getList());
                            if (responseBean.getResult().getList().size() > 0) {
                                handler.post(()->{
                                    historyCompleteTransactionAdapter.updateType(HistoryCompleteTransactionAdapter.LUCKY_TIME_TYPE);
                                    historyCompleteTransactionAdapter.setListNotifyCustom(responseBean.getResult().getList());
                                });
                            }
                            return;
                        }
                    }
                    handler.post(() -> ToastManager.showToast(MainLuckyTimeActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void initView() {
        rvList = findViewById(R.id.rv_main_lucky_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new CommItemDecoration(this,LinearLayout.VERTICAL,getResources().getColor(R.color.divider),1));
        historyCompleteTransactionAdapter = new HistoryCompleteTransactionAdapter(this, null, true, HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        //初始化 开始加载更多的loading View
        historyCompleteTransactionAdapter.setLoadingView(ViewUtil.getFootView(this, LoadingFooter.State.Loading));
        //加载失败，更新footer view提示
        historyCompleteTransactionAdapter.setLoadFailedView(ViewUtil.getFootView(this, LoadingFooter.State.NetWorkError));
        //加载完成，更新footer view提示
        historyCompleteTransactionAdapter.setLoadEndView(ViewUtil.getFootView(this, LoadingFooter.State.TheEnd));
        historyCompleteTransactionAdapter.setOnLoadMoreListener(isReload -> loadMoreData());
        rvList.setAdapter(historyCompleteTransactionAdapter);
    }

    private void loadMoreData() {
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(pageSize++));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_MAIN_LUCKY_SHOW_URL, params, new Callback() {
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
                        if (responseBean.getCode() == 200 && responseBean.getResult().getList() != null) {
                            if(responseBean.getResult().getList().size()>0){
                                handler.post(()->historyCompleteTransactionAdapter.setLoadMoreData(responseBean.getResult().getList()));
                            }else{
                                handler.post(()->historyCompleteTransactionAdapter.loadEnd());
                            }
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(MainLuckyTimeActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}
