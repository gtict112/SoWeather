package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.WebUtils;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/18.
 */

public class AboutMoreActivity extends BaseActivity implements View.OnClickListener {
    private ImageSwitcher imageSwitcher;
    private int Iindex = 0;
    private int[] images = new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_more;
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
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(AboutMoreActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.span_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.span_out));
        imageSwitcher.setImageResource(images[0]);
        //定时执行图片切换
        imageSwitcher.postDelayed(new Runnable() {
            public void run() {
                if (Iindex == images.length - 1) {
                    Iindex = 0;
                } else {
                    Iindex++;
                }
                imageSwitcher.setImageResource(images[Iindex]);
                imageSwitcher.postDelayed(this, 2200);
            }
        }, 2200);
        Button btn_web_home = (Button) findViewById(R.id.btn_web_home);
        btn_web_home.setOnClickListener(this);
        Button btn_share_app = (Button) findViewById(R.id.btn_share_app);
        btn_share_app.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web_home:
                WebUtils.openInternal(this, "https://github.com/lihailin3519/SoWeather");
                break;
            case R.id.btn_share_app:
                Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "暂不支持分享! (*^__^*)", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
