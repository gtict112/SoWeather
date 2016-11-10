package com.example.administrator.soweather.com.example.administrator.soweather.sertvice;

import android.graphics.BitmapFactory;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/10/11.
 */

public class WeatherService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public WeatherService() {
    }

    public void getWeatherData(final ResponseListenter a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://api.heweather.com/x3/weather?" + "cityid=" + cityid + "&" +
                "key=" + Key;//测试
        Request request = new Request.Builder()
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
                final Result<List<WeatherData>> res = ResponseProcessUtil.getWeather(response);
                try {
                    if (res.isSuccess()) {
                        getWeatherImg(new ResponseListenter<List<WeathImg>>() {
                            @Override
                            public void onReceive(Result<List<WeathImg>> result) throws Exception {
                                if (result.isSuccess()) {
                                    String code = new JSONObject(res.getData().get(0).cond).optString("code");
                                    for (int i = 0; i < result.getData().size(); i++) {
                                        if (code.equals(result.getData().get(i).code)) {
                                            String url = result.getData().get(i).icon;
                                            try {
                                                OkHttpClient client = new OkHttpClient();
                                                Request request = new Request.Builder().url(url).build();
                                                ResponseBody body = client.newCall(request).execute().body();
                                                InputStream in = body.byteStream();
                                                res.getData().get(0).drawable = BitmapFactory.decodeStream(in);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        }
                                    }
                                    List<WeatherData.DailyForecase> mDailyForecase = new ArrayList<>();
                                    mDailyForecase = res.getData().get(0).mDailyForecase;
                                    for (int k = 0; k < mDailyForecase.size(); k++) {
                                        String code_d = new JSONObject(mDailyForecase.get(k).cond).optString("code_d");
                                        for (int q = 0; q < result.getData().size(); q++) {
                                            if (code_d.equals(result.getData().get(q).code)) {
                                                String url = result.getData().get(q).icon;
                                                try {
                                                    OkHttpClient client = new OkHttpClient();
                                                    Request request = new Request.Builder().url(url).build();
                                                    ResponseBody body = client.newCall(request).execute().body();
                                                    InputStream in = body.byteStream();
                                                    mDailyForecase.get(k).drawable = BitmapFactory.decodeStream(in);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    a.onReceive(res);
                                }
                            }
                        });
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取数据失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("解析数据失败");
                    try {
                        a.onReceive(res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    e1.printStackTrace();
                }
            }


        });
    }


    private void getWeatherImg(final ResponseListenter b) {
        final String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key
        String hose = "https://api.heweather.com/x3/condition?";
        String url = hose + "search=" + "allcond" + "&" + "key=" + Key;
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
                    b.onReceive(result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Result<List<WeathImg>> res = ResponseProcessUtil.getWeatherImg(response);
                try {
                    if (res.isSuccess()) {
                        b.onReceive(res);
                    } else {
                        b.onReceive(res.setErrorMessage("获取数据失败"));
                    }
                    ;
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("解析数据失败");
                    e1.printStackTrace();
                }
            }
        });
    }
}
