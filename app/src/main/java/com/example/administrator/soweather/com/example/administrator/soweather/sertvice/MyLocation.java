package com.example.administrator.soweather.com.example.administrator.soweather.sertvice;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.CityData;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/31.
 */

public class MyLocation {
    // String key = "F9da85afead8b6e9c4738e5e5b79eb97";
    String Key = "4TFyhXKmr2O6e3RfoajXdGss6h64odNt";
    private final OkHttpClient client = new OkHttpClient();
    private Result<String> result = new Result<String>();
    public MyLocation() {
    }

    public Result<String> getAddress(String latValue, String longValue,final ResponseListenter<String> a) {
        String urlStr = "http://api.map.baidu.com/geocoder?location=" + latValue + "," + longValue + "&output=json&key=" + Key;
        final Request request = new Request.Builder()
                .url(urlStr)
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
                Result<String> res = new Result<String>();
                res.setSuccess(true);
                try {
                    if (res.isSuccess()) {
                        JSONObject jsonObjecty = new JSONObject(response.body().string());
                        String b = jsonObjecty.toString();
                        String dealResult = b.substring(0, b.indexOf("result") +8) + "[" + b.substring(b.indexOf("result") +8, b.length()-1) + "]}";
                        JSONArray jsonObjs;
                        String location = "";
                        try {
                            jsonObjs = new JSONObject(dealResult).getJSONArray("result");
                            JSONObject jsonObj = jsonObjs.getJSONObject(0);
                            String address = jsonObj.getString("formatted_address");
                            String bussiness = jsonObj.getString("business");
                            location = address + ":" + bussiness;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        res.setData(location);
                        a.onReceive(res);
                    } else {
                        a.onReceive(res.setErrorMessage("获取数据失败"));
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("解析数据失败");
                    e1.printStackTrace();
                }
            }
        });
        return null;
    }


}
