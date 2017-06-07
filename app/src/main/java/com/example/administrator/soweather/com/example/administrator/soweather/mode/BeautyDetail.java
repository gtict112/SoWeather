package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class BeautyDetail {
    public String count;
    public String fcount;
    public String id;
    public String img;
    public String time;
    public String title;
    public List<Gallery> gallerys = new ArrayList<>();


    public static class Gallery {
        public String gallery;
        public String gallery_id;
        public String gallery_img;
    }

    public static class Builder implements Serializable {
        public static BeautyDetail creatBeautyDetail() {
            return new BeautyDetail();
        }
    }
}
