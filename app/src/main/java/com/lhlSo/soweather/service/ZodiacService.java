package com.lhlSo.soweather.service;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/5.
 */

public class ZodiacService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public ZodiacService() {
    }


    /**
     * 老黄历
     */
    public void getZodiacService(final ResponseListenter<Zodiac> a, final String date) {
        String Date = null;
        if (date != null) {
            Date = date;
        } else {
            Date = "2016-12-05";
        }


        String Key = "b46a869144ea71c63ba429e1aab897da";
        String url = "http://v.juhe.cn/laohuangli/d?" + "date=" + Date + "&" +
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
                final Result<Zodiac> res = ResponseProcessUtil.getZodiac(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取今日老黄历失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取今日老黄历异常");
                    e1.printStackTrace();
                }
            }
        });
    }
}
