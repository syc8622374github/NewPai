package com.cyc.newpai.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.me.adapter.AddressRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.util.ScreenUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectAddressActivity extends BaseActivity {

    public static final String TYPE_SELECT_ADDRESS = "type_select_address";
    public static final String TYPE_EDIT_ADDRESS = "type_edit_address";
    public static final String TYPE_ADDRESS = "type_address";
    public static final String TYPE_SELECT_DATA =  "type_select_data";

    private AddressRecyclerViewAdapter addressRecyclerViewAdapter;
    private String type = "";
    private RecyclerView rvAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(TYPE_ADDRESS);
        initView();
        initVaryView();
    }

    @Override
    protected View getLoadingTargetView() {
        return rvAddressList;
    }

    private void initData() {
        varyViewHelper.showLoadingView();
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_ADDRESS_LIST_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<AddressBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<AddressBean>>>() {
                        }.getType());
                        if (data.getCode() == 200 && data.getResult().getList().size() > 0) {
                            updateAddressList(data.getResult().getList());
                            handler.post(() -> varyViewHelper.showDataView());
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(() -> varyViewHelper.showEmptyView());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void updateAddressList(List<AddressBean> list) {
        handler.post(() -> addressRecyclerViewAdapter.setListNotify(list));
    }


    private void initView() {
        rvAddressList = findViewById(R.id.rv_address_list);
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        rvAddressList.addItemDecoration(new CommItemDecoration(this
                , LinearLayoutManager.VERTICAL
                , getResources().getColor(R.color.color_list_bg)
                , ScreenUtil.dp2px(this, 15)));
        addressRecyclerViewAdapter = new AddressRecyclerViewAdapter(rvAddressList, type);
        rvAddressList.setAdapter(addressRecyclerViewAdapter);
        addressRecyclerViewAdapter.setOnClickItemListener((BaseRecyclerAdapter.OnAdapterListener<AddressBean>) (view, itemBean, position) -> {
            if(type.equals(TYPE_SELECT_ADDRESS)){
                Intent intent = new Intent();
                intent.putExtra(TYPE_SELECT_DATA,itemBean);
                setResult(2,intent);
                finish();
            }
        });
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        if (type.equals(SelectAddressActivity.TYPE_EDIT_ADDRESS)) {
            ctb_toolbar.setTitle("收货地址");
        }
        ctb_toolbar.setRightAction1("新增", v -> {
            Intent intent = new Intent(this, AddOrEditAddressActivity.class);
            intent.putExtra(AddOrEditAddressActivity.TYPE_ADDRESS, AddOrEditAddressActivity.TYPE_ADD_ADDRESS);
            startActivity(intent);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_address;
    }

    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_address_add:
                Intent intent = new Intent(this, AddOrEditAddressActivity.class);
                intent.putExtra(AddOrEditAddressActivity.TYPE_ADDRESS, AddOrEditAddressActivity.TYPE_ADD_ADDRESS);
                startActivity(intent);
                break;
        }
    }
}
