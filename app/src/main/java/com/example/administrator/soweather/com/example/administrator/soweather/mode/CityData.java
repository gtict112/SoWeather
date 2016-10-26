package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CityData {
    public String id;
    public String cnty;
    public String prov;
    public double lon;
    public double lat;
    public String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public static class Builder {

        public static CityData createCityData() {
            return new CityData();
        }
    }

    public static Result<List<CityData>> setSortData(Result<List<CityData>> res) {
        for (int i = 0; i < res.getData().size()-1; i++) {  //2693  2692
            for (int j = i + 1; j < res.getData().size(); j++) {
                if (res.getData().get(i).getProv().equals(res.getData().get(j).getProv())) {
                    CityData temp = res.getData().get(i);
                    res.getData().set(i, res.getData().get(j));
                    res.getData().set(j, temp);
                }
            }
        }
        return res;
    }


}
