package com.example.administrator.soweather.com.example.administrator.soweather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;

/**
 * Created by Administrator on 2017/5/18.
 */

public class BaseActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appconfiguration.getInstance().addActivity(this);
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
