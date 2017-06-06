package com.example.administrator.soweather.com.example.administrator.soweather.core;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/11/2.
 */

public interface Constans {
    /**
     * 热门城市Id
     */
    enum HotWord {
        北京("北京", "CN101010100"), 上海("上海", "CN101020100"), 广州("广州", "CN101280101"), 深圳(
                "深圳", "CN101280601"), 杭州("杭州", "CN101210101"), 南京("南京", "CN101190101"), 天津("天津", "CN101030100"), 武汉("武汉", "CN101200101"), 长沙("长沙", "CN101250101"), 重庆("重庆", "CN101040100"), 温州("温州", "CN101210701");

        HotWord(String name, String cityId) {
            this.name = name;
            this.cityId = cityId;
        }

        /**
         * @return 城市名称
         */
        public String getName() {
            return name;
        }

        public String getCityId() {
            return cityId;
        }

//        public static String[] getCitys() {
//            String[] Citys = new String[values().length];
//            for (int i = 0; i < Citys.length; i++) {
//                Citys[i] = values()[i].getName();
//            }
//            return Citys;
//        }

        private String name;
        private String cityId;
    }

    /**
     * 首页天气背景
     */
    enum WeatherBgImg {
        晴("晴", new int[]{R.mipmap.sunny, R.mipmap.sunny2, R.mipmap.sunny4}),
        热("热", new int[]{R.mipmap.hot}),
        多云("多云", new int[]{R.mipmap.cloudy3, R.mipmap.cloudy5, R.mipmap.cloudy6, R.mipmap.cloudy7}),
        少云("少云", new int[]{R.mipmap.cloudy4}),
        晴间多云("晴间多云", new int[]{R.mipmap.cloudy2}),
        阴("阴", new int[]{ R.mipmap.cloudy3, R.mipmap.cloudy5, R.mipmap.cloudy6, R.mipmap.cloudy7}),
        有风("有风", new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4, R.mipmap.wind5}),
        平静("平静", new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4, R.mipmap.wind5}),
        微风("微风", new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4, R.mipmap.wind5}),
        和风("和风", new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4, R.mipmap.wind5}),
        清风("清风", new int[]{R.mipmap.wind, R.mipmap.wind2, R.mipmap.wind3, R.mipmap.wind4, R.mipmap.wind5}),
        小雨("小雨", new int[]{R.mipmap.rain3}),
        毛毛雨("毛毛雨", new int[]{R.mipmap.rain3}),
        细雨("细雨", new int[]{  R.mipmap.rain3}),
        冻雨("冻雨", new int[]{  R.mipmap.rain3}),
        阵雨("阵雨", new int[]{  R.mipmap.rain3}),
        强阵雨("强阵雨", new int[]{R.mipmap.rain3}),
        极端降雨("极端降雨", new int[]{R.mipmap.rain3}),
        中雨("大雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        大雨("大雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        暴雨("暴雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        大暴雨("大暴雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        特大暴雨("特大暴雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        小雪("小雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        阵雪("阵雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        中雪("中雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        大雪("大雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        暴雪("暴雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        雨夹雪("雨夹雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        雨雪天气("雨雪天气", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        阵雨夹雪("阵雨夹雪", new int[]{R.mipmap.snow, R.mipmap.snow2, R.mipmap.snow3, R.mipmap.snow4}),
        强风("强风", new int[]{R.mipmap.storm}),
        疾风("疾风", new int[]{R.mipmap.storm}),
        烈风("烈风", new int[]{R.mipmap.storm}),
        大风("大风", new int[]{R.mipmap.storm}),
        薄雾("薄雾", new int[]{R.mipmap.fog}),
        雾("雾", new int[]{R.mipmap.fog}),
        霾("霾", new int[]{R.mipmap.fog}),
        风暴("风暴", new int[]{R.mipmap.storm, R.mipmap.storm2}),
        狂爆风("狂爆风", new int[]{R.mipmap.storm, R.mipmap.storm2}),
        热带风暴("热带风暴", new int[]{R.mipmap.storm, R.mipmap.storm2}),
        飓风("飓风", new int[]{R.mipmap.storm}),
        龙卷风("龙卷风", new int[]{R.mipmap.storm}),
        扬沙("扬沙", new int[]{R.mipmap.micrometeorology, R.mipmap.micrometeorology2}),
        浮尘("浮尘", new int[]{R.mipmap.micrometeorology, R.mipmap.micrometeorology2}),
        沙尘暴("沙尘暴", new int[]{R.mipmap.micrometeorology, R.mipmap.micrometeorology2}),
        强沙尘暴("强沙尘暴", new int[]{R.mipmap.micrometeorology, R.mipmap.micrometeorology2}),
        雷阵雨("雷阵雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        强雷阵雨("强雷阵雨", new int[]{R.mipmap.rain5, R.mipmap.rain6}),
        雷阵雨伴有冰雹("雷阵雨伴有冰雹", new int[]{R.mipmap.rain5, R.mipmap.rain6});

        WeatherBgImg(String name, int[] imgId) {
            this.name = name;
            this.imgId = imgId;
        }

        /**
         * @return 天气名称
         */
        public String getName() {
            return name;
        }

        public int[] getimgId() {
            return imgId;
        }

//        public static String[] getWeatherBgImg() {
//            String[] Citys = new String[values().length];
//            for (int i = 0; i < Citys.length; i++) {
//                Citys[i] = values()[i].getName();
//            }
//            return Citys;
//        }

        private String name;
        private int[] imgId;
    }
}
