package com.cyc.newpai.ui.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.BaseAdapter;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;

import java.util.List;

public class PaySucessAdapter extends CommonBaseAdapter<HomeBean> {

    public PaySucessAdapter(Context context, List<HomeBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, HomeBean data, int position) {
        holder.setText(R.id.tv_home_category_price, "￥" + data.getNow_price() + "");
        int time = 10 - data.getLeft_second();
        String timeStr = "";
        if (time < 10 && time > 0) {
            timeStr = "00:00:0" + time;
        } else if (time >= 10) {
            timeStr = "00:00:" + time;
        }
        holder.setText(R.id.tv_home_category_count_down, timeStr);
        ImageView shopIcon = holder.getView(R.id.iv_shop_icon);
        GlideApp.with(mContext).load(data.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
        holder.setText(R.id.tv_shop_title, data.getGoods_name());
    }

    public void setListNotifyCustom(List mList) {
        getAllData().clear();
        getAllData().addAll(mList);
        if (getAllData().size() == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(getHeaderCount(), getAllData().size());
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.home_main_item;
    }
}
