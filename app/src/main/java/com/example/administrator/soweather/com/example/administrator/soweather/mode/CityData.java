package com.example.administrator.soweather.com.example.administrator.soweather.mode;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CityData {
    public long id;
    public String cnty;
    public String prov;
    public double lon;
    public double lat;
    public String city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
