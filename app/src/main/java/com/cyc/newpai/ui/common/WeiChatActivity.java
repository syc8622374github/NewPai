package com.cyc.newpai.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.common.entity.RechargeDetailBean;
import com.cyc.newpai.ui.me.PaySuccessActivity;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeiChatActivity extends BaseActivity {

    public static final String TYPE_DATA = "type_data";
    private static final String TAG = WeiChatActivity.class.getSimpleName();
    private RechargeDetailBean rechargeDetailBean;
    private TextView price;
    private ImageView qrcode;
    private TextView countDown;
    private TextView orderId;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rechargeDetailBean = (RechargeDetailBean) getIntent().getSerializableExtra(TYPE_DATA);
        initView();
        if(timer==null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        Map<String,String> params = new HashMap<>();
                        params.put("orderid",rechargeDetailBean.getOrderid());
                        OkHttpManager.getInstance(WeiChatActivity.this).postAsyncHttp(HttpUrl.HTTP_RECHARGE_STATUS_URL,params,new Callback(){

                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    if(response.isSuccessful()){
                                        String str = response.body().string();
                                        ResponseBean<Map<String,String>> data = getGson().fromJson(str,new TypeToken<ResponseBean<Map<String,String>>>(){}.getType());
                                        if(data.getResult().get("order_code").equals("success")){
                                            Intent intent = new Intent(WeiChatActivity.this, PaySuccessActivity.class);
                                            intent.putExtra(BaseWebViewActivity.REQUEST_STATUS,"2");
                                            intent.putExtra(BaseWebViewActivity.STATUS_RECHARGE_DATA,rechargeDetailBean);
                                            startActivity(intent);
                                            timer.cancel();
                                        }
                                        Log.i(TAG,data.getResult().toString());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    });
                }
            },2000,1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    private void initView() {
        price = findViewById(R.id.tv_weichat_price);
        qrcode = findViewById(R.id.iv_weichat_qrcode);
        countDown = findViewById(R.id.tv_weichat_expired_count_down);
        orderId = findViewById(R.id.tv_weichat_orderid);
        if(rechargeDetailBean!=null){
            price.setText("￥"+rechargeDetailBean.getRealprice().toString());
            GlideApp.with(this).load("http://mobile.qq.com/qrcode?url="+rechargeDetailBean.getQrcode()).placeholder(R.mipmap.ic_launcher).into(qrcode);
            orderId.setText("订单："+rechargeDetailBean.getOrderid());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wei_chat;
    }
}
