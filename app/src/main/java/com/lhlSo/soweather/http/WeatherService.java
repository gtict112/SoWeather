package com.lhlSo.soweather.http;

import com.lhlSo.soweather.bean.Aqi;
import com.lhlSo.soweather.bean.Dailyforecast;
import com.lhlSo.soweather.bean.Hourlyforecast;
import com.lhlSo.soweather.bean.NowWeather;
import com.lhlSo.soweather.bean.Result;
import com.lhlSo.soweather.bean.Suggestion;
import com.lhlSo.soweather.utils.ResponseListenter;
import com.lhlSo.soweather.utils.ResponseProcessUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/11.
 */

public class WeatherService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public WeatherService() {
    }

    /**
     * 天气集合接口
     */
    public void getWeatherData(final ResponseListenter<Aqi> a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://free-api.heweather.com/v5/weather?" + "city=" + cityid + "&" +
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
                final Result<Aqi> res = ResponseProcessUtil.getAqi(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取实况天气数据失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取实况天气数据异常");
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取实况天气
     *
     * @param a
     * @param cityId
     */
    public void getNowWeatherData(final ResponseListenter<NowWeather> a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://free-api.heweather.com/v5/now?" + "city=" + cityid + "&" +
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
                final Result<NowWeather> res = ResponseProcessUtil.getNowWather(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取实况天气数据失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取实况天气数据异常");
                    e1.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取生活指数
     */
    public void getSuggestionData(final ResponseListenter<Suggestion> a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://free-api.heweather.com/v5/suggestion?" + "city=" + cityid + "&" +
                "key=" + Key;
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
                final Result<Suggestion> res = ResponseProcessUtil.getSuggestion(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取生活指数数据失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取生活指数数据异常");
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 七天-十天预报数据
     */

    public void getDailyforecastData(final ResponseListenter<List<Dailyforecast>> a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://free-api.heweather.com/v5/forecast?" + "city=" + cityid + "&" +
                "key=" + Key;
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
                final Result<List<Dailyforecast>> res = ResponseProcessUtil.getDailyforecast(response);
                if (res.isSuccess()) {
                    a.onReceive(res);
                } else {
                    res.setSuccess(false);
                    res.setErrorMessage("获取日期预报数据失败");
                    a.onReceive(res);
                }
            }
        });
    }


    /**
     * 小时预报数据
     */
    public void getHourlyforecastData(final ResponseListenter<List<Hourlyforecast>> a, final String cityId) {
        String cityid = null;
        if (cityId != null) {
            cityid = cityId;
        } else {
            cityid = "CN101210101";
        }
        String Key = "4e6193ff86d147a2a357dafb47b0f1bc";//和风天气认证Key;
        String url = "https://free-api.heweather.com/v5/hourly?" + "city=" + cityid + "&" +
                "key=" + Key;
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
                final Result<List<Hourlyforecast>> res = ResponseProcessUtil.getHourlyforecast(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取小时预报数据失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取小时预报数据异常");
                    e1.printStackTrace();
                }
            }
        });
    }
}
