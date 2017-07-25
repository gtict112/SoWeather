package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.ui.base.BaseActivity;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HelpFeedbackActivity extends BaseActivity{
    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_feedback;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initView();
        String encoding = "UTF-8";
        String mimeType = "text/html";
        final String html =
                "<a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=fUROSE5LTERNTT0MDFMeEhA\" " +
                        "style=\"text-decoration:none;\"><img src=\"http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png\"/>" +
                        "</a>";
        mWebView.loadDataWithBaseURL("file://", html, mimeType, encoding, "about:blank");
    }


    private void initView() {
        setDisplayHomeAsUpEnabled(true);
        mWebView = (WebView) findViewById(R.id.webview);
    }

}
