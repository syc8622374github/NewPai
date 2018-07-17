package com.cyc.newpai.ui.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.common.entity.SearchMenuBean;
import com.cyc.newpai.util.ScreenUtil;

public class SearchRecyclerViewAdapter extends BaseRecyclerAdapter<SearchMenuBean> {

    public SearchRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(new TextView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.itemView.setBackgroundResource(R.drawable.shape_border_grey_search_02);
        ((TextView)viewHolder.itemView).setText(getList().get(position).getTitle());
        TextView textView = ((TextView)viewHolder.itemView);
        textView.setPadding(ScreenUtil.dp2px(mContext,7)
                ,ScreenUtil.dp2px(mContext,7)
                ,ScreenUtil.dp2px(mContext,7)
                ,ScreenUtil.dp2px(mContext,7));
    }
}
