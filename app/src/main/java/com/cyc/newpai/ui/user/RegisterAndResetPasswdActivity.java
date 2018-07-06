package com.cyc.newpai.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;


public class RegisterAndResetPasswdActivity extends BaseActivity{

    public static final String TYPE_REGISTER = "type_register";
    public static final String TYPE_RESETPASSWD = "type_reset_passwd";
    private String type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_reset_passwd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        switch (type){
            case TYPE_REGISTER:
                ctb_toolbar.setTitle("注册");
                break;
            case TYPE_RESETPASSWD:
                ctb_toolbar.setTitle("重置密码");
                break;
        }
    }

}

