package com.cyc.newpai.ui.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.framework.decoration.SpaceItemDecoration;
import com.cyc.newpai.framework.flowlayout.FlowLayoutManager;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.common.adapter.SearchRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.SearchMenuBean;
import com.cyc.newpai.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        RecyclerView rvList = findViewById(R.id.rv_search_list);
        rvList.setLayoutManager(new FlowLayoutManager());
        rvList.addItemDecoration(new SpaceItemDecoration(ScreenUtil.dp2px(this,3)));
        SearchRecyclerViewAdapter searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(rvList);
        rvList.setAdapter(searchRecyclerViewAdapter);
        List<SearchMenuBean> datas = new ArrayList<>();
        datas.add(new SearchMenuBean("iphone X"));
        datas.add(new SearchMenuBean("美图 Meitu T8s"));
        datas.add(new SearchMenuBean("尼康 xxx"));
        datas.add(new SearchMenuBean("iphone X"));
        datas.add(new SearchMenuBean("iphone X"));
        datas.add(new SearchMenuBean("iphone X"));
        datas.add(new SearchMenuBean("iphone X"));
        searchRecyclerViewAdapter.addListNotify(datas);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }
}
