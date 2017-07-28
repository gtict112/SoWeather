package com.lhlSo.soweather.module.weather.mvp;

import android.content.Context;

import com.lhlSo.soweather.bean.WeatherDate;
import com.lhlSo.soweather.http.OnHttpCallBack;


/**
 * Created by Administrator on 2017/7/27.
 */

public class WeatherContract {
    interface IWeatherView {
        Context getCurContext();//获取上下文对象

        void showProgress();//显示进度条

        void hideProgress();//隐藏进度条

        void showData(WeatherDate weatherDate);//显示数据到View上

        void showInfo(String info);//提示用户,提升友好交互

    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface IWeatherPresenter {
        void geWeather(String city);//获取数据
    }

    /**
     * M逻辑(业务)处理层
     */
    interface IWeatherModel {
        void getWeather(String city, String key, OnHttpCallBack<WeatherDate> callBack);//获取信息
    }
}
