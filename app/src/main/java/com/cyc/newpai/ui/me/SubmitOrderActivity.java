package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.common.adapter.PayMethodRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.PayMethodBean;

import java.util.ArrayList;
import java.util.List;

public class SubmitOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initPayMethod();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_order;
    }

    private void initPayMethod() {
        RecyclerView recyclerView = findViewById(R.id.rv_recharge_method);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PayMethodRecyclerViewAdapter payMethodRecyclerViewAdapter = new PayMethodRecyclerViewAdapter(recyclerView);
        List<PayMethodBean> payMethodBeans = new ArrayList<>();
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_alipay,"支付宝支付",true));
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_wechat,"微信支付",false));
        payMethodRecyclerViewAdapter.setListNotify(payMethodBeans);
        payMethodRecyclerViewAdapter.setOnClickItemListener((view, itemBean, position) -> {
            for(int i=0;i<payMethodBeans.size();i++){
                if(i==position){
                    payMethodBeans.get(i).setCheck(true);
                }else{
                    payMethodBeans.get(i).setCheck(false);
                }
            }
            payMethodRecyclerViewAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(payMethodRecyclerViewAdapter);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.btn_order_detail_ok:
                startActivity(new Intent(this,PaySuccessActivity.class));
                break;
        }
    }
}
