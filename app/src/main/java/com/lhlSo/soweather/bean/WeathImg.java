package com.lhlSo.soweather.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/10/25.
 */

public class WeathImg {
    public Bitmap icon;  //图片地址
    public String txt_zh;   //天气英文
    public String txt_en;  //天气中文
    public String code; //天气id
    public String icon_url;
    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getTxt_zh() {
        return txt_zh;
    }

    public void setTxt_zh(String txt_zh) {
        this.txt_zh = txt_zh;
    }


    public String getTxt_en() {
        return txt_en;
    }

    public void setTxt_en(String txt_en) {
        this.txt_en = txt_en;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
