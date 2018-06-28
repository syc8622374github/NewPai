package com.cyc.newpai.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;

public class HomeRecyclerViewAdapter extends BaseRecyclerAdapter<HomeBean> {

    public HomeRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_item,parent,false);
        ViewHolderGeneral holderGeneral = new ViewHolderGeneral(view);
        return holderGeneral;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral holderGeneral = (ViewHolderGeneral) holder;
        holderGeneral.price.setText(mList.get(position).getPrice()+"");
        holderGeneral.countDown.setText("00:00:0"+mList.get(position).getCountdown()+"");
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
