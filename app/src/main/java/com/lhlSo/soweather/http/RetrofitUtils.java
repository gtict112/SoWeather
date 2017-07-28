package com.lhlSo.soweather.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/26.
 */

public class RetrofitUtils {
    private static Retrofit mRetrofit;
    private static RetrofitUtils instance;
    private static Gson mGson;

    private RetrofitUtils(String baseurl) {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    public static RetrofitUtils getInstance(String baseurl) {
        if (instance == null) {
            synchronized (Retrofit.class) {
                instance = new RetrofitUtils(baseurl);
            }
        }
        return instance;
    }


    private static OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }


    public static Gson gson() {
        if (mGson == null) {
            synchronized (RetrofitUtils.class) {
                mGson = new GsonBuilder().setLenient().create();
            }
        }
        return mGson;
    }
}