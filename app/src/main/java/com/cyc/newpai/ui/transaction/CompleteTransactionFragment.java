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
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;

import java.util.ArrayList;
import java.util.List;

public class CompleteTransactionFragment extends BaseFragment {

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryCompleteTransactionAdapter adapter = new HistoryCompleteTransactionAdapter(recyclerView,HistoryCompleteTransactionAdapter.COMPLETE_TRANSACTION_TYPE);
        List<HisTransactionBean> data = new ArrayList<>();
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        data.add(new HisTransactionBean(R.drawable.ic_avator,true));
        adapter.setListNotify(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickItemListener((view, itemBean, position) -> {
            startActivity(new Intent(getContext(), HomeShopDetailActivity.class));
        });

    }
}
