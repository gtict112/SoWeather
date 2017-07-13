package com.example.administrator.soweather.com.example.administrator.soweather.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.County;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONArray;
import org.json.JSONException;
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

public class CityAndWeatherImgService {
    private final OkHttpClient client = new OkHttpClient();
    private Result<Integer> result = new Result<Integer>();

    public CityAndWeatherImgService() {
    }

    /**
     * 获取城市列表数据
     *
     * @param mainActivity
     * @param a
     * @return
     */
    public Result<Integer> getCityData(Activity mainActivity, final ResponseListenter<Integer> a) {
        String url = "https://cdn.heweather.com/china-city-list.txt";
        Context context = mainActivity;
        final SoWeatherDB cityDB = SoWeatherDB.getInstance(context);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.setErrorMessage(e.toString());
                result.setSuccess(false);
                a.onReceive(result);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Result<Integer> result = new Result<Integer>();
                if (!response.isSuccessful()) {
                    result.setSuccess(false);
                    result.setErrorMessage("\"Unexpected code \" + response");
                }
                //加一个判断,是否code为200
                String txt = response.body().string();
                String list = txt.substring(txt.indexOf("CN101010100"), txt.indexOf("120.538"));
                String[] date = list.split("\n");
                if (date != null && date.length > 0) {
                    List<Province> provinceList = new ArrayList<>();//省份数据
                    List<City> citylist = new ArrayList<>();//城市数据
                    List<County> countyList = new ArrayList<>();//县区数据
                    for (int i = 0; i < date.length; i++) {
                        String str = date[i];
                        String[] strarray = str.split("\t");
                        Province province = new Province();
                        City city = new City();
                        County county = new County();
                        province.setProvinceId(String.valueOf(i)); //省
                        province.setProvinceName(strarray[7]);
                        city.setProvinceeName(strarray[7]);
                        city.setCityId(strarray[0]); //市
                        city.setCityName(strarray[9]);
                        county.setCityName(strarray[9]);
                        county.setCountyName(strarray[2]); //区
                        county.setCountyId(String.valueOf(i));
                        provinceList.add(province);
                        citylist.add(city);
                        countyList.add(county);
                    }
                    cityDB.saveProvinces(provinceList);
                    cityDB.saveCitys(citylist);
                    cityDB.savaCounty(countyList);
                }
                result.setSuccess(true);
                a.onReceive(result);
            }
        });
        return result;
    }

    /**
     * 获取天气图片数据列表
     *
     * @param mainActivity
     * @param a
     * @return
     */
    public Result<Integer> getWeatherImgData(Activity mainActivity, final ResponseListenter<Integer> a) {
        String url = "https://cdn.heweather.com/condition-code.txt";
        Context context = mainActivity;
        final SoWeatherDB cityDB = SoWeatherDB.getInstance(context);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.setErrorMessage(e.toString());
                result.setSuccess(false);
                a.onReceive(result);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Result<Integer> result = new Result<Integer>();
                List<WeathImg> mWeathImg = new ArrayList<WeathImg>();
                if (!response.isSuccessful()) {
                    result.setSuccess(false);
                    result.setErrorMessage("\"Unexpected code \" + response");
                }
                //加一个判断,是否code为200
                String txt = response.body().string();
                String list = txt.substring(txt.indexOf("100"), txt.indexOf("999.png"));
                String[] date = list.split("\n");
                if (date != null && date.length > 0) {
                    List<WeathImg> weathImgs = new ArrayList<WeathImg>();
                    for (String str : date) {
                        String[] strarray = str.split("\t");
                        WeathImg weathImg = new WeathImg();
                        weathImg.setCode(strarray[0]);
                        weathImg.setTxt_zh(strarray[1]);
                        weathImg.setTxt_en(strarray[2]);
                        weathImg.setIcon(getImg(strarray[3]));//请求图片
                        weathImgs.add(weathImg);
                    }
                    cityDB.saveWeathImgs(weathImgs);
                }
                result.setSuccess(true);
                a.onReceive(result);
            }
        });
        return result;
    }

    private Bitmap getImg(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        ResponseBody body = client.newCall(request).execute().body();
        InputStream in = body.byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
    }

}
