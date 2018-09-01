package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.common.BaseWebViewActivity;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.common.WeiChatActivity;
import com.cyc.newpai.ui.common.adapter.PayMethodRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.PayMethodBean;
import com.cyc.newpai.ui.common.entity.RechargeDetailBean;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.ui.me.entity.OrderDetailResultBean;
import com.cyc.newpai.ui.me.entity.SubmitOrderResultBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.SharePreUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SubmitOrderActivity extends BaseActivity {

    private static final String TAG = SubmitOrderActivity.class.getSimpleName();
    private TextView title;
    private TextView nowPrice;
    private LinearLayout llAddAddress;
    private ImageView shopIcon;
    private TextView realPrice;
    private TextView payPrice;
    private TextView freight;
    private TextView coupon;
    private OrderDetailResultBean orderDetailResultBean;
    private MyAuctionBean myAuctionBean;
    private TextView contact;
    private TextView phone;
    private TextView address;
    private String defaultAddress;
    private TextView realPayPrice;
    private AddressBean selectAddr;
    private String payType="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailResultBean = (OrderDetailResultBean) getIntent().getSerializableExtra(OrderDetailActivity.ORDER_DETAIL_BEAN);
        myAuctionBean = (MyAuctionBean) getIntent().getSerializableExtra(OrderDetailActivity.ORDER_DATA_BEAN);
        initView();
        initData();
    }

    private void initData() {
        defaultAddress = SharePreUtil.getPref(this, Constant.DEFAULT_ADDRESS,"");
        if(selectAddr!=null){
            contact.setText("收货人:"+selectAddr.getName());
            phone.setVisibility(View.VISIBLE);
            phone.setText(selectAddr.getMobile());
            address.setText(selectAddr.getArea()+selectAddr.getAddress());
        }else{
            if(!TextUtils.isEmpty(defaultAddress)){
                AddressBean addressBean = getGson().fromJson(defaultAddress,new TypeToken<AddressBean>(){}.getType());
                contact.setText("收货人:"+addressBean.getName());
                phone.setVisibility(View.VISIBLE);
                phone.setText(addressBean.getMobile());
                address.setText(addressBean.getArea()+addressBean.getAddress());
            }else{
                contact.setText("新增收货地址");
                address.setText("下单后我们会尽快为您发货");
                phone.setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        initPayMethod();
        llAddAddress = findViewById(R.id.ll_submit_order_add_str);
        realPrice = findViewById(R.id.tv_submit_order_real_price);
        nowPrice = findViewById(R.id.tv_submit_order_now_price);
        shopIcon = findViewById(R.id.iv_submit_order_icon);
        title = findViewById(R.id.tv_submit_order_title);
        payPrice = findViewById(R.id.tv_submit_order_pay_price);
        freight = findViewById(R.id.tv_submit_order_freight);
        coupon = findViewById(R.id.tv_submit_order_coupon);
        contact = findViewById(R.id.tv_submit_order_address_contact);
        phone = findViewById(R.id.tv_submit_order_address_phone);
        address = findViewById(R.id.tv_submit_order_address_detail);
        realPayPrice = findViewById(R.id.tv_submit_order_real_pay_price);
        if(orderDetailResultBean!=null){
            realPrice.setText(orderDetailResultBean.getDeal_price());
            nowPrice.setText(orderDetailResultBean.getDeal_price());
            GlideApp.with(this).load(orderDetailResultBean.getImage()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
            title.setText(orderDetailResultBean.getGoods_name());
            Float fNowPrice = Float.valueOf(nowPrice.getText().toString());
            Float fFreight = Float.valueOf(freight.getText().toString());
            Float fCoupon = Float.valueOf(coupon.getText().toString());
            payPrice.setText("￥"+(fNowPrice+fFreight-fCoupon));
            realPayPrice.setText(payPrice.getText());
        }
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
        payType = "1";
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_wechat,"微信支付",false));
        payMethodRecyclerViewAdapter.setListNotify(payMethodBeans);
        payMethodRecyclerViewAdapter.setOnClickItemListener((view, itemBean, position) -> {
            for(int i=0;i<payMethodBeans.size();i++){
                if(i==position){
                    payMethodBeans.get(i).setCheck(true);
                    if(payMethodBeans.get(i).getPayMethod().equals("支付宝支付")){
                        payType = "1";
                    }else{
                        payType = "2";
                    }
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
            case R.id.btn_submit_order_ok:
                Map<String,String> params = new HashMap<>();
                params.put("shopid",myAuctionBean.getId());
                params.put("addr_name",contact.getText().toString());
                params.put("addr_mobile",phone.getText().toString());
                params.put("address",address.getText().toString());
                params.put("pay_type",payType);
                OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_PAY_SUBMIT_ORDER_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String str = response.body().string();
                            Log.i(TAG,str);
                            ResponseBean<ResponseResultBean<RechargeDetailBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<RechargeDetailBean>>>(){}.getType());
                            if(data.getCode()==200){
                                if(payType.equals("1")){
                                    Intent intent = new Intent(SubmitOrderActivity.this,BaseWebViewActivity.class);
                                    intent.putExtra(BaseWebViewActivity.REQUEST_STATUS,BaseWebViewActivity.STATUS_RECHARGE);
                                    intent.putExtra(BaseWebViewActivity.STATUS_RECHARGE_DATA,data.getResult().getData());
                                    intent.putExtra(BaseWebViewActivity.REQUEST_URL,data.getResult().getData().getQrcode());
                                    startActivity(intent);
                                }else if(payType.equals("2")){
                                    Intent intent = new Intent(SubmitOrderActivity.this,WeiChatActivity.class);
                                    intent.putExtra(WeiChatActivity.TYPE_DATA,data.getResult().getData());
                                    startActivity(intent);
                                }
                            }
                            handler.post(()->ToastManager.showToast(SubmitOrderActivity.this,data.getMsg(), Toast.LENGTH_SHORT));
                        }
                    }
                });
                break;
            case R.id.ll_submit_order_add_str:
                Intent intent = new Intent(this,SelectAddressActivity.class);
                intent.putExtra(SelectAddressActivity.TYPE_ADDRESS,SelectAddressActivity.TYPE_SELECT_ADDRESS);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            selectAddr = (AddressBean) data.getSerializableExtra(SelectAddressActivity.TYPE_SELECT_DATA);
            initData();
        }
    }
}
