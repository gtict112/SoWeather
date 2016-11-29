package com.example.administrator.soweather.com.example.administrator.soweather.utils;

/**
 * Created by Administrator on 2016/10/11.
 */

public interface ResponseListenter<T> {
    void onReceive(com.example.administrator.soweather.com.example.administrator.soweather.mode.Result<T> result);
}
