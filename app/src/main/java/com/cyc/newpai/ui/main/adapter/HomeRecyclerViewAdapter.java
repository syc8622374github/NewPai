package com.cyc.newpai.ui.main.adapter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;

import java.util.Timer;
import java.util.TimerTask;

public class HomeRecyclerViewAdapter extends BaseRecyclerAdapter<HomeBean> {

    private int count = 99;

    public HomeRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_item,parent,false);
        ViewHolderGeneral holderGeneral = new ViewHolderGeneral(view);
        return holderGeneral;
    }

    int mSecond = 99;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral holderGeneral = (ViewHolderGeneral) holder;
        holderGeneral.price.setText("￥"+mList.get(position).getNow_price()+"");
        int time = mList.get(position).getLeft_second();
        String timeStr = "";
        if(time<10&&time>0){
            timeStr = "00:00:0" + time;
        }else if(time>=10){
            timeStr = "00:00:" + time;
        }else{
            time = 10;
            mList.get(position).setLeft_second(time);
            timeStr = "00:00:" + time;
        }
        holderGeneral.countDown.setText(timeStr);
        //countDownTimer.start();
        //handler.sendEmptyMessageDelayed(1,1000/count);
        /*while (mSecond>0){
            if(time>10){
                timeStr = "00:"+ time +":" + mSecond;
            }else{
                timeStr = "00:0"+ time + ":" + mSecond;
            }
            mSecond -= mSecond / count;
            holderGeneral.countDown.setText(timeStr);
        }*/
        onBindListener(holderGeneral,position);
    }

    private void onBindListener(ViewHolderGeneral holderGeneral, int position) {
        holderGeneral.mView.setOnClickListener(view -> mListener.onItemClickListener(view,mList.get(position),position));
    }

    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {
        //这个CardView采用两层操作
        public final View mView;
        public final TextView countDown;
        public final TextView price;


        public ViewHolderGeneral(View view) {
            super(view);
            mView = view;
            countDown = view.findViewById(R.id.tv_home_category_count_down);
            price = view.findViewById(R.id.tv_home_category_price);
        }

    }
}
