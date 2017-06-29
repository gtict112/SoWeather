package com.example.administrator.soweather.com.example.administrator.soweather.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.SpanActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.core.ParentsAppWidgetProvider;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author way
 */
public class AppWidgetUpdateService extends Service {
    private static final int UPDATE = 0x123;
    private RemoteViews remoteViews;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathImgs = new ArrayList<>();//图片数据
    private NowWeather mNowWeather = new NowWeather();
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();
    private int rand = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    updateTime();
                    break;
                case 2:
                    updatedateNow(mNowWeather);
                    break;
                case 3:
                    updatedateHour(mDailyforecast);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        remoteViews = new RemoteViews(getApplication().getPackageName(),
                R.layout.appwidget_provider);
        if (isNetworkAvailable()) {
            getWeatherData();
        } else {
            toast();
        }
        updateTime();
        getWeatherData();
        Intent intent = new Intent(getApplicationContext(), SpanActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.right, pi);

        // 定义一个定时器去更新天气。实际开发中更新时间间隔可以由用户设置，
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = UPDATE;
                handler.sendMessage(msg);
            }
        }, 1, 3600 * 1000);// 每小时更新一次天气
    }


    // 广播接收者去接收系统每分钟的提示广播，来更新时间
    private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTime();
        }
    };

    private void updatedateNow(NowWeather mNowWeather) {
        /**
         * 设置实时天气数据
         */
        if (mNowWeather != null) {
            try {
                String code = new JSONObject(mNowWeather.cond).optString("code");
                Constans.WeatherBgImg citys[] = Constans.WeatherBgImg.values();
                String txt = new JSONObject(mNowWeather.cond).optString("txt");
                if (txt != null) {
                    for (Constans.WeatherBgImg cu : citys) {
                        if (txt.equals(cu.getName())) {
                            if (cu.getimgId().length > 0) {
                                rand = (int) Math.round(Math.random() * (cu.getimgId().length - 1));
                                remoteViews.setImageViewResource(R.id.widget_bg, cu.getimgId()[rand]);
                            } else {
                                remoteViews.setImageViewResource(R.id.widget_bg, cu.getimgId()[0]);
                            }

                            break;
                        }
                    }
                }
                remoteViews.setTextViewText(R.id.city, mNowWeather.cnty + mNowWeather.city);
                remoteViews.setTextViewText(R.id.fl, "体感温度" + mNowWeather.fl + "℃");
                remoteViews.setTextViewText(R.id.tmp, (mNowWeather.tmp) + "℃");
                remoteViews.setTextViewText(R.id.cond_txt, new JSONObject(mNowWeather.cond).optString("txt"));


                remoteViews.setTextViewText(R.id.desc, "今天相对湿度" + mNowWeather.hum + "%  " + "降水量" + mNowWeather.pcpn + "mm  " + "能见度"
                        + mNowWeather.vis + "Km");
                cityDB = SoWeatherDB.getInstance(getApplicationContext());
                weathImgs = cityDB.getAllWeatherImg();
                if (code != null && weathImgs.size() != 0 && weathImgs != null) {
                    for (int i = 0; i < weathImgs.size(); i++) {
                        if (code.equals(weathImgs.get(i).getCode())) {
                            remoteViews.setImageViewBitmap(
                                    R.id.weather_img,
                                    weathImgs.get(i).getIcon());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatedateHour(List<Dailyforecast> mDailyforecast) {
        /**
         * 设置天数预报数据
         */
        if (mDailyforecast != null && mDailyforecast.size() > 0) {
            try {
                remoteViews.setTextViewText(R.id.date, mDailyforecast.get(0).date.substring(5, mDailyforecast.get(0).date.length()) + "(今)");
                String max = new JSONObject(mDailyforecast.get(0).tmp).optString("max");
                String min = new JSONObject(mDailyforecast.get(0).tmp).optString("min");
                remoteViews.setTextViewText(R.id.tmp_max, max + "℃");
                remoteViews.setTextViewText(R.id.tmp_min, min + "℃");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /**
         * 桌面部件更新
         */
        ComponentName componentName = new ComponentName(
                getApplicationContext(), ParentsAppWidgetProvider.class);
        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(
                componentName, remoteViews);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 获取数据
     */
    private void getWeatherData() {
        String cityid = "CN101210101";
        getNowWeather(cityid);
        getHourlyforecase(cityid);
    }

    /**
     * 获取实时数据
     *
     * @param cityid
     */
    private void getNowWeather(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getNowWeatherData(new ResponseListenter<NowWeather>() {
            @Override
            public void onReceive(Result<NowWeather> result) {
                if (result.isSuccess()) {
                    mNowWeather = result.getData();
                    handler.sendMessage(handler.obtainMessage(2, mNowWeather));
                }
            }
        }, cityid);
    }

    /**
     * 获取天数预报
     *
     * @param cityid
     */
    private void getHourlyforecase(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getDailyforecastData(new ResponseListenter<List<Dailyforecast>>() {
            @Override
            public void onReceive(Result<List<Dailyforecast>> result) {
                if (result.isSuccess()) {
                    mDailyforecast = result.getData();
                    handler.sendMessage(handler.obtainMessage(3, mDailyforecast));
                }
            }
        }, cityid);
    }

    /**
     * 广播更新时间
     */
    private void updateTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HHmm");
        String timeStr = df.format(date);
        remoteViews.setTextViewText(R.id.time, timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4));
        ComponentName componentName = new ComponentName(getApplication(),
                ParentsAppWidgetProvider.class);
        AppWidgetManager.getInstance(getApplication()).updateAppWidget(
                componentName, remoteViews);
    }


    /**
     * 注册服务
     *
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        IntentFilter updateIntent = new IntentFilter();
        updateIntent.addAction("android.intent.action.TIME_TICK");
        registerReceiver(mTimePickerBroadcast, updateIntent);
        super.onStart(intent, startId);
    }

    /**
     * 启动服务
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mTimePickerBroadcast);
        Intent intent = new Intent(getApplicationContext(), AppWidgetUpdateService.class);
        getApplication().startService(intent);
        super.onDestroy();
    }

    /**
     * 判断手机网络是否可用
     *
     * @param
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager mgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 桌面部件手机网络判断
     */
    private void toast() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("提示")
                .setMessage("网络连接未打开")
                .setPositiveButton("前往打开",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", null).create().show();
    }
}
