package com.cyc.newpai.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.main.entity.BidRecordItemBean;
import com.cyc.newpai.util.GlideCircleTransform;

public class BidRecordRecyclerViewAdapter extends BaseRecyclerAdapter<BidRecordItemBean> {

    public BidRecordRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_home_shop_detail_bid_record_item,parent,false);
        return ViewHolder.create(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.setText(R.id.tv_bid_record_name,mList.get(position).getNickname());
        viewHolder.setText(R.id.tv_bid_record_price,mList.get(position).getMoney());
        viewHolder.setText(R.id.tv_bid_record_area,mList.get(position).getIp_address());
        GlideApp.with(mContext)
                .load(mList.get(position).getImg())
                .placeholder(R.drawable.ic_avator_default)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) viewHolder.getView(R.id.iv_bid_record_avator));
    }
}
