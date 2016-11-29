package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class NowWeather {
    public String sc;
    public String dir;
    public String deg;
    public String hum;
    public String pcpn;
    public String fl;
    public String tmp;
    public String pres;
    public String cond;
    public String vis;
    public String city;
    public String cnty;
    public String id;
    public String lat;
    public String lon;
    public String prov;
    public String update;

    public static class Builder implements Serializable {
        public static NowWeather creatNowWeather() {
            return new NowWeather();
        }
    }
}
