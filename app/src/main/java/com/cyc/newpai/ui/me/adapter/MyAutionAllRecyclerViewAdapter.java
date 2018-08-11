package com.cyc.newpai.ui.me.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.util.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MyAutionAllRecyclerViewAdapter extends BaseRecyclerAdapter<MyAuctionBean> {
    private String auctionType;

    public MyAutionAllRecyclerViewAdapter(RecyclerView mRecyclerView, String auctionType) {
        super(mRecyclerView);
        this.auctionType = auctionType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext,R.layout.fragment_my_aution_all_list_item,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_auction_time, DateUtil.formatDate(Long.valueOf(getList().get(position).getLast_bid_time())*1000,"yyyy:MM:dd HH:mm:ss"));
        viewHolder.setText(R.id.tv_auction_name,getList().get(position).getGoods_name());
        viewHolder.setText(R.id.tv_auction_price,"￥"+getList().get(position).getNow_price());
        ImageView icon = viewHolder.getView(R.id.iv_auction_icon);
        TextView tvAuctionType = viewHolder.getView(R.id.tv_auction_type);
        GlideApp.with(mContext).load(getList().get(position).getImage()).placeholder(R.drawable.shop_iphonex).into(icon);
        int time = 10 - (Integer.valueOf(getList().get(position).getServer_time())
                - Integer.valueOf(getList().get(position).getLast_bid_time()));
        String timeStr = "";
        if(time<10&&time>0){
            timeStr = "00:00:0" + time;
        }else if(time>=10){
            timeStr = "00:00:" + time;
        }
        viewHolder.setText(R.id.tv_auction_count_down,timeStr);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(v,getList().get(position),position);
            }
        });
        Button auctionOk = viewHolder.getView(R.id.btn_auction_ok);
        switch (auctionType){
            case "1":
                tvAuctionType.setText("正在拍");
                auctionOk.setText("去付款");
                auctionOk.setText("去竞拍");
                auctionOk.setVisibility(View.VISIBLE);
                viewHolder.setText(R.id.tv_auction_detail,"正在竞拍商品");
                break;
            case "2":
                tvAuctionType.setText("未拍中");
                auctionOk.setVisibility(View.GONE);
                viewHolder.setText(R.id.tv_auction_detail,"未成功拍的商品");
                break;
            case "3":
                tvAuctionType.setText("我拍中");
                auctionOk.setText("去付款");
                auctionOk.setVisibility(View.VISIBLE);
                viewHolder.setText(R.id.tv_auction_detail,"已成功拍的商品");
                break;
            case "4":
                tvAuctionType.setText("待付款");
                viewHolder.setText(R.id.tv_auction_detail,"待付款的商品");
                break;
            case "5":
                tvAuctionType.setText("待晒单");
                viewHolder.setText(R.id.tv_auction_detail,"待晒单的商品");
                break;
        }
    }
}
