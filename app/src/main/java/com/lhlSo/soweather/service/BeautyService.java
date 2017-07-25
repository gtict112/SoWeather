package com.lhlSo.soweather.service;

import android.graphics.Bitmap;


import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/6.
 */

public class BeautyService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public BeautyService() {
    }

    /**
     * 图片列表
     *
     * @param a
     * @param id
     */
    public void getBeautyList(final ResponseListenter<List<BeautyListDate>> a, final String id, final int page) {
        String url = "http://gank.io/api/data/" + id + "/10/" + page;
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
                final Result<List<BeautyListDate>> res = ResponseProcessUtil.getListBeauty(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取福利社图片失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取福利社图片异常");
                    e1.printStackTrace();
                }
            }
        });
    }

    public void loadImage(final String image_url, final ResponseListenter<byte[]> a) {
        final Request request = new Request.Builder().get()
                .url(image_url)
                .build();
        client.newCall(request).enqueue(new Callback() {
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
                final Result<byte[]> res = new Result<>();
                res.setSuccess(false);
                try {
                    if (response.isSuccessful()) {
                        res.setSuccess(true);
                        res.setData(response.body().bytes());
                        a.onReceive(res);//将图片保存到Sd卡  再将图片设置成壁纸
                    } else {
                        res.setSuccess(false);
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    e1.printStackTrace();
                }
            }
        });
    }
}
