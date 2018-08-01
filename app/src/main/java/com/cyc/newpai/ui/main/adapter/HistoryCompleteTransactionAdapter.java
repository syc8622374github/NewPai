package com.cyc.newpai.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.ui.main.entity.BidLuckyBean;
import com.cyc.newpai.util.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

public class HistoryCompleteTransactionAdapter extends CommonBaseAdapter<BidAgeRecordBean> {

    public static final int COMPLETE_TRANSACTION_TYPE = 0x10001;//以往成交记录
    public static final int LUCKY_TIME_TYPE = 0x10002;//竞拍晒单
    public static final int RULE_TYPE = 0x10003;//竞拍规则
    private int resId;
    private int type;

    public HistoryCompleteTransactionAdapter(Context context, List<BidAgeRecordBean> datas, boolean isOpenLoadMore,int type) {
        super(context, datas, isOpenLoadMore);
        updateType(type);
    }

    public void updateType(int type){
        this.type = type;
        switch (type) {
            case COMPLETE_TRANSACTION_TYPE:
                resId = R.layout.fragment_history_transaction_item;
                break;
            case LUCKY_TIME_TYPE:
                resId = R.layout.fragment_history_lucky_item;
                break;
            case RULE_TYPE:
                resId = R.layout.fragment_history_transaction_item;
                break;
            default:
                resId = R.layout.fragment_history_transaction_item;
                break;
        }
    }

    public void setListNotifyCustom(List mList) {
        getAllData().clear();
        getAllData().addAll(mList);
        if(mList.size()==0){
            notifyDataSetChanged();
        }else{
            notifyItemRangeChanged(getHeaderCount(),getAllData().size());
        }
    }

    /*@Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return type;
    }*/

    /*@Override
    public int getItemCount() {
        switch (type) {
            case LUCKY_TIME_TYPE:
                return luckyBeans.size();
            case RULE_TYPE:
                return 1;
        }
        return getAllData().size();
    }*/

    public int getViewType(){
        return type;
    }

    @Override
    protected void convert(ViewHolder holder, BidAgeRecordBean data, int position) {
        try {
            if(type==COMPLETE_TRANSACTION_TYPE){
                holder.getView(R.id.iv_complete_label).setVisibility(View.GONE);
                holder.setText(R.id.tv_his_bid_deal_person,getAllData().get(position).getNickname());
                holder.setText(R.id.tv_hist_deal_price,"￥"+getAllData().get(position).getDeal_price());
                holder.setText(R.id.tv_his_bid_market_price,getAllData().get(position).getMarket_price());
                holder.setText(R.id.tv_his_bid_deal_time,getAllData().get(position).getDeal_time());
                holder.setText(R.id.tv_his_bid_rate,getAllData().get(position).getSave_rate());
                ImageView avator = holder.getView(R.id.iv_avator);
                GlideApp.with(mContext)
                        .load(getAllData().get(position).getImage())
                        .placeholder(R.drawable.ic_avator_default)
                        .transform(new GlideCircleTransform(mContext))
                        .into(avator);
                holder.getView(R.id.iv_complete_label).setVisibility(View.VISIBLE);
            }else if(type==LUCKY_TIME_TYPE){
                ImageView avator = holder.getView(R.id.iv_avator);
                GlideApp.with(mContext)
                        .load(getAllData().get(position).getHead_img())
                        .placeholder(R.drawable.ic_avator_default)
                        .transform(new GlideCircleTransform(mContext))
                        .into(avator);
                holder.setText(R.id.tv_history_lucky_name,getAllData().get(position).getNickname());
                holder.setText(R.id.tv_history_lucky_message,getAllData().get(position).getContent());
                ImageView img = holder.getView(R.id.iv_shop_show_bg);
                GlideApp.with(mContext).load(getAllData().get(position).getImages().get(0)).placeholder(R.mipmap.ic_launcher).into(img);
            }else if(type==RULE_TYPE){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getItemLayoutId() {
        return resId;
    }
}
