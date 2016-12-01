package com.example.administrator.soweather.com.example.administrator.soweather.utils;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.Aqi;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/11.
 */

public class ResponseProcessUtil {
    /**
     * 实况天气解析
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<NowWeather> getNowWather(Response response) throws IOException {
        Result<NowWeather> result = new Result<NowWeather>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            JSONArray data = jsonObjecty.getJSONArray("HeWeather5");
            result.setSuccess(data.length() > 0);
            NowWeather mNowWeatherData = NowWeather.Builder.creatNowWeather();
            result.setData(mNowWeatherData);
            JSONObject templateJson = data.getJSONObject(0);
            mNowWeatherData.city = templateJson.getJSONObject("basic").optString("city");
            mNowWeatherData.cnty = templateJson.getJSONObject("basic").optString("cnty");
            mNowWeatherData.id = templateJson.getJSONObject("basic").optString("id");
            mNowWeatherData.lat = templateJson.getJSONObject("basic").optString("lat");
            mNowWeatherData.lon = templateJson.getJSONObject("basic").optString("lon");
            mNowWeatherData.prov = templateJson.getJSONObject("basic").optString("prov");
            mNowWeatherData.update = templateJson.getJSONObject("basic").optString("update");
            mNowWeatherData.tmp = templateJson.getJSONObject("now").optString("tmp");
            mNowWeatherData.fl = templateJson.getJSONObject("now").optString("fl");
            mNowWeatherData.pres = templateJson.getJSONObject("now").optString("pres");
            mNowWeatherData.hum = templateJson.getJSONObject("now").optString("hum");
            mNowWeatherData.pcpn = templateJson.getJSONObject("now").optString("pcpn");
            mNowWeatherData.cond = templateJson.getJSONObject("now").optString("cond");
            mNowWeatherData.sc = templateJson.getJSONObject("now").getJSONObject("wind").optString("sc");
            mNowWeatherData.dir = templateJson.getJSONObject("now").getJSONObject("wind").optString("dir");
            mNowWeatherData.deg = templateJson.getJSONObject("now").getJSONObject("wind").optString("deg");
            mNowWeatherData.vis = templateJson.getJSONObject("now").optString("vis");
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("天气信息获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }

    /**
     * 天气集合接口获取Aqi数据
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<Aqi> getAqi(Response response) throws IOException {
        Result<Aqi> result = new Result<>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            JSONArray data = jsonObjecty.getJSONArray("HeWeather5");
            result.setSuccess(data.length() > 0);
            JSONObject templateJson = data.getJSONObject(0);
            JSONObject aqi = templateJson.getJSONObject("aqi");
            JSONObject city = aqi.getJSONObject("city");
            Aqi mAqi = Aqi.Builder.creatAqi();
            mAqi.aqi = city.optString("aqi");
            mAqi.co = city.optString("co");
            mAqi.no2 = city.optString("no2");
            mAqi.o3 = city.optString("o3");
            mAqi.pm10 = city.optString("pm10");
            mAqi.pm25 = city.optString("pm25");
            mAqi.qlty = city.optString("qlty");
            mAqi.so2 = city.optString("so2");
            result.setData(mAqi);
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("天气信息获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }

    /**
     * 生活指数解析
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<Suggestion> getSuggestion(Response response) throws IOException {
        Result<Suggestion> result = new Result<Suggestion>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            JSONArray data = jsonObjecty.getJSONArray("HeWeather5");
            result.setSuccess(data.length() > 0);
            Suggestion mSuggestion = Suggestion.Builder.creatSuggestion();
            result.setData(mSuggestion);
            JSONObject templateJson = data.getJSONObject(0);
            mSuggestion.flubrf = templateJson.getJSONObject("suggestion").getJSONObject("flu").optString("brf");//感冒指数简介
            mSuggestion.flutex = templateJson.getJSONObject("suggestion").getJSONObject("flu").optString("txt");//感冒指数详情
            mSuggestion.drsgbrf = templateJson.getJSONObject("suggestion").getJSONObject("drsg").optString("brf");//穿衣指数简介
            mSuggestion.drsgtex = templateJson.getJSONObject("suggestion").getJSONObject("drsg").optString("txt");//穿衣指数详情
            mSuggestion.sportbrf = templateJson.getJSONObject("suggestion").getJSONObject("sport").optString("brf");//运动指数简介
            mSuggestion.sporttex = templateJson.getJSONObject("suggestion").getJSONObject("sport").optString("txt");//运动指数详情
            mSuggestion.travbrf = templateJson.getJSONObject("suggestion").getJSONObject("trav").optString("brf");//旅游指数简介
            mSuggestion.travtex = templateJson.getJSONObject("suggestion").getJSONObject("trav").optString("txt");//旅游指数详情
            mSuggestion.cwbrf = templateJson.getJSONObject("suggestion").getJSONObject("cw").optString("brf");//洗车指数简介
            mSuggestion.cwtex = templateJson.getJSONObject("suggestion").getJSONObject("cw").optString("txt");//洗车指数详情
            mSuggestion.uvbrf = templateJson.getJSONObject("suggestion").getJSONObject("uv").optString("brf");//紫外线指数简介
            mSuggestion.uvtex = templateJson.getJSONObject("suggestion").getJSONObject("uv").optString("txt");//紫外线指数详情
            mSuggestion.comfbrf = templateJson.getJSONObject("suggestion").getJSONObject("comf").optString("brf");//紫外线指数简介
            mSuggestion.comftex = templateJson.getJSONObject("suggestion").getJSONObject("comf").optString("txt");//紫外线指数详情
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("天气信息获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }

    /**
     * 七天-十天预报数据
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<List<Dailyforecast>> getDailyforecast(Response response) throws IOException {
        Result<List<Dailyforecast>> result = new Result<List<Dailyforecast>>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            JSONArray data1 = jsonObjecty.getJSONArray("HeWeather5");
            JSONObject data2 = data1.getJSONObject(0);
            JSONArray data = data2.getJSONArray("daily_forecast");
            result.setSuccess(data.length() > 0);
            List<Dailyforecast> list = new ArrayList<Dailyforecast>();
            result.setData(list);
            for (int i = 0; i < data.length(); i++) {
                JSONObject templateJson = data.getJSONObject(i);
                Dailyforecast mDailyforecast = Dailyforecast.Builder.creatDailyforecast();
                // mDailyforecast.astro = templateJson.getJSONObject("daily_forecast").optString("astro");
                mDailyforecast.cond = templateJson.optString("cond");
                mDailyforecast.date = templateJson.optString("date");
                mDailyforecast.hum = templateJson.optString("hum");
                mDailyforecast.pcpn = templateJson.optString("pcpn");
                mDailyforecast.pop = templateJson.optString("pop");
                mDailyforecast.pres = templateJson.optString("pres");
                mDailyforecast.tmp = templateJson.optString("tmp");
                mDailyforecast.vis = templateJson.optString("vis");
                mDailyforecast.wind = templateJson.optString("wind");
                list.add(mDailyforecast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("天气信息获取失败,请升级客户端或与客服联系...");
        }
        return result;

    }

    /**
     * 小时数据解析
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<List<Hourlyforecast>> getHourlyforecast(Response response) throws IOException {
        Result<List<Hourlyforecast>> result = new Result<List<Hourlyforecast>>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            JSONArray data1 = jsonObjecty.getJSONArray("HeWeather5");
            JSONObject data2 = data1.getJSONObject(0);
            JSONArray data = data2.getJSONArray("hourly_forecast");
            result.setSuccess(data.length() > 0);
            List<Hourlyforecast> list = new ArrayList<Hourlyforecast>();
            result.setData(list);
            for (int i = 0; i < data.length(); i++) {
                JSONObject templateJson = data.getJSONObject(i);
                Hourlyforecast mHourlyforecast = Hourlyforecast.Builder.creatHourlyforecast();
                mHourlyforecast.cond = templateJson.optString("cond");
                mHourlyforecast.date = templateJson.optString("date");
                mHourlyforecast.hum = templateJson.optString("hum");
                mHourlyforecast.pop = templateJson.optString("pop");
                mHourlyforecast.pres = templateJson.optString("pres");
                mHourlyforecast.tmp = templateJson.optString("tmp");
                mHourlyforecast.wind = templateJson.optString("wind");
                list.add(mHourlyforecast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("天气信息获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }
}