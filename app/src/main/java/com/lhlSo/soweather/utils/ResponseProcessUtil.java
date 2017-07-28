package com.lhlSo.soweather.utils;

import com.lhlSo.soweather.bean.BeautyListDate;
import com.lhlSo.soweather.bean.Constellation;
import com.lhlSo.soweather.bean.Result;
import com.lhlSo.soweather.bean.TopNew;
import com.lhlSo.soweather.bean.Zodiac;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/11.
 */

public class ResponseProcessUtil {

    /**
     * 获取老黄历
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<Zodiac> getZodiac(Response response) throws IOException {
        Result<Zodiac> result = new Result<Zodiac>();
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            String code = jsonObjecty.optString("reason");
            if (code.equals("successed")) {
                result.setSuccess(true);
                JSONObject date = jsonObjecty.getJSONObject("result");
                Zodiac mZodiac = Zodiac.Builder.creatZodiac();
                result.setData(mZodiac);
                mZodiac.yangli = date.optString("yangli");
                mZodiac.yinli = date.optString("yinli");
                mZodiac.wuxing = date.optString("wuxing");
                mZodiac.chongsha = date.optString("chongsha");
                mZodiac.baiji = date.optString("baiji");
                mZodiac.jishen = date.optString("jishen");
                mZodiac.yi = date.optString("yi");
                mZodiac.xiongshen = date.optString("xiongshen");
                mZodiac.ji = date.optString("ji");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("老黄历信息获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }

    /**
     * 获取头条新闻
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<List<TopNew>> getTopNews(Response response) throws IOException {
        Result<List<TopNew>> result = new Result<List<TopNew>>();
        List<TopNew> list = new ArrayList<>();
        result.setSuccess(false);
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            String code = jsonObjecty.optString("reason");
            if (code.equals("成功的返回")) {
                result.setSuccess(true);
                JSONObject date = jsonObjecty.getJSONObject("result");
                JSONArray mDate = date.getJSONArray("data");
                for (int i = 0; i < mDate.length(); i++) {
                    JSONObject mNew = mDate.getJSONObject(i);
                    TopNew mNews = TopNew.Builder.creatNews();
                    mNews.title = mNew.getString("title");//标题
                    mNews.date = mNew.getString("date");//时间
                    mNews.thumbnail_pic_s = mNew.optString("thumbnail_pic_s", null);//图片
                    mNews.url = mNew.getString("url");//新闻链接
                    if (mNew.optString("uniquekey", null) != null) {
                        mNews.uniquekey = mNew.getString("uniquekey");//唯一标识
                    }
                    if (mNew.optString("type", null) != null) {
                        mNews.type = mNew.getString("type");//类型 头条
                    }
                    if (mNew.optString("realtype", null) != null) {
                        mNews.realtype = mNew.getString("realtype");//类型二 娱乐
                    }
                    mNews.author_name = mNew.getString("author_name");//来源
                    list.add(mNews);
                }
            }
            result.setData(list);
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }


    /**
     * 获取星座运势
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<Constellation> getConstellation(Response response) throws IOException {
        Result<Constellation> result = new Result<Constellation>();
        result.setSuccess(false);
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            String code = jsonObjecty.optString("resultcode");
            if (code.equals("200")) {
                result.setSuccess(true);
                Constellation mConstellation = Constellation.Builder.creatConstellation();
                result.setData(mConstellation);
                mConstellation.love = jsonObjecty.optString("love", null);
                mConstellation.work = jsonObjecty.optString("work", null);
                mConstellation.money = jsonObjecty.optString("money", null);
                mConstellation.health = jsonObjecty.optString("health", null);
                mConstellation.job = jsonObjecty.optString("job", null);
                mConstellation.all = jsonObjecty.optString("all", null);
                mConstellation.color = jsonObjecty.optString("color", null);
                mConstellation.number = jsonObjecty.optString("number", null);
                mConstellation.QFriend = jsonObjecty.optString("QFriend", null);
                mConstellation.summary = jsonObjecty.optString("summary", null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("星座运势获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }

    /**
     * 获取福利社图片
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static Result<List<BeautyListDate>> getListBeauty(Response response) throws IOException {
        Result<List<BeautyListDate>> result = new Result<>();
        List<BeautyListDate> list = new ArrayList<>();
        result.setSuccess(false);
        try {
            JSONObject jsonObjecty = new JSONObject(response.body().string());
            String code = jsonObjecty.optString("error");
            if (code.equals("false")) {
                result.setSuccess(true);
                JSONArray mDate = jsonObjecty.getJSONArray("results");
                for (int i = 0; i < mDate.length(); i++) {
                    JSONObject mNew = mDate.getJSONObject(i);
                    BeautyListDate mNews = BeautyListDate.Builder.creatBeautyListDate();
                    mNews.img = mNew.optString("url", null);//增加图片尺寸
                    list.add(mNews);
                }
                result.setData(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage("星座运势获取失败,请升级客户端或与客服联系...");
        }
        return result;
    }
}




