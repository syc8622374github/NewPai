package com.cyc.newpai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.me.entity.AddressBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.LocationHelper;
import com.cyc.newpai.util.SharePreUtil;
import com.cyc.newpai.widget.CountDownProgressView;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CountDownProgressView countDownView = findViewById(R.id.cdpv_splash_count_down);
        countDownView.setTimeMillis(5*1000);
        countDownView.setProgressListener(progress -> {
            if(progress==0){
                finish();
                MainActivity.startAct(this);
            }
        });
        countDownView.setOnClickListener(v -> {
            finish();
            MainActivity.startAct(this);
            countDownView.stop();
        });
        countDownView.start();
        requestPermission();
        initData();
    }

    /**
     * 初始化信息数据
     */
    private void initData() {
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_ADDRESS_LIST_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<AddressBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<AddressBean>>>(){}.getType());
                        if(data.getCode()==200&&data.getResult().getList().size()>0){
                            for(AddressBean addressBean : data.getResult().getList()){
                                if(addressBean.getIs_default().equals("1")?true:false){
                                    SharePreUtil.setPref(SplashActivity.this, Constant.DEFAULT_ADDRESS, GsonManager.getGson().toJson(addressBean));
                                }
                            }
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationHelper.BAIDU_READ_PHONE_STATE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
}
