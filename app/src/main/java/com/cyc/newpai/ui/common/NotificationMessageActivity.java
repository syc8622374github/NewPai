package com.cyc.newpai.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.common.adapter.NotificationMessageRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.NotificationMsgBean;
import com.cyc.newpai.util.RecyclerViewUtil;
import com.cyc.newpai.util.ScreenUtil;
import com.cyc.newpai.widget.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

public class NotificationMessageActivity extends BaseActivity {

    private RecyclerView rvList;
    private NotificationMessageRecyclerViewAdapter notificationMessageRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void initView() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.srl_notification_msg_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> initData());
        rvList = findViewById(R.id.rv_notification_msg_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new CommItemDecoration(this,LinearLayoutManager.VERTICAL,getResources().getColor(R.color.color_list_bg), ScreenUtil.dp2px(this,15)));
        notificationMessageRecyclerViewAdapter = new NotificationMessageRecyclerViewAdapter(rvList);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(notificationMessageRecyclerViewAdapter);
        rvList.setAdapter(headerAndFooterRecyclerViewAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(rvList, getFootView());
        }
    }

    protected View getFootView() {
        LoadingFooter mFooterView = null;
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(this);
            mFooterView.setState(LoadingFooter.State.Loading);
        }
        return mFooterView;
    }

    private void initData() {
        List<NotificationMsgBean> datas = new ArrayList<>();
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        datas.add(new NotificationMsgBean("充值成功","500拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_INCOMING));
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        datas.add(new NotificationMsgBean("充值成功","500拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_INCOMING));
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        datas.add(new NotificationMsgBean("充值成功","500拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_INCOMING));
        datas.add(new NotificationMsgBean("竞拍返回的拍币已到账","30拍币已到账","10:02",NotificationMsgBean.TYPE_NOTIFICATION_MSG_PAY));
        notificationMessageRecyclerViewAdapter.setListNotify(datas);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification_message;
    }
}
