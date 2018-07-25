package com.cyc.newpai.ui.transaction;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.common.entity.TopLineBean;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;
import com.cyc.newpai.util.RecyclerViewUtil;
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

    public static CompleteTransactionFragment newInstance() {
        Bundle args = new Bundle();
        CompleteTransactionFragment fragment = new CompleteTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
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
        initList(recyclerView);
        initTopLine(view);
        initVaryView();
        varyViewHelper.showEmptyView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("gid","1");
        params.put("p","1");
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_BID_RECORD_AGO_URL, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                varyViewHelper.showErrorView();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
        adapter = new HistoryCompleteTransactionAdapter(recyclerView,HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        //adapter.setListNotify(data);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
        RecyclerViewUtil.addFootView(recyclerView,new LoadingFooter(getContext()));
        adapter.setOnClickItemListener((view, itemBean, position) -> {
            startActivity(new Intent(getContext(), HomeShopDetailActivity.class));
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动停止
                    boolean isBottom = linearLayoutManager.findLastCompletelyVisibleItemPosition()>= adapter.getItemCount();
                    if (isBottom) {
                        loadMore();
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
    }



    private void loadMore() {
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //data.add(new HisTransactionBean(R.drawable.ic_avator,true));
                //.add(new HisTransactionBean(R.drawable.ic_avator,true));
                //adapter.addListNotify(data);
            }
        },2000);
    }
}
