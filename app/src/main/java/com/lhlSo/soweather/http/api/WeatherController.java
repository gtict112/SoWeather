package com.lhlSo.soweather.http.api;

import com.lhlSo.soweather.http.BaseWeatherResponse;
import com.lhlSo.soweather.mode.NowWeather;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by LHLin on 2017/7/26.  天气接口地址相关
 */

public interface WeatherController {
    @GET("https://free-api.heweather.com/v5/weather")
    Observable<BaseWeatherResponse<NowWeather>> getWeather(@Query("key") String key, @Query("city") String city);
}
