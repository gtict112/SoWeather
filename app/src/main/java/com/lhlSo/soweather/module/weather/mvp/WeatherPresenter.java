package com.lhlSo.soweather.module.weather.mvp;

import com.lhlSo.soweather.bean.WeatherDate;
import com.lhlSo.soweather.http.OnHttpCallBack;

/**
 * Created by Administrator on 2017/7/27.
 */

public class WeatherPresenter implements WeatherContract.IWeatherPresenter {
    WeatherContract.IWeatherModel mIWeatherModel;//M层
    WeatherContract.IWeatherView IWeatherView;//V层
    public String city = "CN101210101";
    public String key = "4e6193ff86d147a2a357dafb47b0f1bc";
    public WeatherDate mWeatherDate = new WeatherDate();

    public WeatherPresenter(WeatherContract.IWeatherView IWeatherView) {
        this.IWeatherView = IWeatherView;
        mIWeatherModel = new WeatherModel();
    }

    @Override
    public void geWeather() {
        IWeatherView.showProgress();//通知V层显示对话框
        mIWeatherModel.getWeather(city, key, new OnHttpCallBack<WeatherDate>() {
            @Override
            public void onSuccessful(WeatherDate mWeatherDate) {
                IWeatherView.hideProgress();//通知V层隐藏对话框
                IWeatherView.showData(mWeatherDate);//将获取到的信息显示到界面之前
            }

            @Override
            public void onFaild(String errorMsg) {
                IWeatherView.hideProgress();//通知V层隐藏对话框
                IWeatherView.showInfo(errorMsg);//通知V层显示错误信息
            }
        });
    }

}
