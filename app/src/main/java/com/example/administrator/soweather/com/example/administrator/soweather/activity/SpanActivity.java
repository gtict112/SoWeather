package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SpanActivity extends FragmentActivity {
    final Appconfiguration appConfig = Appconfiguration.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appConfig.getActivitySet().size() > 0) {
                    Intent intent2 = new Intent(SpanActivity.this, appConfig.getFrontActivityList().get(appConfig.getFrontActivityList().size() - 1).getClass());
                    startActivity(intent2);
                    overridePendingTransition(R.anim.span_in,
                            R.anim.span_out);
                    finish();
                } else {
                    Intent intent = new Intent(SpanActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.span_in,
                            R.anim.span_out);
                    finish();
                }
            }
        }, 1000);
    }
}
