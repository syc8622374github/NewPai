package com.cyc.newpai.ui.transaction;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.widget.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

public class CompleteTransactionFragment extends BaseFragment {

    private List<HisTransactionBean> data;
    private HistoryCompleteTransactionAdapter adapter;

    public static CompleteTransactionFragment newInstance() {
        Bundle args = new Bundle();
        CompleteTransactionFragment fragment = new CompleteTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_transaction,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        initList(recyclerView);
        return view;
    }

    private void initList(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryCompleteTransactionAdapter(recyclerView,HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        data = new ArrayList<>();
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
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
                data.add(new HisTransactionBean(R.drawable.ic_avator,true));
                data.add(new HisTransactionBean(R.drawable.ic_avator,true));
                //adapter.addListNotify(data);
            }
        },2000);
    }
}
