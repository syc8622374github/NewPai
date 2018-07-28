package com.cyc.newpai.ui.main.adapter;

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
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.ui.main.entity.BidLuckyBean;

import java.util.ArrayList;
import java.util.List;

public class HistoryCompleteTransactionAdapter extends BaseRecyclerAdapter<BidAgeRecordBean> {

    public static final int COMPLETE_TRANSACTION_TYPE = 0x10001;//以往成交记录
    public static final int LUCKY_TIME_TYPE = 0x10002;//竞拍晒单
    public static final int RULE_TYPE = 0x10003;//竞拍规则
    private int resId;
    private int type;
    private List<BidLuckyBean> luckyBeans = new ArrayList<>();

    public HistoryCompleteTransactionAdapter(RecyclerView mRecyclerView, int type) {
        super(mRecyclerView);
        updateType(type);
    }

    public void setLuckBean(List item) {
        luckyBeans.clear();
        luckyBeans.addAll(item);
    }

    public void addLuckBean(List item) {
        luckyBeans.addAll(item);
    }

    public int getViewType(){
        return  type;
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
        switch (type) {
            case COMPLETE_TRANSACTION_TYPE:
                //setListNotify(mList);
                this.mList.clear();
                luckyBeans.clear();
                this.mList.addAll(mList);
                notifyDataSetChanged();
                break;
            case LUCKY_TIME_TYPE:
                luckyBeans.clear();
                this.mList.clear();
                luckyBeans.addAll(mList);
                notifyDataSetChanged();
                break;
            case RULE_TYPE:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return type;
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case LUCKY_TIME_TYPE:
                return luckyBeans.size();
            case RULE_TYPE:
                return 1;
        }
        return mList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(type==RULE_TYPE){
            return ViewHolder.create(new TextView(mContext));
        }else{
            return ViewHolder.create(mContext,resId,parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        /*if(mList.get(position).isFinish()){
            viewHolderGeneral.completeLabel.setVisibility(View.VISIBLE);
        }else{
            viewHolderGeneral.completeLabel.setVisibility(View.GONE);
        }*/
        if(type==COMPLETE_TRANSACTION_TYPE){
            viewHolder.getView(R.id.iv_complete_label).setVisibility(View.GONE);
            viewHolder.setText(R.id.tv_his_bid_deal_person,getList().get(position).getNickname());
            viewHolder.setText(R.id.tv_hist_deal_price,"￥"+getList().get(position).getDeal_price());
            viewHolder.setText(R.id.tv_his_bid_market_price,getList().get(position).getMarket_price());
            viewHolder.setText(R.id.tv_his_bid_deal_time,getList().get(position).getDeal_time());
            viewHolder.setText(R.id.tv_his_bid_rate,getList().get(position).getSave_rate());
            ImageView avator = viewHolder.getView(R.id.iv_avator);
            GlideApp.with(mContext).load(getList().get(position).getImage()).placeholder(R.drawable.shop_iphonex).into(avator);
            viewHolder.getView(R.id.iv_complete_label).setVisibility(View.VISIBLE);
        }else if(type==LUCKY_TIME_TYPE){
            ImageView avator = viewHolder.getView(R.id.iv_avator);
            GlideApp.with(mContext).load(luckyBeans.get(position).getHead_img()).placeholder(R.drawable.ic_avator_default).into(avator);
            viewHolder.setText(R.id.tv_history_lucky_name,luckyBeans.get(position).getNickname());
            viewHolder.setText(R.id.tv_history_lucky_message,luckyBeans.get(position).getContent());
            ImageView img = viewHolder.getView(R.id.iv_shop_show_bg);
            GlideApp.with(mContext).load(luckyBeans.get(position).getImages()).into(img);
        }else if(type==RULE_TYPE){

        }
        onBindListener(viewHolder,position);
    }

    private void onBindListener(ViewHolder holderGeneral, int position) {
        holderGeneral.itemView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }
}
