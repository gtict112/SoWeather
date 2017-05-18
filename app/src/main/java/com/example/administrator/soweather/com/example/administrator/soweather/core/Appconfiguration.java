package com.example.administrator.soweather.com.example.administrator.soweather.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.ArraySet;

import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.soweather.com.example.administrator.soweather.general.ProgressDialogFragment;

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
    private Set<FragmentActivity> activitySet;
    // General Settings
    /**
     * 存放最前面的activity。
     */
    private List<FragmentActivity> frontActivityList;

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

    public void addActivity(FragmentActivity activity) {
        activitySet.add(activity);
    }


    public void removeActivity(FragmentActivity activity) {
        activitySet.remove(activity);
    }


    public void closeAllActivities() {
        for (FragmentActivity activity : activitySet) {
            activity.finish();
        }
        activitySet.clear();
    }

    public List<FragmentActivity> getFrontActivityList() {
        return frontActivityList;
    }

    public ProgressDialogFragment showProgressDialog(String message, Activity context) {
        this.mContext = context;
        if (message != null)
            progressDialog.setMessage(message);
        progressDialog.show(context.getFragmentManager(), message);
        return progressDialog;
    }

    public void setProgressMessage(String message) {
        progressDialog.setMessage(message);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
