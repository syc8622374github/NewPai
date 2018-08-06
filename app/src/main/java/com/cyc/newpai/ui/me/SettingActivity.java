package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.me.adapter.SettingRecyclerAdapter;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.SharePreUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity {

    private RecyclerView rvList;
    private SettingRecyclerAdapter settingRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        List<String> title = new ArrayList<>();
        title.add("服务协议");
        title.add("新手帮助");
        settingRecyclerAdapter.setListNotify(title);
    }

    private void initView() {
        rvList = findViewById(R.id.rv_setting_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new CommItemDecoration(this,1,getResources().getColor(R.color.divider),1));
        settingRecyclerAdapter = new SettingRecyclerAdapter(rvList);
        settingRecyclerAdapter.setOnClickItemListener((BaseRecyclerAdapter.OnAdapterListener<String>) (view, itemBean, position) -> {
            if(itemBean.equals("新手帮助")){
                startActivity(new Intent(SettingActivity.this,UserHelpActivity.class));
            }
        });
        rvList.setAdapter(settingRecyclerAdapter);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.btn_setting_logout:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否要退出登录")
                        .setPositiveButton("确定", (dialog, which) -> {
                            SharePreUtil.setPref(SettingActivity.this, Constant.TOKEN,"");
                            SharePreUtil.setPref(SettingActivity.this, Constant.UID,"");
                            setResult(1);
                            finish();
                            dialog.cancel();
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.cancel();
                        }).show();
                break;
        }
    }
}
