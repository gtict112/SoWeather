package com.lhlSo.soweather.http;

import com.lhlSo.soweather.bean.WeatherDate;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 */
public interface WeatherService {
    @GET("weather")
    Observable<BaseWeatherResponse<WeatherDate>> getWeather(@Query("city") String city, @Query("key") String key);
}
