package com.lhlSo.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.lhlSo.soweather.R;
import com.lhlSo.soweather.ui.base.BaseActivity;
import com.lhlSo.soweather.utils.ShareUtils;
import com.lhlSo.soweather.utils.WebUtils;

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
        Button btn_check_update = (Button) findViewById(R.id.btn_check_update);
        btn_check_update.setOnClickListener(this);
        Button btn_feedback = (Button) findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web_home:
                WebUtils.openInternal(this, "https://github.com/lihailin3519/SoWeather");
                break;
            case R.id.btn_share_app:
                ShareUtils.shareText(this, "YOYO天气！https://github.com/lihailin3519/SoWeather");
                break;
            case R.id.btn_check_update:
                Snackbar.make(AboutMoreActivity.this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "当前已是最新版本! (*^__^*)", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.btn_feedback:
                Intent intent = new Intent(this, HelpFeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}
