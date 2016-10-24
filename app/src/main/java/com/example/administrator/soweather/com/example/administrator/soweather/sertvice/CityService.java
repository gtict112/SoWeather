package com.example.administrator.soweather.com.example.administrator.soweather.sertvice;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.CityData;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

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

public class CityService {
    private final OkHttpClient client = new OkHttpClient();
    private final String url = "https://api.heweather.com/x3/citylist?search=allchina&key=4e6193ff86d147a2a357dafb47b0f1bc";//国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
    private Result<List<CityData>> result = new Result<List<CityData>>();

    public CityService() {
    }

    public Result<List<CityData>> getWeatherData(final ResponseListenter<List<CityData>> a) {
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
                Result<List<CityData>> res = ResponseProcessUtil.getHotworld(response);
                try {
                    if (res.isSuccess()) {
                        //进行分级排序处理
                        res = CityData.setSortData(res);
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
