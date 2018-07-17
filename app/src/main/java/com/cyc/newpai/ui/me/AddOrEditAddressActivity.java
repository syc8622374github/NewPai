package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public class AddOrEditAddressActivity extends BaseActivity {

    public static final String TYPE_ADD_ADDRESS = "type_add_address";
    public static final String TYPE_EDIT_ADDRESS = "type_edit_address";
    public static final String TYPE_ADDRESS = "type_address";
    private String type = TYPE_ADD_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(TYPE_ADDRESS);
        initView();
    }

    private void initView() {
        LinearLayout llSetDefaultAddress = findViewById(R.id.ll_add_address_set_default_address);
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        if(type.equals(TYPE_ADD_ADDRESS)){
            llSetDefaultAddress.setVisibility(View.VISIBLE);
            ctb_toolbar.setRightAction1("保存",R.color.colorPrimary, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
            ctb_toolbar.setTitle("新增地址");
        }else if(type.equals(TYPE_EDIT_ADDRESS)){
            llSetDefaultAddress.setVisibility(View.GONE);
            ctb_toolbar.setTitle("编辑新增地址");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_or_edit_address;
    }
}
