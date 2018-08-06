package com.cyc.newpai.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;

import java.util.List;

public class NewHomeRecyclerViewAdapter extends CommonBaseAdapter {
    public static final int HOME_MY_AUCTION_TYPE = 0x11201;
    public static final int HOME_DATA_TYPE = 0x11202;
    public static final int HOME_COLLECTION_TYPE = 0x11203;
    private int type;
    private int resId;

    public NewHomeRecyclerViewAdapter(Context context, List datas, boolean isOpenLoadMore,int type) {
        super(context, datas, isOpenLoadMore);
        updateType(type);
    }

    public void updateType(int type) {
        this.type = type;
        switch (type) {
            case HOME_MY_AUCTION_TYPE:
                resId = R.layout.fragment_my_aution_all_list_item;
                break;
            case HOME_DATA_TYPE:
                resId = R.layout.home_main_item;
                break;
            case HOME_COLLECTION_TYPE:
                resId = R.layout.home_main_item;
                break;
            default:
                resId = R.layout.home_main_item;
                break;
        }
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
    protected void convert(ViewHolder holder, Object data, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        switch (type) {
            case HOME_DATA_TYPE:
                if (data instanceof HomeBean) {
                    HomeBean homeBean = (HomeBean) data;
                    viewHolder.setText(R.id.tv_home_category_price, "￥" + homeBean.getNow_price() + "");
                    int time = 10 - homeBean.getLeft_second();
                    String timeStr = "";
                    if (time < 10 && time > 0) {
                        timeStr = "00:00:0" + time;
                    } else if (time >= 10) {
                        timeStr = "00:00:" + time;
                    } else {
                    }
                    viewHolder.setText(R.id.tv_home_category_count_down, timeStr);
                    ImageView shopIcon = viewHolder.getView(R.id.iv_shop_icon);
                    GlideApp.with(mContext).load(homeBean.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
                    viewHolder.setText(R.id.tv_shop_title, homeBean.getGoods_name());
                }
                break;
            case HOME_MY_AUCTION_TYPE:
                if (data instanceof MyAuctionBean) {
                    MyAuctionBean myAuctionBean = (MyAuctionBean) data;
                    viewHolder.setText(R.id.tv_auction_time, myAuctionBean.getLast_bid_time());
                    viewHolder.setText(R.id.tv_auction_name, myAuctionBean.getGoods_name());
                    viewHolder.setText(R.id.tv_auction_price, "￥" + myAuctionBean.getNow_price());
                    ImageView icon = viewHolder.getView(R.id.iv_auction_icon);
                    TextView tvAuctionType = viewHolder.getView(R.id.tv_auction_type);
                    GlideApp.with(mContext).load(myAuctionBean.getImage()).placeholder(R.drawable.shop_iphonex).into(icon);
                    int time2 = 10 - (Integer.valueOf(myAuctionBean.getServer_time())
                            - Integer.valueOf(myAuctionBean.getLast_bid_time()));
                    String timeStr2 = "";
                    if (time2 < 10 && time2 > 0) {
                        timeStr2 = "00:00:0" + time2;
                    } else if (time2 >= 10) {
                        timeStr2 = "00:00:" + time2;
                    }
                    viewHolder.setText(R.id.tv_auction_count_down, timeStr2);
                }
                break;
            case HOME_COLLECTION_TYPE:
                break;
        }
    }

    @Override
    protected int getViewType(int position, Object data) {
        return type;
    }

    @Override
    protected int getItemLayoutId() {
        return resId;
    }

    @Override
    public void setLoadMoreData(List datas) {
        isLoading = false;
        getAllData().clear();
        getAllData().addAll(datas);
        notifyDataSetChanged();
    }
}
