package com.cyc.newpai.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.entity.BidAgeRecordBean;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;

public class HistoryCompleteTransactionAdapter extends BaseRecyclerAdapter<BidAgeRecordBean> {

    public static final int COMPLETE_TRANSACTION_TYPE = 0x10001;
    public static final int LUCKY_TIME_TYPE = 0x10002;
    public static final int RULE_TYPE = 0x10003;
    private int resId;
    private int type;

    public HistoryCompleteTransactionAdapter(RecyclerView mRecyclerView, int type) {
        super(mRecyclerView);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resId, parent, false);
        return new ViewHolderGeneral(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral viewHolderGeneral = (ViewHolderGeneral) holder;
        /*if(mList.get(position).isFinish()){
            viewHolderGeneral.completeLabel.setVisibility(View.VISIBLE);
        }else{
            viewHolderGeneral.completeLabel.setVisibility(View.GONE);
        }*/
        switch (type) {
            case COMPLETE_TRANSACTION_TYPE:
                viewHolderGeneral.completeLabel.setVisibility(View.GONE);
                viewHolderGeneral.dealPerson.setText(getList().get(position).getNickname());
                viewHolderGeneral.dealPrice.setText(getList().get(position).getDeal_price());
                viewHolderGeneral.dealTime.setText(getList().get(position).getDeal_time());
                viewHolderGeneral.dealPerson.setText(getList().get(position).getNickname());
                viewHolderGeneral.rate.setText(getList().get(position).getRate());
                break;
            case LUCKY_TIME_TYPE:
                break;
            case RULE_TYPE:
                break;
            default:
                break;
        }

        onBindListener(viewHolderGeneral,position);
    }

    private void onBindListener(ViewHolderGeneral holderGeneral, int position) {
        holderGeneral.mView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }

    public class ViewHolderGeneral extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView completeLabel;
        public final TextView dealPerson;
        public final TextView marketPrice;
        public final TextView dealPrice;
        public final TextView dealTime;
        public final TextView rate;

        public ViewHolderGeneral(View itemView) {
            super(itemView);
            mView = itemView;
            completeLabel = itemView.findViewById(R.id.iv_complete_label);
            dealPerson = itemView.findViewById(R.id.tv_his_bid_deal_person);
            marketPrice = itemView.findViewById(R.id.tv_his_bid_market_price);
            dealPrice = itemView.findViewById(R.id.tv_hist_deal_price);
            dealTime = itemView.findViewById(R.id.tv_his_bid_deal_time);
            rate = itemView.findViewById(R.id.tv_his_bid_rate);
        }
    }
}
