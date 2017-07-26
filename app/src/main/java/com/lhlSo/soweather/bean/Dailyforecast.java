package com.lhlSo.soweather.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Dailyforecast implements  Serializable{
    public String astro;
    public String cond;
    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;
    public String tmp;
    public String vis;
    public String wind;

    public static class Builder implements Serializable {
        public static Dailyforecast creatDailyforecast() {
            return new Dailyforecast();
        }
    }
}
