package com.lhlSo.soweather.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/21.
 */

public class Constellation {
    /**
     * 公共
     */
    public String work;//职场运势
    public String love;//感情运势
    public String money;//财富运势
    public String health;//健康运势

    /**
     * 周期查询特有
     */
    public String job;//求职运势

    /**
     * 今日和明日查询特有
     */
    public String all;//综合指数
    public String color;//幸运色
    public String number;//幸运数字
    public String QFriend;//速配星座
    public String summary;//今日描述



    public static class Builder implements Serializable {
        public static Constellation creatConstellation() {
            return new Constellation();
        }
    }
}
