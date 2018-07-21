package com.cyc.newpai.ui.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.common.entity.SearchBean;

public class SearchRecyclerViewAdapter extends BaseRecyclerAdapter<SearchBean> {
    public SearchRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, R.layout.activity_search_item,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_search_title,getList().get(position).getGoods_name());
        viewHolder.setText(R.id.tv_search_price,getList().get(position).getNow_price());

        int seccond = getList().get(position).getLeft_second();
        String countDown = "00:00:";
        if(seccond>=10){
            countDown = "00:00:"+seccond;
        }else if(seccond<10&seccond>=0){
            countDown = "00:00:0"+seccond;
        }
        viewHolder.setText(R.id.tv_search_count_down,countDown);
        viewHolder.setText(R.id.tv_search_title,getList().get(position).getNickname());
    }
}
