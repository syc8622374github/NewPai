package com.cyc.newpai.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
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

public class LoginActivity extends BaseActivity{

    public static final String TYPE_QUICK_LOGIN = "type_quick_login"; //快捷登录
    public static final String TYPE_USER_LOGIN = "type_user_login";  //用户登录

    private TabLayout tabLayout;
    private EditText etPhone;
    private EditText etPwd;
    private EditText etCode;
    private Button btnLogin;
    private TextView tvCode;
    private TextView tvResetPwd;
    private String selectTag;

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
        btnLogin = findViewById(R.id.btn_login);
        tvCode = findViewById(R.id.tv_login_send_check_code);
        tvResetPwd = findViewById(R.id.tv_login_reset_pwd);
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
        for(int i=0;i<DataGenerator.mLoginTabTitle.length;i++){
            tabLayout.addTab(tabLayout.newTab().setCustomView(DataGenerator.getLoginTabView(this,i)));
        }
    }

    private void updateView(TabLayout.Tab tab) {
        for(int i=0;i<tabLayout.getTabCount();i++){
            TextView title = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tv_login_tab_title);
            if(i == tab.getPosition()){
                title.setBackgroundResource(R.drawable.shape_border_bottom_line_red);
            }else{
                title.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
        selectTag = (String) tab.getCustomView().getTag();
        if(selectTag.equals(TYPE_QUICK_LOGIN)){
            tvCode.setVisibility(View.VISIBLE);
            tvResetPwd.setVisibility(View.GONE);
            etCode.setVisibility(View.VISIBLE);
            etPwd.setVisibility(View.GONE);
        }else if(selectTag.equals(TYPE_USER_LOGIN)){
            tvResetPwd.setVisibility(View.VISIBLE);
            tvCode.setVisibility(View.GONE);
            etCode.setVisibility(View.GONE);
            etPwd.setVisibility(View.VISIBLE);
        }
    }

    private void onTabSelectedEvent(TabLayout.Tab tab) {

    }

    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.tv_login_reset_pwd:
                Intent intent = new Intent(this,RegisterAndResetPasswdActivity.class);
                intent.putExtra("type",RegisterAndResetPasswdActivity.TYPE_RESETPASSWD);
                startActivity(intent);
                break;
            case R.id.tv_login_register:
                Intent intentreg = new Intent(this,RegisterAndResetPasswdActivity.class);
                intentreg.putExtra("type",RegisterAndResetPasswdActivity.TYPE_REGISTER);
                startActivity(intentreg);
                break;
            case R.id.btn_login:
                checkDataAndUpdate();
                break;
        }
    }

    private void checkDataAndUpdate() {
        if(selectTag.equals(TYPE_QUICK_LOGIN)){
            String phone = etPhone.getText().toString();
            String code = etCode.getText().toString();
        }else if(selectTag.equals(TYPE_USER_LOGIN)){
            String phone = etPhone.getText().toString();
            String pwd = etPwd.getText().toString();
            if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(pwd)){
                if(!NumUtil.isPhoneNum(phone)){
                    ToastManager.showToast(this,"请输入正确的手机号码",Toast.LENGTH_LONG);
                }else if(pwd.length()<6){
                    ToastManager.showToast(this,"请输入大于6位的密码",Toast.LENGTH_LONG);
                }else{
                    Map<String,String> params = new HashMap<>();
                    params.put("mobile",phone);
                    params.put("password",pwd);
                    OkHttpManager.getInstance(this).postNewPaiInterfaceAynsHttp(HttpUrl.HTTP_LOGIN_URL, params, new OkHttpManager.HttpCallBack<LoginBean>() {
                        @Override
                        public void onFailed(Call call, IOException e) {

                        }

                        @Override
                        public void onSucessed(LoginBean loginBean) {
                            saveUserToken(loginBean);
                        }
                    });
                }
            }else{
                ToastManager.showToast(this,"请输入手机号与密码",Toast.LENGTH_LONG);
            }
        }
    }

    private void saveUserToken(LoginBean result) {
        SharePreUtil.setPref(this, Constant.TOKEN,result.getToken());
        SharePreUtil.setPref(this,Constant.UID,result.getUid());
        ToastManager.showToast(this,"uid:"+result.getUid()+" token:"+Constant.TOKEN,Toast.LENGTH_LONG);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
