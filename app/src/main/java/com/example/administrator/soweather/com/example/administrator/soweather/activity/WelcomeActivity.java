package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/10/10.
 * WelcomeActivity
 * First enter the App
 */

public class WelcomeActivity extends Activity {
    private String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将屏幕设置为全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        webView = (WebView) findViewById(R.id.wv_webview);
        url = "file:///android_asset/guide/index.html";
        loadLocalHtml(url);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadLocalHtml(String url) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //重写此方法，用于捕捉页面上的跳转链接
                if ("http://start/".equals(url)) {
                    //在html代码中的按钮跳转地址需要同此地址一致
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.inuptodown,R.anim.indowntoup);
                    finish();
                }
                return true;
            }
        });
        webView.loadUrl(url);
    }
}
