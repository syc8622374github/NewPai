package com.cyc.newpai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.util.LocationHelper;
import com.cyc.newpai.widget.CountDownProgressView;

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
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocationHelper.BAIDU_READ_PHONE_STATE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
}
