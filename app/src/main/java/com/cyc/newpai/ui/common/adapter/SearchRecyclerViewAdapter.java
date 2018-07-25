package com.cyc.newpai.ui.common.adapter;

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
import com.cyc.newpai.ui.common.entity.SearchBean;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class SearchRecyclerViewAdapter extends BaseRecyclerAdapter<SearchBean> {
    public SearchRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, R.layout.activity_search_item,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_search_title,getList().get(position).getGoods_name());
        viewHolder.setText(R.id.tv_search_price,"￥"+getList().get(position).getNow_price());
        int seccond = getList().get(position).getLeft_second();
        String countDown = "00:00:";
        if(seccond>=10){
            countDown = "00:00:"+seccond;
        }else if(seccond<10&seccond>=0){
            countDown = "00:00:0"+seccond;
        }
        viewHolder.setText(R.id.tv_search_count_down, countDown);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
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
                    viewHolder.setText(R.id.tv_search_count_down, timeStr);
                });
            }
        }, 1000, 1000);
        viewHolder.itemView.setTag(timer);
        viewHolder.setText(R.id.tv_search_title,getList().get(position).getNickname());
        ImageView iconAvator = viewHolder.getView(R.id.iv_search_item_icon);
        try {
            GlideApp.with(mContext).load(getList().get(position).getImage()).placeholder(R.drawable.ic_avator_default).into(iconAvator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolder mHolder = (ViewHolder) holder;
        Timer timer = (Timer) mHolder.itemView.getTag();
        if (timer != null) {
            timer.cancel();
        }
    }
}
