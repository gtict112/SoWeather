package com.example.administrator.soweather.com.example.administrator.soweather.service;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyDetail;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyListDate;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    public void getBeautyList(final ResponseListenter<List<BeautyListDate>> a, final String id, int page) {
        String url = "http://www.tngou.net/tnfs/api/list?id=" + id + "&page=" + page + "&rows=5";
        if (id.equals("福利")) {
            url = " http://gank.io/api/data/福利/20/" + page;
        }
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
                final Result<List<BeautyListDate>> res = ResponseProcessUtil.getBeautyListDate(response, id);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取图库失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取图库失败");
                    e1.printStackTrace();
                }
            }
        });
    }


    /**
     * 图片详情
     *
     * @param a
     * @param id
     */
    public void getBeautyDetail(final ResponseListenter<BeautyDetail> a, String id) {
        String url = "http://www.tngou.net/tnfs/api/show?" + "id=" + id;
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
                final Result<BeautyDetail> res = ResponseProcessUtil.getBeautyDetail(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取图库详情失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取图库详情失败");
                    e1.printStackTrace();
                }
            }
        });
    }
}
