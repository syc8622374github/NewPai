package com.cyc.newpai.ui.category.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.ui.category.entity.CategoryTitleBean;

public class CategoryTitleRecyclerAdapter extends BaseRecyclerAdapter<CategoryTitleBean> {


    public CategoryTitleRecyclerAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_category_title_item,parent,false);
        return new ViewHolderGeneral(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderGeneral viewHolderGeneral = (ViewHolderGeneral) holder;
        TextView textView = (TextView) viewHolderGeneral.itemView;
        if(mList.get(position).isSelect()){
            textView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            textView.setBackgroundResource(R.drawable.shape_border_left_line_red);
        }else{
            textView.setBackgroundColor(Color.parseColor("#00000000"));
            textView.setTextColor(mContext.getResources().getColor(R.color.color_category_title_default));
        }
        textView.setText(mList.get(position).getTitle());
    }

    protected class ViewHolderGeneral extends RecyclerView.ViewHolder{
        public View view;

        public ViewHolderGeneral(View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
