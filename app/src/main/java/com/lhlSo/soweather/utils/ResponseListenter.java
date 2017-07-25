package com.lhlSo.soweather.utils;

import com.lhlSo.soweather.mode.Result;

/**
 * Created by Administrator on 2016/10/11.
 */

public interface ResponseListenter<T> {
    void onReceive(Result<T> result);
}
