package com.example.administrator.soweather.com.example.administrator.soweather.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.soweather.com.example.administrator.soweather.general.ProgressDialogFragment;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Appconfiguration {
    private ProgressDialogFragment progressDialog;
    private Context mContext;
    private static Appconfiguration config;

    public Context getContext() {
        return mContext;
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
        progressDialog.show(context.getFragmentManager(),null);
        return progressDialog;
    }

    public void setProgressMessage(String message) {
        progressDialog.setMessage(message);
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
