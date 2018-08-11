package com.cyc.newpai.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.MainActivity;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.util.Constant;
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


public class RegisterAndResetPasswdActivity extends BaseActivity implements View.OnClickListener{

    public static final String TYPE_REGISTER = "type_register";
    public static final String TYPE_RESETPASSWD = "type_reset_passwd";
    private static final String TAG = RegisterAndResetPasswdActivity.class.getName();
    private static String HTTP_URL = "";
    private String type;
    private Button btnSend;
    private LinearLayout llUserTerms;
    private TextView tvUserTerms;
    private TextView tvSendSMS;
    private int countDown = 60;
    private ImageView ivPwdLogo;
    private EditText etPwd;
    private EditText etPhone;
    private EditText etCode;
    private String phone;
    private int sendSmsType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_reset_passwd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSet();
    }

    private void initSet() {
        type = getIntent().getStringExtra("type");
        switch (type){
            case TYPE_REGISTER:
                ctb_toolbar.setTitle("注册");
                ctb_toolbar.tv_title.setTextColor(Color.BLACK);
                btnSend.setText("注册");
                llUserTerms.setVisibility(View.VISIBLE);
                HTTP_URL = HttpUrl.HTTP_REGISTER＿URL;
                sendSmsType = Constant.SMS_TYPE_REGISTER;
                break;
            case TYPE_RESETPASSWD:
                phone = getIntent().getStringExtra("phone");
                etPhone.setText(phone);
                ctb_toolbar.setTitle("重置密码");
                ctb_toolbar.tv_title.setTextColor(Color.BLACK);
                btnSend.setText("重置密码");
                llUserTerms.setVisibility(View.GONE);
                HTTP_URL = HttpUrl.HTTP_RESET_PWD_URL;
                sendSmsType = Constant.SMS_TYPE_RESETPWD;
                break;
        }
    }

    private void initView() {
        etPhone = findViewById(R.id.et_register_phone);
        etCode = findViewById(R.id.et_register_code);
        btnSend = findViewById(R.id.btn_register_send);
        btnSend.setOnClickListener(this);
        llUserTerms = findViewById(R.id.ll_user_protocol);
        tvUserTerms = findViewById(R.id.tv_user_protocol);
        ivPwdLogo = findViewById(R.id.iv_visible_pwd_logo);
        ivPwdLogo.setOnClickListener(this);
        tvUserTerms.setOnClickListener(this);
        tvSendSMS = findViewById(R.id.tv_send_sms);
        tvSendSMS.setOnClickListener(this);
        etPwd = findViewById(R.id.et_register_pwd);
        etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_user_protocol:
                startActivity(new Intent(this,UserProtocolActivity.class));
                break;
            case R.id.tv_send_sms:
                sendSMS();
                break;
            case R.id.iv_visible_pwd_logo:
                int inputType = etPwd.getInputType();
                switch (inputType){
                    case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD:
                        ivPwdLogo.setImageResource(R.drawable.ic_pwd_visible);
                        etPwd.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPwd.setSelection(etPwd.getEditableText().length());
                        break;
                    case EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                        ivPwdLogo.setImageResource(R.drawable.ic_pwd_invisible);
                        etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPwd.setSelection(etPwd.getEditableText().length());
                        break;
                }
                break;
            case R.id.btn_register_send:
                checkDataAndSendHttp();
                break;
        }
    }

    private void sendSMS() {
        phone = etPhone.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            if(!NumUtil.isPhoneNum(phone)){
                ToastManager.showToast(getApplicationContext(),"请输入正确手机号码",Toast.LENGTH_LONG);
            }else{
                Map<String,String> params = new HashMap<>();
                params.put("mobile",phone);
                params.put("sms_type", sendSmsType+"");
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

    private void checkDataAndSendHttp() {
        phone = etPhone.getText().toString();
        String pwd = etPwd.getText().toString();
        String code = etCode.getText().toString();
        if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(code)){
            if(!NumUtil.isPhoneNum(phone)){
                ToastManager.showToast(getApplicationContext(),"请输入正确手机号码",Toast.LENGTH_LONG);
            }else{
                Map<String,String> params = new HashMap<>();
                params.put("mobile",phone);
                params.put("password",pwd);
                params.put("code",code);
                OkHttpManager.getInstance(this).postAsyncHttp(HTTP_URL, params, new Callback() {
                    ResponseBean responseBean = null;
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String str = response.body().string();
                                if(sendSmsType == Constant.SMS_TYPE_REGISTER){
                                    responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<LoginBean>>() {
                                    }.getType());
                                }else if(sendSmsType == Constant.SMS_TYPE_RESETPWD){
                                    responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean>() {
                                    }.getType());
                                }
                                if(responseBean!=null){
                                    handler.post(() -> ToastManager.showToast(RegisterAndResetPasswdActivity.this,responseBean.getMsg(),Toast.LENGTH_LONG));
                                    if(responseBean.getCode()==200&&responseBean.getMsg().equals("注册成功")){
                                        saveUserToken((LoginBean) responseBean.getResult());
                                    }else if(responseBean.getCode()==200&&responseBean.getMsg().equals("修改成功")){
                                        reLogin();
                                    }
                                    return;
                                }
                            }
                            handler.post(() -> ToastManager.showToast(RegisterAndResetPasswdActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            ToastManager.showToast(getApplicationContext(),"请输入必要信息", Toast.LENGTH_LONG);
        }
    }

    private void reLogin() {
        SharePreUtil.setPref(this,Constant.TOKEN,"");
        SharePreUtil.setPref(this,Constant.UID,"");
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void saveUserToken(LoginBean result) {
        SharePreUtil.setPref(this, Constant.TOKEN, result.getToken());
        SharePreUtil.setPref(this, Constant.UID, result.getUid());
        //handler.post(() -> ToastManager.showToast(getApplicationContext(), "uid:" + result.getUid() + " token:" + result.getUid(), Toast.LENGTH_LONG));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

