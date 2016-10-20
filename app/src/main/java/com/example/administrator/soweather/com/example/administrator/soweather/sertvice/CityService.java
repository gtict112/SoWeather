package com.example.administrator.soweather.com.example.administrator.soweather.sertvice;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.HotworldData;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/11.
 */

public class CityService {
    private final OkHttpClient client = new OkHttpClient();
    private final String url ="https://api.heweather.com/x3/citylist?search=allchina&key=4e6193ff86d147a2a357dafb47b0f1bc";//国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
    private Result result = new Result();
    public CityService() {
    }

    public void getWeatherData(final ResponseListenter a) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.setErrorMessage(e.toString());
                result.setSuccess(false);
                try {
                    a.onReceive(result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Result<HotworldData> res = ResponseProcessUtil.getHotworld(response);
                try {
                    a.onReceive(res);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
