package com.cyc.newpai.util;

import android.content.Context;
import android.text.TextUtils;

public class LoginUtil {
    public static boolean isLogin(Context context){
        String uid = SharePreUtil.getPref(context,Constant.UID,"");
        String token = SharePreUtil.getPref(context,Constant.TOKEN,"");
        if(!TextUtils.isEmpty(uid)&&!TextUtils.isEmpty(token)){
            return true;
        }
        return false;
    }

}
