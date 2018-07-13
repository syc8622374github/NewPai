package com.cyc.newpai.ui.me.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.me.entity.AllRecordBean;

public class AllRecordRecyclerViewAdapter extends BaseRecyclerAdapter<AllRecordBean> {

    public AllRecordRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_all_record_item,parent,false);
        return ViewHolder.create(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_all_record_shop_name,mList.get(position).getTitle());
        viewHolder.setText(R.id.tv_all_record__pay,mList.get(position).getMoney());
        String type = mList.get(position).getType();
        TextView pay = viewHolder.getView(R.id.tv_all_record__pay);
        switch (type){
            case AllRecordBean.RECORD_PRODUCT:
                pay.setTextColor(mContext.getResources().getColor(R.color.color_category_title_default));
                break;
            default:
                pay.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                break;
        }
        viewHolder.setText(R.id.tv_all_record_type,"["+mList.get(position).getType()+"]");
        viewHolder.setText(R.id.tv_all_record_time,mList.get(position).getAdd_time());
    }
}
