package com.cyc.newpai.ui.me.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
        String type = mList.get(position).getType();
        TextView pay = viewHolder.getView(R.id.tv_all_record__pay);
        if(Integer.valueOf(mList.get(position).getMoney())>0){
            pay.setTextColor(mContext.getResources().getColor(R.color.color_category_title_default));
            ((TextView)viewHolder.getView(R.id.tv_all_record__pay)).setTextColor(mContext.getResources().getColor(android.R.color.black));
            viewHolder.setText(R.id.tv_all_record__pay, Html.fromHtml("<font color='#000000'>"+mList.get(position).getMoney()).toString());
        }else{
            viewHolder.setText(R.id.tv_all_record__pay, Html.fromHtml("<font color='"+mContext.getResources().getColor(R.color.colorPrimary)+"'>"+mList.get(position).getMoney()).toString());

        }
        viewHolder.setText(R.id.tv_all_record_type,"["+mList.get(position).getType()+"]");
        viewHolder.setText(R.id.tv_all_record_time,mList.get(position).getAdd_time());
    }
}
