package com.cyc.newpai.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;

public class HomeWindowRecyclerViewAdapter extends BaseRecyclerAdapter<HomeWindowBean> {

    public HomeWindowRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_window_item, parent, false);
        ViewHolderGeneral holder = new ViewHolderGeneral(view);//使用子类初始化ViewHolder
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral mHolder = (ViewHolderGeneral)holder;
        HomeWindowBean bean = mList.get(position);
        mHolder.title.setText(bean.getTitle());
        mHolder.icon.setImageResource(bean.getImageRes());
    }

    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {
        //这个CardView采用两层操作
        public final View mView;
        public final TextView title;
        public final ImageView icon;


        public ViewHolderGeneral(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.tv_home_window_title);
            icon = view.findViewById(R.id.iv_home_window_icon);
        }

    }
}
