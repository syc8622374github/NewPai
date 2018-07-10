package com.cyc.newpai.ui.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class BaseWebViewActivity extends BaseActivity{

    public static final String REQUEST_URL = "request_url";

    private WebView webView;

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
        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
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
        webView.loadUrl(url);
    }
}