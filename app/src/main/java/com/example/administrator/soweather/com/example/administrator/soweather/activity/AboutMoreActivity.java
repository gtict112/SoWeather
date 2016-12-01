package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/11/18.
 */

public class AboutMoreActivity extends Activity implements View.OnClickListener {
    private TextView about;
    private String type;
    private TextView tvTop;
    private ImageView tvButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_more);
        initView();
    }

    private void initView() {
        about = (TextView) findViewById(R.id.about);
        tvTop = (TextView) findViewById(R.id.topTv);
        tvButton = (ImageView) findViewById(R.id.topButton);
        tvButton.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            if (type.equals("1")) {
                about.setVisibility(View.VISIBLE);
                tvTop.setText("产品介绍");
            }
            if (type.equals("2")) {
                about.setVisibility(View.GONE);
                String url = "https://github.com/lihailin3519/SoWeather";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url));
                tvTop.setText("项目github地址");
                startActivity(intent2);
                finish();
            }
            if (type.equals("3")) {
                about.setVisibility(View.GONE);
                String url = "https://github.com/lihailin3519/SoWeather";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url));
                tvTop.setText("项目csdn地址");
                startActivity(intent2);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                break;
        }
    }
}