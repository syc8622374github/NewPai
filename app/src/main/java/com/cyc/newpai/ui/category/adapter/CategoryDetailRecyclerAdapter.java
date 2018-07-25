package com.cyc.newpai.ui.category.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;

import java.util.Timer;
import java.util.TimerTask;

public class CategoryDetailRecyclerAdapter extends BaseRecyclerAdapter<CategoryDetailBean> {

    private Handler handler = new Handler(Looper.getMainLooper());

    public CategoryDetailRecyclerAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, R.layout.fragment_category_detail_item, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageView avator = viewHolder.getView(R.id.iv_category_detail_icon);
        GlideApp.with(mContext).load(getList().get(position).getImage()).placeholder(R.drawable.shop_iphonex).into(avator);
        viewHolder.setText(R.id.tv_category_detail_title, getList().get(position).getGoods_name());
        viewHolder.setText(R.id.tv_category_detail_price, "￥" + getList().get(position).getNow_price());
        int time = mList.get(position).getLeft_second();
        String timeStr;
        if (time < 10 && time > 0) {
            timeStr = "00:00:0" + time;
        } else if (time >= 10) {
            timeStr = "00:00:" + time;
        } else {
            time = 10;
            mList.get(position).setLeft_second(time);
            timeStr = "00:00:" + time;
        }
        viewHolder.setText(R.id.tv_category_detail_count_down, timeStr);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    getList().get(position).setLeft_second(mList.get(position).getLeft_second() - 1);
                    int time = mList.get(position).getLeft_second();
                    String timeStr;
                    if (time < 10 && time > 0) {
                        timeStr = "00:00:0" + time;
                    } else if (time >= 10) {
                        timeStr = "00:00:" + time;
                    } else {
                        time = 10;
                        mList.get(position).setLeft_second(time);
                        timeStr = "00:00:" + time;
                    }
                    viewHolder.setText(R.id.tv_category_detail_count_down, timeStr);
                });
            }
        }, 1000, 1000);
        viewHolder.itemView.setTag(timer);

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
