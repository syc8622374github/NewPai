package com.cyc.newpai.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.MainActivity;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.DataGenerator;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.NumUtil;
import com.cyc.newpai.util.SharePreUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    public static final String TYPE_QUICK_LOGIN = "type_quick_login"; //快捷登录
    public static final String TYPE_USER_LOGIN = "type_user_login";  //用户登录
    private static final String TAG = LoginActivity.class.getName();

    private TabLayout tabLayout;
    private EditText etPhone;
    private EditText etPwd;
    private EditText etCode;
    private Button btnLogin;
    private TextView tvCode;
    private TextView tvResetPwd;
    private String selectTag;
    private int countDown = 60;

    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView tvSendSMS;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initLoginContent();
        initTab();
    }

    private void initLoginContent() {
        etPhone = findViewById(R.id.et_login_phone);
        etPwd = findViewById(R.id.et_login_pwd);
        etCode = findViewById(R.id.et_login_verification_code);
        //btnLogin = findViewById(R.id.btn_login);
        tvCode = findViewById(R.id.tv_login_send_check_code);
        tvResetPwd = findViewById(R.id.tv_login_reset_pwd);
        tvSendSMS = findViewById(R.id.tv_login_send_check_code);
    }

    private void initTab() {
        tabLayout = findViewById(R.id.tl_login_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelectedEvent(tab);
                updateView(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i = 0; i < DataGenerator.mLoginTabTitle.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(DataGenerator.getLoginTabView(this, i)));
        }
    }

    private void updateView(TabLayout.Tab tab) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView title = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tv_login_tab_title);
            if (i == tab.getPosition()) {
                title.setBackgroundResource(R.drawable.shape_border_bottom_line_red);
            } else {
                title.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
        selectTag = (String) tab.getCustomView().getTag();
        if (selectTag.equals(TYPE_QUICK_LOGIN)) {
            tvCode.setVisibility(View.VISIBLE);
            tvResetPwd.setVisibility(View.GONE);
            etCode.setVisibility(View.VISIBLE);
            etPwd.setVisibility(View.GONE);
        } else if (selectTag.equals(TYPE_USER_LOGIN)) {
            tvResetPwd.setVisibility(View.VISIBLE);
            tvCode.setVisibility(View.GONE);
            etCode.setVisibility(View.GONE);
            etPwd.setVisibility(View.VISIBLE);
        }
    }

    private void onTabSelectedEvent(TabLayout.Tab tab) {

    }

    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_login_reset_pwd:
                Intent intent = new Intent(this, RegisterAndResetPasswdActivity.class);
                intent.putExtra("type", RegisterAndResetPasswdActivity.TYPE_RESETPASSWD);
                intent.putExtra("phone", etPhone.getText().toString());
                startActivity(intent);
                break;
            case R.id.tv_login_register:
                Intent intentreg = new Intent(this, RegisterAndResetPasswdActivity.class);
                intentreg.putExtra("type", RegisterAndResetPasswdActivity.TYPE_REGISTER);
                startActivity(intentreg);
                break;
            case R.id.btn_login:
                checkDataAndUpdate();
                break;
            case R.id.tv_login_send_check_code:
                sendSMS();
                break;
        }
    }

    CountDownTimer timer = new CountDownTimer(60 * 1000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvSendSMS.setText(countDown--+"s");
        }

        @Override
        public void onFinish() {
            tvSendSMS.setText("重新发送");
            countDown = 60;
            tvSendSMS.setClickable(true);
        }
    };

    private void sendSMS() {
        String phone = etPhone.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            if(!NumUtil.isPhoneNum(phone)){
                ToastManager.showToast(getApplicationContext(),"请输入正确手机号码",Toast.LENGTH_LONG);
            }else{
                Map<String,String> params = new HashMap<>();
                params.put("mobile",phone);
                params.put("sms_type", Constant.SMS_TYPE_QUICKLOGIN+"");
                OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_SEND_SMS_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            if(response.isSuccessful()){
                                String str = response.body().string();
                                ResponseBean responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean>() {
                                }.getType());
                                handler.post(() -> ToastManager.showToast(getApplicationContext(),responseBean.getMsg(),Toast.LENGTH_SHORT));
                                if(responseBean.getCode()==200||responseBean.getMsg().equals("发送成功")){
                                    timer.start();
                                    tvSendSMS.setClickable(false);
                                }
                                return;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.post(() -> ToastManager.showToast(getApplicationContext(),"短信发送失败",Toast.LENGTH_SHORT));
                    }
                });
            }
        }else{
            ToastManager.showToast(getApplicationContext(),"请输入手机号码",Toast.LENGTH_LONG);
        }
    }

    private void checkDataAndUpdate() {
        if (selectTag.equals(TYPE_QUICK_LOGIN)) {
            String phone = etPhone.getText().toString();
            String code = etCode.getText().toString();
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
                if (!NumUtil.isPhoneNum(phone)) {
                    ToastManager.showToast(this, "请输入正确的手机号码", Toast.LENGTH_LONG);
                } else if (code.length() < 6) {
                    ToastManager.showToast(this, "请输入大于6位的密码", Toast.LENGTH_LONG);
                } else {
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", phone);
                    params.put("code", code);
                    OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_QUICK_LOGIN_URL, params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();

                                    ResponseBean<LoginBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<LoginBean>>() {
                                    }.getType());
                                    if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                        saveUserToken(responseBean.getResult());
                                    }
                                    handler.post(() -> ToastManager.showToast(LoginActivity.this, responseBean.getMsg(), Toast.LENGTH_LONG));
                                    return;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                handler.post(() -> ToastManager.showToast(LoginActivity.this, "登录异常", Toast.LENGTH_LONG));
                            }
                            handler.post(() -> ToastManager.showToast(LoginActivity.this, "登录失败", Toast.LENGTH_LONG));
                        }
                    });
                }
            } else {
                ToastManager.showToast(this, "请输入手机号与验证码", Toast.LENGTH_LONG);
            }
        } else if (selectTag.equals(TYPE_USER_LOGIN)) {
            String phone = etPhone.getText().toString();
            String pwd = etPwd.getText().toString();
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)) {
                if (!NumUtil.isPhoneNum(phone)) {
                    ToastManager.showToast(this, "请输入正确的手机号码", Toast.LENGTH_LONG);
                } else if (pwd.length() < 6) {
                    ToastManager.showToast(this, "请输入大于6位的密码", Toast.LENGTH_LONG);
                } else {
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", phone);
                    params.put("password", pwd);
                    OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_LOGIN_URL, params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();
                                    ResponseBean<LoginBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<LoginBean>>() {
                                    }.getType());
                                    handler.post(() -> ToastManager.showToast(LoginActivity.this, responseBean.getMsg(), Toast.LENGTH_LONG));
                                    if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                        saveUserToken(responseBean.getResult());
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                                handler.post(() -> ToastManager.showToast(LoginActivity.this, "登录异常", Toast.LENGTH_LONG));
                            }
                            handler.post(() -> ToastManager.showToast(LoginActivity.this, "登录失败", Toast.LENGTH_LONG));
                        }
                    });
                }
            } else {
                ToastManager.showToast(this, "请输入手机号与密码", Toast.LENGTH_LONG);
            }
        }
    }

    private void saveUserToken(LoginBean result) {
        SharePreUtil.setPref(this, Constant.TOKEN, result.getToken());
        SharePreUtil.setPref(this, Constant.UID, result.getUid());
        //handler.post(() -> ToastManager.showToast(getApplicationContext(), "uid:" + result.getUid() + " token:" + result.getUid(), Toast.LENGTH_LONG));
        MainActivity.startAct(this,true);
        finish();
    }
}
