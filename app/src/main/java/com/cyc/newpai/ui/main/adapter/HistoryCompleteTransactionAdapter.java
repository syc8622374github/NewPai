package com.cyc.newpai.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.entity.HisTransactionBean;

public class HistoryCompleteTransactionAdapter extends BaseRecyclerAdapter<HisTransactionBean> {

    public static final int COMPLETE_TRANSACTION_TYPE = 0x10001;
    public static final int LUCKY_TIME_TYPE = 0x10002;
    public static final int RULE_TYPE = 0x10003;
    private int resId;

    public HistoryCompleteTransactionAdapter(RecyclerView mRecyclerView, int type) {
        super(mRecyclerView);
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
        if(mList.get(position).isFinish()){
            viewHolderGeneral.completeLabel.setVisibility(View.VISIBLE);
        }else{
            viewHolderGeneral.completeLabel.setVisibility(View.GONE);
        }
        onBindListener(viewHolderGeneral,position);
    }

    private void onBindListener(ViewHolderGeneral holderGeneral, int position) {
        holderGeneral.mView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }

    public class ViewHolderGeneral extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView completeLabel;

        public ViewHolderGeneral(View itemView) {
            super(itemView);
            mView = itemView;
            completeLabel = itemView.findViewById(R.id.iv_complete_label);
        }
    }
}
