package com.cyc.newpai.ui.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.ui.common.entity.NotificationMsgBean;

public class NotificationMessageRecyclerViewAdapter extends BaseRecyclerAdapter<NotificationMsgBean> {

    public NotificationMessageRecyclerViewAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext,R.layout.activity_notification_message_item,parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setText(R.id.tv_notification_msg_title,getList().get(position).getTitle());
        viewHolder.setText(R.id.tv_notification_msg_content,getList().get(position).getContent());
        viewHolder.setText(R.id.tv_notification_msg_time,getList().get(position).getTime());
        int type = getList().get(position).getType();
        if(type == NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY){
            viewHolder.getView(R.id.iv_icon).setBackgroundResource(R.drawable.ic_pay);
        }else if(type == NotificationMsgBean.TYPE_NOTIFICATION_MSG_INCOMING){
            viewHolder.getView(R.id.iv_icon).setBackgroundResource(R.drawable.ic_incoming);
        }
    }
}
