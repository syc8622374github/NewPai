package com.cyc.newpai.ui.category.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CategoryDetailRecyclerAdapter extends CommonBaseAdapter<CategoryDetailBean> {

    private Handler handler = new Handler(Looper.getMainLooper());

    public CategoryDetailRecyclerAdapter(Context context, List<CategoryDetailBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, CategoryDetailBean data, int position) {
        ImageView avator = holder.getView(R.id.iv_category_detail_icon);
        GlideApp.with(mContext).load(data.getImage()).placeholder(R.drawable.shop_iphonex).into(avator);
        holder.setText(R.id.tv_category_detail_title, data.getGoods_name());
        holder.setText(R.id.tv_category_detail_price, "￥" + data.getNow_price());
        int time = data.getLeft_second();
        String timeStr;
        if (time < 10 && time > 0) {
            timeStr = "00:00:0" + time;
        } else if (time >= 10) {
            timeStr = "00:00:" + time;
        } else {
            time = 10;
            data.setLeft_second(time);
            timeStr = "00:00:" + time;
        }
        holder.setText(R.id.tv_category_detail_count_down, timeStr);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    data.setLeft_second(data.getLeft_second() - 1);
                    int time = data.getLeft_second();
                    String timeStr;
                    if (time < 10 && time > 0) {
                        timeStr = "00:00:0" + time;
                    } else if (time >= 10) {
                        timeStr = "00:00:" + time;
                    } else {
                        time = 10;
                        data.setLeft_second(time);
                        timeStr = "00:00:" + time;
                    }
                    holder.setText(R.id.tv_category_detail_count_down, timeStr);
                });
            }
        }, 1000, 1000);
        holder.itemView.setTag(timer);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.fragment_category_detail_item;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolder mHolder = (ViewHolder) holder;
        Timer timer = (Timer) mHolder.itemView.getTag();
        if (timer != null) {
            timer.cancel();
        }
    }
}
