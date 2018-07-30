package com.cyc.newpai.ui.main.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;

import java.util.List;

public class NewHomeRecyclerViewAdapter extends CommonBaseAdapter<HomeBean> {
    public NewHomeRecyclerViewAdapter(Context context, List<HomeBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, HomeBean data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_home_category_price,"￥"+data.getNow_price()+"");
        int time = 10 - data.getLeft_second();
        String timeStr = "";
        if(time<10&&time>0){
            timeStr = "00:00:0" + time;
        }else if(time>=10){
            timeStr = "00:00:" + time;
        }else{
            /*time = 10;
            mList.get(position).setLeft_second(time);
            timeStr = "00:00:" + time;*/
        }
        viewHolder.setText(R.id.tv_home_category_count_down,timeStr);
        ImageView shopIcon = viewHolder.getView(R.id.iv_shop_icon);
        GlideApp.with(mContext).load(data.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
        viewHolder.setText(R.id.tv_shop_title,data.getGoods_name());
        //onBindListener(viewHolder,position);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.home_main_item;
    }

    @Override
    public void setLoadMoreData(List<HomeBean> datas) {
        isLoading = false;
        getAllData().clear();
        getAllData().addAll(datas);
        notifyItemRangeChanged(getHeaderCount(),getAllData().size());
    }
}
