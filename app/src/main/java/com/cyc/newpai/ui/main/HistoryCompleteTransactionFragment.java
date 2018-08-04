package com.cyc.newpai.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;

import java.util.ArrayList;
import java.util.List;

public class HistoryCompleteTransactionFragment extends BaseFragment {

    public static int type;

    public static HistoryCompleteTransactionFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type",type);
        HistoryCompleteTransactionFragment fragment = new HistoryCompleteTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_list,container,false);
        type = getArguments().getInt("type");
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.list);
        //TextView rule = view.findViewById(R.id.tv_rule);
        if(type == HistoryCompleteTransactionAdapter.RULE_TYPE){
            recyclerView.setVisibility(View.GONE);
            //rule.setVisibility(View.VISIBLE);
        }else{
            initList(recyclerView);
        }
    }

    private void refreshData() {

    }

    private void initList(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryCompleteTransactionAdapter adapter = new HistoryCompleteTransactionAdapter(getActivity(),null,true,type);
        /*List<HisTransactionBean> data = new ArrayList<>();
        data.add(new HisTransactionBean(R.drawable.ic_avator));
        data.add(new HisTransactionBean(R.drawable.ic_avator));
        data.add(new HisTransactionBean(R.drawable.ic_avator));
        data.add(new HisTransactionBean(R.drawable.ic_avator));
        data.add(new HisTransactionBean(R.drawable.ic_avator));
        adapter.setListNotify(data);*/
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
