package com.cyc.newpai.ui.main.adapter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.main.entity.HomeBean;

import java.util.Timer;
import java.util.TimerTask;

public class HomeRecyclerViewAdapter extends BaseRecyclerAdapter<HomeBean> {

    private int count = 99;

    public HomeRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext,R.layout.home_main_item,parent);
    }

    int mSecond = 99;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_home_category_price,"￥"+mList.get(position).getNow_price()+"");
        int time = 10 - mList.get(position).getLeft_second();
        String timeStr = "";
        if(time<10&&time>0){
            timeStr = "00:00:0" + time;
        }else if(time>=10){
            timeStr = "00:00:" + time;
        }else{
            /*time = 10;
            mList.get(position).setLeft_second(time);
            timeStr = "00:00:" + time;*/
        }
        viewHolder.setText(R.id.tv_home_category_count_down,timeStr);
        ImageView shopIcon = viewHolder.getView(R.id.iv_shop_icon);
        GlideApp.with(mContext).load(getList().get(position).getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
        viewHolder.setText(R.id.tv_shop_title,getList().get(position).getGoods_name());
        onBindListener(viewHolder,position);
    }

    private void onBindListener(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }
}
