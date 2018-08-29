package com.cyc.newpai.util;

import android.content.Context;

import com.cyc.newpai.http.OkHttpManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;

public class PayHelper {

    public static void pay(Context context, String url, List<String> keys, Map<String, String> params, Callback payCallBack){
        Collections.sort(keys);
        String keyStr = "";
        for(String key : keys){
            keyStr += params.get(key);
        }
        params.put("key",MD5Util.encryption(keyStr));
        OkHttpManager.getInstance(context).postAsyncHttp(url, params, payCallBack);
    }
}
