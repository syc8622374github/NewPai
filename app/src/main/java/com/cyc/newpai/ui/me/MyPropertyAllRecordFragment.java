package com.cyc.newpai.ui.me;

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
import com.cyc.newpai.ui.me.adapter.AllRecordRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.AllRecordBean;

import java.util.ArrayList;
import java.util.List;

public class MyPropertyAllRecordFragment extends BaseFragment {

    private View view;
    private RecyclerView list;
    private AllRecordRecyclerViewAdapter allRecordRecyclerViewAdapter;
    public static final String TYPE_ALL_RECORD = "type_all_record";
    public static final String TYPE_PAY_RECORD = "type_pay_record";
    public static final String TYPE_INCOME_RECORD = "type_income_record";
    private String type;
    private List<AllRecordBean> allRecordBeans;
    private List<AllRecordBean> payRecordBeans;
    private List<AllRecordBean> inComingRecordBeans;

    public static MyPropertyAllRecordFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        MyPropertyAllRecordFragment fragment = new MyPropertyAllRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list,container,false);
        initView();
        return view;
    }

    private void initView() {
        list = view.findViewById(R.id.list);
        allRecordRecyclerViewAdapter = new AllRecordRecyclerViewAdapter(list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(allRecordRecyclerViewAdapter);

        initData();
    }

    private void initData() {
        allRecordBeans = new ArrayList<>();
        payRecordBeans = new ArrayList<>();
        inComingRecordBeans = new ArrayList<>();
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-30","竞拍商品","2018-05-21 11:30"));
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-40","竞拍商品","2018-05-23 11:30"));
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-30","竞拍商品","2018-05-21 11:30"));
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-20","竞拍商品","2018-05-21 11:30"));
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-40","竞拍商品","2018-05-21 11:30"));
        payRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","-70","竞拍商品","2018-05-21 11:30"));

        inComingRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","+30","竞拍返还","2018-05-21 11:30"));
        inComingRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","+400","充值","2018-05-23 11:30"));
        inComingRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","+30","竞拍返还","2018-05-21 11:30"));
        inComingRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","+400","充值","2018-05-21 11:30"));
        inComingRecordBeans.add(new AllRecordBean("apple iphonex (a1268712364876348321)","+70","竞拍返还","2018-05-21 11:30"));

        allRecordBeans.addAll(payRecordBeans);
        allRecordBeans.addAll(inComingRecordBeans);

        switch (type){
            case TYPE_ALL_RECORD:
                allRecordRecyclerViewAdapter.setListNotify(allRecordBeans);
                break;
            case TYPE_PAY_RECORD:
                allRecordRecyclerViewAdapter.setListNotify(payRecordBeans);
                break;
            case TYPE_INCOME_RECORD:
                allRecordRecyclerViewAdapter.setListNotify(inComingRecordBeans);
                break;
        }
    }
}
