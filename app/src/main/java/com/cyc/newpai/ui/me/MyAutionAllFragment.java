package com.cyc.newpai.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.adapter.base.WrapContentLinearLayoutManager;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.me.adapter.MyAutionAllRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.ui.me.entity.OrderDetailResultBean;
import com.cyc.newpai.util.DateUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyAutionAllFragment extends BaseFragment {

    public static final String TAG = MyAutionAllFragment.class.getSimpleName();
    private View view;
    private MyAuctionActivity activity;
    private MyAutionAllRecyclerViewAdapter myAutionAllRecyclerViewAdapter;
    private RecyclerView autionList;
    private Timer timer;
    private int pageSize = 1;
    private String auctionType;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public static MyAutionAllFragment newInstance(String auctionType) {
        Bundle args = new Bundle();
        args.putString("type", auctionType);
        MyAutionAllFragment fragment = new MyAutionAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MyAuctionActivity) getMyActivity();
        auctionType = getArguments().getString("type");
        Log.i(TAG, "onCreate" + auctionType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    protected View getLoadingTargetView() {
        return autionList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initView();
        initVaryView();
        Log.i(TAG, "onCreateView" + auctionType);
        refreshLayout();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated" + auctionType);
    }

    private void initView() {
        autionList = view.findViewById(R.id.list);
        autionList.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new CommItemDecoration(getContext(),LinearLayoutManager.VERTICAL,getResources().getColor(R.color.color_list_bg), ScreenUtil.dp2px(getContext(),10)));
        myAutionAllRecyclerViewAdapter = new MyAutionAllRecyclerViewAdapter(autionList, auctionType);
        myAutionAllRecyclerViewAdapter.setOnClickItemListener((BaseRecyclerAdapter.OnAdapterListener<MyAuctionBean>) (view, itemBean, position) -> {
            if (getAuctionType().equals("1") || getAuctionType().equals("2")) {
                Intent intent = new Intent(getContext(), HomeShopDetailActivity.class);
                intent.putExtra("id", itemBean.getId());
                startActivity(intent);
            } else if (getAuctionType().equals("3")) {
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_DATA_BEAN, itemBean);
                startActivity(intent);
            } else if(getAuctionType().equals("4")){
                Map<String,String> params = new HashMap<>();
                params.put("shopid",itemBean.getId());
                OkHttpManager.getInstance(MyAutionAllFragment.this.getMyActivity()).postAsyncHttp(HttpUrl.HTTP_PAY_SUCCESS_SHOP_DETAIL_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            if(response.isSuccessful()){
                                String str = response.body().string();
                                ResponseBean<ResponseResultBean<OrderDetailResultBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<OrderDetailResultBean>>>(){}.getType());
                                if(data.getCode()==200){
                                    handler.post(()->{
                                        OrderDetailResultBean orderDetailResultBean = data.getResult().getItem();
                                        Intent intent = new Intent(MyAutionAllFragment.this.getMyActivity(),SubmitOrderActivity.class);
                                        intent.putExtra(OrderDetailActivity.ORDER_DETAIL_BEAN,orderDetailResultBean);
                                        intent.putExtra(OrderDetailActivity.ORDER_DATA_BEAN,itemBean);
                                        intent.putExtra(SubmitOrderActivity.TYPE_SUBMIT_ORDER_STATUS,SubmitOrderActivity.TYPE_REPAY_STATUS);
                                        startActivity(intent);
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myAutionAllRecyclerViewAdapter);
        autionList.setAdapter(headerAndFooterRecyclerViewAdapter);
        swipeRefreshLayout = view.findViewById(R.id.srl_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener = () -> initData());
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

    @Override
    protected void onRetryLoad() {
        super.onRetryLoad();
        refreshLayout();
    }

    protected void initData() {
        //varyViewHelper.showLoadingView();
        Map<String, String> param = new HashMap<>();
        param.put("d_type", auctionType);
        //param.put("p",String.valueOf(pageSize));
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_AUCTION_URL, param, new Callback() {
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
                    handler.post(() -> {
                        varyViewHelper.showErrorView();
                    });
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

    @Override
    public void onStart() {
        Log.i(TAG, "onStart" + auctionType);
        super.onStart();
        if (timer == null && (auctionType == activity.auctionTypes[0] || auctionType == activity.auctionTypes[2])) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Map<String, String> param = new HashMap<>();
                    param.put("d_type", auctionType);
                    param.put("p", String.valueOf(pageSize));
                    OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_AUCTION_URL, param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //handler.post(()->varyViewHelper.showErrorView());
                            Log.e(TAG, e.getMessage()+"");
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
                                    if (data.getCode() == 200 && data.getResult().getList() != null) {
                                        if (data.getResult().getList().size() > 0) {
                                            updateList(data.getResult().getList());
                                        }
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        Log.i(TAG, "onStop" + auctionType);
    }

    private void updateList(List<MyAuctionBean> list) {
        handler.post(() -> myAutionAllRecyclerViewAdapter.setListNotify(list));
    }

    private String getAuctionType() {
        return auctionType;
    }
}
