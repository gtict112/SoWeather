package com.lhlSo.soweather.module.weather.mvp;

import com.lhlSo.soweather.bean.WeatherDate;
import com.lhlSo.soweather.http.ApiFactory;
import com.lhlSo.soweather.http.BaseWeatherResponse;
import com.lhlSo.soweather.http.OnHttpCallBack;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/7/27.
 */

public class WeatherModel implements WeatherContract.IWeatherModel {


    @Override
    public void getWeather(String city, String key, final OnHttpCallBack<WeatherDate> callBack) {
        ApiFactory.getWeatherController()
                .getWeather(city, key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseWeatherResponse<WeatherDate>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseWeatherResponse<WeatherDate> weatherDateBaseWeatherResponse) {
                        callBack.onSuccessful(weatherDateBaseWeatherResponse.HeWeather5.get(0));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            if (code == 500 || code == 404) {
                                callBack.onFaild("服务器出错");
                            }
                        } else if (e instanceof ConnectException) {
                            callBack.onFaild("网络断开,请打开网络!");
                        } else if (e instanceof SocketTimeoutException) {
                            callBack.onFaild("网络连接超时!!");
                        } else {
                            callBack.onFaild("发生未知错误" + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
