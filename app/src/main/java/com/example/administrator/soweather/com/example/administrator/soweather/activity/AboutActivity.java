package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;

/**
 * Created by Administrator on 2016/11/10.
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout product_adout;
    private LinearLayout github;
    private LinearLayout csdn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initView();
    }


    private void initView() {
        setDisplayHomeAsUpEnabled(true);
        product_adout = (LinearLayout) findViewById(R.id.product_adout);
        github = (LinearLayout) findViewById(R.id.github);
        csdn = (LinearLayout) findViewById(R.id.csdn);
        product_adout.setOnClickListener(this);
        github.setOnClickListener(this);
        csdn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_adout:
                Intent intent = new Intent(this, AboutMoreActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.github:
                Intent intent2 = new Intent(this, AboutMoreActivity.class);
                intent2.putExtra("type", "2");
                startActivity(intent2);
                break;
            case R.id.csdn:
                Intent intent3 = new Intent(this, AboutMoreActivity.class);
                intent3.putExtra("type", "3");
                startActivity(intent3);
                break;
        }
    }
}
