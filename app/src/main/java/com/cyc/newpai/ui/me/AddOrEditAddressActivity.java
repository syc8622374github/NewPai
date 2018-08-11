package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.util.LocationHelper;
import com.cyc.newpai.util.NumUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddOrEditAddressActivity extends BaseActivity implements View.OnClickListener{

    public static final String TYPE_ADD_ADDRESS = "type_add_address";
    public static final String TYPE_EDIT_ADDRESS = "type_edit_address";
    public static final String TYPE_ADDRESS = "type_address";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_DATA = "type_data";
    private String type = TYPE_ADD_ADDRESS;
    private AddressBean addressBean;
    private CheckBox checkBox;
    private EditText etMobile;
    private EditText etReceiver;
    private EditText etDetailAddress;
    private TextView tvLocalArea;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(TYPE_ADDRESS);
        id = getIntent().getStringExtra(TYPE_ID);
        addressBean = (AddressBean) getIntent().getSerializableExtra(TYPE_DATA);
        initView();
        initData();
    }

    private void initData() {
        LocationHelper locationHelper = new LocationHelper(this);
        locationHelper.setBdLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                tvLocalArea.setText(bdLocation.getProvince() +" "+bdLocation.getCity());
                etDetailAddress.setText(bdLocation.getAddrStr());
            }
        });
        locationHelper.startLocation();
    }

    private void initView() {
        LinearLayout llSetDefaultAddress = findViewById(R.id.ll_add_address_set_default_address);
        checkBox = findViewById(R.id.cb_address_edit_item_check);
        etMobile = findViewById(R.id.et_edit_address_mobile);
        etReceiver = findViewById(R.id.et_edit_address_receiver);
        tvLocalArea = findViewById(R.id.tv_edit_address_local_area);
        etDetailAddress = findViewById(R.id.et_edit_address_detail_address);
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        if(type.equals(TYPE_ADD_ADDRESS)){
            ctb_toolbar.setTitle("新增地址");
        }else if(type.equals(TYPE_EDIT_ADDRESS)){
            llSetDefaultAddress.setVisibility(View.GONE);
            ctb_toolbar.setTitle("编辑新增地址");
            if(addressBean!=null){
                etReceiver.setText(addressBean.getName());
                etMobile.setText(addressBean.getMobile());
                tvLocalArea.setText(addressBean.getArea());
                etDetailAddress.setText(addressBean.getAddress());
                checkBox.setChecked(addressBean.getIs_default().equals("1")?true:false);
            }
        }
        llSetDefaultAddress.setVisibility(View.VISIBLE);
        llSetDefaultAddress.setOnClickListener(this);
        ctb_toolbar.setRightAction1("保存",R.color.colorPrimary, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiverStr = etReceiver.getText().toString();
                String mobileStr = etMobile.getText().toString();
                String localAreaStr = tvLocalArea.getText().toString();
                String detailAddressStr = etDetailAddress.getText().toString();
                boolean isDefault = checkBox.isChecked();
                if(TextUtils.isEmpty(receiverStr)){
                    ToastManager.showToast(AddOrEditAddressActivity.this,"请输入收货人",Toast.LENGTH_SHORT);
                }else if(TextUtils.isEmpty(mobileStr)){
                    ToastManager.showToast(AddOrEditAddressActivity.this,"请输入收货人手机号",Toast.LENGTH_SHORT);
                }else if(TextUtils.isEmpty(localAreaStr)){
                    ToastManager.showToast(AddOrEditAddressActivity.this,"请选择收货地址",Toast.LENGTH_SHORT);
                }else if(TextUtils.isEmpty(detailAddressStr)){
                    ToastManager.showToast(AddOrEditAddressActivity.this,"请输入详细收货地址",Toast.LENGTH_SHORT);
                }else if(!NumUtil.isPhoneNum(mobileStr)){
                    ToastManager.showToast(AddOrEditAddressActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT);
                }
                Map<String,String> params = new HashMap<>();
                if(!TextUtils.isEmpty(id+"")){
                    params.put("id",id);
                }
                params.put("name",receiverStr);
                params.put("mobile",mobileStr);
                params.put("area",localAreaStr);
                params.put("address",detailAddressStr);
                params.put("is_default",isDefault?"1":"0");
                ctb_toolbar.iv_left_action1.setClickable(false);
                OkHttpManager.getInstance(AddOrEditAddressActivity.this).postAsyncHttp(HttpUrl.HTTP_EDIT_ADDRESS_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.post(()->ctb_toolbar.iv_left_action1.setClickable(true));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if(response.isSuccessful()){
                                String str = response.body().string();
                                ResponseBean<Object> data = getGson().fromJson(str,new TypeToken<ResponseBean<Object>>(){}.getType());
                                if(data.getCode()==200){
                                    handler.post(()->ToastManager.showToast(AddOrEditAddressActivity.this,"新增地址成功",Toast.LENGTH_SHORT));
                                    finish();
                                    return;
                                }
                                handler.post(()-> ToastManager.showToast(AddOrEditAddressActivity.this,data.getMsg(), Toast.LENGTH_SHORT));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            handler.post(()->ctb_toolbar.iv_left_action1.setClickable(true));
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_or_edit_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_address_set_default_address:
                checkBox.setChecked(!checkBox.isChecked());
                break;
        }
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.ll_edit_address_area:

                break;
        }
    }
}
