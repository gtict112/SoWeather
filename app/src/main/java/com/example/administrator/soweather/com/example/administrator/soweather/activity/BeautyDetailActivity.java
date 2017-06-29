package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;

/**
 * Created by Administrator on 2017/6/28.
 */

public class BeautyDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView src;
    private String img_url;
    private ImageView topButton;
    private TextView topTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_beauty_detail;
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
        src = (ImageView) findViewById(R.id.src);
        topButton = (ImageView) findViewById(R.id.topButton);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("详情");
        topButton.setOnClickListener(this);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        Intent intent = getIntent();
        if (intent != null) {
            img_url = intent.getStringExtra("id");
        }
        Glide.with(this).load(img_url).animate(R.anim.img_loading).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_loading_eholder).override(width + 40, height).centerCrop()
                .into(src);
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
