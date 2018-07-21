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
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.me.adapter.MyAutionAllRecyclerViewAdapter;
import com.cyc.newpai.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class MyAutionAllFragment extends BaseFragment {

    public static MyAutionAllFragment newInstance() {
        Bundle args = new Bundle();
        MyAutionAllFragment fragment = new MyAutionAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new CommItemDecoration(getContext(),LinearLayoutManager.VERTICAL,getResources().getColor(R.color.color_list_bg), ScreenUtil.dp2px(getContext(),10)));
        MyAutionAllRecyclerViewAdapter myAutionAllRecyclerViewAdapter = new MyAutionAllRecyclerViewAdapter(recyclerView);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myAutionAllRecyclerViewAdapter);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
        List<String> data = new ArrayList<>();
        data.add("1111");
        data.add("1111");
        data.add("1111");
        data.add("1111");
        data.add("1111");
        data.add("1111");
        myAutionAllRecyclerViewAdapter.addListNotify(data);

    }
}
