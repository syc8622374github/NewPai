package com.cyc.newpai.ui.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.common.entity.RechargeDetailBean;
import com.cyc.newpai.ui.me.PaySuccessActivity;
import com.cyc.newpai.util.DialogUtil;
import com.cyc.newpai.util.IntentUtils;
import com.cyc.newpai.util.UrlUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BaseWebViewActivity extends BaseActivity{

    public static final String REQUEST_URL = "request_url";
    public static final String REQUEST_DATA = "request_data";
    public static final String REQUEST_STATUS = "request_status";
    public static final String STATUS_RECHARGE = "status_recharge";
    public static final String STATUS_RECHARGE_DATA = "status_recharge_data";
    private static final String TAG = BaseWebViewActivity.class.getSimpleName();
    private Timer timer;
    private WebView webView;
    private String status;
    private RechargeDetailBean rechargeDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_webview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = findViewById(R.id.wv_webview);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();//设置定位的数据库路径
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();// h5 缓存路径
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染优先级
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);//允许通过file url加载的Javascript可以访问其他的源
            webView.getSettings().setAllowFileAccessFromFileURLs(true);//允许通过file url加载的Javascript读取其他的本地文件
        }
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);//缓存空间大小
        webView.getSettings().setGeolocationDatabasePath(dir);//设置定位数据库缓存路径
        webView.getSettings().setAppCachePath(appCachePath);// 设置数据库缓存路径
        webView.getSettings().setLoadWithOverviewMode(true);//设置webview自适应屏幕大小
        webView.getSettings().setGeolocationEnabled(true);//启用地理定位
        webView.getSettings().setJavaScriptEnabled(true);//允许使用js脚本
        webView.getSettings().setDomStorageEnabled(true);//设置可以使用localStorage
        webView.getSettings().setAppCacheEnabled(true);//设置H5的缓存打开,默认关闭
        webView.getSettings().setDatabaseEnabled(true);//开启数据库缓存
        webView.getSettings().setAllowFileAccess(true);//允许WebView使用File协议
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 请求定位权限默认同意
             * @param origin
             * @param callback
             */
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (url.equals("about:blank")) {
                    return false;
                }
                final Uri uri = Uri.parse(url);
                if (!UrlUtils.isSupportedProtocol(uri.getScheme()) &&
                        IntentUtils.handleExternalUri(view.getContext(),  view, url)) {
                    switch (status){
                        case STATUS_RECHARGE:
                            ProgressDialog progressDialog = DialogUtil.getCustomProgressDialog(BaseWebViewActivity.this,"充值中...");
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(() -> {
                                        Map<String,String> params = new HashMap<>();
                                        params.put("orderid",rechargeDetailBean.getOrderid());
                                        OkHttpManager.getInstance(BaseWebViewActivity.this).postAsyncHttp(HttpUrl.HTTP_RECHARGE_STATUS_URL,params,new Callback(){

                                            @Override
                                            public void onFailure(Call call, IOException e) {

                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                try {
                                                    if(response.isSuccessful()){
                                                        String str = response.body().string();
                                                        ResponseBean<Map<String,String>> data = getGson().fromJson(str,new TypeToken<ResponseBean<Map<String,String>>>(){}.getType());
                                                        if(data.getResult().get("order_code").equals("success")){
                                                            Intent intent = new Intent(BaseWebViewActivity.this, PaySuccessActivity.class);
                                                            intent.putExtra(REQUEST_STATUS,status);
                                                            intent.putExtra(STATUS_RECHARGE_DATA,rechargeDetailBean);
                                                            startActivity(intent);
                                                            progressDialog.cancel();
                                                            timer.cancel();
                                                        }
                                                        Log.i(TAG,data.getResult().toString());
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    });
                                }
                            },1000,1000);
                            break;
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(final WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//当ssl验证出错时接受证书，不阻止页面运行。
            }

            /**
             * 拦截请求
             * @param view
             * @param url
             * @return
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
                return super.shouldInterceptRequest(view,url);
            }
        });
        String url = getIntent().getStringExtra(REQUEST_URL);
        String requestData = getIntent().getStringExtra(REQUEST_DATA);
        status = getIntent().getStringExtra(REQUEST_STATUS);
        if(!TextUtils.isEmpty(status)&&status.equals(STATUS_RECHARGE)){
            rechargeDetailBean = (RechargeDetailBean) getIntent().getSerializableExtra(STATUS_RECHARGE_DATA);
            if(rechargeDetailBean!=null&&rechargeDetailBean.getIstype().equals("2")){
                url = "http://mobile.qq.com/qrcode?url=" + url;
            }
        }
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }else if(!TextUtils.isEmpty(requestData)){
            webView.loadData(requestData,"text/html", "UTF-8");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }
}