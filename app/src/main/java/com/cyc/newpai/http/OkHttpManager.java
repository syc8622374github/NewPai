package com.cyc.newpai.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.SharePreUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpManager {

    private static final MediaType  MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType  MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
    private static final String TAG = OkHttpManager.class.getName();

    private static OkHttpClient mOkHttpClient;
    private static Context context;
    private static OkHttpManager mInstance;
    private Gson gson;

    private Handler handler = new Handler();

    private OkHttpManager(Context context) {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        this.context = context;
        gson = new Gson();

    }

    public static OkHttpManager getInstance(Context context) {
        OkHttpManager inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    public void postAsyncHttp(String url,Map<String,String> params,Callback callback){
        String paramsStr = "";
        if(params!=null){
            Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String>  entry = iterator.next();
                paramsStr += entry.getKey()+"="+entry.getValue()+"&";
            }
        }
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_FORM,paramsStr);
        Request.Builder builder = new Request.Builder().url(url);
        Request request = addHeaders(builder,getHeaders()).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void postOfFileAsyncHttp(String url, Map<String,String> params, Map<String,File> files, Callback callback){
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if(params!=null){
            Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String>  entry = iterator.next();
                multipartBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }
        if(files!=null){
            Iterator<Map.Entry<String,File>> iterator = files.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,File>  entry = iterator.next();
                multipartBodyBuilder.addFormDataPart(entry.getKey()
                        ,entry.getValue().getName()
                        ,RequestBody.create(MediaType.parse("image/png")
                                ,entry.getValue()));
            }
        }
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder builder = new Request.Builder().url(url);
        Request request = addHeaders(builder,getHeaders()).post(requestBody).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public Map<String,String> getHeaders(){
        Map<String,String> headers = defaultHeader();
        return headers;
    }

    private Map<String, String> defaultHeader() {
        Map<String,String> headers = new HashMap<>();
        //headers.put("uid",HttpUrl.UID);
        //headers.put("token",HttpUrl.TOKEN);
        headers.put("uid", SharePreUtil.getPref(context, Constant.UID,""));
        headers.put("token",SharePreUtil.getPref(context, Constant.TOKEN,""));
        return headers;
    }

    private Request.Builder addHeaders(Request.Builder builder,Map<String,String> headers) {

        //addHeader，可添加多个请求头  header，唯一，会覆盖
        Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addHeader(key,value);
        }
        /*builder.addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0")
                .header("sid", "eyJhZGRDaGFubmVsIjoiYXBwIiwiYWRkUHJvZHVjdCI6InFia3BsdXMiLCJhZGRUaW1lIjoxNTAzOTk1NDQxOTEzLCJyb2xlIjoiUk9MRV9VU0VSIiwidXBkYXRlVGltZSI6MTUwMzk5NTQ0MTkxMywidXNlcklkIjoxNjQxMTQ3fQ==.b0e5fd6266ab475919ee810a82028c0ddce3f5a0e1faf5b5e423fb2aaf05ffbf");*/
        return builder;
    }
}
