package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HeartLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/28.
 */

public class LifeActivity extends Activity {
    private Suggestion mSuggestion = new Suggestion();
    private TextView wind;
    private TextView desc;
    private TextView index;
    private String cityid;
    private Handler mHandler;
    private Appconfiguration config = Appconfiguration.getInstance();
    private String dir;
    private String sc;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String vis;
    private String tem_max_min;
    private TextView flubrf;
    private TextView flu_txt;
    private TextView drsgbrf;
    private TextView drsg_txt;
    private TextView travbrf;
    private TextView trav_txt;
    private TextView sportbrf;
    private TextView sport_txt;
    private TextView toptv;
    private ImageView topButton;
    private String city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        initView();
        getData();
        getHandleMessge();
    }

    private void initView() {
        wind = (TextView) findViewById(R.id.wind);
        desc = (TextView) findViewById(R.id.desc);
        index = (TextView) findViewById(R.id.index);
        flubrf = (TextView) findViewById(R.id.flubrf);
        flu_txt = (TextView) findViewById(R.id.flubrf_txt);
        drsgbrf = (TextView) findViewById(R.id.drsgbrf);
        drsg_txt = (TextView) findViewById(R.id.drsgbrf_txt);
        travbrf = (TextView) findViewById(R.id.travbrf);
        trav_txt = (TextView) findViewById(R.id.travbrf_txt);
        sportbrf = (TextView) findViewById(R.id.sportbrf);
        sport_txt = (TextView) findViewById(R.id.sportbrf_txt);
        toptv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            cityid = intent.getStringExtra("cityid");
            dir = intent.getStringExtra("dir");
            sc = intent.getStringExtra("sc");
            fl = intent.getStringExtra("fl");
            hum = intent.getStringExtra("hum");
            pcpn = intent.getStringExtra("pcpn");
            pres = intent.getStringExtra("pres");
            vis = intent.getStringExtra("vis");
            tem_max_min = intent.getStringExtra("tem_max_min");
            city = intent.getStringExtra("city");
            wind.setText("*  " + "今日风力," + dir + sc + "级");
            desc.setText("*  " + "温度" + tem_max_min + "," + "体感温度" + fl + "℃" + "," + "相对湿度" + hum + "%" + "," + "降水量" + pcpn + "mm" + "," + "气压" + pres + "," + "能见度" + vis + "km");
            toptv.setText("今日" + city + "生活资讯");
            String cond = intent.getStringExtra("cond");
            try {
                JSONObject astrodate = new JSONObject(cond);
                index.setText("*  " + "白天" + astrodate.optString("txt_d") + "," + "夜间" + astrodate.optString("txt_n"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getSuggestionData(cityid);
    }

    /**
     * 获取生活指数
     *
     * @param cityid
     */
    private void getSuggestionData(String cityid) {
        config.showProgressDialog("正在加载", this);
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getSuggestionData(new ResponseListenter<Suggestion>() {
            @Override
            public void onReceive(Result<Suggestion> result) {
                if (result.isSuccess()) {
                    config.dismissProgressDialog();
                    mSuggestion = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mSuggestion));
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        setView(mSuggestion);
                        break;
                }
            }
        };
    }

    private void setView(Suggestion mSuggestion) {
        config.dismissProgressDialog();
        flubrf.setText("感冒指数---" + mSuggestion.flubrf);
        flu_txt.setText(mSuggestion.flutex);
        drsgbrf.setText("穿衣指数---" + mSuggestion.drsgbrf);
        drsg_txt.setText(mSuggestion.drsgtex);
        travbrf.setText("旅游指数---" + mSuggestion.travbrf);
        trav_txt.setText(mSuggestion.travtex);
        sportbrf.setText("运动指数---" + mSuggestion.sportbrf);
        sport_txt.setText(mSuggestion.sporttex);
        index.setText(index.getText().toString() + "," + "紫外线强度" + mSuggestion.uvbrf + "," + mSuggestion.cwbrf + "洗车");
    }
}
