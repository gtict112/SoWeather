package com.lhlSo.soweather.http;

import com.lhlSo.soweather.base.AppGlobal;

/**
 * Created by Administrator on 2017/7/28.
 */

public class ApiFactory {
    private static WeatherService weatherController;
    protected static final Object monitor = new Object();

    public static WeatherService getWeatherController() {
        if (weatherController == null) {
            synchronized (monitor) {
                weatherController = RetrofitUtils.getInstance(AppGlobal.WEATHER_URL).create(WeatherService.class);
            }
        }
        return weatherController;
    }
}
