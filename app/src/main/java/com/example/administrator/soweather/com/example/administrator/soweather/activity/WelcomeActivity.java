package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/10/10.
 * WelcomeActivity
 * First enter the App
 */

public class WelcomeActivity extends Activity implements View.OnClickListener {
    private ImageView mWelcomeNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_welcome);
        initView();
        enterMain();
    }

    private void initView() {
        mWelcomeNext = (ImageView) findViewById(R.id.welcome_next);
        mWelcomeNext.setOnClickListener(this);
    }

    private void enterMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWelcomeNext.post(new Runnable() {
                    @Override
                    public void run() {
                        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        mShowAction.setDuration(500);
                        mWelcomeNext.setAnimation(mShowAction);
                        mWelcomeNext.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 800);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_next:
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
        }
    }
}
