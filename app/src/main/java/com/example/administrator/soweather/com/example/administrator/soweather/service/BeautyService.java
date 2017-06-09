package com.example.administrator.soweather.com.example.administrator.soweather.service;

import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyDetail;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyListDate;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseProcessUtil;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
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
    public void getBeautyList(final ResponseListenter<List<BeautyListDate>> a, final String id, final int page) {
        final Result<List<BeautyListDate>> res = new Result<>();
        new Thread() {
            @Override
            public void run() {
                List<BeautyListDate> list = new ArrayList<>();
                try {
                    String urlq = "http://www.mzitu.com/" + id + "/page/" + page;
                    org.jsoup.nodes.Document doc = Jsoup.connect(urlq).timeout(10000).get();
                    org.jsoup.nodes.Element total = doc.select("div.postlist").first();
                    Elements items = total.select("li");
                    for (org.jsoup.nodes.Element element : items) {
                        BeautyListDate mNews = BeautyListDate.Builder.creatBeautyListDate();
                        mNews.img = element.select("img").first().attr("data-original");
                        mNews.title = element.select("img").first().attr("alt");
                        String id = element.select("img").first().attr("data-original").
                                substring(28, element.select("img").first().attr("data-original").length() - 4);
                        id = id.substring(8, id.length() - 10);
                        mNews.id = id;
                        list.add(mNews);
                    }
                    res.setSuccess(true);
                    a.onReceive(res.setData(list));
                } catch (IOException e) {
                    e.printStackTrace();
                    res.setSuccess(false);
                    a.onReceive(res);
                }
            }
        }.start();
    }


    /**
     * 图片详情
     *
     * @param a
     * @param id
     */
    public void getBeautyDetail(final ResponseListenter<BeautyDetail> a, final String id) {
        final Result<BeautyDetail> res1 = new Result<>();
        new Thread() {
            @Override
            public void run() {
                try {
                    String urld = "http://www.mzitu.com/" + id;
                    org.jsoup.nodes.Document doc = Jsoup.connect(urld).timeout(10000).get();
                    org.jsoup.nodes.Element total = doc.select("div.main-image").first();
                    Elements items =  total.select("p");
                    BeautyDetail mNews = BeautyDetail.Builder.creatBeautyDetail();
                    mNews.img = items.select("img").first().attr("src");
                    res1.setSuccess(true);
                    a.onReceive(res1.setData(mNews));
                } catch (IOException e) {
                    e.printStackTrace();
                    res1.setSuccess(false);
                    a.onReceive(res1);
                }
            }
        }.start();
    }
}
