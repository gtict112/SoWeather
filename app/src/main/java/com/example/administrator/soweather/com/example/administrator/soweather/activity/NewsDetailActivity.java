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
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;

/**
 * Created by Administrator on 2016/12/7.
 */

public class NewsDetailActivity extends BaseActivity {
    private WebView content;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        initview();
    }

    private void initview() {
        content = (WebView) findViewById(R.id.content);
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            loadLocalHtml(url);
        }
    }
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadLocalHtml(String url) {
        WebSettings ws = content.getSettings();
        ws.setJavaScriptEnabled(true);
        content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        content.loadUrl(url);
    }
}
