package com.lhlSo.soweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lhlSo.soweather.bean.City;
import com.lhlSo.soweather.bean.County;
import com.lhlSo.soweather.bean.ManageCity;
import com.lhlSo.soweather.bean.Province;
import com.lhlSo.soweather.bean.WeathImg;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市列表数据和天气图片
 * Created by Administrator on 2016/11/15.
 */

public class SoWeatherDB {
    private final String DataBaseName = "Soweather02";

    private final int VERSION = 1;

    private SQLiteDatabase database;

    private static SoWeatherDB weatherDB;
    private final String MANAGE_CITY = "create table managecity("
            + "cityId text," + "cityName text)";

    public SoWeatherDB(Context context) {
        DateBaseHelper dataBaseHelper = new DateBaseHelper(context,
                DataBaseName, null, VERSION);
        database = dataBaseHelper.getWritableDatabase();
    }

    public static SoWeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new SoWeatherDB(context);
        }
        return weatherDB;
    }

    /**
     * 保存省级数据
     *
     * @param provinceList
     */
    public void saveProvinces(List<Province> provinceList) {
        int number = 0;
        Cursor c = database.rawQuery("select * from Province", null);
        number = c.getCount();
        if (provinceList != null && provinceList.size() > 0) {
            ContentValues values = new ContentValues();
            for (int i = 0; i < provinceList.size(); i++) {
                for (int j = provinceList.size() - 1; j > i; j--) {
                    if (provinceList.get(i).getProvinceName().equals(provinceList.get(j).getProvinceName())) {
                        provinceList.remove(j);
                    }
                }
            }
            if (number == 0) {
                for (int i = 0; i < provinceList.size(); i++) {
                    values.put("provinceName", provinceList.get(i).getProvinceName());
                    values.put("provinceId", provinceList.get(i).getProvinceId());
                    database.insert("Province", null, values);
                    values.clear();
                }
            }
        }
    }

    /**
     * 保存城市数据
     *
     * @param cityList
     */
    public void saveCitys(List<City> cityList) {
        int number = 0;
        Cursor c = database.rawQuery("select * from City", null);
        number = c.getCount();
        if (cityList != null && cityList.size() > 0) {
            ContentValues values = new ContentValues();
            for (int i = 0; i < cityList.size(); i++) {
                for (int j = cityList.size() - 1; j > i; j--) {
                    if (cityList.get(i).getCityName().equals(cityList.get(j).getCityName())) {
                        cityList.remove(j);
                    }
                }
            }
            if (number == 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    values.put("cityName", cityList.get(i).getCityName());
                    values.put("cityId", cityList.get(i).getCityId());
                    values.put("provinceName", cityList.get(i).getProvinceeName());
                    database.insert("City", null, values);
                    values.clear();

                }
            }
        }
    }

    /**
     * 保存县区级数据
     *
     * @param countyList
     */
    public void savaCounty(List<County> countyList) {
        int number = 0;
        Cursor c = database.rawQuery("select * from County", null);
        number = c.getCount();
        if (countyList != null && countyList.size() > 0) {
            ContentValues values = new ContentValues();
            for (int i = 0; i < countyList.size(); i++) {
                for (int j = countyList.size() - 1; j > i; j--) {
                    if (countyList.get(i).getCountyName().equals(countyList.get(j).getCountyName())) {
                        countyList.remove(j);
                    }
                }
            }
            if (number == 0) {
                for (int i = 0; i < countyList.size(); i++) {
                    values.put("countyName", countyList.get(i).getCountyName());
                    values.put("countyId", countyList.get(i).getCountyId());
                    values.put("cityName", countyList.get(i).getCityName());
                    database.insert("County", null, values);
                    values.clear();
                }
            }
        }
    }

    /**
     * 保存天气图标信息
     *
     * @param weathImgList
     */
    public void saveWeathImgs(List<WeathImg> weathImgList) {
        int number = 0;
        Cursor c = database.rawQuery("select * from Weatherimg", null);
        number = c.getCount();
        if (weathImgList != null && weathImgList.size() > 0) {
            ContentValues values = new ContentValues();
            if (number == 0) {
                for (int i = 0; i < weathImgList.size(); i++) {
                    values.put("code", weathImgList.get(i).getCode());
                    values.put("txt_zh", weathImgList.get(i).getTxt_zh());
                    values.put("txt_en", weathImgList.get(i).getTxt_en());
                    values.put("icon", getPicture(weathImgList.get(i).getIcon()));
                    values.put("icon_url", weathImgList.get(i).getIcon_url());
                    database.insert("Weatherimg", null, values);
                    values.clear();
                }
            }
        }
    }

    //将bitmap转成成字节保存
    private byte[] getPicture(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }


    /**
     * 保存城市管理/城市数据
     *
     * @param cityid,cityname
     */
    public void savaManagecity(String cityid, String cityname) {
        try {
            database.rawQuery("select * from managecity", null);
        } catch (Exception e) {
            database.execSQL(MANAGE_CITY);
        }
        if (cityid != null && cityname != null & !cityid.equals("") && !cityname.equals("")) {
            ContentValues values = new ContentValues();
            values.put("cityid", cityid);
            values.put("cityname", cityname);
            database.insert("managecity", null, values);
            values.clear();
        }
    }


    //返回城市管理所有城市信息
    public List<ManageCity> getAllManagecity() {
        Cursor cursor = null;
        try {
            cursor = database.query("managecity", null, null, null, null, null, null);
        } catch (Exception e) {
            database.execSQL(MANAGE_CITY);
        }
        List<ManageCity> list = new ArrayList<>();
        ManageCity managecity;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    managecity = new ManageCity();
                    managecity.setCityId(cursor.getString(cursor.getColumnIndex("cityId")));
                    managecity.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                    list.add(managecity);
                } while (cursor.moveToNext());
            }
        }
        return list;
    }

    //删除城市管理的选择删除的城市
    public void deleteManagecity(String cityid) {
        if (cityid != null && !cityid.equals("")) {
            try {
                database.delete("managecity", "cityId" + " = ? ", new String[]{cityid});
            } catch (Exception e) {
            }
        }
    }


    //返回所有省份信息
    public List<Province> getAllProvince() {
        Cursor cursor = database.query("Province", null, null, null, null, null, null);
        List<Province> list = new ArrayList<>();
        Province province;
        if (cursor.moveToFirst()) {
            do {
                province = new Province();
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
                province.setProvinceId(cursor.getString(cursor.getColumnIndex("provinceId")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //返回指定省份下的所有城市
    public List<City> getAllCity(String provinceName) {
        List<City> list = new ArrayList<>();
        City city;
        Cursor cursor = database.query("City", null, "provinceName = ?", new String[]{provinceName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                city.setCityId(cursor.getString(cursor.getColumnIndex("cityId")));
                city.setProvinceeName(provinceName);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //返回指定城市下的所有乡村
    public List<County> getAllCountry(String cityName) {
        List<County> list = new ArrayList<>();
        Cursor cursor = database.query("County", null, "cityName=?", new String[]{cityName}, null, null, null);
        County county;
        if (cursor.moveToFirst()) {
            do {
                county = new County();
                county.setCountyName(cursor.getString(cursor.getColumnIndex("countyName")));
                county.setCountyId(cursor.getString(cursor.getColumnIndex("countyId")));
                county.setCityName(cityName);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //返回所有天气图标数据
    public List<WeathImg> getAllWeatherImg() {
        Cursor cursor = database.query("Weatherimg", null, null, null, null, null, null);
        List<WeathImg> list = new ArrayList<>();
        WeathImg weathImg;
        if (cursor.moveToFirst()) {
            do {
                weathImg = new WeathImg();
                weathImg.setCode(cursor.getString(cursor.getColumnIndex("code")));
                weathImg.setTxt_zh(cursor.getString(cursor.getColumnIndex("txt_zh")));
                weathImg.setTxt_en(cursor.getString(cursor.getColumnIndex("txt_en")));
                if (list.size() < 49) {  //49之后天气图标有误
                    byte[] in = cursor.getBlob(cursor.getColumnIndex("icon"));
                    weathImg.setIcon_url(cursor.getString(cursor.getColumnIndex("icon_url")));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length, null);
                    weathImg.setIcon(bitmap);
                }
                list.add(weathImg);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
