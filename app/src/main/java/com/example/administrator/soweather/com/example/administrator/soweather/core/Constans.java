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

        public static String[] getCitys() {
            String[] Citys = new String[values().length];
            for (int i = 0; i < Citys.length; i++) {
                Citys[i] = values()[i].getName();
            }
            return Citys;
        }

        private String name;
        private String cityId;
    }

    /**
     * 首页天气背景
     */
    enum WeatherBgImg {
        晴("晴", R.mipmap.sunny),
        热("热", R.mipmap.sunny),
        多云("多云", R.mipmap.cloudy),
        少云("多云", R.mipmap.cloudy),
        晴间多云("晴间多云", R.mipmap.cloudy),
        阴("阴", R.mipmap.cloudy2),
        有风("有风", R.mipmap.wind),
        平静("平静", R.mipmap.wind),
        微风("微风", R.mipmap.wind),
        和风("和风", R.mipmap.wind),
        清风("清风", R.mipmap.wind),
        小雨("小雨", R.mipmap.rain),
        毛毛雨("毛毛雨", R.mipmap.rain),
        细雨("细雨", R.mipmap.rain),
        冻雨("冻雨", R.mipmap.rain),
        阵雨("阵雨", R.mipmap.freezing_rain),
        强阵雨("强阵雨", R.mipmap.freezing_rain),
        极端降雨("极端降雨", R.mipmap.extreme_rainfall),
        中雨("大雨", R.mipmap.heavy_rain),
        大雨("大雨", R.mipmap.heavy_rain),
        暴雨("暴雨", R.mipmap.rain),
        大暴雨("大暴雨", R.mipmap.extreme_rainfall),
        特大暴雨("特大暴雨", R.mipmap.extreme_rainfall),

        小雪("小雪", R.mipmap.light_snow),
        阵雪("阵雪", R.mipmap.snow),
        中雪("中雪", R.mipmap.snow),
        大雪("大雪", R.mipmap.snow),
        暴雪("暴雪", R.mipmap.snow),
        雨夹雪("雨夹雪", R.mipmap.light_snow),
        雨雪天气("雨雪天气", R.mipmap.light_snow),
        阵雨夹雪("阵雨夹雪", R.mipmap.light_snow),

        强风("强风", R.mipmap.strong_wind),
        疾风("疾风", R.mipmap.strong_wind),
        烈风("烈风", R.mipmap.strong_wind),
        大风("大风", R.mipmap.strong_wind),

        薄雾("薄雾", R.mipmap.fog),
        雾("雾", R.mipmap.fog),
        霾("霾", R.mipmap.fog),
        风暴("风暴", R.mipmap.storm),
        狂爆风("狂爆风", R.mipmap.storm),
        热带风暴("热带风暴", R.mipmap.storm),
        飓风("飓风", R.mipmap.storm),
        龙卷风("龙卷风", R.mipmap.storm),
        扬沙("扬沙", R.mipmap.micrometeorology),
        浮尘("浮尘", R.mipmap.micrometeorology),
        沙尘暴("沙尘暴", R.mipmap.micrometeorology),
        强沙尘暴("强沙尘暴", R.mipmap.micrometeorology),

        雷阵雨("雷阵雨", R.mipmap.rain),
        强雷阵雨("强雷阵雨", R.mipmap.rain),
        雷阵雨伴有冰雹("雷阵雨伴有冰雹", R.mipmap.rain);

        WeatherBgImg(String name, int imgId) {
            this.name = name;
            this.imgId = imgId;
        }

        /**
         * @return 城市名称
         */
        public String getName() {
            return name;
        }

        public int getimgId() {
            return imgId;
        }

        public static String[] getWeatherBgImg() {
            String[] Citys = new String[values().length];
            for (int i = 0; i < Citys.length; i++) {
                Citys[i] = values()[i].getName();
            }
            return Citys;
        }

        private String name;
        private int imgId;
    }
}
