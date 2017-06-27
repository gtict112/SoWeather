package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.AboutActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.HelpFeedbackActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.SettingSkinActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.SpanActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.WelcomeActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.CashDataManager;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.base.BaseSkinFragment;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2016/11/1.
 */

public class SettingFragment extends BaseSkinFragment implements View.OnClickListener, ResponseListenter<NowWeather> {
    private Switch noti;
    private LinearLayout win;
    private LinearLayout clear;
    private LinearLayout feed_back;
    private LinearLayout setting_about;
    public final static String ACTION_BTN = "com.example.notification.btn.login";
    public final static String INTENT_NAME = "btnid";
    public final static int INTENT_BTN_LOGIN = 1;
    NotificationBroadcastReceiver mReceiver;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private NowWeather mData = new NowWeather();
    private Handler mHandler;
    private String adress;
    private String tmp_txt;
    private Bitmap tmp_img;
    private String hum;
    private NotificationManager notificationManager;
    private String desc;
    private TextView cash_size;
    private LinearLayout noti_layout;
    private Boolean isConfirm = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initView(view);
        cityDB = SoWeatherDB.getInstance(getActivity());
        getData();
        getHandlerMessage();
        getCashSize();
        return view;
    }

    private void getData() {
        WeatherService service = new WeatherService();
        String city = null;
        service.getNowWeatherData(this, city);
    }

    private void getHandlerMessage() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    setView(mData);
                }
            }
        };
    }

    private void setView(NowWeather data) {
        adress = data.cnty + data.city;
        String code = null;
        try {
            tmp_txt = new JSONObject(data.cond).optString("txt");
            code = new JSONObject(data.cond).optString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hum = data.tmp;
        desc = data.dir + data.sc;
        weathimgs = cityDB.getAllWeatherImg();
        for (int i = 0; i < weathimgs.size(); i++) {
            if (code.equals(weathimgs.get(i).getCode())) {
                tmp_img = weathimgs.get(i).getIcon();
            }
        }
    }

    private void initView(View view) {
        noti_layout = (LinearLayout) view.findViewById(R.id.noti_layout);
        cash_size = (TextView) view.findViewById(R.id.cash_size);
        noti = (Switch) view.findViewById(R.id.noti);
        win = (LinearLayout) view.findViewById(R.id.win);
        clear = (LinearLayout) view.findViewById(R.id.clear);
        feed_back = (LinearLayout) view.findViewById(R.id.feed_back);
        setting_about = (LinearLayout) view.findViewById(R.id.setting_about);
        LinearLayout skin_setting = (LinearLayout) view.findViewById(R.id.skin_setting);
        initSwitch(noti);
        noti.setOnClickListener(this);
        noti_layout.setOnClickListener(this);
        clear.setOnClickListener(this);
        feed_back.setOnClickListener(this);
        setting_about.setOnClickListener(this);
        win.setOnClickListener(this);
        skin_setting.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initSwitch(final Switch mSwitch) {
        try {
            mSwitch.setThumbResource(R.mipmap.ic_kline_switch);
            mSwitch.setTrackResource(R.mipmap.background_switch_off);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton arg0,
                                             boolean checked) {
                    if (checked) {
                        mSwitch.setTrackResource(R.mipmap.background_switch_on);
                        notification();
                    } else {
                        mSwitch.setTrackResource(R.mipmap.background_switch_off);
                        notificationManager.cancel(0);
                    }
                }
            });
            mSwitch.setTextOn("");
            mSwitch.setTextOff("");
        } catch (Throwable e) {
            mSwitch.setTextOn("开启");
            mSwitch.setTextOff("关闭");
        }
    }

    private void notification() {
        unregeisterReceiver();
        intiReceiver();
        RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.notifition);
        remoteViews.setTextViewText(R.id.adress, adress);
        remoteViews.setTextViewText(R.id.weather, tmp_txt);
        remoteViews.setTextViewText(R.id.temperature, hum + "℃");
        remoteViews.setImageViewBitmap(R.id.weather_img, tmp_img);
        remoteViews.setTextViewText(R.id.desc, desc + "级");
        Intent intent = new Intent(ACTION_BTN);
        intent.putExtra(INTENT_NAME, INTENT_BTN_LOGIN);
        PendingIntent intentpi = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent2 = new Intent();
        intent2.setClass(getActivity(), SpanActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intentContent = PendingIntent.getActivity(getActivity(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setOngoing(false);
        builder.setAutoCancel(false);
        builder.setContent(remoteViews);
        builder.setTicker("YoYo天气随时为你报道天气");
        builder.setSmallIcon(R.mipmap.app);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.contentIntent = intentContent;
        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void intiReceiver() {
        mReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BTN);
        getActivity().getApplicationContext().registerReceiver(mReceiver, intentFilter);
    }

    private void unregeisterReceiver() {
        if (mReceiver != null) {
            getActivity().getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onReceive(Result<NowWeather> result) {
        if (result.isSuccess()) {
            mData = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(1, mData));
        } else {
            result.getErrorMessage();
        }
    }

    class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BTN)) {
                int btn_id = intent.getIntExtra(INTENT_NAME, 0);
                switch (btn_id) {
                    case INTENT_BTN_LOGIN:
                        Toast.makeText(getActivity(), "从通知栏点登录", Toast.LENGTH_SHORT).show();
                        unregeisterReceiver();
                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(0);
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noti:
                //设置通知
                break;
            case R.id.noti_layout:
                isConfirm = !isConfirm;
                noti.setChecked(isConfirm);
                break;
            case R.id.win:
                Toast.makeText(getActivity(), "桌面小控件可在手机系统小部件进行添加,相关定制功能待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.clear:
                //清除缓存
                CashDataManager.clearAllCache(Appconfiguration.getInstance().getContext());
                Toast.makeText(getActivity(), "清除应用缓存成功", Toast.LENGTH_LONG).show();
                getCashSize();
                break;
            case R.id.feed_back:
                //帮助与反馈
                Intent intent = new Intent(getActivity(), HelpFeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_about:
                Intent intent1 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.skin_setting:
                startActivity(new Intent(getActivity(), SettingSkinActivity.class));
                break;
        }
    }

    /**
     * 获取应用的缓存目录大小
     */
    private void getCashSize() {
        try {
            cash_size.setText(CashDataManager.getTotalCacheSize(Appconfiguration.getInstance().getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
