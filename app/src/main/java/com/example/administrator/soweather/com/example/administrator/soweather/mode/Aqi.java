package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/22.
 */

public class Aqi implements Serializable {
    public String aqi;
    public String co;
    public String no2;
    public String o3;
    public String pm10;
    public String pm25;
    public String qlty;      //共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
    public String so2;

    public static class Builder implements Serializable {
        public static Aqi creatAqi() {
            return new Aqi();
        }
    }
}
