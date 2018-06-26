package com.cyc.newpai.ui.category.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;

public class CategoryDetailRecyclerAdapter extends BaseRecyclerAdapter<CategoryDetailBean> {

    public CategoryDetailRecyclerAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_category_detail_item,parent,false);
        return new ViewHolderGeneral(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolderGeneral extends RecyclerView.ViewHolder{

        public ViewHolderGeneral(View itemView) {
            super(itemView);
        }
    }
}
