package com.cyc.newpai.ui.me;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.adapter.base.WrapContentLinearLayoutManager;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.me.adapter.MyAutionAllRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyOrderListActivity extends BaseActivity {

    private static final String TAG = MyOrderListActivity.class.getSimpleName();
    private RecyclerView list;
    private MyAutionAllRecyclerViewAdapter myAutionAllRecyclerViewAdapter;
    public static final String TYPE_MY_ORDER_LIST = "my_order_list";
    public static final String TYPE_SUBMIT_ORDER_LIST = "submit_order_list";
    public static final String TYPE = "type";
    private String auctionType;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auctionType = getIntent().getStringExtra(TYPE);
        auctionType = TextUtils.isEmpty(auctionType)?"3":auctionType;
        super.onCreate(savedInstanceState);
        initView();
        initVaryView();
        refreshLayout();
    }

    @Override
    protected View getLoadingTargetView() {
        return list;
    }

    public void refreshLayout() {
        if (swipeRefreshLayout == null) {
            initView();
        }
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            onRefreshListener.onRefresh();
        });
    }

    private void initView() {
        list = findViewById(R.id.rv_my_order_list);
        list.setLayoutManager(new WrapContentLinearLayoutManager(this));
        //recyclerView.addItemDecoration(new CommItemDecoration(getContext(),LinearLayoutManager.VERTICAL,getResources().getColor(R.color.color_list_bg), ScreenUtil.dp2px(getContext(),10)));
        myAutionAllRecyclerViewAdapter = new MyAutionAllRecyclerViewAdapter(list, auctionType);
        myAutionAllRecyclerViewAdapter.setOnClickItemListener((BaseRecyclerAdapter.OnAdapterListener<MyAuctionBean>) (view, itemBean, position) -> {
            if (auctionType.equals("3")) {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_DATA_BEAN, itemBean);
                startActivity(intent);
            } else if (auctionType.equals("4")) {
            }
        });
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myAutionAllRecyclerViewAdapter);
        list.setAdapter(headerAndFooterRecyclerViewAdapter);
        swipeRefreshLayout = findViewById(R.id.srl_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener = () -> initData());

    }

    protected void initData() {
        //varyViewHelper.showLoadingView();
        Map<String, String> param = new HashMap<>();
        param.put("d_type", auctionType);
        //param.put("p",String.valueOf(pageSize));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_AUCTION_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> {
                            varyViewHelper.showErrorView();
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        //Log.i(TAG,str);
                        ResponseBean<ResponseResultBean<MyAuctionBean>> data =
                                getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<MyAuctionBean>>>() {
                                }.getType());
                        Log.i(TAG, "sss" + str);
                        if (data.getCode() == 200 && data.getResult().getList() != null) {
                            updateList(data.getResult().getList());
                            if (data.getResult().getList().size() == 0) {
                                handler.post(() -> varyViewHelper.showEmptyView());
                            } else {
                                handler.post(() -> varyViewHelper.showDataView());
                            }
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> varyViewHelper.showErrorView());
                } finally {
                    handler.post(() -> {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
                handler.post(() -> varyViewHelper.showErrorView());
            }
        });
    }

    private void updateList(List<MyAuctionBean> list) {
        handler.post(() -> myAutionAllRecyclerViewAdapter.setListNotify(list));
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        if(auctionType.equals("3")){
            ctb_toolbar.tv_title.setText("我拍中");
        }else if(auctionType.equals("4")){
            ctb_toolbar.tv_title.setText("待付款");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order_list;
    }
}
