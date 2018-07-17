package com.cyc.newpai.ui.me.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.me.SelectAddressActivity;
import com.cyc.newpai.ui.me.entity.AddressBean;

public class AddressRecyclerViewAdapter extends BaseRecyclerAdapter<AddressBean> {
    String type;

    public AddressRecyclerViewAdapter(RecyclerView mRecyclerView,String type) {
        super(mRecyclerView);
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, R.layout.activity_select_address_item,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if(type.equals(SelectAddressActivity.TYPE_SELECT_ADDRESS)){
            viewHolder.getView(R.id.ll_address_edit_content).setVisibility(View.GONE);
            viewHolder.getView(R.id.cb_address_item_check).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.v_divider).setVisibility(View.GONE);
            ((CheckBox)viewHolder.getView(R.id.cb_address_item_check)).setChecked(getList().get(position).isCheck());
        }else{
            viewHolder.getView(R.id.ll_address_edit_content).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.cb_address_item_check).setVisibility(View.GONE);
            viewHolder.getView(R.id.v_divider).setVisibility(View.VISIBLE);
            ((CheckBox)viewHolder.getView(R.id.cb_address_edit_item_check02)).setChecked(getList().get(position).isCheck());
            if(position==0){
                ((TextView)viewHolder.getView(R.id.tv_address_edit_item_default_address_prompt)).setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }else{
                ((TextView)viewHolder.getView(R.id.tv_address_edit_item_default_address_prompt)).setTextColor(mContext.getResources().getColor(android.R.color.black));
            }
        }
        String defaultAddr = "";
        if(position==0){
            defaultAddr = "<font color=#FF6A6A>[默认地址] </font>";
        }
        Spanned spanned = Html.fromHtml(defaultAddr+getList().get(position).getAddress());
        ((TextView)viewHolder.getView(R.id.tv_address_item_str)).setText(spanned);
        viewHolder.setText(R.id.tv_address_item_name,getList().get(position).getName());
        viewHolder.setText(R.id.tv_address_item_phone,getList().get(position).getPhone());
        //viewHolder.setText(R.id.tv_address_item_str,ssb.toString());
        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClickListener(v,getList().get(position),position));
    }
}
