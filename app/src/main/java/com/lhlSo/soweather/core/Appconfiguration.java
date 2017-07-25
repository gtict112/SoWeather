package com.lhlSo.soweather.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;


import com.lhlSo.soweather.general.ProgressDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Appconfiguration {
    private ProgressDialogFragment progressDialog;
    private Context mContext;
    private static Appconfiguration config;

    /**
     * 当前页面集合，方便退出
     */
    private Set<AppCompatActivity> activitySet;
    // General Settings
    /**
     * 存放最前面的activity。
     */
    private List<AppCompatActivity> frontActivityList;

    /**
     * 系统配置文件，全局就一个文件。
     */
    private SharedPreferences generalPreferences;
    private SharedPreferences.Editor generalPreferenceEditor;

    public Context getContext() {
        return mContext;
    }

    public Appconfiguration() {
        progressDialog = new ProgressDialogFragment();
        activitySet = new HashSet<>();
        frontActivityList = new ArrayList<>();

    }


    public static Appconfiguration getInstance() {
        if (config == null) {
            synchronized (Appconfiguration.class) {
                if (config == null) {
                    config = new Appconfiguration();
                }
            }
        }
        return config;
    }

    public void addActivity(AppCompatActivity activity) {
        activitySet.add(activity);
    }


    public void removeActivity(AppCompatActivity activity) {
        activitySet.remove(activity);
    }


    public void closeAllActivities() {
        for (AppCompatActivity activity : activitySet) {
            activity.finish();
        }
        activitySet.clear();
    }

    public Set<AppCompatActivity> getActivitySet() {
        return activitySet;
    }

    public List<AppCompatActivity> getFrontActivityList() {
        return frontActivityList;
    }

    public ProgressDialogFragment showProgressDialog(String message, Activity context) {
        this.mContext = context;
        if (message != null)
            progressDialog.setMessage(message);
        progressDialog.show(context.getFragmentManager(), message);
        return progressDialog;
    }


    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void initGeneralPreferences(Context context) {
        this.mContext = context;
        this.generalPreferences = mContext.getSharedPreferences(
                "generalPreferences",
                Context.MODE_PRIVATE);
        this.generalPreferenceEditor = generalPreferences.edit();
        config.setStartNoti(generalPreferences.getBoolean(
                "isStartNoti", false));

        config.setWinUpdateTime(generalPreferences.getLong(
                "updatime", 3600 * 1000));

        config.setLocationCity(generalPreferences.getString(
                "locationCity", null));

        config.setLocationCounty(generalPreferences.getString(
                "locationCounty", null));

    }

    public void setLocationCity(String locationCity) {
        generalPreferenceEditor.putString(
                "locationCity", locationCity).commit();
    }

    public String getLocationCity() {
        return generalPreferences.getString(
                "locationCity", null);

    }


    public void setLocationCounty(String locationCounty) {
        generalPreferenceEditor.putString(
                "locationCounty", locationCounty).commit();
    }

    public String getLocationCounty() {
        return generalPreferences.getString(
                "locationCounty", null);

    }


    /**
     * 桌面插件服务更新时间
     *
     * @param time
     */
    public void setWinUpdateTime(long time) {
        generalPreferenceEditor.putLong(
                "updatime", time).commit();
    }

    public long getWinUpdateTime() {
        return generalPreferences.getLong(
                "updatime", 3600 * 1000);

    }

    /**
     * 通知开关
     *
     * @param isStartNoti
     */
    public void setStartNoti(boolean isStartNoti) {
        generalPreferenceEditor.putBoolean(
                "isStartNoti", isStartNoti).commit();
    }

    public Boolean getIsStartNoti() {
        return generalPreferences.getBoolean(
                "isStartNoti", false);

    }
}
