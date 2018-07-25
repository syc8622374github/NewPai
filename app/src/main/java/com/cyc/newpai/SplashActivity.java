package com.cyc.newpai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.widget.CountDownProgressView;

import java.util.Timer;

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
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        });
        countDownView.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        });
        countDownView.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
}
