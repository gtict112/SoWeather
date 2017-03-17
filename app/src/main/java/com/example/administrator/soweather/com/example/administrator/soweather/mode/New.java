package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/9.
 */

public class New {
    public String author_name;
    public String thumbnail_pic_s;
    public String category;
    public String title;
    public String thumbnail_pic_s03;
    public String date;
    public String url;
    public Bitmap img_2;
    public static class Builder implements Serializable {
        public static New creatNew() {
            return new New();
        }
    }
}
