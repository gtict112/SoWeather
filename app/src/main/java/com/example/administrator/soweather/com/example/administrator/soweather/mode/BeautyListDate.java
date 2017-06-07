package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/6.
 */

public class BeautyListDate {
    public String id;
    public String galleryclass;//          图片分类
    public String title;//          标题
    public String img;//          图库封面
    public String count;//          访问数
    public String rcount;//           回复数
    public String fcount;//          收藏数
    public String size;//      图片多少张
    public static class Builder implements Serializable {
        public static BeautyListDate creatBeautyListDate() {
            return new BeautyListDate();
        }
    }
}
