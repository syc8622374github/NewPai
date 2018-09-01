package com.cyc.newpai.ui.me.adapter;

import android.content.Intent;
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
import com.cyc.newpai.ui.me.AddOrEditAddressActivity;
import com.cyc.newpai.ui.me.SelectAddressActivity;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.SharePreUtil;

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
            viewHolder.getView(R.id.tv_address_edit_item_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddOrEditAddressActivity.class);
                    intent.putExtra(AddOrEditAddressActivity.TYPE_ID,getList().get(position).getId());
                    intent.putExtra(AddOrEditAddressActivity.TYPE_ADDRESS, AddOrEditAddressActivity.TYPE_EDIT_ADDRESS);
                    intent.putExtra(AddOrEditAddressActivity.TYPE_DATA,getList().get(position));
                    mContext.startActivity(intent);
                }
            });
            viewHolder.getView(R.id.tv_address_edit_item_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        viewHolder.setText(R.id.tv_address_item_name,getList().get(position).getName());
        viewHolder.setText(R.id.tv_address_item_phone,getList().get(position).getMobile());
        //viewHolder.setText(R.id.tv_address_item_str,ssb.toString());
        viewHolder.itemView.setOnClickListener(v -> mListener.onItemClickListener(v,getList().get(position),position));
        String isDefault = getList().get(position).getIs_default();
        CheckBox defaultCheckBox = viewHolder.getView(R.id.cb_address_edit_item_check02);
        defaultCheckBox.setChecked(isDefault.equals("1")?true:false);
        String defaultAddr = "";
        if(isDefault.equals("1")){
            defaultAddr = "<font color=#FF6A6A>[默认地址] </font>";
            SharePreUtil.setPref(mContext, Constant.DEFAULT_ADDRESS, GsonManager.getGson().toJson(getList().get(position)));
        }
        Spanned spanned = Html.fromHtml(defaultAddr+getList().get(position).getArea()+" "+getList().get(position).getAddress());
        ((TextView)viewHolder.getView(R.id.tv_address_item_str)).setText(spanned);
    }
}
