package com.lhlSo.soweather.service;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/5.
 */

public class News {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public News() {
    }


    /**
     * 今日头条
     */
    public void getTopNews(final ResponseListenter<List<TopNew>> a) {
        String url = "http://v.juhe.cn/toutiao/index?type=top&key=b216d9eee8a85d2e4eb906b084a07ae8";
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
                final Result<List<TopNew>> res = ResponseProcessUtil.getTopNews(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取今日头条失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取今日头条异常");
                    e1.printStackTrace();
                }
            }
        });
    }


    /**
     * 今日头条
     */
    public void getWeiXinSelect(final ResponseListenter<List<TopNew>> a, String title) {
        String url = "http://v.juhe.cn/toutiao/index?type=" + title + "&key=b216d9eee8a85d2e4eb906b084a07ae8";
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
                final Result<List<TopNew>> res = ResponseProcessUtil.getTopNews(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取微信精选失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取微信精选异常");
                    e1.printStackTrace();
                }
            }
        });
    }
}
