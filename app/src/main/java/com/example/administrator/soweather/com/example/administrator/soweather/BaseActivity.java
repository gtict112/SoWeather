package com.example.administrator.soweather.com.example.administrator.soweather;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.feng.skin.manager.base.BaseSkinFragmentActivity;

/**
 * Created by Administrator on 2017/5/18.
 */

public class BaseActivity extends BaseSkinFragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appconfiguration.getInstance().addActivity(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#75ADA2"));
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    protected void onStart() {
        super.onStart();
        Appconfiguration.getInstance().getFrontActivityList().add(this);
    }

    protected void onStop() {
        super.onStop();
        Appconfiguration.getInstance().getFrontActivityList().remove(this);

    }

    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
        }
        Appconfiguration.getInstance().removeActivity(this);
    }
}
