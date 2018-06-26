package com.cyc.newpai.ui.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.common.entity.AmountBean;
import com.cyc.newpai.ui.common.entity.PayMethodBean;
import com.cyc.newpai.ui.main.adapter.HistoryCompleteTransactionAdapter;

public class PayMethodRecyclerViewAdapter extends BaseRecyclerAdapter<PayMethodBean> {

    public PayMethodRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pay_method_item,parent,false);
        return new ViewHolderGeneral(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral viewHolderGeneral = (ViewHolderGeneral) holder;
        viewHolderGeneral.icon.setImageResource(mList.get(position).getIcResId());
        viewHolderGeneral.title.setText(mList.get(position).getPayMethod());
        viewHolderGeneral.checkBox.setChecked(mList.get(position).isCheck());
        onBindListener(viewHolderGeneral,position);
    }

    private void onBindListener(ViewHolderGeneral holderGeneral, int position) {
        holderGeneral.mView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }


    public class ViewHolderGeneral extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView icon;
        public final TextView title;
        public final CheckBox checkBox;

        public ViewHolderGeneral(View itemView) {
            super(itemView);
            mView = itemView;
            icon = itemView.findViewById(R.id.iv_pay_method_icon);
            title = itemView.findViewById(R.id.tv_pay_method_title);
            checkBox = itemView.findViewById(R.id.cb_pay_method);
        }
    }
}
