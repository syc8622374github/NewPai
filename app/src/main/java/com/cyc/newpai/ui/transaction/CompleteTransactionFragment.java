package com.cyc.newpai.ui.transaction;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.common.entity.TopLineBean;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.util.ViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CompleteTransactionFragment extends BaseFragment {

    private HistoryCompleteTransactionAdapter adapter;
    private RecyclerView recyclerView;
    private View view;
    private TextSwitcher topLine;
    private List<TopLineBean> topLineBeanList = new ArrayList<>();
    private int recyclerCount = 0;
    private SwipeRefreshLayout refreshLayout;
    private int pageSize = 1;

    public static CompleteTransactionFragment newInstance() {
        Bundle args = new Bundle();
        CompleteTransactionFragment fragment = new CompleteTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complete_transaction,container,false);
        recyclerView = view.findViewById(R.id.list);
        initRefreshLayout(view);
        initList(recyclerView);
        initTopLine(view);
        initVaryView();
        return view;
    }

    private void initRefreshLayout(View view) {
        refreshLayout = view.findViewById(R.id.srl_transaction_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> initData());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("p",String.valueOf(pageSize));
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_NEW_DEAL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(()->varyViewHelper.showErrorView());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<BidAgeRecordBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>(){}.getType());
                        if(data.getCode()==200&&data.getResult().getList()!=null){
                            if(data.getResult().getList().size()>0){
                                updateNewDealData(data.getResult().getList());
                                pageSize++;
                            }
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载异常", Toast.LENGTH_LONG));
                }
                handler.post(() -> ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG));
            }
        });
        OkHttpManager.getInstance(getMyActivity()).postAsyncHttp(HttpUrl.HTTP_HEADLINE_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(()->refreshLayout.setRefreshing(false));
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
                    e.printStackTrace();
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载异常", Toast.LENGTH_LONG));
                } finally {
                    handler.post(()->refreshLayout.setRefreshing(false));
                }
            }
        });
    }

    private void updateNewDealData(List<BidAgeRecordBean> list) {
        handler.post(()->adapter.setNewData(list));
    }

    private void updateTopLine(List<TopLineBean> topLineBeans) {
        if(topLineBeans!=null){
            topLineBeanList.clear();
            topLineBeanList.addAll(topLineBeans);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return recyclerView;
    }

    private void initList(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryCompleteTransactionAdapter(getMyActivity(),null,true,HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        recyclerView.addItemDecoration(new CommItemDecoration(getMyActivity(),DividerItemDecoration.VERTICAL,getResources().getColor(R.color.divider), 1));
        recyclerView.setAdapter(adapter);
        adapter.setLoadingView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.Loading));
        adapter.setLoadEndView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.TheEnd));
        adapter.setLoadFailedView(ViewUtil.getFootView(getMyActivity(), LoadingFooter.State.NetWorkError));
        adapter.setOnItemClickListener((viewHolder, data, position) -> {
            Intent intent = new Intent(getContext(), HomeShopDetailActivity.class);
            intent.putExtra("id",data.getId());
            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(isReload -> loadMore());
        adapter.startLoadMore(recyclerView,linearLayoutManager);
    }

    private void loadMore() {
        Map<String,String> params = new HashMap<>();
        params.put("p",String.valueOf(pageSize));
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_NEW_DEAL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                varyViewHelper.showErrorView();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<BidAgeRecordBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<BidAgeRecordBean>>>(){}.getType());
                        if(data.getCode()==200&&data.getResult().getList()!=null){
                            if(data.getResult().getList().size()>0){
                                handler.post(()->adapter.setLoadMoreData(data.getResult().getList()));
                                pageSize++;
                            }else{
                                adapter.loadEnd();
                            }
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> ToastManager.showToast(getContext(), "数据加载异常", Toast.LENGTH_LONG));
                }
                handler.post(() -> ToastManager.showToast(getContext(), "数据加载失败", Toast.LENGTH_LONG));
            }
        });
    }
}
