package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HelpFeedbackActivity extends BaseActivity implements View.OnClickListener {
    private TextView topTv;
    private ImageView topButton;
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
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        mWebView = (WebView) findViewById(R.id.webview);
        topTv.setText("留言与反馈");
        topButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                break;
            default:
                break;
        }
    }
}
