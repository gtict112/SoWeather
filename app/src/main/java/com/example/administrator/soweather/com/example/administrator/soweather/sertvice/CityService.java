package com.example.administrator.soweather.com.example.administrator.soweather.sertvice;

import android.app.Activity;
import android.content.Context;

import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.db.CityDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.County;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Result<Integer> result = new Result<Integer>();
    private String url = "http://files.heweather.com/china-city-list.json";

    public CityService() {
    }

    public Result<Integer> getCityData(Activity mainActivity, final ResponseListenter<Integer> a) {
        Context context = mainActivity;
        final CityDB cityDB = CityDB.getInstance(context);
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
                String list = txt.substring(txt.indexOf("["), txt.indexOf("]")) + "]";
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(list);
                    List<Province> provinceList = new ArrayList<>();//省份数据
                    List<City> citylist = new ArrayList<>();//城市数据
                    List<County> countyList = new ArrayList<>();//县区数据
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Province province = new Province();
                        City city = new City();
                        County county = new County();
                        province.setProvinceId(String.valueOf(i));
                        province.setProvinceName(jsonObject.optString("provinceZh"));
                        city.setProvinceId(String.valueOf(i));
                        city.setCityId(jsonObject.optString("id"));
                        city.setCityName(jsonObject.optString("cityZh"));
                        county.setCityId(jsonObject.optString("id"));
                        county.setCountyName(jsonObject.optString("leaderZh"));
                        county.setCountyId(String.valueOf(i));
                        provinceList.add(province);
                        citylist.add(city);
                        countyList.add(county);
                    }
                    cityDB.saveProvinces(provinceList);
                    cityDB.saveCitys(citylist);
                    cityDB.savaCounty(countyList);
                } catch (JSONException e) {
                    result.setSuccess(false);
                    result.setErrorMessage(e.toString());
                }
                result.setSuccess(true);
                a.onReceive(result);
            }
        });
        return result;
    }
}
