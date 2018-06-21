package com.cyc.newpai.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.HistoryCompleteTransactionFragment;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;

public class HistoryCompleteTransactionAdapter extends BaseRecyclerAdapter<HisTransactionBean> {

    private int resId;

    public HistoryCompleteTransactionAdapter(RecyclerView mRecyclerView, int type) {
        super(mRecyclerView);
        switch (type){
            case HistoryCompleteTransactionFragment.COMPLETE_TRANSACTION_TYPE:
                resId = R.layout.fragment_history_transaction_item;
                break;
            case HistoryCompleteTransactionFragment.LUCKY_TIME_TYPE:
                resId = R.layout.fragment_history_lucky_item;
                break;
            case HistoryCompleteTransactionFragment.RULE_TYPE:
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

    }

    public class ViewHolderGeneral extends RecyclerView.ViewHolder{

        public ViewHolderGeneral(View itemView) {
            super(itemView);
        }
    }
}
