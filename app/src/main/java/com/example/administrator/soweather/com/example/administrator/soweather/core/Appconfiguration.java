package com.example.administrator.soweather.com.example.administrator.soweather.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.soweather.com.example.administrator.soweather.general.ProgressDialogFragment;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Appconfiguration {
    private ProgressDialogFragment progressDialog;
    private Context mContext;
    private static Appconfiguration config;
    private static SharedPreferences generalPreferences;
    private static SharedPreferences.Editor generalPreferenceEditor;
    private Bitmap drawable;//当天天气图片
    private String adress; //当前位置
    private String tmp_txt;//天气描述
    private String tem;//温度

    public Context getContext() {
        return mContext;
    }
    public void setTmpTxt(String tmp_txt) {
        this.tmp_txt = tmp_txt;
        generalPreferenceEditor.putString("tmp_txt", tmp_txt);
        generalPreferenceEditor.commit();
    }

    public String getTmpTxt() {
        return tmp_txt;
    }



    public Appconfiguration() {
        progressDialog = new ProgressDialogFragment();
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

    public ProgressDialogFragment showProgressDialog(String message, Activity context) {
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
