package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */

public class Hourlyforecast implements Serializable{
    public String cond;
    public String date;
    public String hum;
    public String pop;
    public String pres;
    public String tmp;
    public String wind;

    public static class Builder implements Serializable {
        public static Hourlyforecast creatHourlyforecast() {
            return new Hourlyforecast();
        }
    }
}
