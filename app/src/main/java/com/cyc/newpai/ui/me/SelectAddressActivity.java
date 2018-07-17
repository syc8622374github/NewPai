package com.cyc.newpai.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.me.adapter.AddressRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends BaseActivity {

    public static final String TYPE_SELECT_ADDRESS = "type_select_address";
    public static final String TYPE_EDIT_ADDRESS = "type_edit_address";
    public static final String TYPE_ADDRESS = "type_address";

    private AddressRecyclerViewAdapter addressRecyclerViewAdapter;
    private String type = TYPE_SELECT_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(TYPE_ADDRESS);
        initView();
    }



    private void initView() {
        RecyclerView rvAddressList = findViewById(R.id.rv_address_list);
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        rvAddressList.addItemDecoration(new CommItemDecoration(this
                ,LinearLayoutManager.VERTICAL
                ,getResources().getColor(R.color.color_list_bg)
        , ScreenUtil.dp2px(this,15)));
        addressRecyclerViewAdapter = new AddressRecyclerViewAdapter(rvAddressList,type);
        rvAddressList.setAdapter(addressRecyclerViewAdapter);
        List<AddressBean> addressBeans = new ArrayList<>();
        addressBeans.add(new AddressBean("生苗","18xxxxxxxx","江西省南昌市经济技术开发区西林大街2ss",true));
        addressBeans.add(new AddressBean("生苗","18xxxxxxxx","江西省南昌市经济技术开发区西林大街2ss",false));
        addressBeans.add(new AddressBean("生苗","18xxxxxxxx","江西省南昌市经济技术开发区西林大街2ss",false));
        addressBeans.add(new AddressBean("生苗","18xxxxxxxx","江西省南昌市经济技术开发区西林大街2ss",false));
        addressRecyclerViewAdapter.setListNotify(addressBeans);
        addressRecyclerViewAdapter.setOnClickItemListener((view, itemBean, position) -> {
            for(int i=0;i<addressBeans.size();i++){
                if(i==position){
                    addressBeans.get(i).setCheck(!addressBeans.get(i).isCheck());
                }else{
                    addressBeans.get(i).setCheck(false);
                }
            }
            addressRecyclerViewAdapter.notifyDataSetChanged();
        });
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        if(type.equals(SelectAddressActivity.TYPE_EDIT_ADDRESS)){
            ctb_toolbar.setTitle("收货地址");
            ctb_toolbar.setRightAction1("新增", v -> {

            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_address;
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.btn_address_add:
                Intent intent = new Intent(this,AddOrEditAddressActivity.class);
                intent.putExtra(AddOrEditAddressActivity.TYPE_ADDRESS,AddOrEditAddressActivity.TYPE_ADD_ADDRESS);
                startActivity(intent);
                break;
        }
    }
}
