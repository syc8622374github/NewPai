package com.cyc.newpai.framework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.util.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on  2016/11/03
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter   {
    protected final String life = "AdapterLife";
    protected RecyclerView mRecyclerView;
    protected Context mContext;
    protected List<T> mList =new ArrayList<>();
    protected int mAdapterPosition = 0;
    protected OnAdapterListener mListener;
    /*protected final String mUrlSmallFormat;//小图地址
    protected final String mUrlGeneralFormat;//普通地址
    protected final String mUrlBigFormat;//大图地址*/

    public interface OnAdapterListener {
        void onItemClickListener(View view, Object itemBean, int position);
    }

    public List<T> getList() {
        return mList;
    }

    public void setListNotify(List<T> mList) {
        this.mList.clear();
        this.mList=mList;
        notifyDataSetChanged();
    }

    public void addListNotify(List<T> mList){
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public int getAdapterPosition() {
        return mAdapterPosition;
    }

    public void setAdapterPosition(int mAdapterPosition) {
        this.mAdapterPosition = mAdapterPosition;
    }

    public void setOnClickItemListener(OnAdapterListener mListener) {
        this.mListener = mListener;
    }

    public BaseRecyclerAdapter(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        this.mContext=mRecyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mAdapterPosition = RecyclerViewUtil.getAdapterPosition(mRecyclerView, holder);
    }
}