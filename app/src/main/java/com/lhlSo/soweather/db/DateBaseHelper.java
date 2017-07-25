package com.lhlSo.soweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/15.
 */

public class DateBaseHelper extends SQLiteOpenHelper {
    /**
     * 城市三级列表
     */
    private final String CREATE_PROVINCE = "create table Province ("
            + "provinceName text," + "provinceId text )";

    private final String CREATE_CITY = "create table City("
            + "cityName text," + "cityId text," + "provinceName text)";

    private final String CREATE_COUNTY = "create table County("
            + "countyName text," + "countyId text," + "cityName text)";

    private final String CREATE_WEATHERIMG = "create table Weatherimg("
            + "code text," + "txt_zh text," + "txt_en text," + "icon BLOB," + "icon_url text)";


    public DateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        db.execSQL(CREATE_WEATHERIMG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
