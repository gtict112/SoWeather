package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5.
 */

public class TopNew implements Serializable {
    public String title;
    public String date;
    public String thumbnail_pic_s;
    public String url;
    public String uniquekey;
    public String type;
    public String realtype;
    public String author_name;
    public Bitmap img;
    public static class Builder implements Serializable {
        public static TopNew creatNews() {
            return new TopNew();
        }
    }
}
