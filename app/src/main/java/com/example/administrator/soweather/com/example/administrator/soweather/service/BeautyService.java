package com.example.administrator.soweather.com.example.administrator.soweather.service;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyListDate;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
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
 * Created by Administrator on 2017/6/6.
 */

public class BeautyService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public BeautyService() {
    }

    public void getBeautyList(final ResponseListenter<List<BeautyListDate>> a, String id) {
        String url = "http://www.tngou.net/tnfs/api/list?id=" + id;
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
                final Result<List<BeautyListDate>> res = ResponseProcessUtil.getBeautyListDate(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取图片失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取图片失败");
                    e1.printStackTrace();
                }
            }
        });
    }
}
