package com.lhlSo.soweather.core;

import android.app.Application;



/**
 * Created by Administrator on 2016/11/14.
 */

public class SoWeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Appconfiguration config = Appconfiguration.getInstance();
        config.initGeneralPreferences(getApplicationContext());
    }
}
