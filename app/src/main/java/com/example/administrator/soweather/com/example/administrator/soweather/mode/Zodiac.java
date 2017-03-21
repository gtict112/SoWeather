package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Zodiac implements Serializable {
    public String yangli;
    public String yinli;

    public String wuxing;
    public String chongsha;
    public String baiji;
    public String jishen;
    public String yi;
    public String xiongshen;
    public String ji;

    public static class Builder implements Serializable {
        public static Zodiac creatZodiac() {
            return new Zodiac();
        }
    }
}
